import java.util.HashMap;
import java.util.LinkedList;

public class Mesto {

    double organski, plastika, papir;
    double x, y;
    int index;
    HashMap<Integer, LinkedList<Razdalja>> sosedje;
    LinkedList<Integer> sosedjeIndex;
    double oldOrganski, oldPlastika, oldPapir;
    LinkedList<Mesto> shortestPath;
    double distSource=Double.MAX_VALUE;

    public Mesto (int i, double x1, double y1, double o, double p, double pa){

        organski = o;
        plastika = p;
        papir = pa;

        x = x1;
        y = y1;

        index = i;
        sosedje = new HashMap<>();
        sosedjeIndex = new LinkedList<>();
        shortestPath=new LinkedList<>();


        oldOrganski = o;
        oldPlastika = p;
        oldPapir = p;

    }

    public Mesto(){
        sosedjeIndex=new LinkedList<>();
        sosedje=new HashMap<>();
        shortestPath=new LinkedList<>();
    }

    public double getOdpadki (int tip){
        switch (tip){
            case 1: return organski;
            case 2: return plastika;
            default: return papir;
        }
    }

    public void setDistSource(double distSource) {
        this.distSource = distSource;
    }

    public double getDistSource() {
        return distSource;
    }

    public HashMap<Integer, LinkedList<Razdalja>> getSosedje() {
        return sosedje;
    }

    public void setSosedje(HashMap<Integer, LinkedList<Razdalja>> sosedje) {
        this.sosedje = sosedje;
    }

    public LinkedList<Mesto> getShortestPath() {
        return shortestPath;
    }

    public void setShortestPath(LinkedList<Mesto> shortestPath) {
        this.shortestPath = shortestPath;
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
