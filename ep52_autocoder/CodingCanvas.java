package thebigint.autocoder;

import java.awt.Canvas;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.RenderingHints;
import java.awt.FontMetrics;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.awt.image.BufferedImage;
import javax.swing.JPanel;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.FileInputStream;
import java.awt.event.MouseEvent;

public class CodingCanvas extends JPanel implements Runnable, MouseListener {
    //To prevent warnings while compiling. :-P
    private static final long serialVersionUID = -7016421651275244657L;

    private CoderProperties props = CoderProperties.getInstance();

    private static final RenderingHints rh = new RenderingHints(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

    private String fileContentsStr = null;
    private String []fileContents = null;

    private Thread typerThread = null;
    private boolean isStarted = false;
    private int index = 0;
    private boolean paused = false;

    private int startX = 85;
    private int startY = 25;

    private int x, y;
    private int charWidth, charHeight;

    private String []allStyleChars     = {""+(char)190, ""+(char)169, ""+(char)170, ""+(char)171, 
                                          ""+(char)172, ""+(char)173, ""+(char)174, ""+(char)175, 
                                          ""+(char)176, ""+(char)177, ""+(char)178, ""+(char)179};
    private char endStyleChar          = (char)190;
    private char commentChar           = (char)169;
    private char operatorChar          = (char)170;
    private char statementsChar        = (char)171;
    private char constantsChar         = (char)172;
    private char literalsChar          = (char)173;
    private char objectsChar           = (char)174;
    private char propMethodsChar       = (char)175;
    private char reservedChar          = (char)176;
    private char otherObjsChar         = (char)177;
    private char otherPropsMethodsChar = (char)178;
    private char stringedChar          = (char)179;
    private char numberedChar          = (char)180;

    private Color currentColor = null;

    private BufferedImage bufferImage = null;
    private Graphics2D buffG = null;
    private int imageY = 0;

    SoundPlayer player = new SoundPlayer();

    /**
     * Contructor. Adds the mouse event, Initialize the Ramanujam calculator 
     * and initializes the calculator thread.
     */
    public CodingCanvas(File fileToType) {
        //Load the file, the file after loading will be in variable this.fileContents
        this.loadFile(fileToType);

        //Now format it
        this.formatFile();

        this.fileContents = this.fileContentsStr.split("");

        this.addMouseListener(this);

        player.loadSounds();

        typerThread = new Thread(this);
    }

    public void startTyping() {
        if( !isStarted ) {
            Graphics2D g = (Graphics2D)this.getGraphics();
            g.setFont(props.font);
            FontMetrics metrics = g.getFontMetrics();
            charWidth = metrics.charWidth('A');
            charWidth += props.widthOffset;
            charHeight = (int)props.font.createGlyphVector(metrics.getFontRenderContext(), "ABCDEFGHIJKLMNOPQRSTUVWXYZ01234567890").getVisualBounds().getHeight();
            charHeight += props.heightOffset;

            currentColor = props.regularColor;
    
            x = startX;
            y = startY;

            String []lineSplits = this.fileContentsStr.split("\n");
            int numLines = (lineSplits.length * charHeight) + charHeight;

            Dimension size = this.getSize();
            bufferImage = new BufferedImage(size.width, numLines, BufferedImage.TYPE_INT_RGB);
            buffG = (Graphics2D)bufferImage.getGraphics();
            buffG.setColor(props.backgroundColor);
            buffG.fillRect(0, 0, this.getSize().width, numLines);
            buffG.setFont(props.font);
            imageY = 0;

            typerThread.start();
            isStarted = true;
            index = 0;
        }
        else {
            if( paused ) {
                paused = false;
            }
            else {
                paused = true;
            }
        }
    }

    @Override
    public void paintComponent(Graphics gr) {
        Graphics2D g = (Graphics2D)gr;

        g.setColor(props.backgroundColor);
        g.fillRect(0, 0, this.getSize().width, this.getSize().height);

        if( !isStarted ) {
            return;
        }

        g.drawImage(bufferImage, 0, imageY, this);
    }

    @Override
    public void run() {
        while(index < this.fileContents.length ) {
            if( !paused ) {
                this.drawOnBufferedImage();
                index++;

                this.repaint();
            }
            int typeSpeed = props.typingSpeed;
            if( this.fileContents[index].equals(" ")) {
                typeSpeed = 1;
            }

            try { Thread.sleep(typeSpeed); } catch(Exception e){}
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if( e.getClickCount() == 1 ) {
            startTyping();
        }
        else if( e.getClickCount() == 2 ) {
            index = 0;
            currentColor = props.regularColor;
    
            x = startX;
            y = startY;
        }
    }
    @Override
    public void mousePressed(MouseEvent e) {}
    @Override
    public void mouseReleased(MouseEvent e) {}
    @Override
    public void mouseEntered(MouseEvent e) {}
    @Override
    public void mouseExited(MouseEvent e) {}

    ///////////PRIVATE FUNCTIONS
    private void loadFile(File aFile) {
        try {
            FileInputStream inFile = new FileInputStream(aFile);
            byte []b = new byte[inFile.available()];
            inFile.read(b);
            inFile.close();

            this.fileContentsStr = new String(b);
        }
        catch(Exception exp) {
            exp.printStackTrace();
            this.fileContents = null;
        }
    }

    private void formatFile() {
        for(String aKeyword : props.specialOperators) {
            String regex = "\\b" + aKeyword + "\\b";
            this.fileContentsStr = this.fileContentsStr.replaceAll(regex, operatorChar+aKeyword+endStyleChar);
        }

        //Statements are ½ alt+171
        for(String aKeyword : props.statements) {
            String regex = "\\b" + aKeyword + "\\b";
            this.fileContentsStr = this.fileContentsStr.replaceAll(regex, statementsChar+aKeyword+endStyleChar);
        }

        //Constants are ¼ alt+172
        for(String aKeyword : props.constants) {
            String regex = "\\b" + aKeyword + "\\b";
            this.fileContentsStr = this.fileContentsStr.replaceAll(regex, constantsChar+aKeyword+endStyleChar);
        }

        //Literals are ¡ alt+173
        for(String aKeyword : props.literals) {
            String regex = "\\b" + aKeyword + "\\b";
            this.fileContentsStr = this.fileContentsStr.replaceAll(regex, literalsChar+aKeyword+endStyleChar);
        }

        //Objects are « alt+174
        for(String aKeyword : props.objects) {
            String regex = "\\b" + aKeyword + "\\b";
            this.fileContentsStr = this.fileContentsStr.replaceAll(regex, objectsChar+aKeyword+endStyleChar);
        }

        //properties methods are » alt+175
        for(String aKeyword : props.propertiesMethods) {
            String regex = "\\b" + aKeyword + "\\b";
            this.fileContentsStr = this.fileContentsStr.replaceAll(regex, propMethodsChar+aKeyword+endStyleChar);
        }

        //reserved Words are ░ alt+176
        for(String aKeyword : props.reservedWords) {
            String regex = "\\b" + aKeyword + "\\b";
            this.fileContentsStr = this.fileContentsStr.replaceAll(regex, reservedChar+aKeyword+endStyleChar);
        }

        //other objects are ▒ alt+177
        for(String aKeyword : props.otherObjects) {
            String regex = "\\b" + aKeyword + "\\b";
            this.fileContentsStr = this.fileContentsStr.replaceAll(regex, otherObjsChar+aKeyword+endStyleChar);
        }

        //other properties methods are ▓ alt+178
        for(String aKeyword : props.otherPropertiesMethods) {
            String regex = "\\b" + aKeyword + "\\b";
            this.fileContentsStr = this.fileContentsStr.replaceAll(regex, otherPropsMethodsChar+aKeyword+endStyleChar);
        }

        //Single line comments
        String regex = "\\/\\/.*";
        Matcher mtchr = Pattern.compile(regex).matcher(this.fileContentsStr);
        while( mtchr.find() ) {
            String commentString = mtchr.group();
            String originalCommentString = new String(commentString);
            //Remove all previously added styles
            for( String styleChar : allStyleChars ) {
                commentString = commentString.replaceAll(styleChar, "");
            }
            this.fileContentsStr = this.fileContentsStr.replace(originalCommentString, commentChar+commentString+endStyleChar);
        }

        //Multi line comments
        regex = "/\\*[^*]*\\*+(?:[^/*][^*]*\\*+)*/";
        mtchr = Pattern.compile(regex).matcher(this.fileContentsStr);
        while( mtchr.find() ) {
            String commentString = mtchr.group();
            String originalCommentString = new String(commentString);
            //Remove all previously added styles
            for( String styleChar : allStyleChars ) {
                commentString = commentString.replaceAll(styleChar, "");
            }
            this.fileContentsStr = this.fileContentsStr.replace(originalCommentString, commentChar+commentString+endStyleChar);
        }

        //Strings
        StringBuffer buffer = new StringBuffer("");
        boolean start = true;
        for(int i=0; i<this.fileContentsStr.length(); i++) {
            char chr = this.fileContentsStr.charAt(i);
            if( chr == '\"' ) {
                if( start ) {
                    buffer.append(stringedChar);
                    start = false;
                }
                else {
                    buffer.append(endStyleChar);
                    start = true;
                }
                buffer.append(chr);
            }
            else {
                buffer.append(chr);
            }
        }
        this.fileContentsStr = buffer.toString();

        buffer = new StringBuffer("");
        start = true;
        for(int i=0; i<this.fileContentsStr.length(); i++) {
            char chr = this.fileContentsStr.charAt(i);
            if( chr == '\'' ) {
                if( start ) {
                    buffer.append(stringedChar);
                    start = false;
                }
                else {
                    buffer.append(endStyleChar);
                    start = true;
                }
                buffer.append(chr);
            }
            else {
                buffer.append(chr);
            }
        }
        this.fileContentsStr = buffer.toString();

        //Numbers
        String numbersRegex = "-?\\d+\\.?\\d*";
        mtchr = Pattern.compile(numbersRegex).matcher(this.fileContentsStr);
        buffer = new StringBuffer("");
        while( mtchr.find() ) {
            System.out.println(mtchr.start() + " - " + mtchr.end());
            System.out.println( "|" + this.fileContentsStr.substring(mtchr.start(), mtchr.end()) + "|" );
            // String numberString = mtchr.group();
            // String originalCommentString = new String(numberString);
            // this.fileContentsStr = this.fileContentsStr.replace(originalCommentString, numberedChar+numberString+endStyleChar);
        }
    }

    private void drawOnBufferedImage() {
        buffG.setRenderingHints(rh);

        if( this.fileContents[index].equals("\n") || this.fileContents[index].equals("\r")  || this.fileContents[index].equals("\r\n") ) {
            player.playEnterSound();

            buffG.setColor(props.backgroundColor);
            buffG.fillRect(x, y-(charHeight/2), charWidth, charHeight);

            y += charHeight;
            x = startX;
            index++;

            if( y > this.getSize().height-200 ) {
                imageY -= charHeight;
            }
        }

        if( index > this.fileContents.length-1 ) {
            return;
        }

        if( this.fileContents[index].equals(""+operatorChar) ) {
            currentColor = props.operatorColor;
            index++;
        }
        if( this.fileContents[index].equals(""+statementsChar) ) {
            currentColor = props.statementColor;
            index++;
        }
        if( this.fileContents[index].equals(""+constantsChar) ) {
            currentColor = props.constantsColor;
            index++;
        }
        if( this.fileContents[index].equals(""+literalsChar) ) {
            currentColor = props.literalsColor;
            index++;
        }
        if( this.fileContents[index].equals(""+objectsChar) ) {
            currentColor = props.objectsColor;
            index++;
        }
        if( this.fileContents[index].equals(""+propMethodsChar) ) {
            currentColor = props.propsMethsColor;
            index++;
        }
        if( this.fileContents[index].equals(""+reservedChar) ) {
            currentColor = props.reservedWordsColors;
            index++;
        }
        if( this.fileContents[index].equals(""+otherObjsChar) ) {
            currentColor = props.otherObjColor;
            index++;
        }
        if( this.fileContents[index].equals(""+otherPropsMethodsChar) ) {
            currentColor = props.otherPropsMethodsColor;
            index++;
        }
        if( this.fileContents[index].equals(""+commentChar) ) {
            currentColor = props.commentColor;
            index++;
        }
        if( this.fileContents[index].equals(""+stringedChar) ) {
            currentColor = props.stringsColor;
            index++;
        }
        if( this.fileContents[index].equals(""+numberedChar) ) {
            currentColor = props.numbersColor;
            index++;
        }
        if( this.fileContents[index].equals(""+endStyleChar) ) {
            currentColor = props.regularColor;
            index++;
        }
        if( this.fileContents[index].equals("õ") ) {
            index++;
            paused = true;
        }

        if( index < this.fileContents.length ) {
            buffG.setColor(props.cursorColor);
            buffG.drawString( props.cursorChar, x+charWidth, y);

            buffG.setColor(props.backgroundColor);
            buffG.fillRect(x, y-(charHeight/2), charWidth, charHeight);

            buffG.setColor(currentColor);
            buffG.drawString( this.fileContents[index], x, y);

            if( !this.fileContents[index].equals("\n")  ) {
                x += charWidth;
            }
            if( index % props.frequency == 0 ) {
                player.playRandomMechKeySound();
            }
        }
    }
}
