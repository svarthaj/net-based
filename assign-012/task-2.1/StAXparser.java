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
            XMLInputFactory factory = XMLInputFactory.newInstance();
            factory.setProperty("javax.xml.stream.isNamespaceAware", false);
    
            XMLStreamReader reader = factory.createXMLStreamReader(new FileReader(new File("scientists_dtd.xml")));
            System.out.println("NameSpace URI "+reader.getNamespaceURI());
            XMLStreamReader previousReader = reader;
            while(reader.hasNext()) {
                int eventType = reader.getEventType();
                switch (eventType) {
                    case XMLStreamConstants.START_ELEMENT:
                    System.out.println("START_ELEMENT Type="+eventType);
                    System.out.println("START_ELEMENT eventName = "+ reader.getLocalName()); 
                    if(reader.getAttributeCount()>0){
                        for (int i=0; i< reader.getAttributeCount(); i++)
                        System.out.println("Attribute name : "+reader.getAttributeLocalName(i) + " with Value  = "+ reader.getAttributeValue(i));
                    }
                    /*if(reader.next()== XMLStreamConstants.CHARACTERS)
                    System.out.println("Text -> "+ reader.getElementText());*/
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
                    System.out.println("CHARACTERS Type="+eventType);
                    System.out.println("Text Character -> "+ previousReader.getText().trim());
                    break;
                    default:
                    break;
                }
                previousReader = reader;
                reader.next();
            }
            reader.close();
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }
}

