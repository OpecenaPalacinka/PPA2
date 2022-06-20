import java.util.Arrays;
import java.util.Random;

/**
 * Rozhrani pro radici algoritmy
 * @author Libor Vasa
 */
interface ISortingAlgorithm {

    /**
     * Seradi zadane pole
     * @param data pole, ktere se ma seradit
     */
    public void sort(int[] data);

    public void onlySort(int[] data);

    /**
     * Vrati pocet porovnani v poslednim provedem razeni.
     * Pokud dosud zadne razeni nebylo provedeno, vrati 0.
     * @return pocet porovnani v poslednim provedenem razeni
     */
    public int comparesInLastSort();
}

/**
 * Razeni vkladanim (insertion sort)
 * @author Libor Vasa
 */
class InsertSort implements ISortingAlgorithm {
    /** Pocet porovani v poslednim provedenem razeni */
    int compares = 0;

    /**
     * Vrati true, pokud je prvek na indexu i vetsi nez v, jinak false
     * Inkrementuje citac porovnani.
     * @param data razene pole
     * @param i index prvku, ktery se ma porovnat
     * @param v hodnota, se kterou se ma prvek porovnat
     * @return true, pokud je prvek na indexu i vetsi nez v, jinak false
     */
    boolean greaterThan(int[] data, int i, int v) {
        compares++;
        return data[i] > v;
    }

    @Override
    public void sort(int[] data) {
        compares = 0; //DEBUG -> potřebuji při každém porovnání vynulovat compares
        for (int i = 1; i < data.length; i++) {
            int v = data[i];
            int j = i - 1;
            while((j >= 0) && (greaterThan(data, j, v))) { // DEBUG -> chybělo '=' v první podmínce aby proběhlo porovnání i v prvním průchodu
                // a v druhé podmínce jsem vyměnil i za j abych neporovnával to samé
                data[j + 1] = data[j];
                j--;
            }
            data[j + 1] = v;
        }
    }

    @Override
    public void onlySort(int[] data) {
        for (int i = 1; i < data.length; i++) {
            int v = data[i];
            int j = i - 1;
            while((j >= 0) && (data[j]>v)) { // DEBUG -> chybělo '=' v první podmínce aby proběhlo porovnání i v prvním průchodu
                // a v druhé podmínce jsem vyměnil i za j abych neporovnával to samé
                data[j + 1] = data[j];
                j--;
            }
            data[j + 1] = v;
        }
    }

    @Override
    public int comparesInLastSort() {
        return compares;
    }
}
/**
* Zlepšit výběr pivota
**/
class QuickSort implements ISortingAlgorithm {
    int compares = 0;
    int qSelect(int[] data, int start, int end, int k){
        if(start==end){
            return data[start];
        }
        int i = split2(data,start,end);
        if (i == k){
            return data[i];
        }
        else if (i > k){
            return qSelect(data,start,i-1,k);
        }
        else {
            return qSelect(data,i+1,end,k);
        }
    }

    void quickSort(int[] data, int start, int end){
        if(end<=start) return;
        int i = split(data, start, end);
        quickSort(data, start, i - 1);
        quickSort(data, i + 1, end);

    }

    private int split(int[] data, int start, int end) {
        int pivot = qSelect(data,start,end, data.length/2);
        for (int i = 0; i < data.length-1; i++) {
            while ((data[start]<pivot) && (start<end)) {
                compares++;
                start++;
            }
            if (start<end) {
                data[end] = data[start];
                end--;
            } else break;
            while ((data[end]>pivot)&&(start<end)) {
                compares++;
                end--;
            }
            if (start<end) {
                data[start] = data[end];
                start++;
            } else break;
        }
        data[start] = pivot;
        return(start);
    }

    void quickSort2(int[] data, int start, int end){
        if(end<=start) return;
        int i = split2(data, start, end);
        quickSort(data, start, i - 1);
        quickSort(data, i + 1, end);

    }

    private int split2(int[] data, int start, int end) {
        int pivot = data[end];

        for (int i = 0; i < data.length-1; i++) {
            while ((data[start]<pivot) && (start<end)) {
                start++;
            }
            if (start<end) {
                data[end] = data[start];
                end--;
            } else break;
            while ((data[end]>pivot)&&(start<end)) {
                end--;
            }
            if (start<end) {
                data[start] = data[end];
                start ++;
            } else break;
        }
        data[start] = pivot;
        return(start);
    }


    @Override
    public void sort(int[] data) {
        compares = 0;
        quickSort(data,0, data.length-1);
    }

    @Override
    public void onlySort(int[] data) {
        quickSort(data,0, data.length-1);
    }

    @Override
    public int comparesInLastSort() {
        return compares;
    }
}
/**
* Nulovat čítač a správně počítat
**/
class MergeSort implements ISortingAlgorithm {
    int compares = 0;

    void mergeSort(int[] data, int start, int end) {
        if(end==start) return;
        compares++;
        int mid = (start + end)/2;
        mergeSort(data,start,mid);
        mergeSort(data,mid+1,end);
        int[] temp = bitonic(data,start,mid,end);
        mergeBitonic(data,start,temp);
    }

    void mergeSort2(int[] data, int start, int end) {
        if(end==start) return;
        int mid = (start + end)/2;
        mergeSort2(data,start,mid);
        mergeSort2(data,mid+1,end);
        int[] temp = bitonic(data,start,mid,end);
        mergeBitonic(data,start,temp);
    }

