import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * Doucovane predmety
 * @author Libor Vasa
 */
enum Subject {math, computers}

class Plan{
    public PlanEvent[] planEvents;

    public Plan(PlanEvent[] planEvents) {
        this.planEvents = planEvents;
    }

    /**
     * Vrati true, pokud se tato udalost prekryva se zadanou udalosti, jinak vrati false
     * @return true, pokud se tato udalost prekryva se zadanou udalosti, jinak vrati false
     */
    public boolean isConflict() {
        for (int i = 0; i < planEvents.length; i++) {
            for (int j = i+1; j < planEvents.length; j++) {
                if(planEvents[i].isInConflict(planEvents[j])){
                    return true;
                }
            }
        }
        return false;
    }

    public boolean isOK(){
        int countMath = 0;
        int countComp = 0;
        int[] dnyMath = new int[7];
        int[] dnyComp = new int[7];
        for (PlanEvent event: planEvents) {
            if (event.subject == Subject.math){
                for (int den: dnyMath ) {
                    if (den != event.dayOfWeek) {
                        dnyMath[countMath] = event.dayOfWeek;
                        countMath++;
                    }
                    break;
                }
            } else {
                for (int den: dnyComp ) {
                    if (den != event.dayOfWeek) {
                        dnyComp[countComp] = event.dayOfWeek;
                        countComp++;
                    }
                    break;
                }
            }
            if (countMath >= 3 && countComp >= 2) {
                /* Cvičení 2
                System.out.println("Matika:" + countMath + " info:" + countComp);
                System.out.println("Dny matiky:" + Arrays.toString(dnyMath) + " dny info: " + Arrays.toString(dnyComp));
                */
                return true;
            }
        }
        return false;
    }
}

/**
 * Nabidka tutora na doucovani
 * @author Libor Vasa
 */
public class PlanEvent {
    /** Jmeno tutora */
    public String tutor;
    /** Hodina pocatku doucovani (10 = 10:00 atd.) */
    public int start;
    /** Hodina konce doucovani (10 = 10:00 atd.) */
    public int end;
    /** Den tydne doucovani (0 = Pondeli, 1 = Utery atd.) */
    public int dayOfWeek;
    /** Doucovany predmet */
    public Subject subject;

    /**
     * Vytvori nobou nabidku tutora na doucovani
     */
    public PlanEvent(String tutor, int start, int end, int dayOfWeek, Subject subject) {
        // DEBUG -> v argumentu byl napsán String s malým "s" a všude chybělo "this."
        this.tutor = tutor;
        this.start = start;
        this.end = end;
        this.dayOfWeek = dayOfWeek;
        this.subject = subject;
    }

    /**
     * Vrati true, pokud se tato udalost prekryva se zadanou udalosti, jinak vrati false
     * @param other udalost, ktera se muze prekryvat s touto udalosti
     * @return true, pokud se tato udalost prekryva se zadanou udalosti, jinak vrati false
     */
    public boolean isInConflict(PlanEvent other) {
        if (this.dayOfWeek != other.dayOfWeek) {
            return false;
        }
        if (this.end <= other.start) { //DEBUG musí končit dřív než začíná druhý
            return false;
        }
        if (other.end <= this.start) { //DEBUG musí končit dřív než začíná první
            return false;
        }
        return true;
    }

    public static void main(String[] args) throws FileNotFoundException {
        /* Cvičení 1
        PlanEvent event1 = new PlanEvent("František Vonásek", 10, 13, 1, Subject.math);
        PlanEvent event2 = new PlanEvent("Čeněk Landsmann", 9, 12, 1, Subject.computers);
        PlanEvent event3 = new PlanEvent("Hubert Zámožný", 11, 14, 1, Subject.math);
        PlanEvent event4 = new PlanEvent("Dobromila Musilová-Wébrová", 9, 14, 1, Subject.computers);
        PlanEvent event5 = new PlanEvent("Sisoj Psoič Rispoloženskyj", 11, 12, 1, Subject.math);
        PlanEvent event6 = new PlanEvent("Billy Blaze", 8, 10, 1, Subject.computers);
        PlanEvent event7 = new PlanEvent("Flynn Taggart", 13, 15, 1, Subject.math);
        System.out.println(event1.isInConflict(event2));
        System.out.println(event1.isInConflict(event3));
        System.out.println(event1.isInConflict(event4));
        System.out.println(event1.isInConflict(event5));
        System.out.println(event1.isInConflict(event6));
        System.out.println(event1.isInConflict(event7));
        /* Cvičení 2
        Plan rozvrh = new Plan(new PlanEvent[] {event1,event2,event3,event4,event5,event6,event7});
        System.out.println("Jsou rozvrhové akce v konfliktu? -> "+rozvrh.isConflict());
        System.out.println("Nachází se v rozvrhu alespoň 3 matiky a 2 informatiky v jiné dny? -> "+rozvrh.isOK());
        */

        /* Cvičení 3 */
        Scanner sc = new Scanner(new File("ssc.txt"));
        int counter = 0;
        // Zjistím kolik je řádek v souboru, kvůli velikosti pole
        while (sc.hasNextLine()){
            sc.nextLine();
            counter++;
        }
        sc.close();
        sc = new Scanner(new File("ssc.txt"));
        // Vytvořím pole pro eventy ze souboru
        PlanEvent[] planEvents = new PlanEvent[counter/5];
        for (int i = 0; sc.hasNextLine(); i++) {
            String tutor = sc.nextLine();
            Subject sub;
            if (sc.nextLine().equals("math")){
                sub = Subject.math;
            } else {
                sub = Subject.computers;
            }
            int dayOfWeek = Integer.parseInt(sc.nextLine());
            int start = Integer.parseInt(sc.nextLine());
            int end = Integer.parseInt(sc.nextLine());
            planEvents[i] = new PlanEvent(tutor,start,end,dayOfWeek,sub);
        }

        String denTydne;
        int pocetValidnichReseni = 0;

        // Rekurzi se mi nepodařilo zprovoznit takže bohužel 5 for cyklů
        for (int i = 0; i < planEvents.length; i++) {
            for (int j = i+1; j < planEvents.length; j++) {
                for (int k = j+1; k < planEvents.length; k++) {
                    for (int l = k+1; l < planEvents.length; l++) {
                        for (int m = l+1; m < planEvents.length; m++) {
                            Plan plan = new Plan(new PlanEvent[]{planEvents[i],planEvents[j],planEvents[k],planEvents[l],planEvents[m]});
                            if (plan.isOK() && !plan.isConflict()){
                                System.out.println("Validní rozvrh:\n");
                                pocetValidnichReseni++;
                                for (PlanEvent event: plan.planEvents) {
                                    switch (event.dayOfWeek){
                                        case 0:
                                            denTydne = "v pondělí";
                                            break;
                                        case 1:
                                            denTydne = "v úterý";
                                            break;
                                        case 2:
                                            denTydne = "ve středu";
                                            break;
                                        case 3:
                                            denTydne = "ve čtvrtek";
                                            break;
                                        case 4:
                                            denTydne = "v pátek";
                                            break;
                                        case 5:
                                            denTydne = "v sobotu";
                                            break;
                                        case 6:
                                            denTydne = "v neděli";
                                            break;
                                        default:
                                            denTydne = "Neznámý den";
                                    }
                                    System.out.format("%s doučuje %s %s od: %s do: %s\n" ,event.tutor,event.subject,denTydne,event.start,event.end);
                                }
                                System.out.println("\n");
                            }
                        }
                    }
                }
            }
        }
        System.out.println("Počet správných řešení je: "+pocetValidnichReseni);
    }
}
