import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;


public class Main
{
	private GUI gui = new GUI();
	
    public static void main(String[] args)
    {
		Main p = new Main();
		// Initializes sockets
		ServerSocket server = null;
		Socket       client = null;
		ShutdownHook shutdownHook = new ShutdownHook();
		// Defines the port to be used for the stream
		int port = 9020;
		try
		{
			// Waits until the client connects to server
			server = new ServerSocket(port);
			client = server.accept();
			shutdownHook.attachShutDownHook(server);
		}
		catch (IOException ex)
		{
		    System.out.println("Could not listen on port " + port);
		    System.exit(-1);
		}	
		
		while(true)
		{
		    try
			{
		    	
				BufferedImage bufImage = null;
				InputStream in = new ByteArrayInputStream(p.GetImageByte(client));
				bufImage = ImageIO.read(in);
				in.close();
				p.gui.SetImage(bufImage);
				bufImage.flush();
			}
			catch (IOException ex1)
			{
				Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex1);
				try
				{
					client = server.accept();
				}
				catch (IOException ex2)
				{
					Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex2);
				}
			}
		}
	}
    
    // from www.codeplats.com/7HzyUUjqWe/sending-opencvmat-image-to-websocket-java-client.html
    private byte[] GetImageByte(Socket client)
    {
    	byte[] imageByte = null;
    	int imageSize = 921600;
    	try
    	{
    		// Gets the inputstream from the client
		    InputStream in = client.getInputStream();
		    ByteArrayOutputStream baos = new ByteArrayOutputStream();
		    byte buffer[] = new byte[1024];
		    int remainingBytes = imageSize; // Makes sure that the entire image is read
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
    		Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
    	}
    	return imageByte;
    }
}