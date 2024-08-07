import Model.RegraGramatica;
import Model.Transicao;

import java.util.*;

public class Processa {

    private int ultimoEstado = 0;

    private int proximoEstado = 1;

    public List<String> tokensDaMatriz;

    private final List<Transicao> transicoes;

    public int lengthPalavra;

    private final List<String[]> matrizAutomato;

    private Integer ultimaLinhaMatriz=0;

    private final List<RegraGramatica> regraExistentes;

    private final List<String> ignorarEstados;

    public List<String>fita;
    public List<String[]> afd;

    public Processa() {
        fita=new ArrayList<>();
        fita.add("-");
        tokensDaMatriz = new ArrayList<>();
        transicoes = new ArrayList<>();
        matrizAutomato = new ArrayList<>();
        regraExistentes = new ArrayList<>();
        regraExistentes.add(new RegraGramatica("S",0));
        ignorarEstados=new ArrayList<>();
        ignorarEstados.add("0");
        ignorarEstados.add("-");
        ignorarEstados.add(".");
        afd=new ArrayList<>();

    }

    public void processaLinha(String linha) {

        if (!linha.matches("^<\"[a-zA-Z]\">"))
            processaPalavra(linha);
        else
            processaGramatica(linha);
    }

    private void processaGramatica(String linha) {
        if(linha.contains("<S> ::="))
            processaTransicaoInicial(linha);
        else{
            processaTransicao(linha);
        }
    }

    private void processaTransicao(String linha) {
        String regraAtual = linha.substring(1, 2);
        Integer regraTransicao=0;
        linha = linha.substring(7);
        String[] tokens = linha.split("\\|");
        for (RegraGramatica regra : regraExistentes) {
            if(regra.nome().equals(regraAtual)){
                regraTransicao=regra.transicao();
                break;
            }
        }
        for(String a:tokens){
            String []aux=a.replace("<","").replace(">","").replace(" ","").split("");
            if(a.replace(" ", "").matches("^[a-z]<[a-zA-Z]>$")) {
                if (!tokensDaMatriz.contains(aux[0]))
                    tokensDaMatriz.add(aux[0]);
                Transicao transicao = new Transicao(aux[0],false, retornaTransicaoRegra(aux),regraTransicao);
                transicoes.add(transicao);

            }
            else{
                if(!tokensDaMatriz.contains(aux[0]))
                    tokensDaMatriz.add(aux[0]);
                //processaToken(aux[0],0,false);
                fita.add(regraTransicao.toString());
                Transicao transicao = new Transicao(aux[0],true, regraTransicao,regraTransicao,true);

                transicoes.add(transicao);
            }
        }

    }

    private Integer retornaTransicaoRegra(String[] aux) {
        for (RegraGramatica regra : regraExistentes) {
            if (regra.nome().equals(aux[1])) {
                return regra.transicao();
            }
        }
        ultimoEstado++;
        return ++proximoEstado;
    }


    private void processaTransicaoInicial(String linha) {
        linha = linha.substring(7);
        String[] tokens = linha.split("\\|");
        for(String a:tokens){
            String []aux=a.replace("<","").replace(">","").replace(" ","").split("");
            if(a.replace(" ", "").matches("^[a-z]<[a-zA-Z]>$")){
                if(!tokensDaMatriz.contains(aux[0]))
                    tokensDaMatriz.add(aux[0]);
                existeRegra(aux[1]);
                for(RegraGramatica regra:regraExistentes){
                    if(regra.nome().equals(aux[1])){
                        processaToken(aux[0],false,regra.transicao());
                    }
                }
            }
            else{
                if(!tokensDaMatriz.contains(aux[0]))
                    tokensDaMatriz.add(aux[0]);
                //processaToken(aux[0],0,false);
                fita.add("0");
                processaToken(aux[0],true,0);
            }
        }
    }

    private void processaToken(String aux, boolean estado, Integer transicao) {
        if(estado){
            Transicao transicao1 = new Transicao(aux,true,transicao,0,true);
            transicoes.add(transicao1);
        }
        else{
        Transicao transicaoAux = new Transicao(aux, false,transicao,0);
        transicoes.add(transicaoAux);
        }
    }

    //anota as regras da LR para fazer corretamente as transições da matriz
    private void existeRegra(String nomeRegra) {
        for(RegraGramatica regra:regraExistentes){
            if(regra.nome().equals(nomeRegra)){
                return;
            }
        }
        RegraGramatica regraAux =new RegraGramatica(nomeRegra,proximoEstado);
        regraExistentes.add(regraAux);

    }

