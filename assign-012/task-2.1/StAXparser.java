import java.io.File;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.namespace.QName;
import java.io.FileReader;


public class StAXparser {
    public static void main(String[] args) {
        try{
            //Create the factory to work with
            XMLInputFactory factory = XMLInputFactory.newInstance();
            factory.setProperty("javax.xml.stream.isNamespaceAware", false);
            //From the factory read the file we want
            XMLStreamReader reader = factory.createXMLStreamReader(new FileReader(new File("scientists_dtd.xml")));
            //Print the namespace URI, here there is none that's why we got null
            System.out.println("NameSpace URI "+reader.getNamespaceURI());
            //While there is another event after the one the cursor is pointing at
            while(reader.hasNext()) {
                //We get the type of the element
                int eventType = reader.getEventType();
                switch (eventType) {
                    //If the type is a start element (opening tag) then we print the type code value, the event name, the attribute if they are some
                    case XMLStreamConstants.START_ELEMENT:
                    System.out.println("START_ELEMENT Type="+eventType);
                    System.out.println("START_ELEMENT eventName = "+ reader.getLocalName()); 
                    if(reader.getAttributeCount()>0){
                        for (int i=0; i< reader.getAttributeCount(); i++)
                        System.out.println("Attribute name : "+reader.getAttributeLocalName(i) + " with Value  = "+ reader.getAttributeValue(i));
                    }
                    //If we get to the firstname, lastname or job element then we print the respective text without comments and so on
                    switch(reader.getLocalName()){
                        case "firstname":
                        System.out.println("First Name -> "+ reader.getElementText().trim());
                        break;
                        case "lastname":
                        System.out.println("Last Name -> "+ reader.getElementText().trim());
                        break;
                        case "job":
                        System.out.println("Job -> "+ reader.getElementText().trim());
                        break;
                        default:
                        break;
                    }                    
                    break;
                    case XMLStreamConstants.START_DOCUMENT:
                    System.out.println("START_DOCUMENT Type="+eventType);
                    break;
                    case XMLStreamConstants.DTD:
                    System.out.println("DTD Type="+eventType);
                    System.out.println("Text of the DTD "+ reader.getText());
                    break;
                    case XMLStreamConstants.END_ELEMENT:
                    System.out.println("END_ELEMENT Type="+eventType);
                    System.out.println("END_ELEMENT eventName = "+ reader.getLocalName()); 
                    break;
                    case XMLStreamConstants.COMMENT:
                    System.out.println("COMMENT Type="+eventType);
                    System.out.println("Comment text is ="+ reader.getText());
                    break;
                    case XMLStreamConstants.CHARACTERS:
                    //never goes in because of the second switch from start element 
                    // If we comment the switch element we have something else and we can see the comment we passed 
                    System.out.println("CHARACTERS Type="+eventType);
                    System.out.println("Text Character -> "+ reader.getText().trim());
                    break;
                    default:
                    break;
                }
                reader.next();
            }
            reader.close();
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }
}

