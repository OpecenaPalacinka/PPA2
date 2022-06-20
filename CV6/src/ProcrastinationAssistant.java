import java.util.Random;
import java.util.Scanner;

/**
 * Zasobnik pro uchovani retezcu
 * @author Libor Vasa
 */
interface IStringStack {
    /**
     * Prida retezec do zasobniku
     * @param s retezec, ktery ma byt pridan do zasobniku
     */
    void add(String s);

    /**
     * Vrati retezec z vrcholu zasobniku
     * @return retezec z vrcholu zasobniku
     */
    String get();

    /**
     * Odstrani prvek z vrcholu zasobniku
     */
    void removeLast();
}

/**
 * Asistent omezujici prokrastinaci
 * @author Libor Vasa
 */
class ProcrastinationAssistant {
    public static void main(String[] args) {
        IStringStack stack = new StackArray(); //DEBUG - >'S'
        IStringStack stackReverse = new StackArray();
        /* Úloha 1/2
        stack.add("Naucit se hrat na ukulele");
        stack.add(randomString()); //DEBUG -> chybějící "()" aby to bylo volání funkce

        IStringStack stack10 = new StackArray10();
        long start = System.nanoTime();
        testStack(stack);
        long mezi = System.nanoTime();
        testStack(stack10);
        long konec = System.nanoTime();
        System.out.println("Potřebný čas pro 2x zvětšované pole je -> "+((mezi-start)/1000000)+" ms");
        System.out.println("Potřebný čas pro +10 zvětšované pole je -> "+((konec-mezi)/1000000)+" ms");
        Úloha 3
        Scanner sc = new Scanner(System.in);
        System.out.println("Co je třeba udělat?");
        String first = sc.nextLine();
        String hlavniUkol = first;
        stack.add(first);
        while (stack.get() != null){
            System.out.println("Aktuální úkol: "+stack.get());
            System.out.println("Co s úkolem? (H = Hotovo, R = Rozdělit)");
            first = sc.nextLine();
            if (first.equals("H")){
                stack.removeLast();
            } else if (first.equals("R")){
                stack.removeLast();
                System.out.println("Prosím zadej podúkoly, ukončené prázdným řetězcem");
                while (!(first = sc.nextLine()).equals("")){
                    stackReverse.add(first);
                }
                while (stackReverse.get() != null){
                    stack.add(stackReverse.get());
                    stackReverse.removeLast();
                }

            } else {
                System.out.println("Tohle je špatný znak, končíme!");
                return;
            }
        }
        System.out.println("Hlavní úkol "+hlavniUkol+" byl splněn");
         */
    }


    /**
     * Vygeneruje a vrati nahodny retezec
     * @return nahodny retezec velkych pismen nahodne delky (5 az 24 znaku)
     */
    public static String randomString() {
        StringBuilder sb = new StringBuilder();
        Random r = new Random(); //DEBUG -> potřeba importovat knihovnu java.util.Random
        for (int i = 0; i < (5 + r.nextInt(20)); i++) { // DEBUG -> ')' chybějící zavírací závorka
            sb.append((char) (r.nextInt(24) + 65)); //DEBUG -> '(' chybějící otvírací závorka
        }
        return(sb.toString());
    }

    public static void testStack(IStringStack stack){
        String[] strings = new String[100000];
        for (int i = 0; i < 100000; i++) {
            strings[i] = randomString();
        }
        for (int i = 0; i < 100000; i++) {
            stack.add(strings[i]);
        }
        for (int y = 0; y < 100000; y++) {
            if (!strings[100000-y-1].equals(stack.get())){
                System.out.println("Prvky si neodpovídají, končím!");
                return;
            }
            stack.removeLast();
        }
        System.out.println("Vše proběhlo podle očekávání!");

    }
}
/**
 * Implementace zasobniku retezcu pomoci pole
 * @author Libor Vasa
 */
class StackArray implements IStringStack {
    /** Data v zasobniku */
    private String[] data;
    /** Index pozice, na kterou se vlozi novy prvek */
    private int freeIndex;

    /**
     * Vytvori novy prazdny zasobnik
     */
    public StackArray() {
        data = new String[5]; // DEBUG -> chceme pole Stringů a ne intů
        freeIndex = 0;
    }

    //DEBUG -> přidán @Override pro přehlednost
    @Override
    public void add(String s) {
        if (freeIndex == data.length){
            expandArray();
        }
        data[freeIndex] = s;
        freeIndex++;
    }

    private void expandArray() {
        String[] newArray = new String[data.length*2];
        for (int i = 0; i < data.length; i++) {
            newArray[i] = data[i];
        }
        data = newArray;
    }

    //DEBUG -> chyběl public
    @Override
    public String get() {
        if (freeIndex == 0){
            return null;
        }
        return data[freeIndex-1];
    }

    //DEBUG -> chyběl public
    @Override
    public void removeLast() {
        if (freeIndex == 0){
            System.err.println("Stack je prázdný - nedělám nic!");
            return;
        }
        freeIndex--;
    }
}
/**
 * Implementace zasobniku retezcu pomoci pole
 * @author Libor Vasa
 */
class StackArray10 implements IStringStack {
    /** Data v zasobniku */
    private String[] data;
    /** Index pozice, na kterou se vlozi novy prvek */
    private int freeIndex;

    /**
     * Vytvori novy prazdny zasobnik
     */
    public StackArray10() {
        data = new String[5]; // DEBUG -> chceme pole Stringů a ne intů
        freeIndex = 0;
    }

    //DEBUG -> přidán @Override pro přehlednost
    @Override
    public void add(String s) {
        if (freeIndex == data.length){
            expandArray();
        }
        data[freeIndex] = s;
        freeIndex++;
    }

    private void expandArray() {
        String[] newArray = new String[data.length+10];
        for (int i = 0; i < data.length; i++) {
            newArray[i] = data[i];
        }
        data = newArray;
    }

    //DEBUG -> chyběl public
    @Override
    public String get() {
        if (freeIndex == 0){
            return null;
        }
        return data[freeIndex-1];
    }

    //DEBUG -> chyběl public
    @Override
    public void removeLast() {
        if (freeIndex == 0){
            System.err.println("Stack je prázdný - nedělám nic!");
            return;
        }
        freeIndex--;
    }
}
