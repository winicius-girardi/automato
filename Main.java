
public class Main {



    public static void main(String[] args) {


        Processa processa = new Processa();

        String teste = "token";
        String teste1="alo";
        processa.processaLinha(teste1);
        processa.processaLinha(teste);
        processa.processaLinha("<S> ::= a<A> | e<A> | i<A> | o<A> | u ");
        processa.processaLinha("<A> ::= a<A> | e<A> | i<A> | o<A> | u<A> | ε");
        System.out.println("Matriz AFND:");
        processa.printaMatrizAutomato();
        System.out.println("\nMatriz AFD:");
        processa.printaAFD();//nao funciona ainda


    }
}
