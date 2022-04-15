//To compile
//javac -d . *.java
//To execute
//java thebigint.AutoCoder

package thebigint.autocoder;

import java.awt.Image;
import java.awt.Toolkit;
import javax.swing.JFrame;
import java.awt.Dimension;
import java.io.File;
import java.nio.file.DirectoryStream.Filter;

/**
 * An automatic coder that will take an existing source code
 * and type it on a canvas automcatically with mechanical keyboards
 * et all.
 */
public class AutoCoder {

    public static void main(String []args) {

        String fileToType = null;
        if( args.length == 0 ) {
            System.out.println("Please provide a file to auto-type.");
            System.exit(0);
        }
        fileToType = args[0];
        File inputFile = new File(fileToType);
        if( !inputFile.exists() ) {
            System.out.println("File " + fileToType + "does not exist.");
            System.exit(0);
        }

        JFrame mainFrame = new JFrame("The Big Int Auto Coder");

        Image icon = Toolkit.getDefaultToolkit().getImage("images/icon.png");
        mainFrame.setIconImage(icon);

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
    
        CodingCanvas piCanvas = new CodingCanvas(inputFile);
        mainFrame.getContentPane().add(piCanvas);
    
        //Shows the Window
        mainFrame.setVisible(true);
        mainFrame.setExtendedState(mainFrame.getExtendedState() | JFrame.MAXIMIZED_BOTH);
    }
}
