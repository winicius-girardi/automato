public class Transicao {

    public String token;

    public Integer estadoTransicaoToken;

    public Integer estadoAtual;

    public boolean estadoFinal;

    public boolean terminal;

    public Transicao(String token, boolean estadoFinal,Integer estadoTransicaoToken,Integer estadoAtual) {
        this.estadoFinal = estadoFinal;
        this.token=token;
        this.estadoTransicaoToken=estadoTransicaoToken;
        this.estadoAtual=estadoAtual;
        this.terminal=false;
    }
    public Transicao(String token, boolean estadoFinal,Integer estadoTransicaoToken,Integer estadoAtual,boolean terminal) {
        this.estadoFinal = estadoFinal;
        this.token=token;
        this.estadoTransicaoToken=estadoTransicaoToken;
        this.terminal=terminal;
        this.estadoAtual=estadoAtual;

    }
}
