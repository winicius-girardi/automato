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


    public Processa() {
        tokensDaMatriz = new ArrayList<>();
        transicoes = new ArrayList<>();
        matrizAutomato = new ArrayList<>();
    }

    public void processaLinha(String linha) {

        if (linha.matches("[a-zA-Z]+"))
            processaPalavra(linha);
        else
            processaGramatica(linha);
    }

    private void processaGramatica(String linha) {
    }

    public void processaPalavra(String linha) {

        String[] aux = linha.split("");
        lengthPalavra=aux.length;
        for (int indexAtual=0; indexAtual < aux.length; indexAtual++) {
            if (!tokensDaMatriz.contains(aux[indexAtual])) {
                tokensDaMatriz.add(aux[indexAtual]);
            }
            processaToken(aux[indexAtual],indexAtual);
        }
    }
    private void processaToken(String s,int indexAtual) {
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

        if(indexAtual==lengthPalavra-1) {
            Transicao a = new Transicao(null,true,proximoEstado,ultimoEstado);
            proximoEstado++;
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

    public void criaLinha(Transicao transicao) {
        String []aux = new String[tokensDaMatriz.size()+1];
        for (int i=1;i<aux.length;i++){
            aux[i] = "erro";
        }

        aux[0]=ultimaLinhaMatriz.toString();
        ultimaLinhaMatriz++;
        matrizAutomato.add(aux);
    }

    private boolean existeTransicao(Transicao transicao) {
        return matrizAutomato.size()<transicao.estadoAtual;
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







