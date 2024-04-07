package teste;

import teste.Matriz;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Automato {

    private int ultimoEstado=0;

    private List<String> tokensDaMatriz;

    private Matriz matriz;

    public Automato(){

    }

    public void ProcessaLinha(String linha) {
        String regex = "<[A-Z]> ::=";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(linha);
        if (matcher.find()) {
            ProcessaToken(linha);
        }
        else{
            ProcessaPalavra(linha);
        }
    }

    private void ProcessaPalavra(String linha) {
        String []aux=linha.split("");
        for(String token:aux){
            if(!tokensDaMatriz.contains(token))
                adicionaTokenNaMatriz(token);
        }

    }

    public void ProcessaToken(String linha){
        linha=linha.replaceAll("<[A-Z]> ::=","");
        String []aux=linha.split("[|]");
        String []processa;
        for(String token:aux){
            processa=token.split("<");
            if(!tokensDaMatriz.contains(processa[0]))
                adicionaTokenNaMatriz(processa[0]);
            adicionaEstadoDeTransicao(processa[1],processa[0]);

        }

    }

    private void adicionaEstadoDeTransicao(String estado,String token) {
        estado=estado.replace("> ","");
        //matriz.adicionaToken(estado,token);

    }

    public void adicionaTokenNaMatriz(String token){
        tokensDaMatriz.add(token);
    }




}