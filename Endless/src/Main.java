/*
import java.awt.MouseInfo;
import java.awt.Point;
import java.text.DecimalFormat;
*/
import java.awt.Point;
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
        	java.io.File libs = new java.io.File(path);
			for (String lib : libs.list())
			{
				System.load(path + lib);
			}
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
		
		p.show1 = new ImShow(p.screen1);
		p.show2 = new ImShow(p.screen2);
		p.show3 = new ImShow(p.screen3);
		p.executor = Executors.newScheduledThreadPool(3);
		p.futureList = new ScheduledFuture<?>[3];
		p.format = new DecimalFormat("#.##");
		ShutdownHook hook = new ShutdownHook();
		hook.attachShutDownHook(p.executor);
		
        //DroneDetection detection = new DroneDetection(new Audio(files));
        DroneTracker tracker = new DroneTracker();
        tracker.Setup();
		
		while(true)
		{
			tracker.Track();
			p.show1.SetImage(tracker.GetThresh());
			p.futureList[0] = p.executor.schedule(p.show1, 0, TimeUnit.NANOSECONDS);
			p.show2.SetImage(tracker.GetFeed());
			p.futureList[1] = p.executor.schedule(p.show2, 0, TimeUnit.NANOSECONDS);
			p.show3.SetImage(tracker.GetHSV());
			p.futureList[2] = p.executor.schedule(p.show3, 0, TimeUnit.NANOSECONDS);
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
