
public class Main {



    public static void main(String[] args) {


        Processa processa = new Processa();

        String teste = "token";
        String teste1="alo";
        processa.processaLinha(teste);
        processa.processaLinha(teste1);
        //processa.processaLinha("<S> ::= a<A> | e<A> | i<A> | o<A> | u<A>");
        processa.printaMatrizAutomato();

    }
}
