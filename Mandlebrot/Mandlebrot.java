//To Compile : javac -d . Mandlebrot.java
//To Execute : java codespace.mandlebrot.Mandlebrot
//Java version when this was created : openjdk version "14.0.1" 2020-04-14
package codespace.mandlebrot;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.KeyEvent;
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
public class Mandlebrot extends Canvas implements MouseListener, MouseMotionListener, KeyListener, Runnable {

    private static final long serialVersionUID = 1L;
    
    //Width and height of the window
    private static final int WIDTH = 800;
    private static final int HEIGHT = 800;

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
    private int maxIteration = 1000;
    private int threshold = 10;

    //Color variables
    private int[] colorMap = new int[16];
    private int black = 0;

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
        this.addKeyListener(this);
        this.mouseRect = new Rectangle(0, 0, 0, 0);

        this.initializeColorMap();
        
        mbImage = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_ARGB);
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
            //Area of rectangle basically
            double newArea = (this.maxXVal - this.minXVal) * (this.maxYVal - this.minYVal);
            //8 because the initial limits of the graph are from -2 to 2.
            double zoomed = 8.0 / newArea;

            System.out.println(this.minXVal + " " + this.minYVal + ", " +  this.maxXVal + " " + this.maxYVal + "|A=" + newArea + "|Z=" + zoomed + "|maxIter=" + this.maxIteration + "|threshold=" + this.threshold);

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
     * Get integer value of a color. Alpha is always assumed to be 255.
     * 
     * @param red
     * @param green
     * @param blue
     * @return
     */
    public int getIntFromColor(int red, int green, int blue) {
        red   = (red << 16) & 0x00FF0000;   //Shift red 16-bits and mask out other stuff
        green = (green << 8) & 0x0000FF00;  //Shift Green 8-bits and mask out other stuff
        blue  = blue & 0x000000FF;          //Mask out anything not blue.

        return 0xFF000000 | red | green | blue; //0xFF000000 for 100% Alpha. Bitwise OR everything together.
    }

    /**
     * Initialize the color map.
     */
    private void initializeColorMap() {
        this.colorMap[0] = getIntFromColor(66, 30, 15);
        this.colorMap[1] = getIntFromColor(25, 7, 26);
        this.colorMap[2] = getIntFromColor(9, 1, 47);
        this.colorMap[3] = getIntFromColor(4, 4, 73);
        this.colorMap[4] = getIntFromColor(0, 7, 100);
        this.colorMap[5] = getIntFromColor(12, 44, 138);
        this.colorMap[6] = getIntFromColor(24, 82, 177);
        this.colorMap[7] = getIntFromColor(57, 125, 209);
        this.colorMap[8] = getIntFromColor(134, 181, 229);
        this.colorMap[9] = getIntFromColor(211, 236, 248);
        this.colorMap[10] = getIntFromColor(241, 233, 191);
        this.colorMap[11] = getIntFromColor(248, 201, 95);
        this.colorMap[12] = getIntFromColor(255, 170, 0);
        this.colorMap[13] = getIntFromColor(204, 128, 0);
        this.colorMap[14] = getIntFromColor(153, 87, 0);
        this.colorMap[15] = getIntFromColor(106, 52, 3);

        this.black = getIntFromColor(0, 0, 0);
    }

    /**
     * Taken from https://stackoverflow.com/questions/16500656/which-color-gradient-is-used-to-color-mandelbrot-in-wikipedia
     * 
     * @return
     */
    private int getColor(int n) {
        if (n < this.maxIteration && n > 0) {
            int i = n % 16;
            return this.colorMap[i];
        }
        return this.black;
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

                int n = 0;

                while( n < this.maxIteration ) {
                    double aa = a*a - b*b;
                    double bb = 2 * a * b;

                    if( Math.abs(aa+bb) > this.threshold ) {
                        break;
                    }

                    a = aa + origA;
                    b = bb + origB;

                    n++;
                }

                int clr = getColor(n);
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
        this.currentlyDrawing = false;
        this.mouseRect.setRect(0, 0, 0, 0);
        repaint();
    }

    //Mouse Events
    @Override
    public void mouseClicked(MouseEvent me){}
    @Override
    public void mousePressed(MouseEvent me) {
        if( me.getButton() == MouseEvent.BUTTON3 ) {
            return;
        }
        this.isMouseDown = true;
        this.mouseRect.x = me.getX();
        this.mouseRect.y = me.getY();    
        repaint();
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
        this.mouseRect.width  = me.getX() - this.mouseRect.x;
        this.mouseRect.height = me.getX() - this.mouseRect.x; //Height is same as Width
        if( this.mouseRect.width <= 0) {
            return;
        }
        if( !this.currentlyDrawing ) {
            double oldMinXVal = this.minXVal;
            double oldMinYVal = this.minYVal;
            double oldMaxXVal = this.maxXVal;
            double oldMaxYVal = this.maxYVal;

            int x = this.mouseRect.x + this.mouseRect.width;
            int y = this.mouseRect.y + this.mouseRect.height;

            this.minXVal = map(this.mouseRect.x, 0, WIDTH,  oldMinXVal, oldMaxXVal);
            this.minYVal = map(this.mouseRect.y, 0, HEIGHT, oldMinYVal, oldMaxYVal);
            this.maxXVal = map(x, 0, WIDTH,  oldMinXVal, oldMaxXVal);
            this.maxYVal = map(y, 0, HEIGHT, oldMinYVal, oldMaxYVal);

            startDrawing();
        }
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
            this.mouseRect.height = me.getX() - this.mouseRect.x; //Height is same as width. We want the rectangle to be square
            repaint();
        }
    }
    @Override
    public void mouseMoved(MouseEvent me) {
        if( this.isMouseDown ) {
            this.mouseRect.width  = me.getX() - this.mouseRect.x;
            this.mouseRect.height = me.getX() - this.mouseRect.x; //Height is same as width. We want the rectangle to be square
            repaint();
        }
    }

    @Override
    public void keyTyped(KeyEvent ke) {
        if( this.currentlyDrawing ) {
            System.out.println("Busy...");
            return;
        }
        if( ke.getKeyChar() == '+' ) {
            this.minXVal += 0.01;
            this.minYVal += 0.01;
            this.maxXVal -= 0.01;
            this.maxYVal -= 0.01;
            this.startDrawing();
        }
        else if( ke.getKeyChar() == '-' ) {
            this.minXVal -= 0.01;
            this.minYVal -= 0.01;
            this.maxXVal += 0.01;
            this.maxYVal += 0.01;
            this.startDrawing();
        }
        else if( ke.getKeyChar() == 'a' ) {
            this.maxIteration *= 10;
            this.startDrawing();
        }
        else if( ke.getKeyChar() == 's' ) {
            this.maxIteration /= 10;
            this.startDrawing();
        }
        else if( ke.getKeyChar() == 'z' ) {
            this.threshold *= 10;
            this.startDrawing();
        }
        else if( ke.getKeyChar() == 'x' ) {
            this.threshold /= 10;
            this.startDrawing();
        }
    }

    @Override
    public void keyPressed(KeyEvent ke) {
        if( ke.getKeyCode() == KeyEvent.VK_ESCAPE ) {
            this.minXVal = -2;
            this.minYVal = -2;
            this.maxXVal = 2;
            this.maxYVal = 2;
            this.startDrawing();
        }
    }

    @Override
    public void keyReleased(KeyEvent ke) {}

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