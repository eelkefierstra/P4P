import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.highgui.Highgui;

public class ImShow implements Runnable
{
	private volatile Mat mat;
	private Screen screen;
	
	public ImShow(Screen screen, Mat mat)
	{
		this.screen = screen;
		this.mat = mat;
	}
	
	public synchronized void SetMat(Mat mat)
	{
		this.mat = mat;
	}
	
	public void run()
	{
		try
		{
			MatOfByte matOfByte = new MatOfByte();
			Highgui.imencode(".jpg", mat, matOfByte);
			byte[] byteArray = matOfByte.toArray();
			BufferedImage bufImage = null;
			InputStream in = new ByteArrayInputStream(byteArray);
			bufImage = ImageIO.read(in);
			screen.SetImage(bufImage);
		}
		catch (IOException ex)
		{
			ex.printStackTrace();
		}
	}
}
