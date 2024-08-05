package Model;

import java.util.ArrayList;
import java.util.List;

public class Producao {

    public Integer indice;
    public Integer indiceNaoTerminal;
    public List<Integer> simbolosProducao;


    public Producao(Integer indice, Integer indiceNaoTerminal) {
        this.indice = indice;
        this.indiceNaoTerminal = indiceNaoTerminal;
        simbolosProducao = new ArrayList<Integer>();
    }

    @Override
    public String toString() {
        return "Producao{" +
                "indice=" + indice +
                ", indiceNaoTerminal=" + indiceNaoTerminal +
                ", simbolosProducao=" + simbolosProducao +
                '}';
    }
}


