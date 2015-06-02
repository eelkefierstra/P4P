/*
import java.awt.MouseInfo;
import java.awt.Point;
import java.text.DecimalFormat;
*/
import java.awt.Point;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLDecoder;
import java.text.DecimalFormat;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.concurrent.*;

//@SuppressWarnings("unused")
public class Main
{
	//private GUI gui = new GUI();
	
	private Screen screen1 = new Screen();
	private Screen screen2 = new Screen();
	private Screen screen3 = new Screen();
	
	private double maxfps;
	private double fps;
	private long nextTime;
	private ImShow show1;
	private ImShow show2;
	private ImShow show3;
	private ScheduledExecutorService executor;
	private ScheduledFuture<?>[] futureList;
	private DecimalFormat format;
	
	
	// Main loop
    //@SuppressWarnings("unused")
	public static void main(String[] args)
    {
        Main p = new Main();

		p.screen1.setSize(1280, 720);
		p.screen2.setSize(1280, 720);
		p.screen3.setSize(1280, 720);
		
        try
        {
			String path = p.getClass().getResource("/lib/").toURI().toString();
        	path = path.substring(6);
        	path = path.replaceAll("%20", " ");
			System.load(path + "dronetracker.dll");        
		}
		catch (Exception ex)
		{
			Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
		}
		String[] files = null;
		try
		{
	    	files = p.GetResourceListing(p.getClass(), "/audio/");
		}
		catch (Exception ex)
		{
			Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
		}
		
		FPSCounter counter = new FPSCounter();
		p.show1 = new ImShow(p.screen1);
		p.show2 = new ImShow(p.screen2);
		p.show3 = new ImShow(p.screen3);
		p.executor = Executors.newScheduledThreadPool(3);
		p.futureList = new ScheduledFuture<?>[3];
		p.format = new DecimalFormat("#.##");
		ShutdownHook hook = new ShutdownHook();
		hook.attachShutDownHook(p.executor);
		counter.start();
        //DroneDetection detection = new DroneDetection(new Audio(files));
        DroneTracker tracker = new DroneTracker();
        tracker.Setup();
        boolean first = true;
		
		while(true)
		{
			tracker.Track();
			if (!first)
			{
				p.show1.SetImage(tracker.GetThresh());
				p.futureList[0] = p.executor.schedule(p.show1, 0, TimeUnit.NANOSECONDS);
				p.show2.SetImage(tracker.GetFeed());
				p.futureList[1] = p.executor.schedule(p.show2, 0, TimeUnit.NANOSECONDS);
				p.show3.SetImage(tracker.GetHSV());
				p.futureList[2] = p.executor.schedule(p.show3, 0, TimeUnit.NANOSECONDS);
			}
			else first = false;



			counter.interrupt();
			p.fps = counter.GetFPS();
			if (p.nextTime <= System.nanoTime())
			{
				p.maxfps = 0.0;
				p.nextTime = System.nanoTime() + 2500000000L;
				//audio.SetClip(z);
				//audio.PLayClip();
				/*z++;
				if (z > 22)
				{
					x = 0;
				}*/
			}
			if (p.fps > p.maxfps)
			{
				p.maxfps = p.fps;
			}
			p.screen2.setTitle(p.format.format(p.fps)+" max "+p.format.format(p.maxfps));
			try
			{
				Thread.sleep(0);
			}
			catch (InterruptedException ex)
			{
				Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
			}
		}
	}
	
    /*
    public Point getLocationRelativeTo()
    {
        int x = ((screen2.getX() - MouseInfo.getPointerInfo().getLocation().x) - (screen2.getWidth() / 2) * -1) * -1;
        int y = (screen2.getY() - MouseInfo.getPointerInfo().getLocation().y) + (screen2.getHeight() / 2);
        return new Point(x, y);
    }
    
    public int getXRelativeTo()
    {
        int x = screen2.getX() - MouseInfo.getPointerInfo().getLocation().x;
        return (x + (screen2.getWidth() / 2)) * -1;
    }
    
    public int getYRelativeTo()
    {
        int y = screen2.getY() - MouseInfo.getPointerInfo().getLocation().y;
        return y + screen2.getHeight() / 2;
    }
	*/
	
	private String[] GetResourceListing(Class clazz, String path) throws URISyntaxException, IOException
	{
	      URL dirURL = clazz.getClassLoader().getResource(path);
	      if (dirURL != null && dirURL.getProtocol().equals("file"))
	      {
	        /* A file path: easy enough */
	        return new java.io.File(dirURL.toURI()).list();
	      }

	      if (dirURL == null)
	      {
	        /* 
	         * In case of a jar file, we can't actually find a directory.
	         * Have to assume the same jar as clazz.
	         */
	        String me = clazz.getName().replace(".", "/")+".class";
	        dirURL = clazz.getClassLoader().getResource(me);
	      }
	      
	      if (dirURL.getProtocol().equals("jar"))
	      {
	    	  /* A JAR path */
	    	  String jarPath = dirURL.getPath().substring(5, dirURL.getPath().indexOf("!")); //strip out only the JAR file
	    	  JarFile jar = new JarFile(URLDecoder.decode(jarPath, "UTF-8"));
	    	  Enumeration<JarEntry> entries = jar.entries(); //gives ALL entries in jar
	    	  Set<String> result = new HashSet<String>(); //avoid duplicates in case it is a subdirectory
	    	  while(entries.hasMoreElements())
	    	  {
	    		  String name = entries.nextElement().getName();
	    		  if (name.startsWith(path))
	    		  { //filter according to the path
	    			  String entry = name.substring(path.length());
	    			  int checkSubdir = entry.indexOf("/");
	    			  if (checkSubdir >= 0)
	    			  {
	    				  // if it is a subdirectory, we just return the directory name
	    				  entry = entry.substring(0, checkSubdir);
	    			  }
	    			  result.add(entry);
	    		  }
	    	  }
	    	  jar.close();
	    	  return result.toArray(new String[result.size()]);
	      }
	      throw new UnsupportedOperationException("Cannot list files for URL "+dirURL);
	}
}
