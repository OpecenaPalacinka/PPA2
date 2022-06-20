import java.util.Random;

/**
 * Tri varianty odstraneni duplicitnich hodnot z pole
 * @author Libor Vasa
 */
public class RemoveDuplicates {

    /**
     * Odstrani z pole prvek na indexu index
     * @param data vstupni pole dat
     * @param index index polozky, ktera se ma odstranit
     * @return vysledne pole bez jedne polozky
     */
    public static int[] removeItem(int[] data, int index) {
        //vysledne pole bude o 1 kratsi
        int[] result = new int[data.length - 1];
        //zkopirujeme prvky az do indexu i
        for (int i = 0; i < index; i++) {
            result[i] = data[i];
        }
        //i-ty prvek preskocime a zkopirujeme vsechny zbyvajici prvky
        for (int i = index + 1; i < data.length; i++) {
            result[i - 1] = data[i];
        }
        return result;
    }

    /**
     * Prochazi vsechny polozky a odstranuje duplikaty metodou removeItem()
     * @param data vstupni pole dat
     * @return vysledna data s odstranenymi duplikaty
     */
    public static int[] removeDuplicates1(int[] data) {
        int[] result = data;
        for (int i = 0; i < result.length; i++) {
            for (int j = i + 1; j < result.length; j++) {
                if (result[j] == result[i]) {
                    result = removeItem(result, j);
                    j--; // DEBUG -> potřeba udělat j--
                }
            }
        }
        return result;
    }

    /**
     * Prochazi vsechny polozky a provadi ostraneni vsech duplikatu jedne polozky najednou
     * @param data vstupni pole dat
     * @return vysledna data s odstranenymi duplikaty
     */
    public static int[] removeDuplicates2(int[] data) {
        int[] result = data;
        for(int i = 0; i < result.length; i++){
            //spocteme, kolik ma polozka result[i] duplikatu
            int count = 0; //pocet duplikatu
            for (int j = i + 1; j < result.length; j++) {
                if (result[j] == result[i]) {
                    count++; // DEBUG -> velké "C" místo malého "c"
                }
            }
            //pokud je alespon jeden duplikat, pak ho odstranime
            if (count > 0) {
                //vysledek bude o count kratsi
                int[] newResult = new int[result.length - count];
                //prvky az do indexu i muzeme jednoduse zkopirovat
                for (int k = 0; k <= i; k++) {
                    newResult[k] = result[k];
                }
                int index = i; //index v cilovem poli
                for (int k = i + 1;k < result.length; k++) {
                    if (result[k] != result[i]) { //neni duplikat
                        newResult[index+1] = result[k]; // DEBUG -> index +1 protože bez +1 se odstraní i ten jeden prvek který hledáme
                        index++;
                    }
                }
                result = newResult;
            }
        }
        return result;
    }

    /**
     * Pouziva redukci pomoci pole zaznamu, zda dane cislo bylo nalezeno v datech ci nikoli
     * @param data vstupni pole dat
     * @return vysledna data s odstranenymi duplikaty
     */
    public static int[] removeDuplicates3(int[] data) {
        //nejdrive jen zjistime, kolik mame unikatnich cisel
        boolean[] encountered = new boolean[1000000];
        int count = 0; //pocet unikatnich cisel
        for (int i = 0; i < data.length; i++) {
            if (!encountered[data[i]]) { //nove objevene cislo
                encountered[data[i]] = true;
                count++;
            }
        }
        //v promenne count je ted pocet unikatnich cisel
        //pole encountered ted pouzijeme jeste jednou stejnym zpusobem
        encountered = new boolean[1000000];
        int[] result = new int[count];
        int index = 0;
        for (int i = 0; i < data.length; i++) {
            if (!encountered[data[i]]) {
                result[index] = data[i];
                encountered[data[i]] = true;
                index++;
            }
        }
        return result;
    }

    /**
     * Generuje nahodna data v rozsahu do 100 000,
     * cimz se simuluje, ze cca 90% cisel je "neaktivnich"
     * @param count pocet pozadovanych cisel
     * @return pole nahodnych cisel
     */
    public static int[] generateData(int count) {
        int[] result = new int[count];
        Random r = new Random();
        for (int i = 0; i < result.length; i++) {
            result[i] = r.nextInt(100000);
        }
        return result;
    }

    public static void sameArray(int[] data1, int[] data2){
        for (int i = 0; i < data1.length; i++) {
            for (int j = i + 1; j < data2.length; j++) {
                if (data2[j] == data1[i]) {
                    System.out.println("Data se NErovnají!");
                    return;
                }
            }
        }
        System.out.println("Data se rovnají!");
    }

    /**
     *
     * @param n velikost dat
     * @param mode 1 - remove1, 2 - remove2, 3 - remove3, 4 - removeAll
     * @return pole časů
     */
    public static double[] timeElapse(int n,int mode){
        int[] data = generateData(n);
        double[] result;
        if (mode == 1){
            result = new double[2];
            result[0] = System.nanoTime();
            removeDuplicates1(data);
            result[1] = System.nanoTime();
            return result;
        }
        if (mode == 2){
            result = new double[2];
            result[0] = System.nanoTime();
            removeDuplicates2(data);
            result[1] = System.nanoTime();
            return result;
        }
        if (mode == 3){
            result = new double[2];
            result[0] = System.nanoTime();
            removeDuplicates3(data);
            result[1] = System.nanoTime();
        }
        else {
            result = new double[4];
            result[0] = System.nanoTime();
            removeDuplicates1(data);
            result[1] = System.nanoTime();
            removeDuplicates2(data);
            result[2] = System.nanoTime();
            removeDuplicates3(data);
            result[3] = System.nanoTime();
        }
        return result;
    }

