import java.io.*;
import java.net.*;

public class client {
  public static void main(String[] args) throws IOException{
    // Configure the UDP request with filename, client address and port number
    // create socket and bind it
    DatagramSocket udpSocket = new DatagramSocket(3001);
    // send one datagram
    String info = "myfile.txt+localhost+2000";
    byte[] data = info.getBytes();
    DatagramPacket packet = new DatagramPacket(data, data.length);
    // set receiver address
    packet.setAddress(InetAddress.getByName("localhost"));
    packet.setPort(3000);
    udpSocket.send(packet);
    System.out.println("Sending UDP packet: "+info);

    // Opens TCP socket waiting for the server connection
    ServerSocket serverSocket = null;
    try { serverSocket = new ServerSocket(2000); }
    catch (IOException e) {
      System.err.println("Could not listen on port 2000.");
      System.exit(1);
    }
    Socket clientSocket = null;
    try { clientSocket = serverSocket.accept(); }
    catch (IOException e) {
      System.err.println("Accept failed");
      System.exit(1);
    }

    // Receiving server response
    PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
    PrintWriter fileOutput = new PrintWriter("new_file.txt", "UTF-8");
    BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
    String inputLine, outputLine;
    System.out.println("Receiving file...");
    while ((inputLine = in.readLine()) != null) {
      outputLine = inputLine;
      out.println(outputLine);
      fileOutput.println(outputLine);
    }
    System.out.println("File saved as: new_file.txt");
    out.close(); in.close(); fileOutput.close();
    clientSocket.close(); serverSocket.close();
  }
}
