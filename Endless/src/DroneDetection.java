import java.awt.MouseInfo;
import java.awt.Point;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.opencv.core.*;
import org.opencv.highgui.Highgui;
import org.opencv.highgui.VideoCapture;
import org.opencv.imgproc.Imgproc;
import org.opencv.imgproc.Moments;


public class DroneDetection
{
	private static int H_min = 9;
	private static int H_max = 28;
	private static int S_min = 226;
	private static int S_max = 256;
	private static int V_min = 153;
	private static int V_max = 256;
	
	private static final int frame_width = 320;
	private static final int frame_height = 240;
	
	private static int maxNumObjects = 1500;
	private static int minObjectArea = 10*10;
	private static int maxObjectArea = (int)((double)frame_width*(double)frame_height/1.5);
	
	private Screen screen1 = new Screen();
	private Screen screen2 = new Screen();
	private Screen screen3 = new Screen();
	
	private ScheduledFuture<?>[] futureList;
	//private boolean trackObjects = true;
	//private boolean useMorph = true;
	
	private Mat cameraFeed = new Mat();
	private Mat HSV = new Mat();
	private Mat threshold = new Mat();
	private int x=0,y=0;

	private VideoCapture capture = new VideoCapture();
	private FPSCounter counter = new FPSCounter();
	private Audio audio;
	
	private double maxfps;
	private double fps;
	private long nextTime;
	private ImShow show1;
	private ImShow show2;
	private ImShow show3;
	private ScheduledExecutorService executor;
	private DecimalFormat format;
	private int z = 0;

	public DroneDetection(Audio audio)
	{
		capture.open(0);
		capture.set(Highgui.CV_CAP_PROP_FRAME_WIDTH,frame_width);
		capture.set(Highgui.CV_CAP_PROP_FRAME_HEIGHT,frame_height);
		
        counter = new FPSCounter();
        counter.start();
        this.audio = audio;
        
        maxfps = 0.0;
        fps = 0.0;
        nextTime = System.nanoTime() + 1000000000;
		show1 = new ImShow(screen1, threshold);
		show2 = new ImShow(screen2, cameraFeed);
		show3 = new ImShow(screen3, HSV);
		executor = Executors.newScheduledThreadPool(3);
		futureList = new ScheduledFuture<?>[3];
		format = new DecimalFormat("#.##");
		ShutdownHook hook = new ShutdownHook();
		hook.attachShutDownHook(executor);
	   	}
	
