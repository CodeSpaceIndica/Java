//To Compile : javac -d . MandelbrotZoom.java
//To Execute : java thebigint.mandelbrotzoom.Mandelbrot
//Java version when this was created : openjdk version "14.0.1" 2020-04-14
//Generate mp4 using this ffmpeg command
//ffmpeg -framerate 25 -i image_frame_%03d.png new.mp4
package thebigint.mandelbrotzoom;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 * Mandlebrot Set Canvas class as well as its main function.
 * 
 * It also implements the Runnable interface so that the mandlebrot set is created in a 
 * separate thread.
 * 
 * The main method will create the JFrame and will place this canvas over itself.
 */
public class MandelbrotZoom {
    //Width and height of the window
    private static final int WIDTH = 800;
    private static final int HEIGHT = 800;

    private static final int NUM_FRAMES = 10;

    //This buffered image and the pixel will be used to draw the mandlebrot set
    private BufferedImage mbImage;
    private int[] imagePixels;

    //Starting MIN AND MAX VALUES. They start from -2 to 2.
    private double sMinX = -2;
    private double sMinY = -2;
    private double sMaxX = 2;
    private double sMaxY = 2;

    //Ending MIN AND MAX VALUES.
    // private double eMinX = -0.6002735730728121;
    // private double eMinY = -0.6646192892692977;
    // private double eMaxX = -0.6002735278513613;
    // private double eMaxY = -0.6646192440478469;
    private double eMinX = -1.2576470439078538;
    private double eMinY = 0.3780652779236957;
    private double eMaxX = -1.2576470439074896;
    private double eMaxY = 0.3780652779240597;

    //-1.2576470439078538 0.3780652779236957, -1.2576470439074896 0.3780652779240597

    private double minXIncr;
    private double minYIncr;
    private double maxXIncr;
    private double maxYIncr;

    //Mandlebrot variables.
    private int maxIteration = 1000;
    private int threshold = 10;

    //Color variables
    private int[] colorMap = new int[16];
    private int black = 0;

    /**
     * Constructor. 
     * 1. INitializes the color map
     * 1. Initializes the BufferedImage for drawing the Mandlebrot
     */
    public MandelbrotZoom() {
        this.initializeColorMap();

        this.minXIncr = (this.eMinX - this.sMinX) / NUM_FRAMES;
        this.minYIncr = (this.eMinY - this.sMinY) / NUM_FRAMES;
        this.maxXIncr = (this.eMaxX - this.sMaxX) / NUM_FRAMES;
        this.maxYIncr = (this.eMaxY - this.sMaxY) / NUM_FRAMES;
        System.out.println(minXIncr + " " + minYIncr + ", " + maxXIncr + " " + maxYIncr);
        System.out.println("-------------------------");

        mbImage = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_ARGB);
        imagePixels = new int[WIDTH * HEIGHT];
        mbImage.getRGB(0, 0, WIDTH, HEIGHT, imagePixels, 0, WIDTH);
        for(int i=0; i<imagePixels.length; i++) {
            imagePixels[i] = 0XFF000000;
        }
        mbImage.setRGB(0, 0, WIDTH, HEIGHT, imagePixels, 0, WIDTH);
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
     * This is where the Mandlebrot set is calculated.
     */
    public void generateFrames() {
        int f = 0;
        //for(int f=0; f<NUM_FRAMES+1; f++) {
        while( this.sMinX < this.eMinX || this.sMinY < this.eMinY
                || this.sMaxX > this.eMaxX || this.sMaxY > this.eMaxY ) {
            for(int x=0; x<WIDTH; x++) {
                for(int y=0; y<HEIGHT; y++) {
                    double a = map(x, 0, WIDTH, this.sMinX, this.sMaxX);
                    double b = map(y, 0, HEIGHT, this.sMinY, this.sMaxY);
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
            }
            //Done drawing. Save it
            mbImage.setRGB(0, 0, WIDTH, HEIGHT, imagePixels, 0, WIDTH);
            String fileName = "images\\image_frame_" + String.format("%03d", (f+1)) + ".png";
            try {
                File outputfile = new File(fileName);
                ImageIO.write(mbImage, "png", outputfile);
            } catch (IOException e) {
                //Nothing to handle
                e.printStackTrace();
            }
            System.out.println("Saved Frame " + f + " to file " + fileName);

            this.sMinX += this.minXIncr;
            this.sMinY += this.minYIncr;
            this.sMaxX += this.maxXIncr;
            this.sMaxY += this.maxYIncr;

            System.out.println(this.sMinX + ", " + this.sMinY + " - " + this.sMaxX + ", " + this.sMaxY);

            this.minXIncr *= 0.9;
            this.minYIncr *= 0.9;
            this.maxXIncr *= 0.9;
            this.maxYIncr *= 0.9;

            f++;
        }
    }

    /**
     * Main method.
     * 
     * @param args
     */
    public static void main(String args[]) {
        MandelbrotZoom mZoom = new MandelbrotZoom();
        mZoom.generateFrames();
    }
}