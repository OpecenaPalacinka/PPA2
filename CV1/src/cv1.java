import java.io.File;
import java.io.FileNotFoundException;
import java.util.Random;
import java.util.Scanner;

public class cv1 {
    /**
     * Metoda rozhoduje, zda se predana hodnota x nachazi v predanem serazenem poli
     * @param data vstupni pole serazenych hodnot od nejmensi po nejvetsi
     * @param x hledana hodnota
     * @return true, kdyz se x nachazi v poli data, jinak false
     */
    static boolean intervalSubdivision(int[] data, int x) {
        int left = 0; //leva hranice intervalu
        int right = data.length - 1; //prava hranice intervalu DEBUG - potřeba ubrat -1
        int mid = (left + right) / 2; //index uprostred intervalu
        while (data[mid] != x) {
            if (left == right || left>right) { //DEBUG přidaná podmínka - pokud je levá hranice větší než pravá tak konec
                return false;
            }
            //nyni zmensime interval
            if (data[mid] > x) {
                right = mid - 1;
            }
            else {
                left = mid + 1;
            }
            mid = (left + right) / 2;
        }
        return true;
    }

    public static int[] generateSequence(){
        Random random = new Random();
        int sizeOfSequence = random.nextInt(100000-10000)+10000;
        int[] sequence = new int[sizeOfSequence];
        int rn=(int) (Math.random() * (100 - 10 + 1) + 10);
        sequence[0] = rn;
        for(int i=1;i<sequence.length;i++) {
            int rand=(int) (Math.random() * (100 - 10 + 1) + 10);
            rn = sequence[i-1]+rand;
            sequence[i] = rn;
        }

        return sequence;
    }

    static boolean sequentialSearch(int[] data, int x){
        for (int i : data) {
            if (i==x){
                return true;
            }   
        }
        return false;
    }

    /**
     * Metoda kontroluje zda je předaná posloupnost seřazená
     * @param data Posloupnost, kterou chceme zkontrolovat
     * @return  True pokud je posloupnost seřazená
     */
    private static boolean isSorted(int[] data) {
        boolean jeSerazene=true;
        for(int i=0;i<data.length-1;i++) {
            if (data[i] > data[i + 1]) {
                jeSerazene = false;
                break;
            }
        }

        return jeSerazene;
    }

    public static int[] loadData(File file) throws FileNotFoundException {
        Scanner sc = new Scanner(file);
        int i = 0;
        int j = 0;
        while (sc.hasNextLine()){
            sc.nextLine();
            i++;
        }
        int[] data = new int[i];
        sc.close();
        sc = new Scanner(file);
        while (sc.hasNextLine()){
            data[j] = Integer.parseInt(sc.nextLine());
            j++;
        }
        return data;
    }

    public static void getInfo(int[] data){
        float celkemSeq = 0;
        float celkemInter = 0;
        for (int a = 0; a < 10000; a++) {
            int find = (int) ((Math.random() * (data[data.length-1] - data[0])) + data[0]);
            long start1 = System.nanoTime();
            sequentialSearch(data, find);
            long stop1 = System.nanoTime();
            celkemSeq += stop1 - start1;
            //System.out.println("Sequential search finished in " + (stp - strt) + " ns");
            //System.out.println("Number found: " + fnd);


            long start2 = System.nanoTime();
            intervalSubdivision(data, find);
            long stop2 = System.nanoTime();
            //System.out.println("Interval subdivision finished in " + (stop - start) + " ns");
            //System.out.println("Number found: " + found);
            celkemInter += stop2 - start2;
        }
        System.out.println("Počet prvků posloupnosti: "+data.length);
        System.out.println("Doba nutná pro vyhledání 10 000 prvků pomocí půlení intervalů: "+celkemInter);
        System.out.println("Doba nutná pro vyhledání 10 000 prvků pomocí sekvenčního vyhledávání: "+celkemSeq);
        System.out.println("Sekvenční je: "+celkemSeq/celkemInter+"x rychlejší!");
        System.out.println();
    }

