package thebigint.autocoder;

import java.io.FileInputStream;
import java.util.Properties;
import java.awt.Color;
import java.awt.Font;
import java.io.File;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

/**
 * A single properties loader.
 */
public class CoderProperties {
    private static final String PROPFILENAME = "autocoder.properties";

    private static CoderProperties _instance = null;
    
    private Properties props = null;

    public String formatFile = null;

    public String commentStart = null;
    public String commentEnd = null;
    public String singleComment = null;
    public String []specialOperators = null;
    public String []statements = null;
    public String []constants = null;
    public String []literals = null;
    public String []objects = null;
    public String []propertiesMethods = null;
    public String []reservedWords = null;
    public String []otherObjects = null;
    public String []otherPropertiesMethods = null;

    public Font font = null;

    public Color backgroundColor        = null;
    public Color regularColor           = null;
    public Color commentColor           = null;
    public Color stringsColor           = null;
    public Color numbersColor           = null;
    public Color operatorColor          = null;
    public Color statementColor         = null;
    public Color constantsColor         = null;
    public Color literalsColor          = null;
    public Color objectsColor           = null;
    public Color propsMethsColor        = null;
    public Color reservedWordsColors    = null;
    public Color otherObjColor          = null;
    public Color otherPropsMethodsColor = null;
    public Color cursorColor            = null;

    public String cursorChar            = "_";

    public int typingSpeed = 50;

    public boolean isMuted = false;
    public float volume    = 1.0f;
    public int frequency   = 5;

    /**
     * Load properties
     */
    private CoderProperties() {
        try {
            props = new Properties();
            FileInputStream in = new FileInputStream(PROPFILENAME);
            props.load( in );
            in.close();

            formatFile = props.getProperty("ac.format.file");
            loadFormatFile();

            font = getFontProperty("ac.font.name", "ac.font.size");

            backgroundColor         = getColorProperty("ac.color.background");
            regularColor            = getColorProperty("ac.color.regular");
            commentColor            = getColorProperty("ac.color.comments");
            stringsColor            = getColorProperty("ac.color.strings");
            numbersColor            = getColorProperty("ac.color.numbers");
            operatorColor           = getColorProperty("ac.color.specialoperators");
            statementColor          = getColorProperty("ac.color.statements");
            constantsColor          = getColorProperty("ac.color.constants");
            literalsColor           = getColorProperty("ac.color.literals");
            objectsColor            = getColorProperty("ac.color.objects");
            propsMethsColor         = getColorProperty("ac.color.propertiesmethods");
            reservedWordsColors     = getColorProperty("ac.color.reservedwords");
            otherObjColor           = getColorProperty("ac.color.otherobjects");
            otherPropsMethodsColor  = getColorProperty("ac.color.otherpropertiesmethods");
            cursorColor             = getColorProperty("ac.color.cursor");

            cursorChar = getProperty("ac.cursor.char");

            typingSpeed = Integer.parseInt( getProperty("ac.typing.speed") );

            isMuted   = getBooleanProperty("ac.sound.mute");
            volume    = getFloatProperty("ac.sound.volume");
            frequency = getIntProperty("ac.sound.frequency");
        }
        catch(Exception exp) {
            exp.printStackTrace();
        }
    }

    private void loadFormatFile() {
        try {
            Properties fileProps = new Properties();
            FileInputStream in = new FileInputStream(this.formatFile);
            fileProps.load( in );
            in.close();

            commentStart            = fileProps.getProperty("commentstart");
            commentEnd              = fileProps.getProperty("commentend");
            singleComment           = fileProps.getProperty("singlecomment");
            specialOperators        = this.getFileArrayProperty(fileProps, "specialoperators");
            statements              = this.getFileArrayProperty(fileProps, "statements");
            constants               = this.getFileArrayProperty(fileProps, "constants");
            literals                = this.getFileArrayProperty(fileProps, "literals");
            objects                 = this.getFileArrayProperty(fileProps, "objects");
            propertiesMethods       = this.getFileArrayProperty(fileProps, "propertiesmethods");
            reservedWords           = this.getFileArrayProperty(fileProps, "reservedwords");
            otherObjects            = this.getFileArrayProperty(fileProps, "otherobjects");
            otherPropertiesMethods  = this.getFileArrayProperty(fileProps, "otherpropertiesmethods");
        }
        catch(Exception exp) {
            exp.printStackTrace();
        }
    }

    public static CoderProperties getInstance() {
        if( _instance == null ) {
            _instance = new CoderProperties();
        }

        return _instance;
    }

    public String getProperty(String key) {
        return this.props.getProperty(key);
    }

    public Font getFontProperty(String nameKey, String sizeKey) {
        String nameVal = this.props.getProperty(nameKey);
        String sizeVal = this.props.getProperty(sizeKey);
        int size = Integer.parseInt(sizeVal);

        return new Font(nameVal, Font.PLAIN, size);
    }

    public Color getColorProperty(String key) {
        String propVal = "#" + this.props.getProperty(key);
        return Color.decode(propVal);
    }

    public boolean getBooleanProperty(String key) {
        String propVal = this.props.getProperty(key);
        return Boolean.valueOf(propVal);
    }

    public float getFloatProperty(String key) {
        String propVal = this.props.getProperty(key);
        return Float.parseFloat(propVal);
    }

    public int getIntProperty(String key) {
        String propVal = this.props.getProperty(key);
        return Integer.parseInt(propVal);
    }

    public Properties getAllProperties() {
        return this.props;
    }

    public String[] getFileArrayProperty(Properties fProps, String key) {
        String propVal = fProps.getProperty(key);
        if( propVal != null ) {
            return propVal.split(",");
        }
        return new String[0];
    }
}