    public void processaPalavra(String linha) {
        String[] aux = linha.split("");
        lengthPalavra=aux.length;
        boolean estado;
        for (int indexAtual=0; indexAtual < aux.length; indexAtual++) {
            if (!tokensDaMatriz.contains(aux[indexAtual])) {
                tokensDaMatriz.add(aux[indexAtual]);
            }
            estado=indexAtual==aux.length-1;
            if(estado)
                fita.add(String.valueOf(proximoEstado));
            processaToken(aux[indexAtual],indexAtual,estado);
        }
    }

    private void processaToken(String s,int indexAtual,boolean estado) {
        if(indexAtual==0){
            Transicao aux1 = new Transicao(s,false,proximoEstado,0);
            proximoEstado++;
            ultimoEstado++;
            transicoes.add(aux1);
            return;
        }
        else {
            Transicao aux = new Transicao(s, false, proximoEstado, ultimoEstado);
            proximoEstado++;
            ultimoEstado++;
            transicoes.add(aux);

        }

        if(estado) {
            Transicao a = new Transicao(null,true,proximoEstado,ultimoEstado);
            ultimoEstado++;
            transicoes.add(a);
        }

    }

    private void montaMatrizAutomato() {
        adicionaTransicao();
        for (Transicao transicao : transicoes) {
            montaLinhaToken(transicao);
        }
        adicionaEstadoErro();
    }

    private void adicionaEstadoErro() {
        String[]aux = new String[tokensDaMatriz.size()+1];
        Arrays.fill(aux, "-");
        aux[0]=aux[0]+"*";
        matrizAutomato.add(aux);
    }


    private String[] adicionaEstadoAfd() {
        String[]aux = new String[tokensDaMatriz.size()+1];
        Arrays.fill(aux, "-");
        aux[0]=aux[0]+"*";
        return aux;
    }

    private void adicionaTransicao() {
        String[]aux = new String[tokensDaMatriz.size()+1];
        aux[0]="-";
        for (int i = 1; i < aux.length; i++) {
            aux[i] = tokensDaMatriz.get(i-1);
        }
        matrizAutomato.add(aux);
    }

    public void montaLinhaToken(Transicao transicao) {
        if (!existeTransicao(transicao))
            criaLinha();
        achaLinha(transicao);

    }

    //adiciona a transicao de um token a uma estado da gramática
    private void achaLinha(Transicao transicao){
        String[] aux=retornaLinha(transicao);
        String[] tokens=matrizAutomato.getFirst();
        if(transicao.estadoFinal&&!aux[0].contains("*"))
            aux[0]=aux[0]+"*";
        for (int k=1;k<tokensDaMatriz.size()+1;k++){
            if(tokens[k].equals(transicao.token)){
                if(!transicao.terminal) {
                    if(aux[k].equals("-"))
                        aux[k] = transicao.estadoTransicaoToken.toString();
                    else
                        aux[k]=aux[k]+","+transicao.estadoTransicaoToken.toString();
                }
                else {
                    aux[k]=transicao.token;
                }
                break;
            }
        }
    }

    private String[] retornaLinha(Transicao transicao) {
        for(String[]aux:matrizAutomato){
            if(aux[0].replace("*","").equals(transicao.estadoAtual.toString()))
                return aux;
        }
        return matrizAutomato.get(ultimaLinhaMatriz);
    }

    private String[] retornaLinha(String transicao) {
        for(String[]aux:matrizAutomato){
            if(aux[0].replace("*","").equals(transicao))
                return aux;
        }
        return matrizAutomato.get(ultimaLinhaMatriz);
    }
    public String[] retornaLinhaAFD(String token){
            for(String[]aux:afd){
                if(aux[0].replace("*","").equals(token))
                    return aux;


            }
            return null;
    }

    //cria linha para adicionar na matriz do automato e preenche com estados de erro e o nome do estado de transicao.
    public void criaLinha() {
        String []aux = new String[tokensDaMatriz.size()+1];
        for (int i=1;i<aux.length;i++){
            aux[i] = "-";
        }

        aux[0]=ultimaLinhaMatriz.toString();
        ultimaLinhaMatriz++;
        matrizAutomato.add(aux);
    }

    //verifica se o regra da transicao da gramatica existe na matriz.
    private boolean existeTransicao(Transicao transicao) {
        for(String[]aux:matrizAutomato){
            if(aux[0].replace("*","").equals(transicao.estadoAtual.toString()))
                return true;
        }
        return false;
    }
    public void printaMatrizAutomato() {
        montaMatrizAutomato();
        int numColumns = matrizAutomato.getFirst().length;
        int[] columnWidths = new int[numColumns];
        for (String[] row : matrizAutomato) {
            for (int i = 0; i < numColumns; i++) {
                columnWidths[i] = Math.max(columnWidths[i], row[i].length());
            }
        }
        for (String[] row : matrizAutomato) {
            for (int i = 0; i < numColumns; i++) {
                System.out.printf(" %-" + (columnWidths[i] + 2) + "s\t|", row[i]);
            }
            System.out.println();
        }

    }


