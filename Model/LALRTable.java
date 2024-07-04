package Model;

import java.util.ArrayList;
import java.util.List;

public class LALRTable
{
    public List<Simbolo>  simbolos;
    public List<Integer> estados;
    public List<List<AcaoLALR>>  acaoLALR;

    public LALRTable(){
        simbolos = new ArrayList<>();
        estados = new ArrayList<>();
        acaoLALR = new ArrayList<>();
    }

}
