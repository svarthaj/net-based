import java.io.*;
import java.nio.charset.Charset;
import java.net.*;

public class server {
  public static void main(String[] args) throws IOException {
    // Opens TCP socket waiting for the client connection
    ServerSocket serverSocket = null;
    try { serverSocket = new ServerSocket(8000); }
    catch (IOException e) {
      System.err.println("Could not listen on port 8000.");
      System.exit(1);
    }
    Socket clientSocket = null;
    try { clientSocket = serverSocket.accept(); }
    catch (IOException e) {
      System.err.println("Accept failed");
      System.exit(1);
    }

    // Receiving client request
    // We open the client input as character stream and the output as bytes stream
    BufferedReader clientIn = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
    OutputStream clientOut = clientSocket.getOutputStream();

    // Retrieve the first line of the packet "GET <filename> HTTP/x.x"
    String[] reqLine = clientIn.readLine().split(" ");

    // Treat request that are not GET
    if(!reqLine[0].equals("GET")) {
      System.err.println(reqLine[0]+" not implemented.");
      System.exit(1);
    }

    // Try to open the file
    try {
      // If succeds read the file as a byte stream a write it to a byte array
      FileInputStream fileIn = new FileInputStream(reqLine[1].split("/")[1]);
      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      byte buffer[] = new byte[1024];
      for(int s; (s=fileIn.read(buffer)) != -1;) {
        baos.write(buffer, 0, s);
      }
      byte[] result = baos.toByteArray();

      // Set the header response with appropriate encoding plus the byte array from the file
      clientOut.write(new String("HTTP/1.1 200 OK\r\n\r\n").getBytes(Charset.forName("ISO-8859-1")));
      clientOut.write(result);
      clientOut.flush();
      fileIn.close();
    } catch (FileNotFoundException e) {
      // If cannot open the file send a 404 error
      clientOut.write(new String("HTTP/1.1 404 Not Found\r\n\r\n <!DOCTYPE html>\r\n<html>\r\n<head>\r\n<title>404</title>\r\n</head>\r\n<body>\r\n<h1>404 - Object not found</h1>\r\r</body>\r\n</html>").getBytes(Charset.forName("ISO-8859-1")));
      clientOut.flush();
    }

    clientSocket.close(); serverSocket.close();
  }
}
