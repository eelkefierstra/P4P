/*
import java.awt.MouseInfo;
import java.awt.Point;
import java.text.DecimalFormat;
*/
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import org.opencv.core.*;

public class Main
{
	//private GUI gui = new GUI();
	/*
	static int H_min = 9;
	static int H_max = 28;
	static int S_min = 226;
	static int S_max = 256;
	static int V_min = 153;
	static int V_max = 256;
	
	static final int frame_width = 640;
	static final int frame_height = 480;
	
	static int maxNumObjects = 1500;
	static int minObjectArea = 10*10;
	static int maxObjectArea = (int)((double)frame_width*(double)frame_height/1.5);
	
	private Screen screen1 = new Screen();
	private Screen screen2 = new Screen();
	private Screen screen3 = new Screen();
	*/
	// Main loop
    @SuppressWarnings("unused")
	public static void main(String[] args)
    {
    	System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    	/*
		boolean trackObjects = true;
		boolean useMorph = true;
		
		Mat cameraFeed = new Mat();
		Mat HSV = new Mat();
		Mat threshold = new Mat();
		int x=0,y=0;
		
		VideoCapture capture = new VideoCapture();
		capture.open(0);
		capture.set(Highgui.CV_CAP_PROP_FRAME_WIDTH,frame_width);
		capture.set(Highgui.CV_CAP_PROP_FRAME_HEIGHT,frame_height);
		
		BufferedImage image = null;*/
        Main p = new Main();/*
        FPSCounter counter = new FPSCounter();
        counter.start();
		try {
			image = javax.imageio.ImageIO.read(p.getClass().getResource("/images/Konachan.com - 199548 atha braids brown_eyes brown_hair hat long_hair original ponytail.png"));;
		}
		catch (IOException e1)
		{
			e1.printStackTrace();
		}
        double maxfps = 0.0f;
        double fps = 0.0f;
        long nextTime = System.nanoTime() + 1000000000;
		ImShow show1 = new ImShow(p.screen1, threshold);
		ImShow show2 = new ImShow(p.screen2, cameraFeed);
		ImShow show3 = new ImShow(p.screen3, HSV);
		ScheduledExecutorService executor = Executors.newScheduledThreadPool(3);
		ScheduledFuture<?>[] futureList = new ScheduledFuture<?>[3];
		DecimalFormat format = new DecimalFormat("#.##");
		ShutdownHook hook = new ShutdownHook();
		hook.attachShutDownHook(executor);
		int z = 0;
	    */
        DroneDetection detection = new DroneDetection();
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
		
		ServoController servos = new ServoController();
	    Audio audio = new Audio(files);
	    audio.SetClip(0);


		while(true)
		{
			detection.Loop();
			/*
			capture.read(cameraFeed);
			Imgproc.cvtColor(cameraFeed,HSV, Imgproc.COLOR_BGR2HSV);
			Core.inRange(HSV,new Scalar(H_min,S_min,V_min),new Scalar(H_max,S_max,V_max),threshold);
			
			if(useMorph)
				morphOps(threshold);
			
			if(trackObjects)
				trackFilteredObject(x,y,threshold,cameraFeed);
			
			p.screen3.setTitle(p.getLocationRelativeTo().toString());
			//servos.Update(p.getLocationRelativeTo());
			
			//Zou afbeelding in venster moeten laten zien
			show1.SetMat(threshold);
		    futureList[0] = executor.schedule(show1, 0, TimeUnit.NANOSECONDS);
			show2.SetMat(cameraFeed);
		    futureList[1] = executor.schedule(show2, 0, TimeUnit.NANOSECONDS);
			show3.SetMat(HSV);
		    futureList[2] = executor.schedule(show3, 0, TimeUnit.NANOSECONDS);
		    
			counter.interrupt();
			fps = counter.GetFPS();
			if (nextTime <= System.nanoTime())
			{
				maxfps = 0.0;
				nextTime = System.nanoTime() + 2500000000L;
				//audio.SetClip(z);
				//audio.PLayClip();
				z++;
				if (z > 22)
				{
					x = 0;
				}
			}
			if (fps > maxfps)
			{
				maxfps = fps;
			}
			p.screen2.setTitle(format.format(fps)+" max "+format.format(maxfps));
			try
			{
				Thread.sleep(0);
			}
			catch (InterruptedException ex)
			{
				Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
			}
			*/
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
		return res;
    }
}
