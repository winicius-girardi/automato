import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Main {



    public static void main(String[] args) {


        Processa processa = new Processa();
        String nomeArquivo = "entrada_automato.txt";

        try (BufferedReader br = new BufferedReader(new FileReader(nomeArquivo))) {
            String linha;
            while ((linha = br.readLine()) != null) {
                processa.processaLinha(linha);
            }
        } catch (IOException e) {
            System.err.println("Erro ao ler o arquivo: " + e.getMessage());
        }
        System.out.println("Matriz AFND:");
        processa.printaMatrizAutomato();
        System.out.println("\nMatriz AFD:");
        processa.printaAFD();


    }
}
