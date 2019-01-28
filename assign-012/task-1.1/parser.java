import java.io.File;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;

public class parser {
  public static void main(String[] args) {
    try {
      File inputFile = new File("scientists.xml");
      DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
      DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
      Document doc = dBuilder.parse(inputFile);
      doc.getDocumentElement().normalize();
      NodeList nList = doc.getElementsByTagName("person");
      for (int temp = 0; temp < nList.getLength(); temp++) {
        Node nNode = nList.item(temp);
        if (nNode.getNodeType() == Node.ELEMENT_NODE) {
          Element eElement = (Element) nNode;
          System.out.println("First Name : "
          + eElement
          .getElementsByTagName("firstname")
          .item(0)
          .getTextContent().trim());
          System.out.println("Last Name : "
          + eElement
          .getElementsByTagName("lastname")
          .item(0)
          .getTextContent().trim());
          System.out.println("Date of birth : "
          + eElement.getAttribute("born"));
          System.out.println("Date of death : "
          + eElement.getAttribute("deceased") + "\n");
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}