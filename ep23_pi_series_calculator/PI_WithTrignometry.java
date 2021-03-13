package codespace.piseries;

import java.math.BigDecimal;

/**
 * Slowest of them all. Calculating PI with trignometry.
 * 
 * More information here.
 * https://math.stackexchange.com/questions/3343498/calculating-pi-using-pi-lim-n-to-inftyn-sin-frac180-circn
 */
public class PI_WithTrignometry extends PISeries {
    private BigDecimal n = new BigDecimal(1);
    private BigDecimal rad180 = null;

    public PI_WithTrignometry() {
        rad180 = new BigDecimal( Math.toRadians(180) );
    }

    public void calculatePI() {
        //piDouble = n * Math.sin(rad180 / n);
        BigDecimal sinCalc = new BigDecimal( Math.sin(rad180.divide(n, PRECISION).doubleValue()) );
        super.PI = n.multiply(sinCalc);
        n = n.add(ONE);
    }

    public String getName() {
        return "Trignometry";
    }
}    
