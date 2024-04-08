
public class Main {



    public static void main(String[] args) {


        Processa processa = new Processa();

        String teste = "token";
        String teste1="alo";
        processa.processaLinha(teste1);
        processa.processaLinha(teste);
        processa.exibeMatrizAFND();
        System.out.println("qtd colunas " +processa.tokensDaMatriz.size());

    }
}
