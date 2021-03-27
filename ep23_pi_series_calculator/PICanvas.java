package codespace.piseries;

import java.awt.Canvas;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Font;
import java.awt.Color;
import java.awt.RenderingHints;
import java.awt.FontMetrics;
import java.util.ArrayList;
import javax.swing.JPanel;

/**
 * A canvas(JPanel) where all of our PI Values are drawn
 */
public class PICanvas extends JPanel implements Runnable {
    private static final long serialVersionUID = -7016421651275244657L;

    private static final String REFERENCE_PI = "3.1415926535897932384626433832795028841971693993751058209749445923078164062862089986280348253421170679";
    
    private static final RenderingHints rh = new RenderingHints(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
    private static final Font PI_FONT = new Font(Font.MONOSPACED, Font.PLAIN, 24);
    private static final Color PI_COLOR = new Color(100, 255, 100);

    private PICalculatorThread calculatorThread = null;
    private PISeries []piCalculators = null;

    public void startCalculating() {
        //Create a list of all the PI Series classes
        ArrayList<PISeries> calcList = new ArrayList<PISeries>();
        calcList.add(new PI_WithTrignometry());
        calcList.add(new PI_MadhavaLeibniz());
        calcList.add(new PI_Nilakantha());
        calcList.add(new PI_MachinLike());
        calcList.add(new PI_Ramanujam());
        piCalculators = calcList.toArray(new PISeries[calcList.size()]);

        //Pass them to a thread where they will be invoked separately.
        calculatorThread = new PICalculatorThread(piCalculators);
        Thread t = new Thread(calculatorThread);
        t.start();

        //Start the current canvas thread that will repaint 
        //the canvas once every 40 milliseconds (25fps)
        Thread thisCanvas = new Thread(this);
        thisCanvas.start();
    }

    @Override
    public void paintComponent(Graphics gr) {
        Graphics2D g = (Graphics2D)gr;

        g.setRenderingHints(rh);

        g.setColor(Color.BLACK);
        g.fillRect(0, 0, this.getSize().width, this.getSize().height);
        
        g.setFont(PI_FONT);
        FontMetrics metrics = g.getFontMetrics();
        int charWidth = metrics.charWidth('0');

        //Draw the reference PI
        g.setColor(PI_COLOR);
        g.drawString("Reference", 10, 40);
        g.drawString(":", 260, 40);
        g.drawString(REFERENCE_PI, 275, 40);

        int y = 90;

        //Draw the calculated PI values
        for (PISeries piCalculator : piCalculators) {
            g.setColor(Color.WHITE);
            g.drawString(piCalculator.getName(), 10, y);
            g.drawString(":", 260, y);
            String piStr = piCalculator.PI.toString();
            g.drawString(piStr, 275, y);

            int m = matchAt(piStr);
            g.setColor(PI_COLOR);
            g.fillRect(275, y+5, (m*charWidth), 3);

            y += 40;
        }

        //NUmber of cycles so far.
        g.setColor(Color.WHITE);
        g.drawString("Cycles :" + calculatorThread.cycles, 10, this.getSize().height-20);
    }

    private int matchAt(String piVal) {
        for(int i=0; i<piVal.length(); i++) {
            if( i > piVal.length()-1 || i > REFERENCE_PI.length()-1 ) {
                return REFERENCE_PI.length();
            }
            if( piVal.charAt(i) != REFERENCE_PI.charAt(i) ) {
                return i;
            }
        }
        return 0;
    }

    public void run() {
        while(true) {
            this.repaint();

            try { Thread.sleep(40); }catch(Exception exp){}
        }
    }
}
