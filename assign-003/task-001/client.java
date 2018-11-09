import java.io.*;
import java.net.*;
import java.nio.charset.Charset;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import javax.imageio.ImageIO;

public class client {
  public static void main(String[] args) throws IOException {
    // Get host
    System.out.println("Enter URL...");
    BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
    String[] urlIn = stdIn.readLine().split("/",2);

    // Establish TCP connection with host
    Socket clientSocket = null;
    OutputStream out = null;
    InputStream in = null;
    try {
      clientSocket = new Socket(urlIn[0], 80);
      out = clientSocket.getOutputStream();
      in = clientSocket.getInputStream();
    } catch (UnknownHostException e) {
      System.err.println("Don't know about host: "+urlIn[0]);
      System.exit(1);
    } catch (IOException e) {
      System.err.println("Couldn't get I/O for the connection to: "+urlIn[0]);
      System.exit(1);
    }

    // Prepare HTTP request
    String httpReq = "GET /" + urlIn[1] + " HTTP/1.1" +
      "\r\nAccept: text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8"+
      "\r\nAccept-Enconding: gzip, deflate"+
      "\r\nAccept-Language: en-US,en;q=0.5"+
      "\r\nUser-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:63.0) Gecko/20100101 Firefox/63.0" +
      "\r\nHost: " + urlIn[0] +
      "\r\nUpgrade-Insecure-Requests: 1"+
      "\r\nConnection: close\r\n\r\n";
    System.out.println("\n\nHTTP Request: \n\n" + httpReq);
    out.write(httpReq.getBytes(Charset.forName("ISO-8859-1")));

    // Get HTTP response
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    byte buffer[] = new byte[1024];
    for(int s; (s=in.read(buffer)) != -1;) {
      baos.write(buffer, 0, s);
    }
    byte[] result = baos.toByteArray();

    // Figure out where is the breakline between header and message body by loading 4 adjacent bytes and comparing them with CRLF CRLF
    int messageIndex = -1;
    boolean found = false;
    for(int i=0; i<result.length; i++){
      if (found) { break;}
      int currentByte = new Byte(result[i]).intValue();
      int f = new Byte(result[i+1]).intValue();
      int s = new Byte(result[i+2]).intValue();
      int t = new Byte(result[i+3]).intValue();
      if (currentByte == 13 && f == 10 && s==13 && t==10){
          messageIndex = i+4;
          found = true;
      }
    }
    // Make two new byte arrays one for header other for message body
    byte[] header = new byte[messageIndex];
    byte[] message = new byte[result.length - messageIndex];
    System.arraycopy(result, 0, header, 0, messageIndex);
    //System.arraycopy(result, messageIndex, message, 0, result.length-messageIndex);
    //changed it by message.length since it's the same value because message is made from result.length-messageIndex
    System.arraycopy(result, messageIndex, message, 0, message.length);

    // Parse the header to find out the content type and save the message body accordingly
    String headerStr = new String(header, Charset.forName("ISO-8859-1"));
    // check for 404
    // ifolderlinks.ru/404?utm_source=CMblog&utm_medium=link&utm_campaign=funny404pages
    if(headerStr.contains("404")) { System.err.println("404 - Object not found"); }
    if(headerStr.contains("text/html")) {

      //save html
      FileOutputStream fileOut = new FileOutputStream("new_html.html");
      fileOut.write(message);
      fileOut.flush(); fileOut.close();
    } else if (headerStr.contains("image/jpeg")) {
      //save jpeg
      FileOutputStream fileOut = new FileOutputStream("new_jpeg.jpg");
      fileOut.write(message);
      fileOut.flush(); fileOut.close();
    } else {
      //save everything else as txt
      FileOutputStream fileOut = new FileOutputStream("new_txt.txt");
      fileOut.write(message);
      fileOut.flush(); fileOut.close();
    }

    out.close(); in.close();
    clientSocket.close();
  }
}
