package codespace.piseries;

import java.math.BigDecimal;

/**
 * The Nilakantha series. Taken from here
 * https://www.codeproject.com/Articles/813185/Calculating-the-Number-PI-Through-Infinite-Sequenc
 * 
 */
public class PI_Nilakantha extends PISeries {
    private BigDecimal numerator = new BigDecimal(4.0d);
    private BigDecimal n1 = new BigDecimal(2.0d);
    private BigDecimal n2 = new BigDecimal(3.0d);
    private BigDecimal n3 = new BigDecimal(4.0d);
    private BigDecimal nIncr = new BigDecimal(2.0d);

    public PI_Nilakantha() {
        PI = new BigDecimal(3.0);
    }

    public void calculatePI() {
        PI = PI.add( numerator.divide( 
            n1.multiply(n2, PRECISION).multiply(n3, PRECISION)
            , PRECISION), PRECISION);
        n1 = n1.add(nIncr);
        n2 = n2.add(nIncr);
        n3 = n3.add(nIncr);
        numerator = numerator.negate();
    }

    public String getName() {
        return "Nilakantha";
    }
}
