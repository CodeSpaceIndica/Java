package codespace.piseries;

/**
 * A simple thread class that will invoke the PI calculations.
 */
public class PICalculatorThread extends Thread {

    public long cycles = 0;

    private PISeries[] piCalculators = null;

    public PICalculatorThread(PISeries[] piCalculators) {
        this.piCalculators = piCalculators;
    }

    public void run() {
        while ( cycles <= Long.MAX_VALUE ) {
            for (PISeries piCalculator : piCalculators) {
                piCalculator.calculatePI();
            }
            cycles++;
            try { Thread.sleep(50); } catch(Exception e){}
        }
    }
}
