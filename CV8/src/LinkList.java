import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;

/**
 * Prvek spojoveho seznamu
 * @author Libor Vasa
 */
class Link {
    /** Data prvku - jeden znak */
    char data;
    /** Dalsi prvek spojoveho seznamu */
    Link next;
}
/**
 * Spojovy seznam znaku
 * @author Libor Vasa
 */
class LinkList {
    /** Prvni prvek seznamu */
    Link first;
    static int pocetZnaku=0;
    static int pocetI=0;
    static int pocetR=0;

    public static void main(String[] args) throws Exception {
        LinkList myList = new LinkList();
        MyIterator myIterator = new MyIterator(myList);

        FileReader input = new FileReader("input.txt");
        FileReader instrukce = new FileReader("instructions.txt");

        try (BufferedReader bufferedReader = new BufferedReader(input)) {
            String s;
            while ((s = bufferedReader.readLine()) != null) {
                for (int i = 0; i < s.toCharArray().length; i++) {
                    myIterator.insert(s.charAt(i));
                    myIterator.next();
                    pocetZnaku++;
                }
            }

        } catch (Exception e) {
            System.err.println("Chyba při čtení ze souboru.");
        }

        //System.out.println("Počet načtených znaků z input.txt: "+pocetZnaku);

        try (BufferedReader bufferedReader = new BufferedReader(instrukce)) {
            String string;
            while ((string = bufferedReader.readLine()) != null) {
                String[] radka = string.split(" ");
                switch (radka[0]) {
                    case "N":
                        myIterator.next();
                        break;
                    case "B":
                        myIterator.moveToFirst();
                        break;
                    case "I":
                        myIterator.insert(radka[1].charAt(0));
                        pocetI++;
                        break;
                    case "R":
                        myIterator.remove();
                        pocetR++;
                        break;
                }
            }
        } catch (Exception e) {
            System.err.println("Chyba při čtení ze souboru.");
        }

        //System.out.println("Počet instrukcí I v instructions.txt: "+pocetI);
        //System.out.println("Počet instrukcí R v instructions.txt: "+pocetR);
        //System.out.println("Počet znaků po provedení instrukcí v instructions.txt: "+(pocetZnaku+pocetI-pocetR));

        try {
            FileWriter fileWriter = new FileWriter("output.txt");
            fileWriter.write(printList(myList));
            fileWriter.close();
        } catch (Exception e) {
            System.err.println("Chyba při zápisu do souboru");
        }

        //System.out.println("Reálný počet znaků: "+pocetZnaku);

        /*
        myIterator.insert('a');
        myIterator.insert('k');
        myIterator.insert('l');
        myIterator.next();
        myIterator.insert('a');
        myIterator.next();
        myIterator.insert('s');

        printList(myList);

        myIterator.moveToFirst();
        myIterator.insert('v');
        myIterator.insert('i');
        myIterator.insert('p');
        myIterator.next();
        myIterator.next();
        myIterator.next();
        myIterator.remove();
        myIterator.remove();
        myIterator.remove();
        myIterator.remove();
        myIterator.insert('o');
        myIterator.next();
        myIterator.remove();

        printList(myList);
        */
    }

    static String printList(LinkList myList) throws Exception {
        pocetZnaku = 0;
        MyIterator myIterator = new MyIterator(myList);
        StringBuilder stringBuilder = new StringBuilder();
        while (myIterator.hasNext()) {
            stringBuilder.append(myIterator.get());
            pocetZnaku++;
            myIterator.next();
        }
        return stringBuilder.toString();
    }
}

class MyIterator{

    Link current;
    LinkList list;

    public MyIterator(LinkList list){
        this.list = list;
        this.current = null; //ukazovatko je pred prvnim prvkem
    }

    /**
     * Vlozi novy znak do seznamu
     * @param letter znak, ktery se vlozi do seznamu
     */
    void insert(char letter) {
        Link newLink = new Link();
        newLink.data = letter;
        if (current == null) {
            newLink.next = list.first;
            list.first = newLink;
        }
        else {
            newLink.next = current.next; //Potřeba prohodit řádky aby se nově vložený prvek dal napravo
            current.next = newLink;      // Prohodit s tímto řádkem
        }
    }


    /**
     * Posune aktualni prvek na dalsi v seznamu
     * @throws Exception pokud zadny dalsi prvek neni
     */
    void next() throws Exception {
        if (list.first == null) {
            throw new Exception();
        }
        if (current == null) {
            current = list.first;
        }
        else {
            current = current.next;
            if (current == null) {
                throw new Exception();
            }
        }
    }

    /**
     * Vrati znak v aktualnim prvku seznamu
     * @return znak v aktualnim prvku seznamu
     */
    char get() throws Exception {
        //chyběla signatura "throws Exception, aby metoda mohla vyhazovat Exception
        if (list.first == null) {
            throw new Exception();
        }
        if (current == null) {
            return list.first.data;
        }
        if (current.next != null) {
            return current.next.data;
        }
        else {
            throw new Exception();
        }
    }

    /**
     * Zmeni aktualni prvek na prvni prvek seznamu
     */
    void moveToFirst() {
        current = null;
    }

    /**
     * Vraci true, pokud existuje nasledujici prvek seznamu
     * @return true, pokud existuje nasledujici prvek seznamu
     */
    boolean hasNext() {
        if (current == null) {
            return list.first != null;
        }
        return current.next != null;
    }

    /**
     * Odebírá prvek seznamu
     * @throws Exception pokud první prvek je roven null
     */
    void remove() throws Exception {
        if (list.first == null) {
            throw new Exception();
        }
        if (current == null) {
            list.first = list.first.next;
        }
        else if (current.next == null) {
            throw  new Exception();
        }
        else {
            current.next = current.next.next;
        }
    }
}


