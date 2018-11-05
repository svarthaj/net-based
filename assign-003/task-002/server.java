import java.io.*;
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
    BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
    PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
    String inputLine, resLine=null;
    inputLine = in.readLine();
    String[] tok = inputLine.split(" ");
    if (!tok[0].equals("GET")) {
      System.err.println(tok[0] + " not Implemented!");
      System.exit(1);
    }

    // Sending HTTP response
    resLine = "HTTP/1.1 200 OK\r\n\r\n";
    BufferedReader fileReader = new BufferedReader(new FileReader(tok[1].split("/")[1]));
    String fileInput;
    while ((fileInput = fileReader.readLine()) != null) {
      resLine += fileInput;
      resLine += "\r\n";
    }
    out.println(resLine);
    System.out.println("HTTP response:\n\n"+resLine);

    out.close(); in.close();
    clientSocket.close(); serverSocket.close();
  }
}
