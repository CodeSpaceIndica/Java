package codespace.piseries.ramanujam;

/**
 * A simple thread class that will invoke the PI calculations.
 */
public class PICalculatorThread extends Thread {

    public boolean runThread = true;

    public long cycles  = 0;
    public long totTime = 0;
    public long avgTime = 0;

    private PI_Ramanujam piCalculator = null;

    public PICalculatorThread(PI_Ramanujam piCalculator) {
        this.piCalculator = piCalculator;
    }

    public void run() {
        while ( cycles <= Long.MAX_VALUE && runThread ) {
            long start = System.currentTimeMillis();
            piCalculator.calculatePI();
            long end = System.currentTimeMillis();
            long diff = end-start;
            totTime += diff;
            cycles++;
            avgTime = totTime / cycles;

            //Wait only 1 milliseconds per calculation
            try { Thread.sleep(1); } catch(Exception e){}
        }
    }
}