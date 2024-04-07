package teste;

import java.util.List;
import java.util.Vector;

public class Matriz {

    private String [][]matriz;

    private int ultimaLinha=0;
    private int ultimaColuna=1;

    private int quantdadeLinhasMatriz=0;
    private int quantidadeColunasMatriz=0;


    /*public void adicionaToken(String estado, String token){
        matriz[ultimaLinha][0]=estado;
        matriz[ultimaLinha][ultimaColuna]=token;
        ultimaLinha++;
        ultimaColuna++;
    }
*/
    public void tokenExisteNaMatriz(String token){
        for(int i=0;i<ultimaLinha;i++){
            if(matriz[i][0].equals(token))
                return;
        }
        adicionaToken(token);

    }

    public void adicionaToken(String token){
        matriz[ultimaLinha][0]=token;
        ultimaLinha++;
    }
    public void aumentaLinhasMatriz(){
        String [][]aux=new String[quantdadeLinhasMatriz+1][quantidadeColunasMatriz];
        for(int i=0;i<quantdadeLinhasMatriz;i++){
            System.arraycopy(matriz[i], 0, aux[i], 0, quantidadeColunasMatriz);
        }
        quantdadeLinhasMatriz++;
        matriz=aux;
    }

    public void aumentaColunasMatriz(){
        String [][]aux=new String[quantdadeLinhasMatriz][quantidadeColunasMatriz+1];
        for(int i=0;i<quantdadeLinhasMatriz;i++){
            for(int j=0;j<quantidadeColunasMatriz;j++){
                aux[i][j]=matriz[i][j];
            }
        }
        quantidadeColunasMatriz++;
        matriz=aux;
    }

}
