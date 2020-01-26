import java.util.HashMap;
import java.util.LinkedList;

public class Mesto {

    double organski, plastika, papir;
    double x, y;
    int index;
    HashMap<Integer, LinkedList<Razdalja>> sosedje;
    LinkedList<Integer> sosedjeIndex;
    double oldOrganski, oldPlastika, oldPapir;

    public Mesto (int i, double x1, double y1, double o, double p, double pa){

        organski = o;
        plastika = p;
        papir = pa;

        x = x1;
        y = y1;

        index = i;
        sosedje = new HashMap<>();
        sosedjeIndex = new LinkedList<>();

        oldOrganski = o;
        oldPlastika = p;
        oldPapir = p;

    }

    public double getOdpadki (int tip){
        switch (tip){
            case 1: return organski;
            case 2: return plastika;
            default: return papir;
        }
    }

    public void resetOrganski (){
        organski = oldOrganski;
    }

    public void resetPlastika(){
        plastika = oldPlastika;
    }

    public void resetPapir(){
        papir = oldPapir;
    }

    public void setOdpadki(int tip, int k){
        if (tip == 1)
            organski = k;
        else if (tip == 2)
            plastika = k;
        else
            papir = k;
    }
}
