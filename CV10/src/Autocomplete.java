import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Uzel binarniho vyhledavaciho stromu (BST) se jmeny souboru
 * @author Libor Vasa
 */
class Node {
    /** Klic - jmeno souboru */
    public String key;
    /** Levy potomek */
    public Node left;
    /** Pravy potomek */
    public Node right;

    /**
     * Vytvori novy uzel binarniho vyhledavaciho stromu
     * @param key klic - jmeno souboru
     */
    public Node(String key) {
        this.key = key;
    }
}

/**
 * Binarni vyhledavaci strom se jmeny souboru
 * @author Libor Vasa
 */
class BinarySearchTree {
    /** Koren binarniho vyhledavaciho stromu */
    Node root;
    int size;
    int numberOfNodes = 0;

    /**
     * Prida do BST prvek se zadanym klicem - jmenem souboru
     * @param key klic - jmeno souboru
     */
    void add(String key){
        if (root == null) { // DEBUG -> musí být == protože chceme porovnávat
            root = new Node(key); // DEBUG -> velké 'N' protože vytvářím nový objekt
        }
        else {
            addUnder(root, key);
        }
        numberOfNodes++;
    }

    /**
     * Vlozi pod zadany uzel novy uzel se zadanym klicem
     * @param n uzel, pod ktery se ma novy uzel vlozit
     * @param key klic noveho uzlu - nazev souboru
     */
    void addUnder(Node n, String key) {
        if (key.compareToIgnoreCase(n.key) < 0) {
            // uzel patri doleva, je tam misto?
            if (n.left == null) {
                n.left = new Node(key);
            }
            else {
                addUnder(n.left, key);
            }
        }
        else {
            // uzel patri doprava, je tam misto?
            if (n.right == null) {
                n.right = new Node(key);
            }
            else {
                addUnder(n.right, key);
            }
        }
    }

    boolean contains(String key){
        Node n = root;
        while (n != null){
            if (n.key.equals(key)){
                return true;
            }
            if (n.key.compareTo(key) > 0){
                n = n.left;
            } else {
                n = n.right;
            }
        }
        return false;
    }

    void printSorted(){
        for (String result: getSortedKeys()) {
            System.out.println(result);
        }
    }

    int printAllStartingWith(String prefix){
        int numberOfNodes = 0;
        ArrayList<String> contains = new ArrayList<>();
        Node n = root;
        Node save = null;
        while (n != null){
            String nKey = (String) n.key.subSequence(0,prefix.length());
            if (nKey.equals(prefix)){
                if (!contains.contains(n.key)){
                    contains.add(n.key);
                    numberOfNodes++;
                }
                if (save == null){
                    save = n;
                }
                if (n.left != null){
                    n = n.left;
                } else if (n.right != null) {
                    n = n.right;
                } else {
                    n = save.right;
                    save = null;
                    continue;
                }

            } else if (nKey.compareTo(prefix) >= 0){
                n = n.left;
                numberOfNodes++;
            } else {
                n = n.right;
                numberOfNodes++;
            }
            if (n == null && save != null){
                n = save.right;
                save = null;
            }

        }
        size = contains.size();
        for(String str:contains){
            System.out.println(str);
        }
        return numberOfNodes;
    }

    void remove(String key){
        Node n = root;
        Node pred = null;
        while (!key.equals(n.key)) {
            pred = n;
            if (key.compareTo(n.key) < 0) {
                n = n.left;
            } else {
                n = n.right;
            }
        }
        if ((n.left == null) || (n.right == null)){
            Node replace = n.left;
            if (n.right != null){
                replace = n.right;
            }
            if (pred == null){
                root = replace;
            } else {
                if (pred.left == n){
                    pred.left = replace;
                } else {
                    pred.right = replace;
                }
            }
        } else {
            Node leftMax = n.left;
            Node leftMaxPred = n;
            while (leftMax.right != null){
                leftMaxPred = leftMax;
                leftMax = leftMax.right;
            }
            n.key = leftMax.key;
            if (leftMax != n.left){
                leftMaxPred.right = leftMax.left;
            } else {
                n.left = leftMax.left;
            }
        }
    }

    ArrayList<String> getSortedKeys(){
        ArrayList<String> result = new ArrayList<>();
        getSortedKeysR(root,result);
        return result;
    }

    void getSortedKeysR(Node n, ArrayList<String> result){
        if (n!=null) {
            getSortedKeysR(n.left, result);
            result.add(n.key);
            getSortedKeysR(n.right, result);
        }
    }
}

/**
 * Trida pro doplnovani textu na zaklade historie
 * @author Libor Vasa
 */
public class Autocomplete {
    public static void main(String[] args) throws IOException {
        BinarySearchTree bst = new BinarySearchTree();
        /*
        bst.add("http://portal.zcu.cz");
        bst.add("http://courseware.zcu.cz");
        bst.add("http://coz.cz");
        bst.add("http://zcu.cz");

        if(bst.contains("http://zcu.cz")){
            System.out.println("Yes, obsahuje");
        } else {
            System.out.println("Noup, neobsahuje");
        }

        bst.printSorted();

        System.out.println("Počet prohledaných vrcholů: "+bst.printAllStartingWith("http://c"));

         */

        BufferedReader bufferedReader = new BufferedReader(new FileReader("requests.txt"));
        String s;
        String[] ss;
        while ((s = bufferedReader.readLine()) != null){
            ss = s.split(" ");
            switch (ss[0]) {
                case "A" -> bst.add(ss[1]);
                case "R" -> bst.remove(ss[1]);
                case "P" -> {
                    int projeto = bst.printAllStartingWith(ss[1]);
                    System.out.println(bst.numberOfNodes + "/" + projeto + "/" + bst.size);
                }
            }
        }
    }
}