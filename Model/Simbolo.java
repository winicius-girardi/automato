package Model;

public class Simbolo {

    private String nome;
    private Integer indice;

public Simbolo(String nome, Integer indice) {
    this.nome = nome;
    this.indice = indice;
}

    @Override
    public String toString() {
        return "Simbolo{" +
                "nome='" + nome + '\'' +
                ", indice=" + indice +
                '}';
    }
}

