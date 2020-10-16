//To Compile : javac -d . Mandlebrot.java
//To Execute : java codespace.mandlebrot.Mandlebrot
//Java version when this was created : openjdk version "14.0.1" 2020-04-14
package codespace.mandlebrot;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseEvent;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.awt.BasicStroke;
import javax.swing.JFrame;

/**
 * Mandlebrot Set Canvas class as well as its main function.
 * 
 * It also implements the Runnable interface so that the mandlebrot set is created in a 
 * separate thread.
 * 
 * The main method will create the JFrame and will place this canvas over itself.
 */
public class Mandlebrot extends Canvas implements MouseListener, MouseMotionListener, Runnable {

    private static final long serialVersionUID = 1L;
    
    //Width and height of the window
    private static final int WIDTH = 800;
    private static final int HEIGHT = 600;

    //Capture mouse states
    private boolean isMouseDown;
    private Rectangle mouseRect;

    //This buffered image and the pixel will be used to draw the mandlebrot set
    private BufferedImage mbImage;
    private int[] imagePixels;

    //MIN AND MAX VALUES. Default range is from -2 to 2.
    private double minXVal = -2;
    private double maxXVal = 2;
    private double minYVal = -2;
    private double maxYVal = 2;

    //Mandlebrot variables.
    private int maxIteration = 10000;
    private int threshold = 10;

    //A flag to determine if the set is currently being drawn
    private boolean currentlyDrawing = false;
    private int x = 0;

    //Basic stroke of width 2.
    private BasicStroke stroke = new BasicStroke(2);

    /**
     * Constructor. 
     * 1. Add mouse events.
     * 2. Initializes the BufferedImage for drawing the Mandlebrot
     */
    public Mandlebrot() {
        this.addMouseListener(this);
        this.addMouseMotionListener(this);
        this.mouseRect = new Rectangle(0, 0, 0, 0);
        
        mbImage = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
        imagePixels = new int[WIDTH * HEIGHT];
        mbImage.getRGB(0, 0, WIDTH, HEIGHT, imagePixels, 0, WIDTH);
        for(int i=0; i<imagePixels.length; i++) {
            imagePixels[i] = 0XFF000000;
        }
        mbImage.setRGB(0, 0, WIDTH, HEIGHT, imagePixels, 0, WIDTH);
    }

    /**
     * Start the thread to start drawing the mandlebrot set.
     * Do not start if it is already running.
     */
    public void startDrawing() {
        if( !this.currentlyDrawing ) {
            x = 0;
            this.currentlyDrawing = true;
            Thread aThread = new Thread(this);
            aThread.start();
        }
    }

    /**
     * Override update so that we don't get flickering when drawing.
     */
    @Override
    public void update(Graphics g) {
        paint(g);
    }

    /**
     * Draws the image and the rectangle plotted by the mouse.
     */
    @Override
    public void paint(Graphics gr) {
        Graphics2D graphics = (Graphics2D)gr;
        graphics.drawImage(mbImage, 0, 0, this);

        graphics.setStroke(stroke);
        graphics.drawRect(this.mouseRect.x, this.mouseRect.y, this.mouseRect.width, this.mouseRect.height);

    }

    /**
     * Maps a number of a given input range to a number of the output range.
     * 
     */
    private double map(double inputNum, double minInput, double maxInput, double minOutput, double maxOutput) {
        return (inputNum - minInput) * (maxOutput - minOutput) / (maxInput - minInput) + minOutput;
    }

    /**
     * The Run method of the thread. This is where the Mandlebrot set is calculated.
     */
    @Override
    public void run() {
        while(this.x < WIDTH) {
            for(int y=0; y<HEIGHT; y++) {
                double a = map(x, 0, WIDTH, this.minXVal, this.maxXVal);
                double b = map(y, 0, HEIGHT, this.minYVal, this.maxYVal);
                double origA = a;
                double origB = b;

                var n = 0;

                while( n < this.maxIteration ) {
                    var aa = a*a - b*b;
                    var bb = 2 * a * b;

                    if( Math.abs(aa+bb) > this.threshold ) {
                        break;
                    }

                    a = aa + origA;
                    b = bb + origB;

                    n++;
                }

                float hue = (float)map(n, 0, this.maxIteration, 0, 360);
                int clr = Color.HSBtoRGB(hue, 1, 1);
                int px = (x + y * WIDTH);
                this.imagePixels[px] = clr;
            }
            x++;
            
            if( x % 10 == 0 ) {
                mbImage.setRGB(0, 0, WIDTH, HEIGHT, imagePixels, 0, WIDTH);
                repaint();
            }
        }
        //Done drawing. Repaint one more time and set flag to currently NOT drawing.
        mbImage.setRGB(0, 0, WIDTH, HEIGHT, imagePixels, 0, WIDTH);
        repaint();
        this.currentlyDrawing = false;
    }

