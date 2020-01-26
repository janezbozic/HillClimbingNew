import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Random;

public class HillClimbing{

    static LinkedList<Mesto> mesta;

    public static void main (String []args) throws IOException {

        int numIterations = 50000;

        mesta = new LinkedList<>();

        double maxCap = read("Problem5.txt");

        int steviloTovornjakovOrganski = (int) (Math.ceil(vsotaSmeti(1) / maxCap));
        int steviloTovornjakovPlastika = (int) Math.ceil(vsotaSmeti(2) / maxCap);
        int steviloTovornjakovPapir = (int) Math.ceil(vsotaSmeti(3) / maxCap);

        Solution fs = firstSolution(steviloTovornjakovOrganski, steviloTovornjakovPlastika, steviloTovornjakovPapir, maxCap);

        fs.cena = fs.vsotaCen();

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

    private static Solution firstSolution(int steviloTovornjakovOrganski, int steviloTovornjakovPlastika, int steviloTovornjakovPapir, double maxCap) {

        int stevec = 0;

        Solution sol = new Solution(steviloTovornjakovOrganski, steviloTovornjakovPlastika, steviloTovornjakovPapir, mesta ,maxCap);

        dodajRandom(sol, 1);
        dodajRandom(sol, 2);
        dodajRandom(sol, 3);

        sol.initVsaMesta();

        return sol;

    }

    public static void dodajRandom(Solution sol, int tip){

        int stevec = 0;

        LinkedList<Tovornjak> tovornjaki = sol.getTovornjake(tip);

        while (sol.jeCisto(tip) > 0){
            int index = getRandomMesto();
            while (tovornjaki.get(stevec).pot.size() > 0 && tovornjaki.get(stevec).pot.get(tovornjaki.get(stevec).pot.size()-1) == index+1){
                index = getRandomMesto();
            }
            tovornjaki.get(stevec).pot.add(index+1);
            sol.setkolicina(index, tip, 0);
            stevec++;
            if (stevec == tovornjaki.size())
                stevec = 0;
        }

        for (int i = 0; i < tovornjaki.size(); i++){
            if (tovornjaki.get(i).pot.get(tovornjaki.get(i).pot.size()-1) != 1)
                tovornjaki.get(i).pot.add(1);
        }

    }

    public static int getRandomMesto (){
        Random rand = new Random();
        int n = rand.nextInt(mesta.size());
        return n;
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
