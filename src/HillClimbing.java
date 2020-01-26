import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class HillClimbing{

    static LinkedList<Tovornjak> tOrganski=new LinkedList<>();
    static LinkedList<Tovornjak> tPlastika=new LinkedList<>();
    static LinkedList<Tovornjak> tPapir=new LinkedList<>();
    public static double [] vsaMestaOrgranski;
    public static double [] vsaMestaPlastika;
    public static double [] vsaMestaPapir;

    static LinkedList<Mesto> mesta;
    static double maxCap;

    public static void main (String []args) throws IOException {

        int numIterations = 10000;

        mesta = new LinkedList<>();

        maxCap = read("Problem2.txt");
        vsaMestaOrgranski = new double [mesta.size()];
        vsaMestaPlastika = new double [mesta.size()];
        vsaMestaPapir = new double [mesta.size()];
        initVsaMesta();
        dijkstra(mesta.get(0), 0);
        greedyPot(1);
        greedyPot(2);
        greedyPot(3);

        for(int i=0;i<tOrganski.size();i++){
            tOrganski.get(i).clear();
        }
        for(int i=0;i<tPlastika.size();i++){
            tPlastika.get(i).clear();
        }
        for(int i=0;i<tPapir.size();i++){
            tPapir.get(i).clear();
        }
        Solution fs = new Solution(tOrganski, tPlastika, tPapir, mesta, maxCap);
        fs.cena=fs.vsotaCen();
        //fs.cena=cost(tOrganski)+cost(tPlastika) + cost(tPapir);

        Solution min = fs;

        for (int i=0;i<numIterations;i++){

            Solution nSol = new Solution(mesta, maxCap, min);

            if (nSol.cena < min.cena){
                min = nSol;
            }


        }


        System.out.println(min.jeCisto(1));
        System.out.println(min.jeCisto(2));
        System.out.println(min.jeCisto(3));
        System.out.println("\n\n");

        for (int i = 0; i<Math.max(Math.max(min.tOrganski.size(), min.tPlastika.size()), min.tPapir.size()); i++){
            if (i < min.tOrganski.size()){
                System.out.print(1);
                for (int j = 0; j<min.tOrganski.get(i).pot.size(); j++){
                    System.out.print("," + min.tOrganski.get(i).pot.get(j));
                }
                System.out.println();
            }
            if (i < min.tPlastika.size()){
                System.out.print(2);
                for (int j = 0; j<min.tPlastika.get(i).pot.size(); j++){
                    System.out.print("," + min.tPlastika.get(i).pot.get(j));
                }
                System.out.println();
            }
            if (i < min.tPapir.size()){
                System.out.print(3);
                for (int j = 0; j<min.tPapir.get(i).pot.size(); j++){
                    System.out.print("," + min.tPapir.get(i).pot.get(j));
                }
                System.out.println();
            }
        }

        System.out.println("\n\n" + min.cena);

    }

    private static double cost(LinkedList<Tovornjak> tovornjaki){
        double cena=tovornjaki.size()*10;
        double cas=0;
        for(int i=0;i<tovornjaki.size();i++){
            cas=tovornjaki.get(i).cas;
            if(cas>8*60){
                cena += 8*10;
                cena += (cas - 8*60) / 60 * 20;
            }
            else{
                cena+=cas*10/60;
            }
            cena+=tovornjaki.get(i).dolzinaPoti*0.1;
        }
        return cena;
    }

    public static double vsotaSmeti(int tip){
        double vsota = 0;
        for (int i = 0; i<mesta.size(); i++){
            if (tip == 1){
                vsota += mesta.get(i).organski;
            }
            else if (tip == 2){
                vsota += mesta.get(i).plastika;
            }
            else {
                vsota += mesta.get(i).papir;
            }
        }
        return vsota;
    }

    private static void greedyPot(int tip){
        double [] tab;

        if (tip == 1)
            tab = vsaMestaOrgranski;
        else if (tip == 2)
            tab = vsaMestaPlastika;
        else
            tab = vsaMestaPapir;

        while(jeCisto(tip)!=0) {
            Tovornjak t = new Tovornjak(tip);
            Mesto m1 = mesta.get(0);
            Mesto naslednje = new Mesto();
            int index=0;
            int test= lahkoPobere(t, m1, tab);
            while (index != -1) {
                t.pot.add(m1.index);
                t.pobrano+=tab[m1.index-1];
                //if(m1.index!=1 && tab[m1.index-1]>0)
                //t.cas+=12;
                tab[m1.index-1]=0;
                index = lahkoPobere(t, m1, tab);
                if(index==-1)
                    break;
                double razdalja = findShortest(m1, index);
                t.dolzinaPoti+=razdalja;
                t.cas+=razdalja*6/5;
                naslednje = mesta.get(index - 1);
                m1 = naslednje;
                //t.pobrano += tab[m1.index-1];
                //tab[m1.index-1]=0;
                /*if (index == -1) {
                    t.pot.add(m1.index);
                    t.pobrano+=tab[m1.index-1];
                    tab[m1.index-1]=0;
                }*/
            }
            t.cas+=12*(t.pot.size()-1);
            for (int i = m1.shortestPath.size() - 1; i >= 0; i--) {
                t.pot.add(m1.shortestPath.get(i).index);
                if(i==0)
                    break;

                // System.out.println("index: "+index+" t.size: "+t.pot.size());
//                System.out.println(m1.shortestPath.get(0).index+" "+m1.shortestPath.get(i).index);
                t.dolzinaPoti+=findShortest(m1, m1.shortestPath.get(i).index);
                m1=m1.shortestPath.get(i);
            }
            t.cas+=30;
            if(test==-1) {
                if(t.pot.size()!=0) {
                    t.pot.clear();
                    t.cas=0;
                    t.dolzinaPoti=0;
                }
                for (int i = 0; i < tab.length; i++) {
                    if (tab[i] != 0) {
                        Mesto curr=new Mesto();
                        double razdalja=0;
                        for (int j = 0; j < mesta.get(i).shortestPath.size(); j++) {
                            t.pot.add(mesta.get(i).shortestPath.get(j).index);
                            curr=mesta.get(i).shortestPath.get(j);
                            if(j==mesta.get(i).shortestPath.size()-1)
                                break;
                            razdalja=findShortest(curr, mesta.get(i).shortestPath.get(j+1).index);
                            t.dolzinaPoti+=razdalja;
                            t.cas+=razdalja*6/5;
                        }
                        razdalja=findShortest(curr, mesta.get(i).index);
                        t.dolzinaPoti+=razdalja;
                        t.cas+=razdalja*6/5;
                        t.pot.add(i+1);
                        t.cas+=12;
                        curr=mesta.get(i);
                        razdalja=findShortest(curr, mesta.get(i).shortestPath.get(mesta.get(i).shortestPath.size()-1).index);
                        t.dolzinaPoti+=razdalja;
                        t.cas+=razdalja*6/5;
                        for (int j = mesta.get(i).shortestPath.size() - 1; j >= 0; j--) {
                            t.pot.add(mesta.get(i).shortestPath.get(j).index);
                            if(j==0)
                                break;
                            curr=mesta.get(i).shortestPath.get(j);
                            razdalja=findShortest(curr, mesta.get(i).shortestPath.get(j-1).index);
                            t.dolzinaPoti+=razdalja;
                            t.cas+=razdalja*6/5;
                        }

                        t.cas+=30;
                        t.pobrano+=tab[i];
                        tab[i]=0;
                        break;
                    }
                }
            }
            if (tip == 1)
                tOrganski.add(t);
            else if (tip == 2)
                tPlastika.add(t);
            else
                tPapir.add(t);
        }
    }

    public static void initVsaMesta() {
        for (int i = 0; i<mesta.size(); i++){
            vsaMestaOrgranski[i] = mesta.get(i).getOdpadki(1);
            vsaMestaPlastika[i] = mesta.get(i).getOdpadki(2);
            vsaMestaPapir[i] = mesta.get(i).getOdpadki(3);
        }
    }


    private static double findShortest(Mesto trenutno, int naslednje){
        double min=Double.MAX_VALUE;
        //
        //System.out.println(trenutno.index+" "+naslednje);

        for(int i=0;i<trenutno.sosedje.get(naslednje).size();i++){
            if(trenutno.sosedje.get(naslednje).get(i).velikost<min)
                min=trenutno.sosedje.get(naslednje).get(i).velikost;
        }
        return min;
    }

    public static int jeCisto (int tip){

        int count = 0;

        for (int i = 0; i<vsaMestaOrgranski.length; i++){
            if (tip == 1 && vsaMestaOrgranski[i] > 0)
                count++;
            else if (tip == 2 && vsaMestaPlastika[i] > 0)
                count++;
            else if (tip == 3 && vsaMestaPapir[i] > 0)
                count++;
        }

        return count;
    }

    private static int lahkoPobere(Tovornjak t, Mesto trenutno, double[] tab){
        double min=Double.MAX_VALUE;
        int index=-1;
        if(t.vrstaSmeti==1) {
            for (int i = 0; i < trenutno.sosedjeIndex.size(); i++) {
                if (t.pobrano + mesta.get(trenutno.sosedjeIndex.get(i)-1).organski <= maxCap && tab[trenutno.sosedjeIndex.get(i)-1] > 0) {
                    for(int j=0;j<trenutno.sosedje.get(trenutno.sosedjeIndex.get(i)).size();j++){
                        if (trenutno.sosedje.get(trenutno.sosedjeIndex.get(i)).get(j).kapaciteta >=t.pobrano && trenutno.sosedje.get(trenutno.sosedjeIndex.get(i)).get(j).velikost < min) {
                            min=trenutno.sosedje.get(trenutno.sosedjeIndex.get(i)).get(j).velikost;
                            index = trenutno.sosedjeIndex.get(i);
                        }
                    }
                }
            }
        }
        else if(t.vrstaSmeti==2){
            for (int i = 0; i < trenutno.sosedjeIndex.size(); i++) {
                if (t.pobrano + mesta.get(trenutno.sosedjeIndex.get(i)-1).plastika <= maxCap && tab[trenutno.sosedjeIndex.get(i)-1] > 0) {
                    for(int j=0;j<trenutno.sosedje.get(trenutno.sosedjeIndex.get(i)).size();j++){
                        if (trenutno.sosedje.get(trenutno.sosedjeIndex.get(i)).get(j).kapaciteta >=t.pobrano && trenutno.sosedje.get(trenutno.sosedjeIndex.get(i)).get(j).velikost<min)
                            index=trenutno.sosedjeIndex.get(i);
                    }
                }
            }
        }
        else{
            for (int i = 0; i < trenutno.sosedjeIndex.size(); i++) {
                if (t.pobrano + mesta.get(trenutno.sosedjeIndex.get(i)-1).papir <= maxCap && tab[trenutno.sosedjeIndex.get(i)-1] > 0) {
                    for(int j=0;j<trenutno.sosedje.get(trenutno.sosedjeIndex.get(i)).size();j++){
                        if (trenutno.sosedje.get(trenutno.sosedjeIndex.get(i)).get(j).kapaciteta >=t.pobrano && trenutno.sosedje.get(trenutno.sosedjeIndex.get(i)).get(j).velikost<min)
                            index=trenutno.sosedjeIndex.get(i);
                    }
                }
            }
        }

        return index;
    }

    private static Mesto getLowestDistanceMesto(Set<Mesto> neobravnavani) {
        Mesto lowestDistanceMesto = new Mesto();
        double lowestDistance = Double.MAX_VALUE;
        for (Mesto mesto: neobravnavani) {
            double nodeDistance = mesto.getDistSource();
            if (nodeDistance < lowestDistance) {
                lowestDistance = nodeDistance;
                lowestDistanceMesto = mesto;
            }
        }
        return lowestDistanceMesto;
    }

    private static void CalculateMinimumDistance(Mesto evaluationMesto, double razdalja, Mesto sourceMesto) {
        double sourceDistance = sourceMesto.getDistSource();
        if (sourceDistance + razdalja < evaluationMesto.getDistSource() && evaluationMesto.sosedje.containsKey(sourceMesto.index)) {
            evaluationMesto.setDistSource(sourceDistance + razdalja);
            LinkedList<Mesto> shortestPath = new LinkedList<>(sourceMesto.getShortestPath());
            shortestPath.add(sourceMesto);
            evaluationMesto.setShortestPath(shortestPath);
        }
    }

    public static void dijkstra(Mesto trenutno, double teza){
        Mesto source = trenutno;
        source.setDistSource(0);
        HashSet<Mesto> obravnavani=new HashSet<>();
        HashSet<Mesto> neobravnavani=new HashSet<>();

        neobravnavani.add(source);
        while(neobravnavani.size()!=0){
            Mesto current = new Mesto();
            current=getLowestDistanceMesto(neobravnavani);
            neobravnavani.remove(current);
            for(Map.Entry<Integer, LinkedList<Razdalja>> sosedRazd:
                    current.getSosedje().entrySet()){
                Mesto sosed = mesta.get(sosedRazd.getKey() - 1);
                double razdalja = Double.MAX_VALUE;
                for (int i = 0; i < sosedRazd.getValue().size(); i++) {
                    if (sosedRazd.getValue().get(i).velikost < razdalja && teza <= sosedRazd.getValue().get(i).kapaciteta)
                        razdalja = sosedRazd.getValue().get(i).velikost;
                }
                if (!obravnavani.contains(sosed)) {
                    CalculateMinimumDistance(sosed, razdalja, current);
                    neobravnavani.add(sosed);
                }

            }
            obravnavani.add(current);
        }
    }

    private static double read(String s) throws IOException {

        File file = new File(s);

        BufferedReader br = new BufferedReader(new FileReader(file));

        String [] line1 = br.readLine().split(",");
        int steviloMest = Integer.parseInt(line1[0]);
        double maxCap = Double.parseDouble(line1[1]);

        for (int i = 0; i<steviloMest; i++){
            String [] line = br.readLine().split(",");
            Mesto m = new Mesto(Integer.parseInt(line[0]), Double.parseDouble(line[1]), Double.parseDouble(line[2]),  Double.parseDouble(line[3]),  Double.parseDouble(line[4]),  Double.parseDouble(line[5]));
            mesta.add(m);
        }

        String st;
        while ((st = br.readLine()) != null){
            String [] line = st.split(",");
            Mesto m1 = mesta.get(Integer.parseInt(line[0])-1);
            Mesto m2 = mesta.get(Integer.parseInt(line[1])-1);
            Razdalja r = new Razdalja(Double.parseDouble(line[4]), Double.parseDouble(line[2]));
            if (Integer.parseInt(line[3]) == 0){
                if (m1.sosedje.get(m2.index) == null) {
                    m1.sosedje.put(m2.index, new LinkedList<Razdalja>());
                }
                m1.sosedje.get(m2.index).add(r);
                if (!m1.sosedjeIndex.contains(m2.index))
                    m1.sosedjeIndex.add(m2.index);
                if (m2.sosedje.get(m1.index) == null) {
                    m2.sosedje.put(m1.index, new LinkedList<Razdalja>());
                }
                m2.sosedje.get(m1.index).add(r);
                if (!m2.sosedjeIndex.contains(m1.index))
                    m2.sosedjeIndex.add(m1.index);
            }
            else {
                if (m1.sosedje.get(m2.index) == null) {
                    m1.sosedje.put(m2.index, new LinkedList<Razdalja>());
                }
                m1.sosedje.get(m2.index).add(r);
                if (!m1.sosedjeIndex.contains(m2.index))
                    m1.sosedjeIndex.add(m2.index);
            }
        }

        return maxCap;

    }

}
