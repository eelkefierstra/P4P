/*
import java.awt.MouseInfo;
import java.awt.Point;
import java.text.DecimalFormat;
*/
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

//import org.opencv.core.Core;

public class Main
{
	//private GUI gui = new GUI();
	
	static
	{
    	System.loadLibrary("dronedetection");
	}
	
	// Main loop
    //@SuppressWarnings("unused")
	public static void main(String[] args)
    {
    	//System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        Main p = new Main();
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
			    
        DroneDetection detection = new DroneDetection(new Audio(files));
        
		while(true)
		{
			detection.Loop();
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
