import java.io.*;

class genericQueue<T> {
    private genericLink<T> first, last;

    public void add(T elem){
        genericLink<T> nl = new genericLink<T>();
        nl.data = elem;
        if (first == null) {
            first = nl;
        } else {
            last.next = nl;
        }
        last = nl;
    }

    public T get(){
        if (first == null){
            return null;
        }
        return first.data;
    }

    public void removeFirst() {
        if (first != null) {
            first = first.next;
        }
        else {
            System.out.println("Remove call on empty queue. Probably error, continuing...");
        }
    }
}

class genericLink<T>{
    T data;
    genericLink<T> next;
}


/**
 * Prichozi hovor do call centra
 * @author Libor Vasa
 */
class IncomingCall {
    /** Volajici cislo */
    int callingNumber;
    /** Cas kdy hovor prisel (v sekundach od zacatku smeny) */
    int time;
}

/**
 * Prvek spojoveho seznamu
 * @author Libor Vasa
 */
class Link {
    /** Data prvku - prichozi hovor do call centra */
    IncomingCall data;
    /** Dalsi prvek spojoveho seznamu */
    Link next;
}

/**
 * Fronta prichozich hovoru
 * @author Libor Vasa
 */
class CallerQueue {
    /** Prvni prvek fronty */
    private Link first;
    /** Posledni prvek fronty */
    private Link last;

    /**
     * Prida prichozi hovor na konec fronty
     * @param call prichozi hovor
     */
    public void add(IncomingCall call) {
        Link nl = new Link();
        nl.data = call;
        if (first == null) {
            first = nl;
        }
        else {
            last.next = nl; //DEBUG -> chybělo next
        }
        last = nl;
    }

    /**
     * Vrati prvni prichozi hovor nebo null, pokud je fronta prazdna
     * @return prvni prichozi hovor nebo null, pokud je fronta prazdna
     */
    public IncomingCall get() {
        if (first == null) { // DEBUG -> oprava '!' na '=' protože chci vrátit null když je fronta prázdná
            return null;
        }
        else {
            return first.data;
        }
    }

    /**
     * Odstrani prvni prichozi hovor z fronty, pokud fronta neni prazdna
     */
    public void removeFirst() {
        if (first != null) {
            first = first.next;
        }
        else {
            System.out.println("Remove call on empty queue. Probably error, continuing...");
        }
    }
}

class Operator {
    /** Volný operátor*/
    FreeOperator data;
    /** Operator */
    Operator next;
}

class OperatorQueue {
    /** První operátor*/
    private Operator first;
    /** Poslední operátor*/
    private Operator last;
    /** Metoda funguje na stejném principu jako v CallerQueue*/
    public void add(FreeOperator call) {
        Operator nl = new Operator();
        nl.data = call;
        if (first == null) {
            first = nl;
        }
        else {
            last.next = nl;
        }
        last = nl;

    }
    /** Metoda funguje na stejném principu jako v CallerQueue*/
    public FreeOperator get() {
        if (first == null) {
            return null;
        }
        else {
            return first.data;
        }
    }
    /** Metoda funguje na stejném principu jako v CallerQueue*/
    public void removeFirst() {
        if (first != null) {
            first = first.next;
        }
        else {
            System.out.println("Remove call on empty queue. Probably error, continuing...");
        }
    }
}

class FreeOperator {
    public String name;
    public int time;

    public FreeOperator(String name, int time){
        this.name = name;
        this.time = time;
    }
}

/**
 * Odbavovani prichozich hovoru pomoci operatoru
 * @author Libor Vasa
 */
class Dispatcher {
    /** Fronta prichozich hovoru */
    private genericQueue<IncomingCall> callerQueue;
    /** Fronta operatoru */
    private genericQueue<FreeOperator> operatorQueue;

    public String operatorAKANoWorker;
    public int numberForoperator;
    public int timeOfWaiting = 0;

    /**
     * Vytvori novou instanci s prazdnymi frontami
     */
    public Dispatcher() {
        this.callerQueue = new genericQueue<>();
        this.operatorQueue = new genericQueue<>();
    }

    /**
     * Zaradi prichozi hovor do fronty
     * @param number telefonni cislo prichoziho hvoru
     * @param time cas zacatku hovoru (v sekundach od zacatku smeny)
     */
    public void call(int number, int time) {
        IncomingCall call = new IncomingCall();
        call.callingNumber = number;
        call.time = time;
        callerQueue.add(call);
    }

    /**
     * Zaradi volneho operatora do fronty
     * @param name jmeno volneho operatora
     * @param time cas zarazeni volneho operatora do fronty (v sekundach od zacatku smeny)
     */
    public void freeOperator(String name, int time) {
        operatorQueue.add(new FreeOperator(name, time)); // operator name se time sekund od zacatku smeny prihlasil jako dostupny
    }

    /**
     * Priradi nejdele cekajici hovor z fronty nejdele cekajicimu operatorovi z fronty
     */
    public String dispatchCall() {
        if (callerQueue.get() != null && operatorQueue.get() != null){
            IncomingCall volajici = callerQueue.get();
            FreeOperator operator = operatorQueue.get();
            callerQueue.removeFirst();
            operatorQueue.removeFirst();
            return assignCall(volajici,operator);
        }
        return "";
    }

    /**
     * Priradi zadany prichozi hovor zadanemu volnemu operatorovi
     * @param call prichozi hovor
     * @param operator volny operator
     */
    private String assignCall(IncomingCall call, FreeOperator operator) {
        if (operator.time-call.time > timeOfWaiting){
            operatorAKANoWorker = operator.name;
            timeOfWaiting = operator.time-call.time;
            numberForoperator = call.callingNumber;
        }

        return   operator.name + " is answering call from +420 " + call.callingNumber + "\r\n"
                + "The caller has waited for " + Math.max(0, operator.time - call.time) + " seconds.\r\n";
    }
}

public class CallDispatching {
    public static void main(String[] args) throws IOException {
        Dispatcher d = new Dispatcher();
        /* Úloha 1/2
        d.freeOperator("Tonda", 0);
        d.dispatchCall();
        d.freeOperator("Jarmila", 10);
        d.dispatchCall();
        d.call(608123456, 15);
        d.dispatchCall();
        d.call(723987654, 35);
        d.dispatchCall();
        d.call(602112233, 45);
        d.dispatchCall();
        d.freeOperator("Pepa", 62);
        d.dispatchCall();
        d.call(608987654, 124);
        d.dispatchCall();
        d.freeOperator("Tonda", 240);
        d.dispatchCall();
        */
        File file = new File("callCentrum.txt");
        BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter("dispatching.txt"));
        String line;
        String[] parsedLine;
        while ((line = bufferedReader.readLine()) != null){
            parsedLine = line.split(" ");
            if (parsedLine[0].equals("O")){
                d.freeOperator(parsedLine[2],Integer.parseInt(parsedLine[1]));
            } else if (parsedLine[0].equals("C")) {
                d.call(Integer.parseInt(parsedLine[2]),Integer.parseInt(parsedLine[1]));
            }
            bufferedWriter.write(d.dispatchCall());
        }
        bufferedWriter.flush();
	//Špatný výpis, zkoukni zadání
        System.out.println("Nejdelší pauzu měl/a "+d.operatorAKANoWorker+", který čekal "+d.timeOfWaiting+" sekund a nakonec byl spojen s číslem: +420 "+d.numberForoperator);

    }
}
