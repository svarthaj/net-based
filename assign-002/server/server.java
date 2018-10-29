import java.net.*;
import java.io.*;

public class server {
  public static void main(String[] args) throws IOException {
    // Create UDP socket for listening for file request
    // create server socket and bind it
    DatagramSocket udpSocket = new DatagramSocket(3000);
    // allocate space for received datagram
    byte[] data = new byte[256];
    DatagramPacket packet = new DatagramPacket(data, data.length);
    // receive one datagram
    System.out.println("Waiting for UDP packet...");
    udpSocket.receive(packet);
    byte[] dataReceived = packet.getData();
    int bytesReceived = packet.getLength();
    System.out.println("UDP packet received");
    String dataString = new String(dataReceived);
    System.out.println("Message received: "+dataString);
    String info[] = dataString.split("\\+");

    // Establish TCP connection with the client
    Socket echoSocket = null;
    PrintWriter out = null;
    BufferedReader in = null;
    try {
      echoSocket = new Socket(info[1], Integer.parseInt(info[2].trim()));
      out = new PrintWriter(echoSocket.getOutputStream(), true);
      in = new BufferedReader(new InputStreamReader(echoSocket.getInputStream()));
    } catch (UnknownHostException e) {
      System.err.println("Don't know about host: "+info[1]);
      System.exit(1);
    } catch (IOException e) {
      System.err.println("Couldn't get I/O for the connection to: "+info[1]);
      System.exit(1);
    }

    // Load and send file via TCP connection
    System.out.println("Opening file: "+info[0]);
    BufferedReader fileReader = new BufferedReader(new FileReader(info[0]));
    String fileInput;
    System.out.println("Sending file: "+info[0]);
    while ((fileInput = fileReader.readLine()) != null) {
      out.println(fileInput);
      System.out.println("Line received by client: " + in.readLine() + "... OK");
    }
    System.out.println("Sending file complete");
    out.close(); in.close();
    fileReader.close(); echoSocket.close();
  }
}