	public void Loop()
	{
		capture.read(cameraFeed);
		Imgproc.cvtColor(cameraFeed,HSV, Imgproc.COLOR_BGR2HSV);
		Core.inRange(HSV,new Scalar(H_min,S_min,V_min),new Scalar(H_max,S_max,V_max),threshold);
		
		//if(useMorph)
			morphOps(threshold);
		
		//if(trackObjects)
			trackFilteredObject(x,y,threshold,cameraFeed);
		
		screen3.setTitle(getLocationRelativeTo().toString());
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
		screen2.setTitle(format.format(fps)+" max "+format.format(maxfps));
		try
		{
			Thread.sleep(0);
		}
		catch (InterruptedException ex)
		{
			Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	public static void drawObject(double x,double y,Mat frame)
	{
		Core.circle(frame,new org.opencv.core.Point(x,y),20,new Scalar(0.0,255.0,0.0),2);
		if(y-25>0)
		    Core.line(frame,new org.opencv.core.Point(x,y),new org.opencv.core.Point(x,y-25),new Scalar(0,255,0),2);
	    else Core.line(frame,new org.opencv.core.Point(x,y),new org.opencv.core.Point(x,0),new Scalar(0,255,0),2);
	    if(y+25<frame_height)
		    Core.line(frame,new org.opencv.core.Point(x,y),new org.opencv.core.Point(x,y+25),new Scalar(0,255,0),2);
	    else Core.line(frame,new org.opencv.core.Point(x,y),new org.opencv.core.Point(x,frame_height),new Scalar(0,255,0),2);
	    if(x-25>0)
		    Core.line(frame,new org.opencv.core.Point(x,y),new org.opencv.core.Point(x-25,y),new Scalar(0,255,0),2);
	    else Core.line(frame,new org.opencv.core.Point(x,y),new org.opencv.core.Point(0,y),new Scalar(0,255,0),2);
	    if(x+25<frame_width)
		    Core.line(frame,new org.opencv.core.Point(x,y),new org.opencv.core.Point(x+25,y),new Scalar(0,255,0),2);
	    else Core.line(frame,new org.opencv.core.Point(x,y),new org.opencv.core.Point(frame_width,y),new Scalar(0,255,0),2);

	    Core.putText(frame,Double.toString(x)+","+Double.toString(y),new org.opencv.core.Point(x,y+30),1,1,new Scalar(0,255,0),2);
	}
	
	public static void morphOps(Mat thresh)
	{
		//create structuring element that will be used to "dilate" and "erode" image.
		//the element chosen here is a 3px by 3px rectangle

		Mat erodeElement = Imgproc.getStructuringElement( Imgproc.MORPH_RECT,new Size(3,3));
	    //dilate with larger element so make sure object is nicely visible
		Mat dilateElement = Imgproc.getStructuringElement( Imgproc.MORPH_RECT,new Size(8,8));

		Imgproc.erode(thresh,thresh,erodeElement);
		Imgproc.erode(thresh,thresh,erodeElement);

		Imgproc.dilate(thresh,thresh,dilateElement);
		Imgproc.dilate(thresh,thresh,dilateElement);
	}
	
	public static Boolean trackFilteredObject(int x, int y, Mat threshold, Mat cameraFeed)
	{
		Mat temp = new Mat();
		threshold.copyTo(temp);
		//these two vectors needed for output of findContours
		//Vector<Point> contours = new Vector<Point>();
		List<MatOfPoint> contours = new ArrayList<MatOfPoint>();
		Mat hierarchy = new Mat();
		//find contours of filtered image using openCV findContours function
		Imgproc.findContours(temp,contours, hierarchy, Imgproc.RETR_CCOMP, Imgproc.CHAIN_APPROX_SIMPLE );
		//use moments method to find our filtered object
		double refArea = 0;
		boolean objectFound = false;
		if (!hierarchy.empty()) 
		{
			//int numObjects = hierarchy.size();
	        //if number of objects greater than MAX_NUM_OBJECTS we have a noisy filter
	        //if(numObjects<maxNumObjects)
	        //{
				try
				{
					for (int index = 0; index >= 0; index = (int) (hierarchy.get(index,0))[0])
					{
						//Moments moment = new Moments((Mat)contours.toArray(new int[4][4])[index]);
			        	Moments moment = Imgproc.moments( (contours.get(index)), false );
						double area = moment.get_m00();

						//if the area is less than 20 px by 20px then it is probably just noise
						//if the area is the same as the 3/2 of the image size, probably just a bad filter
						//we only want the object with the largest area so we safe a reference area each
						//iteration and compare it to the area in the next iteration.
		                if(area>minObjectArea && area<maxObjectArea && area>refArea)
		                {
							x = (int) (moment.get_m10()/area);
							y = (int) (moment.get_m01()/area);
							objectFound = true;
							refArea = area;
						}
		                else objectFound = false;
					}
				}
				catch(Exception e)
				{
					
				}
				
				//let user know you found an object
				if(objectFound ==true)
				{
					Core.putText(cameraFeed,"Tracking Object",new org.opencv.core.Point(0,50),2,1,new Scalar(0,255,0),2);
					//draw object location on screen
					drawObject(x,y,cameraFeed);
					System.out.println("coor: "+x+ "," +y);
					return true;
				}
				else
				{
					Core.putText(cameraFeed,"TOO MUCH NOISE! ADJUST FILTER",new org.opencv.core.Point(0,50),1,2,new Scalar(0,0,255),2);
					System.out.println("Geen object gevonden");
					return false;
				}
			//}
		}
		System.out.println("Geen overeenkomsten in het beeld");
		return false;
	}

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
}
