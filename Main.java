import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main {



    public static void main(String[] args) {


        Processa processa = new Processa();
        String nomeArquivo = "entrada_automato.txt";
        List<Fita> fita = new ArrayList<>();
        fita.add(new Fita("-", 0));
        int numeroLinha = 1;
        try (BufferedReader br = new BufferedReader(new FileReader(nomeArquivo))) {
            String linha;
            while ((linha = br.readLine()) != null) {
                if (linha.matches("^[a-z]+")) {
                    fita.add(new Fita(linha, numeroLinha));
                    numeroLinha++;
                }
                processa.processaLinha(linha);
            }
        } catch (IOException e) {
            System.err.println("Erro ao ler o arquivo: " + e.getMessage());
        }

        while (true) {
            if (processa.fita.size() > fita.size()) {
                fita.add(new Fita("fechamentoQualquer", numeroLinha));
                continue;
            }
            break;
        }

        System.out.println("Matriz AFND:");
        processa.printaMatrizAutomato();
        System.out.println("\nMatriz AFD:");
        processa.printaAFD();

        System.out.println("\nFita Saída AFND:\n");
        for(String a:processa.fita) {
            System.out.print(a+"\t");

        }


        System.out.println("\n\nTabela Saida AFND\n");
        int k=0;
        for(Fita f: fita){
            System.out.println("Token:\t"+f.label);
            System.out.println("Linha:\t"+f.linha);
            System.out.println("Estado:\t"+processa.fita.get(k)+"\n\n");
            f.estado=processa.fita.get(k);
            k++;
        }


        System.out.println("\nFita Saída AFND:\n");

        for(String[]a:processa.afd){
            if(a[0].contains("*")){
                System.out.print(a[0].replace("*","")+"\t");
            }
        }

        System.out.println("\n\nTabela Saida AFD\n");

        for(Fita f: fita){
            String s = verificaEstado(f.estado, processa, f);
            System.out.println("Token:\t"+f.label);
            System.out.println("Linha:\t"+f.linha);
            System.out.println("Estado:\t"+ s +"\n\n");
            k++;
        }
    }

    public static String verificaEstado(String s,Processa processa,Fita f) {
        if(!processa.fita.contains(s)){
            return s;
        }
        for(RegraMudadas regra:processa.regraMudadas){
            for(String a:regra.antigas){
                if(a.equals(s)){
                    if(processa.fita.contains(s)&&f.label.contains("fechamentoQualquer")){
                        System.out.println("Token:\t"+f.label);
                        System.out.println("Linha:\t"+f.linha);
                        System.out.println("Estado:\t"+f.estado+"\n\n");
                    }
                    return regra.novaRegra;
                }
            }
        }
        return s;
    }

}
