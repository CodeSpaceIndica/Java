package codespace.piseries;

import java.math.BigDecimal;
import java.math.MathContext;

/**
 * An abstract class that defines the behaviour of 
 * classes that will calculate the value of PI.
 */
public abstract class PISeries {
    public abstract void calculatePI();
    public abstract String getName();

    protected static final MathContext PRECISION = new MathContext(100);

    protected static final BigDecimal ONE = new BigDecimal(1);

    public BigDecimal PI = new BigDecimal(0.0d);
}
