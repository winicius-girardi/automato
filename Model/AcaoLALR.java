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

    public AcaoLALR(Integer valor,Integer acao){
        this.valor=valor;
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

}
