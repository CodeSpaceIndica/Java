package codespace.piseries;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * Ramanujam's algorithm. 
 * More information here
 * http://pdfs.semanticscholar.org/6810/c8ac1ad01821a504e66f5d8665aeeae93cab.pdf
 */
public class PI_Ramanujam extends PISeries {

    private static final BigInteger FOUR = new BigInteger("4");
    private static final BigInteger MULTIPLICAND_1 = new BigInteger("26390");
    private static final BigDecimal MULTIPLICAND_2 = new BigDecimal("1103");
    private static final BigInteger MULTIPLICAND_3 = new BigInteger("396");

    private BigDecimal coefficient = null;

    private BigInteger n = BigInteger.ZERO;
    private BigInteger nIncr = BigInteger.ONE;

    private BigDecimal seriesSum = new BigDecimal(0);

    public PI_Ramanujam() {
        //Turns out square root of 2 itself is an irrational number
        //So instead of calculating it, we just take it from a site 
        //that already has it calculated. Here square root of 2 is
        //calculated to 100 terms.
        //Taken from here. https://apod.nasa.gov/htmltest/gifcity/sqrt2.1mil
        BigDecimal sqrtOf2=  new BigDecimal("1.4142135623730950488016887242096980785696718753769480731766797379907324784621070388503875343276415727350138462309122970249248360558507372126441214970999358314132226659275055927557999505011527820605714701095599716059702745345968620147285174186408891986", PRECISION);
        BigDecimal coeff1 = new BigDecimal(2, PRECISION);
        BigDecimal coeff2 = new BigDecimal(9801, PRECISION);
        coefficient = coeff1.multiply(sqrtOf2, PRECISION)
                            .divide(coeff2, PRECISION);
    }

    public void calculatePI() {
        //Variable result1 should contain result of
        // (4n)! / n!^4
        BigDecimal firstNumerator = new BigDecimal( getFactorial( FOUR.multiply(n) ), PRECISION );
        BigInteger nFactorial = getFactorial(n);
        BigDecimal firstDenominator = new BigDecimal( nFactorial.pow(4), PRECISION );
        BigDecimal result1 = firstNumerator.divide(firstDenominator, PRECISION);

        //Variable result2 should contain result of
        // (26390n + 1103) / (396 ^ (4*n))
        BigDecimal secondNumerator = new BigDecimal(MULTIPLICAND_1.multiply(n), PRECISION);
        secondNumerator = secondNumerator.add(MULTIPLICAND_2);
        int pow = n.multiply(FOUR).intValue();
        BigDecimal secondDenominator = new BigDecimal(MULTIPLICAND_3.pow(pow), PRECISION);
        BigDecimal result2 = secondNumerator.divide(secondDenominator, PRECISION);

        BigDecimal seriesResult = result1.multiply(result2, PRECISION);

        seriesSum = seriesSum.add(seriesResult, PRECISION);

        BigDecimal coeffAndSeries = coefficient.multiply(seriesSum, PRECISION);

        PI = ONE.divide(coeffAndSeries, PRECISION);

        n = n.add(nIncr);
    }

    /**
     * Get a factorial of a number. Please pass a positive number
     */
    private BigInteger getFactorial(BigInteger aNum) {
        BigInteger factorial = new BigInteger("1");
        if(aNum.longValue() == 0 || aNum.longValue() == 1) {
            return factorial;
        }
        while(aNum.longValue() > 1) {
            factorial = factorial.multiply(aNum);
            aNum = aNum.subtract(BigInteger.ONE);
        }
        return factorial;
    }

    public String getName() {
        return "Ramanujam";
    }
}
