import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SuppressWarnings("ALL")
public class Processa {

    private int ultimoEstado = 0;

    private int proximoEstado = 1;

    public List<String> tokensDaMatriz;

    private List<Transicao> transicoes;

    public int lengthPalavra;

    private List<String[]> matrizAutomato;

    private Integer ultimaLinhaMatriz=0;

    private List<RegraGramatica> regraExistentes;

    public Processa() {
        tokensDaMatriz = new ArrayList<>();
        transicoes = new ArrayList<>();
        matrizAutomato = new ArrayList<>();
        regraExistentes = new ArrayList<>();
        regraExistentes.add(new RegraGramatica("S",0));
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
    }

    private void processaTransicaoInicial(String linha) {
        linha = linha.substring(7);
        String[] tokens = linha.split("\\|");
        for(String a:tokens){
            String []aux=a.replace("<","").replace(">","").replace(" ","").split("");
            if(a.replace(" ", "").matches("^[a-z]<[a-zA-Z]>$")){
                if(!tokensDaMatriz.contains(aux[0]))
                    tokensDaMatriz.add(aux[0]);
                if(existeRegra(aux[1], proximoEstado)){
                    for(RegraGramatica regra:regraExistentes){
                        if(regra.getNome().equals(aux[1])){
                            processaToken(aux[0],false,regra.getTransicao());
                        }
                    }
                }
                else {
                    processaToken(aux[0], 0, false);
                }}
        }
    }

    private void processaToken(String aux, boolean estado, Integer transicao) {
        Transicao transicaoAux = new Transicao(aux,estado,transicao,0);
        transicoes.add(transicaoAux);
    }

    //anota as regras da LR para fazer corretamente as transições da matriz
    private boolean existeRegra(String nomeRegra, Integer proximoEstado) {
        for(RegraGramatica regra:regraExistentes){
            if(regra.getNome().equals(nomeRegra)){

                return true;
            }
        }
        regraExistentes.add(new RegraGramatica(nomeRegra,proximoEstado));
        return false;
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
        Arrays.fill(aux, "erro");
        aux[0]=aux[0]+"*";
        matrizAutomato.add(aux);
    }

    private void adicionaTransicao() {
        String[]aux = new String[tokensDaMatriz.size()+1];
        aux[0]=" ";
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
        String[] aux=matrizAutomato.get(transicao.estadoTransicaoToken);
        if(transicao.estadoAtual==0)
            aux=matrizAutomato.get(1);
        String[] tokens=matrizAutomato.get(0);
        if(transicao.estadoFinal)
            aux[0]=aux[0]+"*";
        for (int k=1;k<tokensDaMatriz.size()+1;k++){
            if(tokens[k].equals(transicao.token)){
                aux[k]=transicao.estadoTransicaoToken.toString();
                break;
            }
        }
    }

    //cria linha para adicionar na matriz do automato e preenche com estados de erro e o nome do estado de transicao.
    public void criaLinha(Transicao transicao) {
        String []aux = new String[tokensDaMatriz.size()+1];
        for (int i=1;i<aux.length;i++){
            aux[i] = "erro";
        }

        aux[0]=ultimaLinhaMatriz.toString();
        ultimaLinhaMatriz++;
        matrizAutomato.add(aux);
    }

    //verifica se o regra da transicao da gramatica existe na matriz.
    private boolean existeTransicao(Transicao transicao) {
        for(String[]aux:matrizAutomato){
            if(aux[0].equals(transicao.estadoAtual.toString()))
                return true;
        }
        return false;
    }
    public void printaMatrizAutomato() {
        montaMatrizAutomato();
        for(String []a:matrizAutomato){
            for(String s:a){
                System.out.printf("%s\t",s);
            }
            System.out.println();
        }
    }
}







