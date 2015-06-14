import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;


public class Main
{
	private GUI gui = new GUI();
	private Socket server;
	private byte imageByte[];
	private int imageSize = 921600;
	
    public static void main(String[] args)
    {
		Main p = new Main();
		for(int i = 0; i < 3; i++)
		{
			try
			{
				p.gui.setTitle("Connectie poging #" + (i + 1));
				p.server = new Socket(InetAddress.getByName("192.168.1.100"), 2000);
			}
			catch (Exception ex)
			{
				Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
				if (i == 2) p.gui.dispatchEvent(new WindowEvent(p.gui, WindowEvent.WINDOW_CLOSING));
			}
		}
		while(true)
		{
			try
			{
				p.SetImageByte();
				BufferedImage bufImage = null;
				InputStream in = new ByteArrayInputStream(p.imageByte);
				bufImage = ImageIO.read(in);
				in.close();
				p.gui.SetImage(bufImage);
				bufImage.flush();
			}
			catch (IOException ex)
			{
				Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
			}
		}
	}
    
    private void SetImageByte()
    {
    	try
    	{
		    InputStream in = server.getInputStream();
		    ByteArrayOutputStream baos = new ByteArrayOutputStream();
		    byte buffer[] = new byte[1024];
		    int remainingBytes = imageSize; //
		    while (remainingBytes > 0)
		    {
		    	int bytesRead = in.read(buffer);
		    	if (bytesRead < 0)
		    	{
		    		throw new IOException("Unexpected end of data");
		    	}
		    	baos.write(buffer, 0, bytesRead);
		    	remainingBytes -= bytesRead;
		    }
		    in.close();
		    imageByte = baos.toByteArray();  
		    baos.close();
	    }
    	catch (IOException ex)
    	{
    		
    	}
    }
}