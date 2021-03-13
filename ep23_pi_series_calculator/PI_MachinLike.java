package codespace.piseries;

import java.math.BigDecimal;

/**
 * MachinLike algorithm. 
 * More information here
 * http://personalpages.to.infn.it/~zaninett/pdf/machin.pdf
 */
public class PI_MachinLike extends PISeries {
    private BigDecimal multiplicand = new BigDecimal(4.0d);
    
    private BigDecimal numerator = new BigDecimal(1.0d);
    private BigDecimal incr = new BigDecimal(2.0d);
    private BigDecimal n = new BigDecimal(1.0d);
    private int iN = 1;

    private BigDecimal n1n = new BigDecimal(5.0d);
    private BigDecimal n1r = new BigDecimal(0.0d);//Result

    private BigDecimal n2n = new BigDecimal(239.0d);
    private BigDecimal n2r = new BigDecimal(0.0d);//Result

    public PI_MachinLike() {
    }

    public void calculatePI() {
        n1r = n1r.add( 
            numerator.divide(
                n.multiply( n1n.pow(iN), PRECISION), 
            PRECISION), 
        PRECISION);

        n2r = n2r.add( 
            numerator.divide(
                n.multiply( n2n.pow(iN), PRECISION), 
            PRECISION), 
        PRECISION);

        n = n.add(incr);
        iN += 2;
        numerator = numerator.negate();

        PI = multiplicand.multiply(
            multiplicand.multiply(n1r, PRECISION)
            .subtract(n2r, PRECISION)
            , PRECISION);
    }

    public String getName() {
        return "Machin-Like";
    }
}
