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

    		Socket socketC = null;
        try {
            socketC = new Socket(InetAddress.getLocalHost(),2000);
            System.out.println("Demande de connexion");
        }catch(UnknownHostException e) {
        e.printStackTrace();
    	}catch (IOException e) {
    	e.printStackTrace();
    	}

    // Receiving server response
    PrintWriter out = new PrintWriter(socketC.getOutputStream(), true);
    PrintWriter fileOutput = new PrintWriter("new_file.txt", "UTF-8");
    BufferedReader in = new BufferedReader(new InputStreamReader(socketC.getInputStream()));
    String inputLine, outputLine;
    System.out.println("Receiving file...");
    while ((inputLine = in.readLine()) != null) {
      outputLine = inputLine;
      out.println(outputLine);
      fileOutput.println(outputLine);
    }
    System.out.println("File saved as: new_file.txt");
    out.close(); in.close(); fileOutput.close();
    socketC.close();
  }
}
