public class RegraGramatica {


    private String nome;
    private Integer transicao;





    public RegraGramatica(String nome, Integer transicao) {
        this.nome = nome;
        this.transicao = transicao;
    }

    public void setTransicao(Integer transicao) {
        this.transicao =transicao;
    }
    public Integer getTransicao() {
        return transicao;
    }

    public String getNome() {
        return nome;
    }
}
