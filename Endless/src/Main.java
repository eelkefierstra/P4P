/*
import java.awt.MouseInfo;
import java.awt.Point;
import java.text.DecimalFormat;
*/
import java.awt.Point;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

//import org.opencv.core.Core;

@SuppressWarnings("unused")
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
    	//System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        Main p = new Main();
        try
        {
        	String path = p.getClass().getResource("/lib/").toURI().toString();
        	path = path.substring(6);
        	//path = path.replace('%', ' ');
        	//path = path.substring(0, 21) + path.substring(23);
			System.load(path + "dronetracker.dll");
        }
		catch (Exception ex)
		{
			Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
		}
		String[] files = null;
		try
		{
			java.io.File file = new java.io.File(p.getClass().getResource("/audio/").toURI());
	    	files = p.GetFileNames(file.list());
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
	
	private native void Loop();
	
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
