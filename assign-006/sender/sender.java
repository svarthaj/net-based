import java.net.*;
import java.io.*;
import java.security.*;
import javax.crypto.*;
import javax.crypto.spec.*;
import java.util.Arrays;
import java.util.Base64;

public class sender {
  // Global variables used for AES encryption/decryption
  private static SecretKeySpec secretKey;
  private static byte[] key;

  public static void main(String[] args) throws IOException, NoSuchAlgorithmException, InvalidKeyException, IllegalBlockSizeException, NoSuchPaddingException, BadPaddingException {
    // Create TCP Socket
    ServerSocket senderSocket = null;
    try { senderSocket = new ServerSocket(2000); }
    catch (IOException e) {
      System.err.println("Could not listen on port 2000.");
      System.exit(1);
    }
    Socket receiverSocket = null;
    try { receiverSocket = senderSocket.accept(); }
    catch (IOException e) {
      System.err.println("Accept failed");
      System.exit(1);
    }

    // Read keys
    PublicKey pubkey_snd = readPublicKey("../code-assignment_6/key_a.public");
    PublicKey pubkey_rcv = readPublicKey("../code-assignment_6/key_b.public");
    PrivateKey pvtkey_snd = readPrivateKey("../code-assignment_6/key_a.privat"); // only read its own pvtkey

    // Configure AES encryption/decryption password
    String AESpwd = "super-secret";

    // Encrypt Symmetric Key with RSA
    Cipher rsa = Cipher.getInstance("RSA");
    rsa.init(Cipher.ENCRYPT_MODE, pubkey_rcv); // RSA encypt with sender public key
    byte[] cipherAESpwd = rsa.doFinal(AESpwd.getBytes());

    // Send encrypted symmetric key
    OutputStream receiverOut = receiverSocket.getOutputStream();

    receiverOut.write(cipherAESpwd);
    receiverOut.flush();

    // Load mp4 file
    // If succeds read the file as a byte stream a write it to a byte array
    FileInputStream fileIn = new FileInputStream("../code-assignment_6/video.mp4");
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    byte buffer[] = new byte[1024];
    for(int s; (s=fileIn.read(buffer)) != -1;) {
      baos.write(buffer, 0, s);
    }
    byte[] mp4Bytes = baos.toByteArray();
    fileIn.close();

    // Encrypt mp4 file with AES using symmetric Key
    byte[] mp4Cipher = null;
    try {
      setKey(AESpwd);
      Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
      cipher.init(Cipher.ENCRYPT_MODE, secretKey);
      mp4Cipher = cipher.doFinal(mp4Bytes);
    }
    catch (Exception e) {
      System.out.println("Error while encrypting mp4file: " + e.toString());
      System.exit(1);
    }

    // Send encrypted mp4 file
    receiverOut.write(mp4Cipher);
    receiverOut.flush();

    senderSocket.close(); receiverSocket.close();
  }

  public static void setKey(String myKey) {
    MessageDigest sha = null;
    try {
        key = myKey.getBytes("UTF-8");
        sha = MessageDigest.getInstance("SHA-1");
        key = sha.digest(key);
        key = Arrays.copyOf(key, 16);
        secretKey = new SecretKeySpec(key, "AES");
    }
    catch (NoSuchAlgorithmException e) {
        e.printStackTrace();
    }
    catch (UnsupportedEncodingException e) {
        e.printStackTrace();
    }
  }

  private static PublicKey readPublicKey(String path) {
    PublicKey key= null;
    try{
      FileInputStream fis;
      fis = new FileInputStream(path);
      ObjectInputStream in = new ObjectInputStream(fis);
      key = (PublicKey) in.readObject();
      in.close();
      return key;
    }
    catch(FileNotFoundException e) {
      e.printStackTrace();
    }
    catch(IOException e) {
      e.printStackTrace();
    }
    catch(ClassNotFoundException e) {
      e.printStackTrace();
    }
    return null;
  }

  private static PrivateKey readPrivateKey(String path) {
    PrivateKey key= null;
    try{
      FileInputStream fis;
      fis = new FileInputStream(path);
      ObjectInputStream in = new ObjectInputStream(fis);
      key = (PrivateKey) in.readObject();
      in.close();
      return key;
    }
    catch(FileNotFoundException e) {
      e.printStackTrace();
    }
    catch(IOException e) {
      e.printStackTrace();
    }
    catch(ClassNotFoundException e) {
      e.printStackTrace();
    }
    return null;
  }
}
