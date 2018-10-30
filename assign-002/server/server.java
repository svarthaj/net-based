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
    // Opens TCP socket waiting for the server connection
    ServerSocket serverSocket = null;
    Socket clientSocket = null;
    try {
	            serverSocket = new ServerSocket(2000);
	            System.out.println("[Server] listening to port "+serverSocket.getLocalPort());
	            clientSocket = serverSocket.accept();
	            System.out.println("Client connected");
	        } catch (IOException e) {
	            System.out.println("Exception caught when trying to listen on port "
	                + 2000 + " or listening for a connection");
	            System.out.println(e.getMessage());
	        }


    // Load and send file via TCP connection
    System.out.println("Opening file: "+info[0]);
    BufferedReader fileReader = new BufferedReader(new FileReader(info[0]));
    String fileInput;
    System.out.println("Sending file: "+info[0]);
    while ((fileInput = fileReader.readLine()) != null) {
      System.out.println("La file ..."+fileInput);
      out.println(fileInput);
      System.out.println("Line received by client: " + in.readLine() + "... OK");
    }
    System.out.println("Sending file complete");
    out.close(); in.close();
    fileReader.close(); echoSocket.close();
  }
}
