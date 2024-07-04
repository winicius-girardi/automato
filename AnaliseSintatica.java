import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import Model.*;
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

    public void leSimbolos()   {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setNamespaceAware(false);
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(new File("Entrada/teste.xml"));
            Element mSymbol =(Element) document.getElementsByTagName("m_Symbol").item(0);
            NodeList symbolList = mSymbol.getElementsByTagName("Symbol");
            for (int i = 0; i < symbolList.getLength(); i++) {
                Element symbolElement = (Element) symbolList.item(i);
                String index = symbolElement.getAttribute("Index");
                String name = symbolElement.getAttribute("Name");
                Simbolo simbolo = new Simbolo(name,Integer.valueOf(index));
                lalrTable.simbolos.add(simbolo);
            }

        }
        catch (Exception e) {
            System.out.println("Erro ao ler xml");
        }

    }

    public void leProducoes(){
        try {

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setNamespaceAware(false);
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(new File("Entrada/teste.xml"));

            Element mProductionElement = (Element) document.getElementsByTagName("m_Production").item(0);

            // Obtendo todos os elementos <Production> dentro de <m_Production>
            NodeList productionList = mProductionElement.getElementsByTagName("Production");

            // Iterando sobre os elementos <Production> e extraindo atributos e <ProductionSymbol>
            for (int i = 0; i < productionList.getLength(); i++) {
                Element productionElement = (Element) productionList.item(i);
                String index = productionElement.getAttribute("Index");
                String nonTerminalIndex = productionElement.getAttribute("NonTerminalIndex");
                Producao producao = new Producao(Integer.valueOf(index),Integer.valueOf(nonTerminalIndex));
                NodeList productionSymbolList = productionElement.getElementsByTagName("ProductionSymbol");
                for (int j = 0; j < productionSymbolList.getLength(); j++) {
                    Element productionSymbolElement = (Element) productionSymbolList.item(j);
                    String symbolIndex = productionSymbolElement.getAttribute("SymbolIndex");
                    producao.simbolosProducao.add(Integer.valueOf(symbolIndex));
                }
                producoes.add(producao);
            }
        }
        catch (Exception e){
            System.out.println("Erro ao ler xml");
        }
    }
    public void leLALR(){
        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse("Entrada/teste.xml");

            doc.getDocumentElement().normalize();

            NodeList tableList = doc.getElementsByTagName("LALRTable");
            Node tableNode = tableList.item(0);

            if (tableNode.getNodeType() == Node.ELEMENT_NODE) {
                Element tableElement = (Element) tableNode;
                int count = Integer.parseInt(tableElement.getAttribute("Count"));
                int initialState = Integer.parseInt(tableElement.getAttribute("InitialState"));
                //count e initialState é desnecessario aparentemente
                //LALRTable lalrTable = new LALRTable(count, initialState);

                NodeList stateList = tableElement.getElementsByTagName("LALRState");
                for (int i = 0; i < stateList.getLength(); i++) {
                    Node stateNode = stateList.item(i);

                    if (stateNode.getNodeType() == Node.ELEMENT_NODE) {
                        Element stateElement = (Element) stateNode;
                        int stateIndex = Integer.parseInt(stateElement.getAttribute("Index"));
                        //LALRState lalrState = new LALRState(stateIndex);
                        //creio que abaixo esta correto
                        lalrTable.estados.add(stateIndex);
                        NodeList actionList = stateElement.getElementsByTagName("LALRAction");
                        //percorre os nodos filhos e faz as ações lalr
                        List<AcaoLALR> listAcao = new ArrayList<>();
                        for (int j = 0; j < actionList.getLength(); j++) {
                            Node actionNode = actionList.item(j);
                            if (actionNode.getNodeType() == Node.ELEMENT_NODE) {


                                Element actionElement = (Element) actionNode;
                                int symbolIndex = Integer.parseInt(actionElement.getAttribute("SymbolIndex"));
                                String action = actionElement.getAttribute("Action");
                                int value = Integer.parseInt(actionElement.getAttribute("Value"));
                                AcaoLALR acaoLALR = new AcaoLALR(value,Integer.valueOf(action));
                                listAcao.add(acaoLALR);
                                //CODIGO ABAIXO DE ADICIONAR OS SIMBOLOS TALVEZ SEJA DESNECESSARIO, AVERIGUAR, CASO PRECISE FALTAR PEGAR SIMBOLO?
//                                lalrTable.simbolos.add(simbolo);
//                                Simbolo simbolo = new Simbolo(,symbolIndex);

                                // LALRAction lalrAction = new LALRAction(symbolIndex, action, value);
                                //lalrState.addAction(lalrAction);
                            }
                        }
                        lalrTable.acaoLALR.add(listAcao);

                        //lalrTable.addState(lalrState);
                    }
                }

                // Print the parsed LALRTable
                System.out.println(lalrTable);
            }

        } catch (Exception e) {
            System.out.println("Erro ao ler xml");
        }
    }
}

