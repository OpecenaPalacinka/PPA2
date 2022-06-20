import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Random;

/**
 * Zaznam hash tabulky s nahledy obrazku
 * @author Libor Vasa
 */
class Entry{
    /** Nazev souboru s obrazkem */
    public String fileName;
    /** Index v databazi nahledu obrazku */
    int indexInDatabase;
    /** Dalsi zaznam (pro potreby hash tabulky) */
    public Entry next;

    /**
     * Vyvtori novy zaznam se zadanym nazvem souboru a indexem do databaze
     * @param fn nazev souboru s obrazkem
     * @param index index v databazi nahledu obrazku
     */
    public Entry (String fn, int index) {
        fileName = fn;
        indexInDatabase = index;
    }
}

/**
 * Hash tabulka pro uchovani zaznamu s nahledem obrazku
 * @author Libor Vasa
 */
class HashTable {
    /** Zaznamy tabulky */
    Entry[] data;

    /**
     * Vytvori novou hash tabulku se zadanou kapacitou
     * @param capacity kapacita tabulky
     */
    public HashTable(int capacity) {
        data = new Entry[capacity]; //DEBUG -> potřeba vytvořit pole, aby nebyl NullPointer
    }

    /**
     * Prida zaznam do has tabulky
     * @param key klic - nazev souboru s obrazkem
     * @param value hodnota - index v databazi nahledu obrazku
     */
    public void add(String key, int value) {
        Entry newEntry = new Entry(key, value);
        int index = getHashCode(key);
        newEntry.next = data[index]; //DEBUG -> prohození řádků, přidání prvku na začátek
        data[index] = newEntry;     // prohozený řádek
    }

    /**
     * Vypocte a vrati hash kod pro zadany klic
     * @param s klic, pro ktery se ma vypocitat hash kod
     * @return hash kod pro zadany klic
     */
    int getHashCode(String s){
        int vysledek = 7;

        for(int k = 0; k < s.length(); k++) {
            vysledek = ((vysledek * 31) + s.charAt(k)) % (data.length);
        }
        return vysledek;
    }

    public int get(String key){
        int hash = getHashCode(key);
        Entry current = data[hash];
        while (current!=null) {
            if (current.fileName.equals(key))
                return (current.indexInDatabase);
            current = current.next;
        }
        return -1;
    }

}

class HashTableDumb extends HashTable{

    /**
     * Vytvori novou hash tabulku se zadanou kapacitou
     *
     * @param capacity kapacita tabulky
     */
    public HashTableDumb(int capacity) {
        super(capacity);
    }

    @Override
    int getHashCode(String s) {
        return 0;
    }
}

/**
 * Hlavni trida programu
 * @author Libor Vasa
 */
public class Thumbnails {
    static String[] fileNames;
    static Random random = new Random();
    static int pocetDotazu;
    static HashTable table;
    public static final int[] capacities = new int[]{1000,1009,30030,100000,100003};

    public static void main(String[] args) throws IOException {
        readFile();
        System.out.println("Počet zpracovaných dotazů za 2 sekundy, hloupá rozptylová funkce:");
        iterate(true);
        System.out.println("Počet zpracovaných dotazů za 2 sekundy, chytrá rozptylová funkce:");
        iterate(false);
    }

    public static void readFile() throws IOException {
        fileNames = new String[100000];
        BufferedReader bufferedReader = new BufferedReader(new FileReader("ImageNames.txt"));
        for (int i = 0; i < fileNames.length; i++) {
            fileNames[i] = bufferedReader.readLine();
        }
    }

    public static void iterate(boolean dumb){
        for (int capacity : capacities) {
            if(dumb){
                table = new HashTableDumb(capacity);
            } else {
                table = new HashTable(capacity);
            }
            workWithTimer();
            System.out.println("Pro C = "+capacity+" bylo zpracováno "+pocetDotazu+" dotazů");
        }
    }

    public static void workWithTimer(){
        double t2;
        pocetDotazu = 0;
        double t1 = System.currentTimeMillis();
        do {
            work();
            t2 = System.currentTimeMillis();
        } while (t2 - t1 < 2000);
    }

    public static void work(){
        int rnd;
        rnd = random.nextInt(100000);
        if(table.get(fileNames[rnd]) == -1){
            table.add(fileNames[rnd], 0);
            pocetDotazu += 2;
        } else {
            pocetDotazu++;
        }
    }

    /**
     * Vygeneruje a vrati nahodny nazev obrazku
     * @return nahodny nazev obrazku
     */
    private static String randomImageName() {
        Random r = new Random(); //DEBUG -> chyběl import "import java.util.Random;"
        int year = 2005 + r.nextInt(13);
        int month = 1 + r.nextInt(12);
        int day = 1 + r.nextInt(28);
        int img = 1 + r.nextInt(9999);
        return String.format("c:\\fotky\\%d-%02d-%02d\\IMG_%04d.CR2", year, month, day, img);
    }
}