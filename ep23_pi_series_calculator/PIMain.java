//To Compile : javac -d . *.java
//To Execute : java codespace.piseries.PIMain
//Java version when this was created : openjdk version "14.0.1" 2021-03-13

package codespace.piseries;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.awt.Dimension;
import javax.swing.JFrame;

/**
 * This is our main class that will create the frame,
 * create the thread that will calculate all the PI Serieses.
 */
public class PIMain {

  public static void main(String args[]) {
    JFrame mainFrame = new JFrame("PI Series Calculators");

    //When window X button is clcked, it exits the application.
    mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    
    Dimension screenSize = mainFrame.getToolkit().getScreenSize();

    //Set size to 1000, 600
    mainFrame.setSize(screenSize.width-50, 600);

    //Get the resolution of the screen and put Main window in the middle 
    //of the screen.
    int xLoc = screenSize.width/2;
    int yLoc = 100;
    mainFrame.setLocation(xLoc, yLoc);

    PICanvas piCanvas = new PICanvas();
    mainFrame.getContentPane().add(piCanvas);

    //Shows the Window
    mainFrame.setVisible(true);

    piCanvas.startCalculating();
  }
}
