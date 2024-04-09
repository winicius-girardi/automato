import java.util.ArrayList;
import java.util.List;

public class Transicao {

    public String token;

    public Integer estadoTransicaoToken;

    public Integer estadoAtual;

    public boolean estadoFinal;


    public Transicao(String token, boolean estadoFinal,Integer estadoTransicaoToken,Integer estadoAtual) {
        this.estadoFinal = estadoFinal;
        this.token=token;
        this.estadoTransicaoToken=estadoTransicaoToken;
        this.estadoAtual=estadoAtual;
    }
}
