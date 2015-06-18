import java.util.logging.Level;
import java.util.logging.Logger;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.Clip;

public class Audio
{
	int x;
	String[] files;
    AudioInputStream stream;
    DataLine.Info info;
    Clip clip;
    
    public Audio(String[] files)
    {
    	// 
    	this.files = files;
    }
    
    // plays the selected clip
    public void PLayClip()
    {
    	clip.start();
    }
    
    // sets the clip to be played
    public void SetClip(int x)
    {
    	this.x = x;
    	try
    	{
			stream = AudioSystem.getAudioInputStream(getClass().getResource(files[x]));
			AudioFormat format = stream.getFormat();
			// Converts the clip to little endian
		    info = new DataLine.Info(Clip.class, new AudioFormat(format.getSampleRate(), format.getSampleSizeInBits(), format.getChannels(), true, false));
		    clip = (Clip) AudioSystem.getLine(info);
		    clip.open(stream);
		}
    	catch (Exception ex) 
    	{
			Logger.getLogger(GUI.class.getName()).log(Level.SEVERE, null, ex);
		}
    }
    
    // Gets the name of the clip that is to be played
    public String GetClip()
    {
    	return files[x];
    }
}