    //Mouse Events
    @Override
    public void mouseClicked(MouseEvent me){}
    @Override
    public void mousePressed(MouseEvent me) {
        this.isMouseDown = true;
        this.mouseRect.x = me.getX();
        this.mouseRect.y = me.getY();    
        repaint();
        if( !this.currentlyDrawing ) {
            this.minXVal = map(this.mouseRect.x, 0, WIDTH, this.minXVal, this.maxXVal);
            this.minYVal = map(this.mouseRect.y, 0, HEIGHT, this.minYVal, this.maxYVal);    
        }
    }
    @Override
    public void mouseReleased(MouseEvent me) {
        //If Right clicked, then reset everything back to original dimentions and repaint.
        if( me.getButton() == MouseEvent.BUTTON3 ) {
            this.minXVal = -2;
            this.maxXVal = 2;
            this.minYVal = -2;
            this.maxYVal = 2;
            startDrawing();
            return;
        }
        this.isMouseDown = false;
        this.mouseRect.width  = this.mouseRect.x - me.getX();
        this.mouseRect.height = this.mouseRect.y - me.getY();
        if( !this.currentlyDrawing ) {
            this.maxXVal = map(me.getX(), 0, WIDTH, this.minXVal, this.maxXVal);
            this.maxYVal = map(me.getY(), 0, HEIGHT, this.minYVal, this.maxYVal);
            double diff1 = 4 / (this.maxXVal - this.minXVal);
            double diff2 = 4 / (this.maxYVal - this.minYVal);
            double zoom = (diff1 + diff2) / 2;
            System.out.println( zoom );
        }
        startDrawing();
    }
    @Override
    public void mouseEntered(MouseEvent me){}
    @Override
    public void mouseExited(MouseEvent me){}

    //Mouse Motion Events
    @Override
    public void mouseDragged(MouseEvent me) {
        if( this.isMouseDown ) {
            this.mouseRect.width  = me.getX() - this.mouseRect.x;
            this.mouseRect.height = me.getY() - this.mouseRect.y;
            repaint();
        }
    }
    @Override
    public void mouseMoved(MouseEvent me) {
        if( this.isMouseDown ) {
            this.mouseRect.width  = me.getX() - this.mouseRect.x;
            this.mouseRect.height = me.getY() - this.mouseRect.y;
            repaint();
        }
    }

    /**
     * Main method. Creates JFrame, puts Mandlebrot Canvas inside the JFrame and makes 
     * it visible.
     * 
     * @param args
     */
    public static void main(String args[]) {
        JFrame mandlebrotFrame = new JFrame("Mandlebrot set - Zoom");

        //When window X button is clcked, it exits the application.
        mandlebrotFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Set size to 1000, 800
        mandlebrotFrame.setSize(WIDTH, HEIGHT);
        //Don't allow window resize.
        mandlebrotFrame.setResizable(false);

        //Get the resolution of the screen and put Mandlebrot window in the middle 
        //of the screen.
        Dimension screenSize = mandlebrotFrame.getToolkit().getScreenSize();
        int xLoc = screenSize.width/2 - WIDTH/2;
        int yLoc = screenSize.height/2 - HEIGHT/2;
        mandlebrotFrame.setLocation(xLoc, yLoc);

        Mandlebrot mandlebrotCanvas = new Mandlebrot();
        mandlebrotFrame.getContentPane().add(mandlebrotCanvas);

        //Shows the Window
        mandlebrotFrame.setVisible(true);

        mandlebrotCanvas.startDrawing();
    }
  
}