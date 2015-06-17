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
		ServerSocket server = null;
		Socket       client = null;
		//BufferedReader in;
		ShutdownHook shutdownHook = new ShutdownHook();
		int port = 9020;
		//int nr = 0;
		try
		{
			server = new ServerSocket(port);
			client = server.accept();
			shutdownHook.attachShutDownHook(server);
		    //in = new BufferedReader(new InputStreamReader(  client.getInputStream()));
		    //out = new PrintWriter(client.getOutputStream(), true);
		    /*
		    while (true)
		    {
		    	nr++;
		    	String str = in.readLine();
		    	System.out.println(nr + ": " + str);
		    }*/
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
    
    private byte[] GetImageByte(Socket client)
    {
    	byte[] imageByte = null;
    	int imageSize = 921600;
    	try
    	{
		    InputStream in = client.getInputStream();
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
    		Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
    	}
    	return imageByte;
    }
    /*
    private BufferedImage ConvertImage(byte[] imageByte)
    {
    	
    }*/
}