    public static void main(String[] args) throws FileNotFoundException {
        /* Cvičení 1
        int[] data = new int[] {1, 3, 5, 41, 48, 52, 63, 71};
        int x = 72;
        boolean found;
        boolean foundSequ;
        long start;
        long stop;
        if (isSorted(data)){
            start = System.nanoTime();
            found = intervalSubdivision(data, x);
            stop = System.nanoTime();

            foundSequ = sequentialSearch(data,x);

            System.out.println("Interval subdivision finished in " + (stop - start) + " ns");
            / Ověření správnosti metody dělení intervalem /
            if (found == foundSequ){
                System.out.println("Both metodes work!");
            } else {
                System.out.println("Something went wrong :(");
            }
            System.out.println("Number found: " + found);
        } else {
            System.out.println("Posloupnost není sezařená, nelze provést hledání pomocí intervalů!");
        }
        */

        /* Cvičení 2
        long celkemSeq = 0;
        long celkemInter = 0;
        int pocetStejnychOdpovedi = 0;
        int i;
        int a=0;

        for(i=0;i<1000;i++) {
            int[] generatedData = generateSequence();
            for (a = 0; a < 1000; a++) {
                int find = (int) ((Math.random() * (generatedData[generatedData.length-1] - generatedData[0])) + generatedData[0]);
                long start1 = System.nanoTime();
                boolean found1 = sequentialSearch(generatedData, find);
                long stop1 = System.nanoTime();
                celkemSeq += stop1 - start1;
                //System.out.println("Sequential search finished in " + (stp - strt) + " ns");
                //System.out.println("Number found: " + fnd);


                long start2 = System.nanoTime();
                boolean found2 = intervalSubdivision(generatedData, find);
                long stop2 = System.nanoTime();
                //System.out.println("Interval subdivision finished in " + (stop - start) + " ns");
                //System.out.println("Number found: " + found);
                celkemInter += stop2 - start2;
                if (found1==found2){
                    pocetStejnychOdpovedi++;
                }
            }
        }

        System.out.println("Celkem Sequential search: " + celkemSeq/1000000 + " ms,	Prumerne: " + celkemSeq/1000 + " ns");
        System.out.println("Celkem Interval subdivision: " + celkemInter/1000000 + " ms,Prumerne: " + celkemInter/1000 + " ns");
        System.out.println("Počet stejných odpovědí u obou metod byl: "+pocetStejnychOdpovedi+" maximální možný počet je: "+ i*a);
        */

        /* Cvičení 3 */
        int[] data1 = loadData(new File("Exercise01/seq1.txt"));
        int[] data2 = loadData(new File("Exercise01/seq2.txt"));
        int[] data3 = loadData(new File("Exercise01/seq3.txt"));
        int[] data4 = loadData(new File("Exercise01/seq4.txt"));
        int[] data5 = loadData(new File("Exercise01/seq5.txt"));
        int[] data6 = loadData(new File("Exercise01/seq6.txt"));
        int[] data7 = loadData(new File("Exercise01/seq7.txt"));
        int[] data8 = loadData(new File("Exercise01/seq8.txt"));
        int[] data9 = loadData(new File("Exercise01/seq9.txt"));
        int[] data10 = loadData(new File("Exercise01/seq10.txt"));

        boolean sorted1 = isSorted(data1);
        boolean sorted2 = isSorted(data2);
        boolean sorted3 = isSorted(data3);
        boolean sorted4 = isSorted(data4);
        boolean sorted5 = isSorted(data5);
        boolean sorted6 = isSorted(data6);
        boolean sorted7 = isSorted(data7);
        boolean sorted8 = isSorted(data8);
        boolean sorted9 = isSorted(data9);
        boolean sorted10 = isSorted(data10);

        System.out.println("Je posloupnost 1 seřazená: "+sorted1);
        System.out.println("Je posloupnost 2 seřazená: "+sorted2);
        System.out.println("Je posloupnost 3 seřazená: "+sorted3);
        System.out.println("Je posloupnost 4 seřazená: "+sorted4);
        System.out.println("Je posloupnost 5 seřazená: "+sorted5);
        System.out.println("Je posloupnost 6 seřazená: "+sorted6);
        System.out.println("Je posloupnost 7 seřazená: "+sorted7);
        System.out.println("Je posloupnost 8 seřazená: "+sorted8);
        System.out.println("Je posloupnost 9 seřazená: "+sorted9);
        System.out.println("Je posloupnost 10 seřazená: "+sorted10);

        if (sorted1){
            System.out.println("Posloupnost 1:");
            getInfo(data1);
        }
        if (sorted2){
            System.out.println("Posloupnost 2:");
            getInfo(data2);
        }
        if (sorted3){
            System.out.println("Posloupnost 3:");
            getInfo(data3);
        }
        if (sorted4){
            System.out.println("Posloupnost 4:");
            getInfo(data4);
        }
        if (sorted5){
            System.out.println("Posloupnost 5:");
            getInfo(data5);
        }
        if (sorted6){
            System.out.println("Posloupnost 6:");
            getInfo(data6);
        }
        if (sorted7){
            System.out.println("Posloupnost 7:");
            getInfo(data7);
        }
        if (sorted8){
            System.out.println("Posloupnost 8:");
            getInfo(data8);
        }
        if (sorted9){
            System.out.println("Posloupnost 9:");
            getInfo(data9);
        }
        if (sorted10){
            System.out.println("Posloupnost 10:");
            getInfo(data10);
        }


    }
}
