//To Compile : javac -d . *.java
//To Execute : java codespace.piseries.ramanujam.PIMain
//Java version when this was created : openjdk version "14.0.1" 2021-03-13

package codespace.piseries.ramanujam;

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
    //Load the reference files first
    PILoadRefs.loadFile();

    JFrame mainFrame = new JFrame("Ramanujam PI Calculator");

    //When window X button is clcked, it exits the application.
    mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    
    Dimension screenSize = mainFrame.getToolkit().getScreenSize();

    //Set size to 1000, 600
    mainFrame.setSize(screenSize.width, screenSize.height);

    //Get the resolution of the screen and put Main window in the middle 
    //of the screen.
    int xLoc = 0;//screenSize.width/2 - ((screenSize.width-50)/2);
    int yLoc = 0;
    mainFrame.setLocation(xLoc, yLoc);

    PICanvas piCanvas = new PICanvas();
    mainFrame.getContentPane().add(piCanvas);

    //Shows the Window
    mainFrame.setVisible(true);
    mainFrame.setExtendedState(mainFrame.getExtendedState() | JFrame.MAXIMIZED_BOTH);
  }
}