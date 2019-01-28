import javax.xml.namespace.QName;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.events.XMLEvent;
import javax.xml.stream.events.StartElement;
import java.io.FileReader;
import java.io.File;


public class StAXIteratorParser {
    public static void main(String[] args) {
        try{
            XMLInputFactory factory = XMLInputFactory.newInstance();
            factory.setProperty("javax.xml.stream.isNamespaceAware", false);

            XMLEventReader reader =factory.createXMLEventReader(new FileReader(new File("scientists.xml")));
            QName born = new QName("born");
            QName deceased = new QName("deceased");
            while(reader.hasNext()) {
                XMLEvent event = reader.nextEvent();
                if (event.isStartElement()) {
                    StartElement startElement = (StartElement)event;
                    switch(startElement.getName().toString()) {
                        case "person":
                        System.out.println("Born : " +startElement.getAttributeByName(born));
                        System.out.println("Deceased : " +startElement.getAttributeByName(deceased));
                        break;
                        case "firstname":
                        System.out.println("First Name :" + reader.getElementText().trim());
                        break;
                        case "lastname":
                        System.out.println("Last Name :" + reader.getElementText().trim());
                        break;
                        case "job":
                        System.out.println("Job :" + reader.getElementText().trim());
                        break;
                        default:
                        break;
                    }
                } 
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}

/* I would say both solutions are easy to implement but this one might be more efficient than the DOM solution */
