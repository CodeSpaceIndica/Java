package codespace.piseries.ramanujam;

import java.awt.Canvas;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Font;
import java.awt.Color;
import java.awt.RenderingHints;
import java.awt.FontMetrics;
import java.awt.Rectangle;
import java.util.ArrayList;
import javax.swing.JPanel;
import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;

/**
 * A canvas(JPanel) where all of our PI Values are drawn
 */
public class PICanvas extends JPanel implements Runnable, MouseListener {
    //To prevent warnings while compiling. :-P
    private static final long serialVersionUID = -7016421651275244657L;

    //private static final RenderingHints rh = new RenderingHints(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

    //Fonts we will use. Footer is larger than main font
    private static final Font PI_FONT = new Font("Ubuntu Mono", Font.PLAIN, 20);
    private static final Font FT_FONT = new Font("Ubuntu Mono", Font.PLAIN, 26);

    //Colors to be used 
    private static final Color BG_COLOR = new Color(0, 12, 25);
    private static final Color PI_COLOR = new Color(100, 125, 255);
    private static final Color FT_COLOR = new Color(239, 228, 176);

    //values that will be calculated dynamically
    private int charsPerRow = 190;
    private int maxRows = 43;
    private int fontHgt = 24;

    //Thread variables.
    private PICalculatorThread calculatorThread = null;
    private boolean runThread = true;
    private int maxPrecision = PI_Ramanujam.MAX_PRECISION-1;
    private boolean started = false;
    
    //The main calculator
    private PI_Ramanujam ramanujamPICalculator = null;

    private long startTime, endTime, timeElap;

    /**
     * Contructor. Adds the mouse event, Initialize the Ramanujam calculator 
     * and initializes the calculator thread.
     */
    public PICanvas() {
        this.addMouseListener(this);
        ramanujamPICalculator = new PI_Ramanujam();

        calculatorThread = new PICalculatorThread(ramanujamPICalculator);
    }

    /**
     * Called with the mouse is clicked on the canvas.
     */
    public void startCalculating() {
        Graphics2D g = (Graphics2D)this.getGraphics();
        g.setFont(PI_FONT);
        FontMetrics metrics = g.getFontMetrics();
        int charWidth = metrics.charWidth('0');
        charsPerRow = (int)((this.getSize().width-40)/charWidth);
        maxRows = (int)((this.getSize().height-50)/fontHgt);

        //Pass them to a thread where they will be invoked separately.
        Thread t = new Thread(calculatorThread);
        t.start();

        startTime = System.currentTimeMillis();

        //Start the current canvas thread that will repaint the canvas
        Thread thisCanvas = new Thread(this);
        thisCanvas.start();
    }

    @Override
    public void paintComponent(Graphics gr) {
        Graphics2D g = (Graphics2D)gr;

        //g.setRenderingHints(rh);

        g.setColor(BG_COLOR);
        g.fillRect(0, 0, this.getSize().width, this.getSize().height);

        g.setFont(PI_FONT);

        int y = 20;

        //Draw the calculated PI values
        String piStr = ramanujamPICalculator.PI.toString();
        int index = this.matchAt(piStr);
        piStr = piStr.substring(0, index);
        ArrayList<String> valueSplits = splitStringsByLength(piStr);
        if( valueSplits.size() > maxRows ) {
            do {
                valueSplits.remove(0);
            } while(valueSplits.size() > maxRows);
            // int tY = (valueSplits.size() - maxRows) * fontHgt;
            // g.translate(0, -tY);
        }
        g.setColor(PI_COLOR);
        for(String piPart : valueSplits) {
            g.drawString(piPart, 20, y);
            y += fontHgt;
        }

        endTime = System.currentTimeMillis();
        timeElap = endTime - startTime;
        String timeElapsedStr = ((long)(timeElap / 1000)) + "." + ((long)(timeElap % 1000));
        String footer = String.format("cycles : %5s | average time : %4s | digits : %5s | elapsed time : %8s", calculatorThread.cycles, calculatorThread.avgTime, index, timeElapsedStr);
        g.setColor(FT_COLOR);
        g.setFont(FT_FONT);
        g.drawString(footer, 10, this.getSize().height-20);

        if( index >= maxPrecision ) {
            this.runThread = false;
            this.calculatorThread.runThread = false;
        }
    }

    /**
     * Compares a pi value with reference PI and sees what position the PI matches.
     * @param piVal
     * @return
     */
    private int matchAt(String piVal) {
        for(int i=0; i<piVal.length(); i++) {
            if( i > piVal.length()-1 || i > PILoadRefs.referencePI.length()-1 ) {
                return PILoadRefs.referencePI.length();
            }
            if( piVal.charAt(i) != PILoadRefs.referencePI.charAt(i) ) {
                return i;
            }
        }
        return 0;
    }

    /**
     * Splits a long string into equal lenghts and returns an array of that. 
     * 
     * @param aString
     * @return
     */
    private ArrayList<String> splitStringsByLength(String aString) {
        ArrayList<String> splitList = new ArrayList<String>();
        if( aString.length() <= charsPerRow ) {
            splitList.add(aString);
        }
        else {
            int startIdx = 0;
            int endIdx   = 0;
            do {
                endIdx = startIdx+charsPerRow;
                if( endIdx > aString.length()-1 ) {
                    endIdx = aString.length()-1;
                }
                String aStr = aString.substring(startIdx, endIdx);
                splitList.add(aStr);

                startIdx += charsPerRow;
            } while( startIdx <= aString.length()-1 );
        }

        return splitList;
    }

    public void run() {
        while(runThread) {
            this.repaint();

            try { Thread.sleep(150); }catch(Exception exp){}
        }
    }

    /**
     * When clicked, calls the startCalculating method.
     */
    public void mouseClicked(MouseEvent me) {
        if( !started ) {
            this.startCalculating();
            started = true;
        }
    }

    public void mousePressed(MouseEvent me) {}
    public void mouseReleased(MouseEvent me) {}
    public void mouseEntered(MouseEvent me) {}
    public void mouseExited(MouseEvent me) {}
}