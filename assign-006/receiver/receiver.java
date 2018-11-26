import java.io.*;
import java.net.*;
import java.security.*;
import javax.crypto.*;
import javax.crypto.spec.*;
import java.util.Arrays;
import java.util.Base64;

public class receiver {
  // Global variables used for AES encryption/decryption
  private static SecretKeySpec secretKey;
  private static byte[] key;

  public static void main(String[] args) throws IOException, NoSuchAlgorithmException, InvalidKeyException, IllegalBlockSizeException, NoSuchPaddingException, BadPaddingException {
    // Configure TCP connection
    Socket receiverSocket = null;
    OutputStream out = null;
    InputStream in = null;
    try {
      receiverSocket = new Socket("localhost", 2000);
      out = receiverSocket.getOutputStream();
      in = receiverSocket.getInputStream();
    } catch (UnknownHostException e) {
      System.err.println("Don't know about host");
      System.exit(1);
    } catch (IOException e) {
      System.err.println("Couldn't get I/O");
      System.exit(1);
    }
    // Read keys
    PublicKey pubkey_snd = readPublicKey("../code-assignment_6/key_a.public");
    PublicKey pubkey_rcv = readPublicKey("../code-assignment_6/key_b.public");
    PrivateKey pvtkey_rcv = readPrivateKey("../code-assignment_6/key_b.privat"); // only read its own pvtkey

    // Receive Encrypted symmetric Key
    byte[] pwdCipher = new byte[128]; //read the 128 bit key
    in.read(pwdCipher);

    // Decrypt symmetric key using RSA
    Cipher rsa = Cipher.getInstance("RSA");
    rsa.init(Cipher.DECRYPT_MODE, pvtkey_rcv);
    byte[] AESpwd = rsa.doFinal(pwdCipher);

    // Receive encrypted mp4 file
    ByteArrayOutputStream mp4Baos = new ByteArrayOutputStream();
    byte mp4Buffer[] = new byte[1024];
    for(int s; (s=in.read(mp4Buffer)) != -1;) {
      mp4Baos.write(mp4Buffer, 0, s);
    }
    byte[] mp4Cipher = mp4Baos.toByteArray();

    // Decrypt mp4 file using symmetric Key
    byte[] mp4File = null;
    try {
      setKey(new String(AESpwd));
      Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5PADDING");
      cipher.init(Cipher.DECRYPT_MODE, secretKey);
      mp4File = cipher.doFinal(mp4Cipher);
    }
    catch (Exception e) {
      System.out.println("Error while decrypting mp4 file: " + e.toString());
      System.exit(1);
    }

    // Store mp4 file
    FileOutputStream fileOut = new FileOutputStream("new_video.mp4");
    fileOut.write(mp4File);
    fileOut.flush(); fileOut.close();
    System.out.println("Saved new_video.mp4");

    out.close(); in.close();
    receiverSocket.close();
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