    public static double deltaT(double t1, double t2){
        return (Math.abs(t1-t2))/1000000000; //doba trvání převedená na sekundy
    }

    public static void main(String[] args) {
        int count = 30000;

        /* Cvičení 1
        int[] data = generateData(count);
        int[] reducedData1 = removeDuplicates1(data);
        int[] reducedData2 = removeDuplicates2(data);
        int[] reducedData3 = removeDuplicates3(data);
        // Pokud metody fungují správně, výsledná pole by měli mít stejně prvků
        System.out.println("Délka pole 1: "+reducedData1.length);
        System.out.println("Délka pole 2: "+reducedData2.length);
        System.out.println("Délka pole 3: "+reducedData3.length);
        // Pole by měli mít stejné prvky -> zkontroluji pomocí metody sameArray
        // Lze použít i Arrays.equals
        // Pokud jsou data1 i data2 shodná a zároveň jsou shodná data2 a data3 potom i data1 a data3 jsou shodné
        sameArray(reducedData1,reducedData2);
        sameArray(reducedData3,reducedData2);
        System.out.println("All done.");
        */

        // Cvičení 3
        int step = 11000;
        int pomocnejCounter = 0;
        double[] result = timeElapse(count,4);

        double deltaT1 = deltaT(result[1],result[0]);
        double deltaT2 = deltaT(result[2],result[1]); //doba trvání převedená na sekundy
        double deltaT3 = deltaT(result[3],result[2]);

        while (deltaT1<1){
            count += step;
            result = timeElapse(count,1);
            deltaT1 = deltaT(result[1],result[0]);
        }
        long celkovejPocetZaVterinu1 = (long)(1/(deltaT1/count));
        System.out.println("První metoda trvala pres jednu sekundu (konkretne "+deltaT1+" s) pro " +count+" čísel");
        count = 30000;

        while (deltaT2<1){
            count += step;
            result = timeElapse(count,2);
            deltaT2 = deltaT(result[1],result[0]);
        }
        long celkovejPocetZaVterinu2 = (long)(1/(deltaT2/count)); //počet prvků které metoda zvládne za sekundu
        System.out.println("Druhá metoda trvala pres jednu sekundu (konkretne "+deltaT2+" s) pro " +count+" čísel");
        count = 30000;

        while (pomocnejCounter<100){
            if(deltaT3>1){
                long celkovejPocetZaVterinu3 = (long)(1/(deltaT3/count));
                System.out.println("Třetí metoda trvala pres jednu sekundu (konkretne "+deltaT3+" s) pro " +count+" čísel");
                break;
            }
            count += step;
            pomocnejCounter++;
            result = timeElapse(count,3);
            deltaT3 = deltaT(result[1],result[0]);
        }
        long celkovejPocetZaVterinu3 = (long)(1/(deltaT3/count));
        System.out.println("Cyklus byl ukončen po 100 iteracích, metoda 3 je geniálně rychlá, proto jsou potřeba obrovské čísla.");
        System.out.println("Pokud chcete zjistit, kolik je potřeba čísel, doporučuji nastavit proměnnou step alespoň na 50milionů a zakomentovat předešlé dva while cykly.");
        System.out.println("Na mém PC to bylo okolo 800 milionů prvků.\n");


        count = 60000;
        if(celkovejPocetZaVterinu1<celkovejPocetZaVterinu2 && celkovejPocetZaVterinu1<celkovejPocetZaVterinu3){
            System.out.println("Nejpomalejší metoda je removeDuplicates1.");
            while (deltaT1<10){
                count += step;
                result = timeElapse(count,1);
                deltaT1 = deltaT(result[1],result[0]);
            }
            System.out.println("První metoda trvala pres 10 sekundu (konkretne "+deltaT1+" s) pro " +count+" čísel");
        } else if(celkovejPocetZaVterinu2<celkovejPocetZaVterinu3){
            System.out.println("Nejpomalejší metoda je removeDuplicates2.");
            while (deltaT2<10){
                count += step;
                result = timeElapse(count,2);
                deltaT2 = deltaT(result[1],result[0]);
            }
            System.out.println("Druhá metoda trvala pres 10 sekundu (konkretne "+deltaT2+" s) pro " +count+" čísel");
        } else {
            System.out.println("Nejpomalejší metoda je removeDuplicates3.");
            while (deltaT3<10){
                count += step;
                result = timeElapse(count,3);
                deltaT3 = deltaT(result[1],result[0]);
            }
            System.out.println("Třetí metoda trvala pres 10 sekundu (konkretne "+deltaT3+" s) pro " +count+" čísel");
        }

        System.out.println();
        System.out.println("n         t1        a1     t2        a2     t3        a3");
        count = 1000;
        for (int i = 0; i < 10; i++) {
            result = timeElapse(count,4);
            deltaT1 = deltaT(result[1],result[0]);
            deltaT2 = deltaT(result[2],result[1]);
            deltaT3 = deltaT(result[3],result[2]);
            if(deltaT1>deltaT2 && deltaT1>deltaT3){
                System.out.format("%d   %.5f   %.3f  %.5f   %.3f   %.5f   %.3f\n",count,deltaT1, 1.0,deltaT2,deltaT1/deltaT2,deltaT3,deltaT1/deltaT3);
            } else if(deltaT2>deltaT3){
                System.out.format("%d   %.5f   %.3f  %.5f   %.3f   %.5f   %.3f\n",count,deltaT1,deltaT2/deltaT1,deltaT2, 1.0,deltaT3,deltaT2/deltaT3);
            } else {
                System.out.format("%d   %.5f   %.3f  %.5f   %.3f   %.5f   %.3f\n",count,deltaT1,deltaT3/deltaT1,deltaT2,deltaT3/deltaT2,deltaT3, 1.0);
            }
            count += step;
        }
         //
    }
}