    int[] bitonic(int[] data, int start, int mid, int end) {
        int[] result = new int[end-start+1];
        for(int i = start; i<=mid; i++)
            result[i-start] = data[i];
        for (int i = mid+1; i<=end; i++)
            result[end - start + mid + 1 - i] = data[i];
        return result;
    }
    void mergeBitonic(int[] data, int start, int[] bitonic) {
        int i = 0;
        int j = bitonic.length -1;
        for(int k = 0; k<bitonic.length; k++)
            data[start+k] = bitonic[i]<bitonic[j] ? bitonic[i++]:bitonic[j--];
    }


    @Override
    public void sort(int[] data) {
        mergeSort(data,0,data.length-1);
    }

    @Override
    public int comparesInLastSort() {
        return compares;
    }

    @Override
    public void onlySort(int[] data) {
        mergeSort2(data,0,data.length-1);
    }
}

class JavaSort implements ISortingAlgorithm{

    @Override
    public void sort(int[] data) {
        Arrays.sort(data);
    }

    @Override
    public int comparesInLastSort() {
        return 0;
    }

    @Override
    public void onlySort(int[] data) {
        Arrays.sort(data);
    }
}

/**
 * Testovani razeni
 * @author Libor Vasa
 */
public class SortingTest {
    public static void test(ISortingAlgorithm algorithm) {
        if (testCorrectness(algorithm)) {
            System.out.println("\n" + algorithm.getClass() + "\n");
            testCounts(algorithm);
        }
        System.out.println();
    }

    public static void main(String[] args) {
        ISortingAlgorithm algorithm = new InsertSort();
        ISortingAlgorithm algorithm2 = new QuickSort();
        ISortingAlgorithm algorithm3 = new MergeSort();
        ISortingAlgorithm algorithm4 = new JavaSort();

        test(algorithm);
        test(algorithm2);
        test(algorithm3);
        test(algorithm4);

    }

    /**
     * Otestuje pocty porovani zadaneho razeni
     *
     * @param algorithm testovany algoritmus razeni
     */
    private static void testCounts(ISortingAlgorithm algorithm) {
        int MIN_LENGTH = 100;
        int MAX_LENGTH = 100000;
        int TEST_COUNT = 100;
        for (int length = MIN_LENGTH; length < MAX_LENGTH; length *= 2) {
            int minComp = Integer.MAX_VALUE;
            int maxComp = 0;
            int sorted = -1;
            long casNese1 = 0;
            long casNese2 = 0;
            long casSer1 = 0;
            long casSer2 = 0;
            for (int test = 0; test < TEST_COUNT; test++) {
                int[] data = generateData(length);
                int[] dataForOnlySort = new int[data.length];
                for (int j = 0; j < data.length; j++) {
                    dataForOnlySort[j] = data[j];
                }
                algorithm.sort(data);
                /*
                if (algorithm.comparesInLastSort() > maxComp) {
                    maxComp = algorithm.comparesInLastSort();
                }
                if (algorithm.comparesInLastSort() < minComp) {
                    minComp = algorithm.comparesInLastSort();
                }
                 */

                casNese1 = System.nanoTime();
                algorithm.onlySort(dataForOnlySort);
                casNese2 = System.nanoTime();

                // Program spadl na Stack overflow pro quicksort pro prvky větší než 12 800
                // Ale pouze pro již seřazené posloupnosti, vznikne moc velké zanoření rekurze
                // Tato podmínka zabrání aby se quicksort spustil pro delší seřazené posloupnosti
                // Bohužel jsem nevymyslel způsob, jakým vybírat správný index aby quicksort začal fungovat
                if (algorithm.getClass() != QuickSort.class || length < 55000) {
                    algorithm.sort(data);
                    sorted = algorithm.comparesInLastSort();
                    casSer1 = System.nanoTime();
                    algorithm.onlySort(dataForOnlySort);
                    casSer2 = System.nanoTime();
                }

            }
            System.out.println("Length: " + length + ", Min:" + minComp + ", Max:" + maxComp);
            if (sorted == -1) {
                System.out.println("For sorted: ERROR - Something went wrong.");
            } else {
                System.out.println("For sorted: " + sorted);
            }
            System.out.println("Čas nutný pro řazení onlySort: " + (casNese2 - casNese1) + " nanosekund");
            if (casSer2 - casSer1 == 0) {
                System.out.println("Čas nutný pro řazení onlySort již seřazené: ERROR - Something went wrong.");
            } else {
                System.out.println("Čas nutný pro řazení onlySort již seřazené: " + (casSer2 - casSer1) + " nanosekund");
            }


        }
    }

    /**
     * Otestuje spravnost zadaneho razeni
     *
     * @param algorithm testovany algoritmus razeni
     */
    private static boolean testCorrectness(ISortingAlgorithm algorithm) {
        for (int i = 0; i < 100; i++) {
            int[] data = generateData(100);
            int[] dataCopy = new int[data.length];
            for (int j = 0; j < data.length; j++) {
                dataCopy[j] = data[j];
            }
            algorithm.sort(data);
            Arrays.sort(dataCopy);
            for (int j = 0; j < data.length; j++) {
                if (data[j] != dataCopy[j]) { //DEBUG změněno i na j
                    System.out.println("Algorithm failed, terminating.");
                    return false;
                }
            }
        }
        System.out.println("Algorithm passed test, continuing.");
        return true;
    }

    /**
     * Vygeneruje pole o zadane velikosti c nahodnych cisel z intervalu <0; c)
     *
     * @param c velikost vygenerovaneho pole a horni hranice generovanych hodnot
     * @return vygenerovane pole nahodnych cisel
     */
    private static int[] generateData(int c) {
        int[] result = new int[c];
        Random rnd = new Random();
        for (int i = 0; i < c; i++) {
            result[i] = rnd.nextInt(c);
        }
        return result;
    }
}