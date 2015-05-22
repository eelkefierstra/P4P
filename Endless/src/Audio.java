import java.util.logging.Level;
import java.util.logging.Logger;
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
    	this.files = files;
    }
    
    public void PLayClip()
    {
    	clip.start();
    }
    
    public void SetClip(int x)
    {
    	this.x = x;
    	try
    	{
			stream = AudioSystem.getAudioInputStream(getClass().getResource(files[x]));
		    info = new DataLine.Info(Clip.class, stream.getFormat());
		    clip = (Clip) AudioSystem.getLine(info);
		    clip.open(stream);
		}
    	catch (Exception ex) 
    	{
			Logger.getLogger(GUI.class.getName()).log(Level.SEVERE, null, ex);
		}
    }
    
    public String GetClip()
    {
    	return files[x];
    }
}
