import java.io.*;
import java.net.*;
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
    //String[] reqIn = stdIn.readLine().split("/", 2);
    String urlReq = stdIn.readLine();
    URL url = new URL("http://"+urlReq);
    String[] reqIn = urlReq.split("/", 2);
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

/*
    while ((inputLine = in.readLine()) != null) {
      System.out.println(inputLine);
    }
      String[] headerTag = inputLine.split(": ", 2);
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
    // We read each line from the last line read by in
    BufferedReader inPrime = in;
    String response=inPrime.readLine();
    while (!response.contains("Content-Type:")){
      //while the line being read does not contain the string Content-type we read the following line and so on
      response = inPrime.readLine();
    }
    // We stand at the line Content-type: of the MIME
    // We know watch if it contains text/html or image/jpeg to know the extension of the file we want to download
    if(response.contains("text/html")){
      FileOutputStream htmlOutput = new FileOutputStream("new_html.html");
      //if it's a text/html then we continue reading the header until we have the Doctype or html message
      int loop = 0;
      while(loop == 0){
        //If it contains the DOCTYPE message than we stop the while loop, it sure comes before an html word
        if(response.contains("DOCTYPE")){
          loop = 1;
        }
        //If it's a bad html page that doesnt specify doctype (here google for example) then we search for the tag html and we break the loop when we find it
        else if(response.contains("<HTML>")){
          loop = 1;
        }
        //If it has neither of the two key words we read the next line and search it again
        else {
          response= inPrime.readLine();
        }
      }
      //Since we are at the DOCTYPE or <HTML> line we have to write it down before going to next line
      htmlOutput.write(response.getBytes());
      System.out.println(response);
      //We read the informations and write the bytes in an HTML outputfile
      while ((response = inPrime.readLine()) != null) {
        System.out.println(response);
        htmlOutput.write(response.getBytes());
      }
      htmlOutput.close();
    }else if(response.contains("image/jpeg")){      
      //If it's an image we create a BufferedImage
      BufferedImage image = null;
        try {
          //The bufferedImage will read the url and write the img in a new file
            image = ImageIO.read(url);           
            ImageIO.write(image, "jpg",new File("new_image.jpg"));
            
        } catch (IOException e) {
        	e.printStackTrace();
        }
      /* Here we wanted to do as in the HTML but img files have binary data and the bytes would be saved but other informations as well that makes impossible to open the file.
      
      FileOutputStream jpegOutput = new FileOutputStream("new_image.jpg");
      System.out.println("BEFORE WHILE" + response);
      response = inPrime.readLine();
      while ((response = inPrime.readLine()) != null) {
        System.out.println(response);
        jpegOutput.write(response.getBytes());
      }
      jpegOutput.close();*/
     
    }
    out.close(); in.close();
    clientSocket.close();
  }
}
