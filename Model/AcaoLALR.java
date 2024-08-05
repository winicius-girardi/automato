package Model;

public class AcaoLALR {


//  Model.Acao eh enum com
//    Shift = 1,
//    Reduce = 2,
//    GoTo = 3,
//    Aceitar = 4,
//    Erro = 5

    public Acao acao;
    public Integer valor;
    public Integer salto;

    public AcaoLALR(Integer valor,Integer acao,Integer salto){
        this.valor=valor;
        this.salto=salto;
        switch (acao) {
            case 1:
                this.acao=Acao.Shift;
                break;
            case 2:
                this.acao=Acao.Reduce;
                break;
            case 3:
                this.acao=Acao.GoTo;
                break;
            case 4:
                this.acao=Acao.Aceitar;
                break;
            case 5:
                this.acao= Acao.Erro;
                break;
        }

    }

    @Override
    public String toString() {
        return "AcaoLALR{" +
                "acao=" + acao +
                ", valor=" + valor +
                '}';
    }
}
