import java.io.File;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import org.w3c.dom.Attr;

public class builder {
  public static void main(String[] args) {
    try {
      File inputFile = new File("scientists.xml");
      DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
      DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
      Document doc = dBuilder.parse(inputFile);
      doc.getDocumentElement().normalize();
      Element root = doc.getDocumentElement();
      //create new person
      Element newPerson = doc.createElement("person");
      root.appendChild(newPerson);
      //set birthday
      Attr born = doc.createAttribute("born");
      born.setValue("1879");
      newPerson.setAttributeNode(born);
      Attr deceased = doc.createAttribute("deceased");
      deceased.setValue("1955");
      newPerson.setAttributeNode(deceased);
      //set firstname
      Element firstname = doc.createElement("firstname");
      firstname.appendChild(doc.createTextNode("Albert"));
      newPerson.appendChild(firstname);
      //set lastname
      Element lastname = doc.createElement("lastname");
      lastname.appendChild(doc.createTextNode("Einstein"));
      newPerson.appendChild(lastname);
      //set job
      Element job = doc.createElement("job");
      job.appendChild(doc.createTextNode("Physicist"));
      newPerson.appendChild(job);

      /* rewrite the content into xml file - NOT SURE IF NECESSARY
      TransformerFactory transformerFactory = TransformerFactory.newInstance();
      Transformer transformer = transformerFactory.newTransformer();
      DOMSource source = new DOMSource(doc);
      StreamResult result = new StreamResult(new File("scientists.xml"));
      transformer.transform(source, result); */

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
          System.out.println("Job : "
          + eElement
          .getElementsByTagName("job")
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
