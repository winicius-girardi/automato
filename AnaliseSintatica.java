import java.util.ArrayList;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


public class AnaliseSintatica {

    private LALRTable lalrTable;
    private List<Producao> producoes;


    public AnaliseSintatica (){
        lalrTable = new LALRTable();
        producoes = new ArrayList<>();
    }

    public void leProd() {
        try {
            // Cria um documento a partir do arquivo XML
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse("teste.xml");

            // Normaliza o documento XML
            document.getDocumentElement().normalize();

            // Obtém todos os elementos com a tag "m_Symbol Count"
            NodeList nodeList = document.getElementsByTagName("m_Symbol");

            // Itera sobre os elementos encontrados
            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);

                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;

                    // Obtém o conteúdo da tag
                    String conteudo = element.getTextContent();
                    System.out.println("Conteúdo de <m_Symbol Count>: " + conteudo);
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}
