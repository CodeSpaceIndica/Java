package codespace.piseries.ramanujam;

import java.io.FileInputStream;

/**
 * Loads the PI Reference and Root Two values from
 * their files and makes them available as strings.
 */
public class PILoadRefs {

    public static String referencePI = null;
    public static String rootTwo     = null;

    public static void loadFile() {
        try {
            FileInputStream in = new FileInputStream("piref.txt");
            byte []b = new byte[ in.available() ];
            in.read(b);
            in.close();
            referencePI = new String(b);
        }
        catch(Exception e) {
            e.printStackTrace();
            referencePI = null;
        }

        try {
            FileInputStream in = new FileInputStream("roottwo.txt");
            byte []b = new byte[ in.available() ];
            in.read(b);
            in.close();
            rootTwo = new String(b);
        }
        catch(Exception e) {
            e.printStackTrace();
            rootTwo = null;
        }
    }
}