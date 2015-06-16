import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
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
		ServerSocket server;
		BufferedReader in;
		PrintWriter out;
		
		Socket client;
		int port = 9020;
		int nr = 0;
		try{
		    server = new ServerSocket(port); 
		    
		    client = server.accept();
		    
		    in = new BufferedReader(new InputStreamReader(  client.getInputStream()));
		    out = new PrintWriter(client.getOutputStream(), true);
		    
		    while (true) {
		    	nr++;
		    	String str = in.readLine();
		    	System.out.println(nr + ": " + str);
		    }
		    
		    
		  } catch (IOException e) {
			  e.printStackTrace();
		    System.out.println("Could not listen on port " + port);
		    System.exit(-1);
		  }
		finally {
			System.out.println("***********************************\n*                                 *\n***********************************\n");
		}
		
		
		
		/*
		String HOST = "192.168.1.100";
		for(int i = 0; i < 3; i++)
		{
			
			try
			{
				p.gui.setTitle("Connectie poging #" + (i + 1));
				System.err.println(InetAddress.getByName(HOST).getCanonicalHostName());
				p.server = new Socket(InetAddress.getByName(HOST), 9020);
				break;
			}
			catch (Exception ex)
			{
				ex.printStackTrace();
				System.err.println(ex.getLocalizedMessage());
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
		*/
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