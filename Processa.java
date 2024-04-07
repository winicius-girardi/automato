import java.util.ArrayList;
import java.util.List;

public class Processa {

    private int ultimoEstado=0;

    private int proximoEstado=1;

    private List<String> tokensDaMatriz;

    private List<Transicao> transicoes;

    private List<Integer> estadosFinais;



    public Processa(){
        tokensDaMatriz = new ArrayList<>();
        transicoes= new ArrayList<>();
        estadosFinais= new ArrayList<>();
    }

    public void processaLinha(String linha){

        if(linha.matches("[a-zA-Z]+"))
            processaPalavra(linha);
        else
            processaGramatica(linha);
    }

    public void processaPalavra (String  linha){
        String[] aux = linha.split("");
        Transicao transicao = new Transicao();

        for(int i=0; i<aux.length;i++){
            if(!tokensDaMatriz.contains(aux[i])){
                tokensDaMatriz.add(aux[i]);
            }
            processaEstado(aux[i],transicao);

        }
        estadosFinais.add(ultimoEstado);
        transicoes.add(transicao);


    }

    public void processaEstado(String token,Transicao transicao){
        transicao.tokenTransicao.add(token);
        transicao.estadoTransicao.add(proximoEstado);
        proximoEstado++;
        ultimoEstado++;
    }


    public void processaGramatica (String  linha){

    }

    public void exibeMatrizAFND(){
        System.out.println("Matriz AFND");
        System.out.print("\t| ");
        for (String token : tokensDaMatriz){
            System.out.print(token + " | " );
        }
        System.out.println();
        for (Transicao transicao : transicoes){

            for(String token:transicao.tokenTransicao){
                System.out.print(transicao.estadoTransicao.get(transicao.tokenTransicao.indexOf(token))-1);
                printaLinhaMatriz(tokensDaMatriz.indexOf(token),transicao);
                System.out.println();
            }
        }

    }

    public void printaLinhaMatriz(int index,Transicao transicao) {
        System.out.print("\t| ");
        for (String token : tokensDaMatriz){
            if(token.equals(tokensDaMatriz.get(index))){
                System.out.print( transicao.estadoTransicao.get(transicao.tokenTransicao.indexOf(token))+ " | ");
                continue;
            }
            System.out.print( "  | " );
        }

    }


}
