import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;

public class ImShow implements Runnable
{
	private volatile byte[] bytes;
	private Screen screen;
	
	public ImShow(Screen screen)
	{
		this.screen = screen;
		this.bytes = null;
	}
	
	public void SetImage(byte[] bytes)
	{
		this.bytes = bytes;
	}
	
	public void run()
	{
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
