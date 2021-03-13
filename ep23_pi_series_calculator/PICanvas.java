package codespace.piseries;

import java.awt.Canvas;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Font;
import java.awt.Color;
import java.awt.RenderingHints;
import java.util.ArrayList;
import javax.swing.JPanel;

/**
 * A canvas(JPanel) where all of our PI Values are drawn
 */
public class PICanvas extends JPanel implements Runnable {
    private static final long serialVersionUID = -7016421651275244657L;

    private static final String REFERENCE_PI = "3.141592653589793238462643383279502884197169399375105820974944592307816406286208998628034";

    private static final RenderingHints rh = new RenderingHints(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
    private static final Font PI_FONT = new Font(Font.MONOSPACED, Font.PLAIN, 28);
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

        //Draw the reference PI
        g.setColor(PI_COLOR);
        g.drawString("Reference", 10, 40);
        g.drawString(":", 260, 40);
        g.drawString(REFERENCE_PI, 275, 40);

        int y = 100;

        //Draw the calculated PI values
        g.setColor(Color.WHITE);
        for (PISeries piCalculator : piCalculators) {
            g.drawString(piCalculator.getName(), 10, y);
            g.drawString(":", 260, y);
            g.drawString(piCalculator.PI.toString(), 275, y);
            y += 60;
        }

        //NUmber of cycles so far.
        g.drawString("Cycles :" + calculatorThread.cycles, 10, this.getSize().height-50);
    }

    public void run() {
        while(true) {
            this.repaint();

            try { Thread.sleep(40); }catch(Exception exp){}
        }
    }
}
