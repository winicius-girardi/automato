import Model.Fita;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main {



    public static void main(String[] args) {


        Processa processa = new Processa();
        String nomeArquivo = "Entrada/entrada_automato.txt";
        List<Fita> fita = new ArrayList<>();
        int numeroLinha = 1;
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
        System.out.println("\nModel.Fita\n");



        String arquivoProcessa = "Entrada/entrada_reconhece.txt";
        String[] estadoInicial ;
        estadoInicial= processa.afd.get(2);
        try (BufferedReader br = new BufferedReader(new FileReader(arquivoProcessa))) {
            String token;
            String[] auxToken;
            while ((token = br.readLine()) != null) {
                auxToken=token.split(" ");
                for(String t:auxToken){
                    Fita aux = new Fita(t, numeroLinha);
                    aux.estado=processa.validaToken(t,0,estadoInicial);
                    fita.add(aux);

                }
                numeroLinha++;
            }
        } catch (IOException e) {
            System.err.println("Erro ao ler o arquivo: " + e.getMessage());
        }
        System.out.println("\nTabela de símbolos:\n");
        for(Fita f: fita){
            System.out.println("Token:\t"+f.label);
            System.out.println("Linha:\t"+f.linha);
            System.out.println("Estado:\t"+f.estado+"\n\n");
        }

        System.out.println("\nModel.Fita saída\n");
        System.out.print("Estados:\t");
        for(Fita f:fita){
            System.out.print(f.estado+"\t");
        }
        System.out.println();
        AnaliseSintatica analisa = new AnaliseSintatica();
        analisa.leSimbolos();
        analisa.leProducoes();
        analisa.leLALR();
        //analisa.exibeSimbolos();
        var erros=analisa.analisa(fita);
        for(String i:erros){
            System.out.println(i);
        }

    }
}
//<S> ::= 'if' <Id> | 'print'<C> | 'id'<H>
//<Id> ::= 'id' <Op> 'id' <A>|'id' <Op> 'number' <A>
//<Op> ::= '>'| '<'| '=='
//<A> ::= '{'<B>
//<B> ::= '}'<I>
//<C> ::= '('<D> | '('<E>
//<D> ::= 'id'<Op2><F>| 'id'<F>
//<Op2>::= '+'<D> | '-' <D> |
//<E> ::= 'string'<F>
//<F> ::=')'<G> |
//<G> ::=';'
//<H> ::= '='
//<I> ::= 'else'<Id> |