package thebigint.autocoder;

import java.io.File;
import java.util.ArrayList;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;

public class SoundPlayer {
    private static final int KEY_SOUND_FILENAME_START = 1;
    private static final int KEY_SOUND_FILENAME_END   = 27;

    private static final String ENTER_KEY_SOUND = "sounds/mech_enter.wav";

    private ArrayList<Clip> soundClips = new ArrayList<Clip>();
    private Clip enterKeySound = null;

    CoderProperties props = CoderProperties.getInstance();


    public void loadSounds() {
        try {
            File soundFile = new File(ENTER_KEY_SOUND);
            AudioInputStream inStream = AudioSystem.getAudioInputStream( soundFile.toURI().toURL() );
            enterKeySound = AudioSystem.getClip();
            enterKeySound.open(inStream);
            FloatControl gainControl = (FloatControl)enterKeySound.getControl(FloatControl.Type.MASTER_GAIN);
            float range = gainControl.getMaximum() - gainControl.getMinimum();
            float gain = (range * props.volume) + gainControl.getMinimum();
            gainControl.setValue(gain);

            for(int i=KEY_SOUND_FILENAME_START; i<=KEY_SOUND_FILENAME_END; i++) {
                String fileName = "sounds/mech" + i + ".wav";
                File sndFile = new File(fileName);
                AudioInputStream aSoundStream = AudioSystem.getAudioInputStream( sndFile.toURI().toURL() );
                Clip aClip = AudioSystem.getClip();
                aClip.open(aSoundStream);
                FloatControl clipGainControl = (FloatControl)aClip.getControl(FloatControl.Type.MASTER_GAIN);
                range = clipGainControl.getMaximum() - clipGainControl.getMinimum();
                gain = (range * props.volume) + clipGainControl.getMinimum();
                clipGainControl.setValue(gain);
    
                soundClips.add(aClip);
            }
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    public void playEnterSound() {
        if( props.isMuted ) {
            return;
        }
        if( enterKeySound != null ) {
            try {
                enterKeySound.setFramePosition(0);
                enterKeySound.start();
            }
            catch(Exception exp) {
                System.out.println( exp.getMessage() );
            }
        }
    }

    public void playRandomMechKeySound() {
        if( props.isMuted ) {
            return;
        }
        if( soundClips.size() > 0 ) {
            int randomNum = 0;
            do {
                randomNum = (int)(Math.random() * soundClips.size());
            } while( randomNum > soundClips.size()-1 );
            Clip aClip = soundClips.get(randomNum);
            try {
                aClip.setFramePosition(0);
                aClip.start();
            }
            catch(Exception exp) {
                System.out.println( exp.getMessage() );
            }
        }
    }
}
