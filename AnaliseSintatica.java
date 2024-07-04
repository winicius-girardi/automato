import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
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
                                String salto = actionElement.getAttribute("Value");
                                AcaoLALR acaoLALR = new AcaoLALR(symbolIndex,Integer.valueOf(action),Integer.valueOf(salto));
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
    public List<String> analisa(List<Fita> fita){
        List<String> erros= new ArrayList<>();
        List<Integer> tokensIndices = mapeiaFitaSaidaAnaliseSintatica(fita);
        List<Integer> pilha = new ArrayList<>();
        int posicaoLeitura=0;
        pilha.add(lalrTable.estados.getFirst());
        while(true){
            Integer estadoAtual= pilha.getLast();
            Integer tokenAtual= tokensIndices.get(posicaoLeitura);
            //OBTER A ACAO PARA O ESTADO ATUAL "LINHA" e TOKEN ATUAL no caso TOKEN INDICE?
            List<AcaoLALR> listAcao=lalrTable.acaoLALR.get(estadoAtual);
            AcaoLALR aux = null;

            System.out.println("Estados");
            for(int i:pilha){
                System.out.print(i+"\t");
            }
            System.out.println("\n");

            for(AcaoLALR a:listAcao){
                if(a.valor.equals(tokenAtual)){
                    aux=a;
                }
            }

            if(aux==null){
                if(posicaoLeitura==fita.size()){
                    Fita f=fita.get(posicaoLeitura-1);
                    erros.add("Expressão invalida detectada! "+ f.label + " na linha " +f.linha +" nao é uma expressao");
                }
                else{
                    Fita f=fita.get(posicaoLeitura);
                    System.out.println("Token "+f.label +" inesperado na linha "+f.linha);
                }
                break;
            }
            else if(aux.acao.equals(Acao.Aceitar)){
                break;
            }
            else if(aux.acao.equals(Acao.Shift)){
                pilha.add(aux.valor);
                posicaoLeitura++;
            }
            else if(aux.acao.equals(Acao.GoTo)){
                System.out.println(posicaoLeitura);
                List<AcaoLALR> a=lalrTable.acaoLALR.get(aux.salto);
                for(AcaoLALR i:a){
                    if(i.salto.equals(aux.salto)){
                        pilha.add(aux.salto);
                        break;
                    }
                }
                posicaoLeitura++;

            }
            else if(aux.acao.equals(Acao.Reduce)){
                Producao producaoReducao = producoes.get(aux.salto);
                if (producaoReducao != null) {
                    int tamanhoProducao = producaoReducao.simbolosProducao.size();
                    for (int i = 0; i < tamanhoProducao; i++) {
                        pilha.remove(pilha.size() - 1);
                    }
                    int estadoTopoAposRemocao = pilha.get(pilha.size() - 1);

                    // Obter o próximo estado usando a tabela GOTO
                    List<AcaoLALR> gotoAction = lalrTable.acaoLALR.get(estadoTopoAposRemocao);

                    for (AcaoLALR a : gotoAction) {
                        if(a.salto.equals(producaoReducao.indiceNaoTerminal)){
                            pilha.add(a.salto);
                            break;
                        }
                    }
//                    if (gotoAction != null && gotoAction.acao.equals(Acao.GoTo)) {
//                        pilha.add(gotoAction.valor);
//                    } else {
//                        erros.add("Erro ao processar GOTO após redução.");
//                        break;
//                    }
                } else {
                    erros.add("Produção de redução não encontrada.");
                    break;
                }

            }
            if(posicaoLeitura==fita.size()){
                erros.add("Código informado não foi reconhecido");
                break;
            }


        }


        return erros;
    }
    public AcaoLALR gotoLALR(Integer estadoAtual,int aux){
        return  lalrTable.acaoLALR.get(estadoAtual).get(aux);
    }

    public List<Integer> mapeiaFitaSaidaAnaliseSintatica(List<Fita> fita){
            List<Integer> tokensIndices = new ArrayList<>();
            for(Fita f: fita){
                tokensIndices.add(pegaIndiceRetornaIndiceGoldParser(f.label));
            }
            tokensIndices.add(lalrTable.estados.getFirst());
            return tokensIndices;
    }

    public Integer pegaIndiceRetornaIndiceGoldParser(String label){

        if(label.matches("="))
            return 3;
        if (label.matches(";"))
            return 14;
        if (label.matches("\\("))
            return 8;
        if (label.matches("\\)"))
            return 13;
        if (label.matches("\\{"))
            return 7;
        if (label.matches("}"))
            return 12;
        if (label.matches("if"))
            return 6;
        if (label.matches("else"))
            return 4;
        if (label.matches("==" )||label.matches(">")||label.matches("<")||label.matches(">=")||label.matches("<=")||label.matches("!="))
            return 10;
        if (label.matches("-?\\d+"))
            return 9;
        if (label.matches("[a-zA-Z][a-zA-Z0-9]*"))
            return 5;
        return 1;
    }



    public void exibeSimbolos(){
        for(Simbolo simbolo : lalrTable.simbolos){
            System.out.println(simbolo);
        }
    }
}

