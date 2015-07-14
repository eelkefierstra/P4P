import java.awt.Point;
import java.text.DecimalFormat;
import java.util.List;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.concurrent.*;

public class Main
{
	private Screen screen = new Screen();
	
	private double maxfps;
	private double minfps;
	private double fps;
	private long nextTime;
	
	private DecimalFormat format;
	
	// Main loop
    //@SuppressWarnings("unused")
	public static void main(String[] args)
    {
        Main mainVar = new Main();
        DroneTracker tracker = new DroneTracker();
        FPSCounter counter = new FPSCounter();
        ServoController servController = new ServoController();
        ShutdownHook hook = new ShutdownHook();
		
        try
        {
			String path = mainVar.getClass().getResource("/lib/").toURI().toString();
        	path = path.substring(6);
        	path = "/" + path.replaceAll("%20", " ");
			System.load(path + "libdronetracker.so");
		}
		catch (Exception ex)
		{
			Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
		}
		String[] files = null;
		try
		{
			java.io.File file = new java.io.File(mainVar.getClass().getResource("/audio/").toURI());
	    	files = mainVar.GetFileNames(file.list());
		}
		catch (Exception ex)
		{
			Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
		}
		
		Audio audio = new Audio(files);
		
		
        tracker.Setup();
        tracker.Connect();
		mainVar.format = new DecimalFormat("#.##");
		mainVar.minfps = 10;
		counter.start();
		
		hook.attachShutDownHook();
		long lastKnownTime = System.nanoTime();
        int fIndex = 0;
        int index = 1;
        boolean first = true;
		int z = 0;
		Point[] IdleMove = new Point[4];
		IdleMove[0] = new Point(-55,  38); //p1
		IdleMove[1] = new Point(-55, -38); //p2
		IdleMove[2] = new Point( 55,  38); //p3
		IdleMove[3] = new Point(-55,  38); //p4
		
		while(true)
		{
			if (tracker.Track())
			{
				lastKnownTime = System.nanoTime();
				servController.Update(tracker.GetLoc());
			}
			else
			{
				if (lastKnownTime + 2500000000L <= System.nanoTime())
				{
					servController.Update(IdleMove[index]);
					fIndex++;
					if (fIndex == 150)
					{
						index++;
						fIndex = 0;
					}
					if (index == 4)
					{
						index = 0;
					}
				}
			}
			/*
			if (!first)
			{
				System.out.println("Getting error code");
				int i = tracker.SendFeed();
				switch(i)
				{
					case -1:
						System.out.println("Failure");
						break;
					case 0:
						break;
					default:
						System.out.println("Bytes sent: " + i);
				}
			}
			else first = false;
			*/
			counter.interrupt();
			mainVar.fps = counter.GetFPS();
			if (mainVar.nextTime <= System.nanoTime())
			{
				mainVar.minfps = mainVar.maxfps;
				mainVar.maxfps = 0.0;
				mainVar.nextTime = System.nanoTime() + 2500000000L;
				audio.SetClip(z);
				audio.PLayClip();
				z++;
				if (z > 22)
				{
					z = 0;
				}
			}
			if (mainVar.fps > mainVar.maxfps)
			{
				mainVar.maxfps = mainVar.fps;
			}
			else if (mainVar.fps < mainVar.minfps)
			{
				mainVar.minfps = mainVar.fps;
			}
			mainVar.screen.setTitle(mainVar.format.format(mainVar.fps)+" max " + mainVar.format.format(mainVar.maxfps) + " min " + mainVar.format.format(mainVar.minfps) + " PWM1 " + servController.GetLocation(2) + " PWM2 " + servController.GetLocation(3) + " PWM3 " + servController.GetLocation(0) + " PWM4 " + servController.GetLocation(1));
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
	
	private String[] GetFileNames(String[] files) throws InterruptedException, ExecutionException
    {
    	String[] res = new String[files.length];
    	ExecutorService executor = Executors.newFixedThreadPool(3);
	    List<Future<String>> futurelist = new ArrayList<Future<String>>();
	    for (String file : files)
	    {
		    futurelist.add(executor.submit(new SomeCallableTask(file)));
		}
		int i = 0;
		for (Future<String> future : futurelist)
		{
		   	res[i] = future.get();
		   	i++;
		}
		executor.shutdown();
		return res;
    }

}