    public void printaAFD() {
        determinizaMatriz();
    }

    private void determinizaMatriz() {
        afd.add(matrizAutomato.getFirst());
        boolean determinismo=true;
        for(RegraGramatica regraGramatica:regraExistentes){
            String[]aux=retornaLinha(regraGramatica.transicao().toString());
            for(int i=1;i<aux.length;i++){
                if(aux[i].contains(",")){
                    novaTransicao(aux[i]);
                    aux[i]="["+aux[i].replace(",","")+"]";
                    afd.add(aux);
                    determinismo=false;
                }
            }
            if(determinismo)
                afd.add(aux);
            determinismo=true;
        }
        for(String[]a:matrizAutomato){
            if(!ignorarEstados.contains(a[0].replace("*","")))
                afd.add(a);
        }
        afd.add(adicionaEstadoAfd());
        List<String> estados=new ArrayList<>();
        List<String[]> remove =new ArrayList<>();
        for(String[]a:afd){
            if(!estados.contains(a[0]))
                estados.add(a[0]);
            else
                remove.add(a);
        }
        for(String[]a:remove){
            afd.remove(a);
        }
        var numColumns = afd.getFirst().length;
        int[] columnWidths = new int[numColumns];
        for (String[] row : afd) {
            for (int i = 0; i < numColumns; i++) {
                columnWidths[i] = Math.max(columnWidths[i], row[i].length());
            }
        }
        afd.sort(new StringArrayComparator());
        for (String[] row : afd) {
            for (int i = 0; i < numColumns; i++) {
                System.out.printf(" %-" + (columnWidths[i] + 2) + "s\t|", row[i]);
            }
            System.out.println();
        }

    }

    private void novaTransicao(String transicao) {
        String[] aux= transicao.split(",");
        String[] estado = new String[tokensDaMatriz.size()+1];
        String[] estadoCopiado;
        boolean estadoEhFinal= false;
        for(String a:aux){
            if(!ignorarEstados.contains(a.replace("*","")))
                ignorarEstados.add(a.replace("*",""));
            if(retornaLinha(a)[0].contains("*")){
                estado[0]="["+transicao.replace(",","").replace("[","").replace("]","")+"*]";
                estadoEhFinal=true;
                //break;
            }
        }
        if(!estadoEhFinal)
            estado[0]="["+transicao.replace(",","")+"]";
        for(String a:aux){
            estadoCopiado=retornaLinha(a);
            for(int i=1;i<estado.length;i++){
                if(estado[i]!=null&&!estado[i].contains("-")){
                    if(estadoCopiado[i].contains("-"))
                        continue;
                    estado[i]=estado[i]+","+estadoCopiado[i];
                }
                else
                    estado[i]=estadoCopiado[i];

            }
        }
        for(int k=0;k<estado.length;k++){
            if(estado[k].contains(",")){
                //a="["+a.replace(",","")+"]";
                novaTransicao(estado[k]);
                estado[k]="["+estado[k].replace(",","")+"]";

            }
        }
        afd.add(estado);
    }

    public String validaToken(String token,Integer posicao,String[]estado) {
        Integer colunaToken=retornaColunaToken(token.charAt(posicao));
        if(colunaToken!=null){
            if(posicao<token.length()-1) {
                if (!estado[colunaToken].contains("-")) {
                    String[] aux = retornaLinhaAFD(estado[colunaToken]);
                    if(posicao==token.length()-1){
                        return estado[0].replace("*","");
                    }
                    posicao++;
                    return validaToken(token, posicao, aux);
                }
            }
            if(posicao==token.length()-1){
                if(estado[0].contains("*"))
                    return estado[0].replace("*","");
                return estado[colunaToken].replace("*","");
            }
        }

        return "a";
    }


    public Integer retornaColunaToken(char token){
        for(int k=0;k<tokensDaMatriz.size();k++){
            if(tokensDaMatriz.get(k).charAt(0)==token)
                return k+1;
        }
        return null;
    }



    static class StringArrayComparator implements Comparator<String[]> {
        @Override
        public int compare(String[] arr1, String[] arr2) {
            // Comparar os elementos do primeiro índice de cada array
            return arr1[0].compareTo(arr2[0]);
        }
    }


}







