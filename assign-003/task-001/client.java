import java.io.*;
import java.net.*;

public class client {
  public static void main(String[] args) throws IOException {
    // Get host
    System.out.println("Enter URL...");
    BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
    String urlIn = stdIn.readLine();

    // Establish TCP connection with host
    Socket clientSocket = null;
    PrintWriter out = null;
    BufferedReader in = null;
    try {
      clientSocket = new Socket(urlIn, 80);
      out = new PrintWriter(clientSocket.getOutputStream(), true);
      in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
    } catch (UnknownHostException e) {
      System.err.println("Don't know about host: "+urlIn);
      System.exit(1);
    } catch (IOException e) {
      System.err.println("Couldn't get I/O for the connection to: "+urlIn);
      System.exit(1);
    }

    // Read object name
    System.out.println("Connection established...");
    System.out.println("Enter object name...");
    String[] reqIn = stdIn.readLine().split("/", 2);

    // Prepare HTTP request
    String httpReq = "GET /" + reqIn[1] + " HTTP/1.1" +
      "\r\nAccept: text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8"+
      "\r\nAccept-Enconding: gzip, deflate"+
      "\r\nAccept-Language: en-US,en;q=0.5"+
      "\r\nUser-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:63.0) Gecko/20100101 Firefox/63.0" +
      "\r\nHost: " + reqIn[0] +
      "\r\nUpgrade-Insecure-Requests: 1"+
      "\r\nConnection: close\r\n\r\n";
    System.out.println("\n\nHTTP Request: \n\n" + httpReq);
    out.println(httpReq);

    // Get HTTP response
    System.out.println("\n\nHTTP Response: \n");
    //String inputLine;
    String inputLine = in.readLine();
    System.out.println(inputLine);
    String resCode = inputLine.split(" ")[1];

    if (resCode.equals("404")){
      System.err.println("Bad request (missing object!)\n\n");
      System.exit(1);
    }

    while ((inputLine = in.readLine()) != null) {
      System.out.println(inputLine);

      /*String[] headerTag = inputLine.split(": ", 2);
      if (headerTag[0].equals("Content-Type")){
        switch(headerTag[1]){
          case "image/jpeg":
            inputLine = in.readLine();
            FileOutputStream jpegOutput = new FileOutputStream("new_image.jpg");
            while ((inputLine = in.readLine()) != null) {
              System.out.println(inputLine);
              jpegOutput.write(inputLine.getBytes());
            }
            jpegOutput.close();
            break;
          case "text/html":
            PrintWriter htmlOutput = new PrintWriter("new_file.html", "UTF-8");
            while ((inputLine = in.readLine()) != null) {
              htmlOutput.println(inputLine);
            }
            htmlOutput.close();
            break;
        }
      }*/
    }

    out.close(); in.close();
    clientSocket.close();

  }
}
