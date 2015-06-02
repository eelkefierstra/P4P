import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

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
			screen.SetImage(bufImage);
		}
		catch (IOException ex)
		{
			ex.printStackTrace();
		}
	}
}
