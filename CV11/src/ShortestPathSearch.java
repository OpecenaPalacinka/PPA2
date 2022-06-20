import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

/**
 * Prvek spojoveho seznamu pro ulozeni sousedu vrcholu grafu
 * @author Libor Vasa
 */
class Link {
    /** Cislo souseda */
    int neighbour;
    /** Odkaz na dalsiho souseda */
    Link next;

    /**
     * Vytvori novy prvek seznamu pro ulozeni souseda vrcholu grafu
     * @param n cislo souseda
     * @param next odkaz na dalsiho souseda
     */
    public Link(int n, Link next) {
        this.neighbour = n; //DEBUG -> doplněné this
        this.next = next; //DEBUG -> doplněné this
    }
}

/**
 * Graf pro ulozeni mapy
 * @author Libor Vasa
 */
class Graph {
    /** Sousedi jednotlivych vrcholu (hrany) */
    Link[] edges;
    int delkaRadky;

    /**
     * Inicializuje sousedy jednotlivych vrcholu (hrany)
     * @param vertexCount pocet vrcholu grafu
     */
    public void initialize(int vertexCount) {
        this.edges = new Link[vertexCount];
    }

    /**
     * Prida do grafu novou obousmernou hranu
     * @param start cislo pocatecniho vrcholu
     * @param end cislo koncoveho vrcholu
     */
    public void addEdge(int start, int end) {
        Link n = new Link(end, edges[start]); //DEBUG->prohozené souřadnice Linku
        edges[start] = n;
        Link n1 = new Link(start, edges[end]);
        edges[end] = n1;
    }

    ArrayList<Integer> neighbours(int v) {
        ArrayList<Integer> result = new ArrayList<>();
        Link n = edges[v];
        while (n!=null){
            result.add(n.neighbour);
            n = n.next;
        }
        return result;
    }


    public int shortestPathLength(int start, int end) {
        int[] result;
        result = distance(start);
        return result[end];
    }

    public int farthestVertex(int start){
        int pomoc = Integer.MIN_VALUE;
        int index = 0;
        int[] result;
        result = distance(start);
        for (int i = 0; i < result.length; i++) {
            if (result[i] > pomoc){
                pomoc = result[i];
                index = i;
            }
        }
        return index;
    }

    public int[] distance(int start){
        int[] result = new int[edges.length];
        for(int i = 0;i<edges.length;i++){
            result[i] = -1;
        }
        result[start] = 0;

        int[] mark = new int[edges.length];
        mark[start] = 1;

        Queue<Integer> q = new LinkedList<>();
        q.add(start);
        while (!q.isEmpty()){
            int v = q.remove();
            ArrayList<Integer> nbs = neighbours(v);
            for (int n : nbs) {
                if (mark[n] == 0) {
                    mark[n] = 1;
                    q.add(n);
                    result[n] = result[v] + 1;
                }
            }
            mark[v] = 2;
        }
        return result;
    }

    public String loadFromFile(){
        StringBuilder string = new StringBuilder();
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader("map.txt"))) {
            String s;
            int counter = 0;
            while ((s = bufferedReader.readLine()) != null){
                string.append(s);
                counter++;
            }
            delkaRadky = string.length() / counter;
        } catch (Exception e) {
            System.err.println("Chyba při čtení ze souboru.");
        }
        return string.toString();
    }

}

/**
 * Hledani nejkratsi cesty v grafu
 * @author Libor Vasa
 */
public class ShortestPathSearch {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String nacteno;
        Graph g = new Graph();
        nacteno = g.loadFromFile();
        g.initialize(nacteno.length());

        for (int i = 0; i < nacteno.length(); i++) {
            if ((i + g.delkaRadky) > nacteno.length()) {
                if (i + 1 < nacteno.length()) {
                    if (nacteno.charAt(i) == 'a' && nacteno.charAt(i + 1) == 'a') {
                        g.addEdge(i, i + 1);
                    }
                }
            } else {
                if (nacteno.charAt(i) == 'a' && nacteno.charAt(i + 1) == 'a') {
                    g.addEdge(i, i + 1);
                }
                if (nacteno.charAt(i) == 'a' && nacteno.charAt(i + g.delkaRadky - 1) == 'a') {
                    g.addEdge(i, i + g.delkaRadky - 1);
                }
                if (nacteno.charAt(i) == 'a' && nacteno.charAt(i + g.delkaRadky) == 'a') {
                    g.addEdge(i, i + g.delkaRadky);
                }
                if ((i+g.delkaRadky+1<nacteno.length()) && nacteno.charAt(i) == 'a' && nacteno.charAt(i + g.delkaRadky + 1) == 'a') {
                    g.addEdge(i, i + g.delkaRadky + 1);
                }
            }
        }

        char[] poleCharu = nacteno.toCharArray();
        int s;

        while (true) {
            System.out.print("Zadejte startovní vrchol: ");
            s = sc.nextInt();
            if(poleCharu[s] == 'a'){
                poleCharu[s] = 's';
                break;
            } else {
                System.out.println("Tento vrchol je nedostupný, zkuste to znovu ->");
            }
        }

        int [] poleVzdalenosti = g.distance(s);
        int vzdalenost = g.shortestPathLength(s,g.farthestVertex(s));

        for (int j = 0; j < nacteno.length() ; j++) {
            if (poleVzdalenosti[j]==vzdalenost){
                poleCharu[j] = 'k';
                break;
            }
        }

        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter("result.txt"))) {
            for (int i = 0; i < poleCharu.length ; i++) {
                if (i % (g.delkaRadky) == 0 && i > 5){
                    bufferedWriter.newLine();
                }
                bufferedWriter.write(poleCharu[i]);
            }
            bufferedWriter.flush();

        } catch (Exception e) {
            System.err.println("Chyba při zapisování do souboru.");
        }

        System.out.println("Nejvzdálenejší bod je "+vzdalenost+" hran daleko");


        /*
        g.addEdge(0, 1);
        g.addEdge(0, 5);
        g.addEdge(1, 7);
        g.addEdge(1, 2);
        g.addEdge(2, 7);
        g.addEdge(2, 8);
        g.addEdge(4, 9);
        g.addEdge(5,10);
        g.addEdge(7, 8);
        g.addEdge(7,12);
        g.addEdge(8, 9);
        g.addEdge(8,12);
        g.addEdge(8,13);
        g.addEdge(9,13);
        g.addEdge(9,14);
        g.addEdge(10,15);
        g.addEdge(12,13);
        g.addEdge(12,17);
        g.addEdge(12,18);
        g.addEdge(13,14);
        g.addEdge(13,18);
        g.addEdge(13,19);
        g.addEdge(14,19);
        g.addEdge(17,18);
        g.addEdge(18,19);
        System.out.println(g.shortestPathLength(15, 19));
        System.out.println(g.farthestVertex(15));

         */
    }
}