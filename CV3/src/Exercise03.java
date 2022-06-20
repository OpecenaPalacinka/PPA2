/**
 * Hlavni trida programu pro numericky vypocet urciteho integralu
 * @author Libor Vasa
 */
public class Exercise03 {
    public static void main(String[] args) {
        /*
        LinearFunction lf = new LinearFunction(0, 1);
        Integrator integrator = new Integrator();
        integrator.setDelta(0.0001);
        QuadraticPolynomial qf = new QuadraticPolynomial(1, 0, 0);
        qf.setEpsilon(0.001);
        lf.setEpsilon(0.001);
        double integral = integrator.integrate(lf, 0, 10);
        double integralQP = integrator.integrate(qf, 0, 10); // 333.333 -> výsledek (vychází)
        System.out.println(integral);
        System.out.println(integralQP);
        double derivace = lf.differentiate(2); //Derivace 0x+1 = 0
        double derivaceQP = qf.differentiate(2); //Derivace x^2 = 2x -> 2*1 = 2
        System.out.println(derivace);
        System.out.println(derivaceQP);
        GeneralPolynomial gp = new GeneralPolynomial(new double[]{3, 2, 1, 0});
        double integralGP = integrator.integrate(gp, 0, 10); // 8216.666 -> výsledek (vychází)
        System.out.println(integralGP+"\n");
        */
        IFunction myPolynomial = new GeneralPolynomial(new double[]{7, -5, 3, -15});
        IFunction firstDerivative = new Derivative(myPolynomial);
        IFunction secondDerivative = new Derivative(firstDerivative);
        IFunction thirdDerivative = new Derivative(secondDerivative);

        for (double x = 0; x < 10.1; x += 0.2) {
            // 21x^2 + -10x + 3
            // 42x - 10
            // 42
            System.out.println(thirdDerivative.valueAt(x));
        }

        // jeste overeni treba pro funkci sinus
        IFunction sin = new Sine();
        IFunction cos = new Derivative(sin);
        System.out.println("Cosinus");
        for (double x = 0; x < Math.PI * 2; x += 0.3) {
            System.out.println(cos.valueAt(x));
        }
    }
}

/**
 * Obecna matematicka funkce
 * @author Libor Vasa
 */
interface IFunction {
    /**
     * Vypocte a vrati hodnotu funkce v zadanem bode
     * @param p bod, ve kterem ma byt urcena hodnota funkce
     * @return hodnotu funkce v zadanem bode
     */
    double valueAt(double p);
    /** Vypočte derivaci v daném bodě */
    double differentiate(double x);
}

/**
 * Trida pro numericky vypocet urciteho integralu funkce
 * @author Libor Vasa
 */
class Integrator {
    /** Krok pro vypocet integralu */
    double delta;

    /**
     * Numbericky vypocte a vrati urcity integral zadane fukce
     * @param f funkce, jejiz integral se ma vypocitat
     * @param a pocatek intervalu integrace
     * @param b konec intervalu integrace
     * @return urcity integral zadane funkce
     */
    public double integrate(IFunction f, double a, double b) {
        double result = 0;
        double p = a; // p = spodní hranice integrálu
        double v = f.valueAt(p); // v = hodnota funkce ve spodní hranici integrálu
        while (p + delta < b) {
            //obdelniky sirky delta
            result += delta * v; // delta = x -- v == y
            p += delta; // k p připočtu delta -> posunu se o jeden dílek
            v = f.valueAt(p); // přepočítám v pro aktuální p
        }
        // jeste posledni obdelnik, ktery bude uzsi nez delta
        result += (b - p) * v; //DEBUG - prohození b a p -> protože b > p
        return result;
    }

    /**
     * Nastavi krok pro vypocet integralu
     * @param d krok pro vypocet integralu
     */
    public void setDelta(double d) {
        this.delta = d; //DEBUG - delta změněno na d
    }

}

/**
 * Linerani funkce
 * @author Libor Vasa
 */
class LinearFunction extends AbstractFunction{
    /** Smernice funkce */
    double k;
    /** Posun funkce */
    double q;

    /**
     * Vytvori novou linearni funkci se zadanymi koeficienty
     * @param k smernice funkce
     * @param q posun funkce
     */
    public LinearFunction(double k, double q) {
        this.k = k;
        this.q = q;
    }

    @Override //DEBUG doplnění implementované metody - smazání funkce getValue a změna x na p
    public double valueAt(double p) {
        return (k * p + q);
    }

    @Override
    public double differentiate(double x) {
        return k;
    }
}

class QuadraticPolynomial extends AbstractFunction{
    double a;
    double b;
    double c;

    public QuadraticPolynomial(double a, double b, double c) {
        this.a = a;
        this.b = b;
        this.c = c;
    }

    @Override
    public double valueAt(double p) {
        return ((a*(p*p)) + (b*p) + c) ;
    }

}

abstract class AbstractFunction implements IFunction{
    double h = 0.1;
    double epsilon = 0.01;

    public void setEpsilon(double e) {
        this.epsilon = e;
    }

    @Override
    public double differentiate(double x) {
        double f;
        double f1;
        h = 0.1;
        do{
            f = (valueAt(x+h)-valueAt(x))/h;
            h *= 0.5;
            f1 = (valueAt(x+h)-valueAt(x))/h;
            if (h < 0.0000000000000000000000000005){ // Ochrana proti nekonečnému cyklu u špatných funkcí
                break;
            }
        } while (Math.abs(f - f1) > epsilon);
        return f1;
    }
}

class GeneralPolynomial extends AbstractFunction{
    double[] coefs;

    public GeneralPolynomial(double[] coefs) {
        this.coefs = coefs;
    }

    @Override
    public double valueAt(double p) {
        double result = 0;
        double x;
        for (int i = 0; i < coefs.length; i++) {
            /* Metoda bez Math.pow() */
            x = 1;
            for (int j = 0; j < coefs.length-i-1; j++) {
                x *= p;
            }
            result += coefs[i]*x;
            /* Metoda s Math.pow()
            result += coefs[i]*Math.pow(p,coefs.length-i-1);
            */
        }
        return result;
    }

}

class Sine extends AbstractFunction{

    @Override
    public double valueAt(double p) {
        return Math.sin(p);
    }

    @Override
    public double differentiate(double x) {
        return Math.cos(x);
    }
}

class Derivative extends AbstractFunction{
    IFunction f;

    public Derivative(IFunction f) {
        this.f = f;
    }

    @Override
    public double valueAt(double p) {
        return f.differentiate(p);
    }
}
