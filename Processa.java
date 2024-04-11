import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Processa {

    private int ultimoEstado = 0;

    private int proximoEstado = 1;

    public List<String> tokensDaMatriz;

    private List<Transicao> transicoes;

    public int lengthPalavra;

    private List<String[]> matrizAutomato;

    private Integer ultimaLinhaMatriz=0;

    private List<RegraGramatica> regraExistentes;

    private List<String> ignorarEstados;

    public Processa() {
        tokensDaMatriz = new ArrayList<>();
        transicoes = new ArrayList<>();
        matrizAutomato = new ArrayList<>();
        regraExistentes = new ArrayList<>();
        regraExistentes.add(new RegraGramatica("S",0));
        ignorarEstados=new ArrayList<>();
        ignorarEstados.add("0");
        ignorarEstados.add("-");
        ignorarEstados.add(".");
    }

    public void processaLinha(String linha) {

        if (linha.matches("[a-zA-Z]+"))
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
            if(regra.getNome().equals(regraAtual)){
                regraTransicao=regra.getTransicao();
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
                Transicao transicao = new Transicao(aux[0],true, regraTransicao,regraTransicao,true);
                transicoes.add(transicao);
            }
        }

    }

    private Integer retornaTransicaoRegra(String[] aux) {
        for (RegraGramatica regra : regraExistentes) {
            if (regra.getNome().equals(aux[1])) {
                return regra.getTransicao();
            }
        }
        RegraGramatica regra= new RegraGramatica(aux[0],proximoEstado);
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
                    if(regra.getNome().equals(aux[1])){
                        processaToken(aux[0],false,regra.getTransicao());
                    }
                }
            }
            else{
                if(!tokensDaMatriz.contains(aux[0]))
                    tokensDaMatriz.add(aux[0]);
                //processaToken(aux[0],0,false);
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
        Transicao transicaoAux = new Transicao(aux,estado,transicao,0);
        transicoes.add(transicaoAux);
        }
    }

    //anota as regras da LR para fazer corretamente as transições da matriz
    private void existeRegra(String nomeRegra) {
        for(RegraGramatica regra:regraExistentes){
            if(regra.getNome().equals(nomeRegra)){
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
            criaLinha(transicao);
        achaLinha(transicao);

    }

    //adiciona a transicao de um token a uma estado da gramática
    private void achaLinha(Transicao transicao){
        String[] aux=retornaLinha(transicao);//verificar
//        if(transicao.estadoAtual==0)
//            aux=matrizAutomato.get(1);
        String[] tokens=matrizAutomato.get(0);
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

    //cria linha para adicionar na matriz do automato e preenche com estados de erro e o nome do estado de transicao.
    public void criaLinha(Transicao transicao) {
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
        for(String []a:matrizAutomato){
            for(String s:a)
                System.out.printf("\t%s\t|",s);
            System.out.println();
        }
    }

    public void printaAFD() {
        determinizaMatriz();
    }

    private void determinizaMatriz() {
        List<String[]> afd=new ArrayList<>();
        afd.add(matrizAutomato.getFirst());
        boolean determinismo=true;
        for(RegraGramatica regraGramatica:regraExistentes){
            String[]aux=retornaLinha(regraGramatica.getTransicao().toString());
            for(int i=1;i<aux.length;i++){
                if(aux[i].contains(",")){
                    String[] transicao=novaTransicao(aux[i]);
                    aux[i]="["+aux[i].replace(",","")+"]";
                    afd.add(aux);
                    afd.add(transicao);
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

        int numColumns = afd.getFirst().length;
        int[] columnWidths = new int[numColumns];
        for (String[] row : afd) {
            for (int i = 0; i < numColumns; i++) {
                columnWidths[i] = Math.max(columnWidths[i], row[i].length());
            }
        }
        for (String[] row : afd) {
            for (int i = 0; i < numColumns; i++) {
                System.out.print(String.format(" %-" + (columnWidths[i] + 2) + "s\t|", row[i]));
            }
            System.out.println();
        }

    }

    private String[] novaTransicao(String transicao) {
        String[] aux= transicao.split(",");
        String[] estado = new String[tokensDaMatriz.size()+1];
        String[] estadoCopiado;

        boolean estadoEhFinal= false;
        for(String a:aux){
            if(!ignorarEstados.contains(a.replace("*","")))
                ignorarEstados.add(a.replace("*",""));
            if(retornaLinha(a)[0].contains("*")){
                estado[0]="["+transicao.replace(",","")+"*]";
                estadoEhFinal=true;
                break;
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
        return estado;

    }
//
//    private void determinizaMatriz() {
//        boolean novoEstado=false;
//        List<String[]> afd= new ArrayList<String[]>();
//        afd.add(matrizAutomato.get(0));
//        for(String s:matrizAutomato.get(1)){
//            if(s.contains(",")){
//                afd.add(criaNovoEstado(s));//checar se o novo estado cria indeterminismo.
//                novoEstado=true;
//            }
//            if(novoEstado)
//                afd.add(matrizAutomato.get(1));
//
//        }
//        for(String[]a:afd){
//            for(String s:a){
//                System.out.printf("%s\t",s);
//            }
//            System.out.println();
//        }
//        }
//
//    private String[] criaNovoEstado(String estadosTransicoes) {
//        String[] aux = estadosTransicoes.split(",");
//        String[] novoEstado = new String[tokensDaMatriz.size()+1];
//        for(String estado:aux){
//            String[] transicaoAux=retornaLinha(estado);
//            if(novoEstado[0]!=null){
//                novoEstado[0]="["+novoEstado[0].replace("[","").replace("]","")+transicaoAux[0]+"]";
//            }
//            else {
//                novoEstado[0] = transicaoAux[0];
//            }
//            for(int k=1;k<tokensDaMatriz.size()+1;k++){
//                if(novoEstado[k]==null){
//                    novoEstado[k]=transicaoAux[k];
//                }
//                else
//                    novoEstado[k]="["+novoEstado[k].replace("[","").replace("]","") +transicaoAux[k]+"]";
//            }
//        }
//        return novoEstado;
//    }

}







