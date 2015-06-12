import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;

public class ImShow implements Runnable
{
	private boolean tresh = false;
	private boolean feed = false;
	
	private Screen screen;
	private DroneTracker tracker;
	public boolean done;
	
	public ImShow(int index, Screen screen, DroneTracker tracker)
	{
		this.screen = screen;
		this.tracker = tracker;
		this.done = true;
		switch(index)
		{
		case 1:
			this.tresh = true;
			break;
		case 2:
			this.feed = true;
			break;
		}
	}
	
	public void run()
	{
		byte[] bytes = null;
		done = false;
		if (tresh) bytes = tracker.GetThresh();
		if (feed) bytes = tracker.GetFeed();
		done = true;
		try
		{
			BufferedImage bufImage = null;
			InputStream in = new ByteArrayInputStream(bytes);
			bufImage = ImageIO.read(in);
			in.close();
			screen.SetImage(bufImage);
			bufImage.flush();
		}
		catch (IOException ex)
		{
			Logger.getLogger(ImShow.class.getName()).log(Level.SEVERE, null, ex);
		}
	}
}
