import java.util.List;
import java.util.ArrayList;
import java.util.Vector;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

import org.opencv.highgui.*;
import org.opencv.imgproc.Imgproc;
import org.opencv.imgproc.Moments;
import org.opencv.core.*;


public class Main
{
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
	
	static Screen screen1 = new Screen();
	static Screen screen2 = new Screen();
	static Screen screen3 = new Screen();
	
	public static void drawObject(double x,double y,Mat frame)
	{
		Core.circle(frame,new Point(x,y),20,new Scalar(0.0,255.0,0.0),2);
		if(y-25>0)
		    Core.line(frame,new Point(x,y),new Point(x,y-25),new Scalar(0,255,0),2);
	    else Core.line(frame,new Point(x,y),new Point(x,0),new Scalar(0,255,0),2);
	    if(y+25<frame_height)
		    Core.line(frame,new Point(x,y),new Point(x,y+25),new Scalar(0,255,0),2);
	    else Core.line(frame,new Point(x,y),new Point(x,frame_height),new Scalar(0,255,0),2);
	    if(x-25>0)
		    Core.line(frame,new Point(x,y),new Point(x-25,y),new Scalar(0,255,0),2);
	    else Core.line(frame,new Point(x,y),new Point(0,y),new Scalar(0,255,0),2);
	    if(x+25<frame_width)
		    Core.line(frame,new Point(x,y),new Point(x+25,y),new Scalar(0,255,0),2);
	    else Core.line(frame,new Point(x,y),new Point(frame_width,y),new Scalar(0,255,0),2);

	    Core.putText(frame,Double.toString(x)+","+Double.toString(y),new Point(x,y+30),1,1,new Scalar(0,255,0),2);
	}
	
	

//	private static Scalar Scalar(double d, double e, double f) {
//		Scalar scala = new Scalar(d,e,f);
//		return scala;
//	}
//
//
//	private static Point Point(double myX, double myY) {
//		Point punt = new Point(myX,myY);
//		return punt;
//	}

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
					Core.putText(cameraFeed,"Tracking Object",new Point(0,50),2,1,new Scalar(0,255,0),2);
					//draw object location on screen
					drawObject(x,y,cameraFeed);
					System.out.println("coor: "+x+ "," +y);
					return true;
				}
				else
				{
					Core.putText(cameraFeed,"TOO MUCH NOISE! ADJUST FILTER",new Point(0,50),1,2,new Scalar(0,0,255),2);
					System.out.println("Geen object gevonden");
					return false;
				}
			//}
		}
		System.out.println("Geen hierarchy");
		return false;
	}
	
	public static void main( String[] args )
	{
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
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
		
        @SuppressWarnings("unused")
		BufferedImage image = null;
        Main p = new Main();
        @SuppressWarnings("unused")
		Mat mat = new Mat();
        
		try {
			image = ImageIO.read(p.getClass().getResource("/images/Konachan.com - 199548 atha braids brown_eyes brown_hair hat long_hair original ponytail.png"));;
		}
		catch (IOException e1)
		{
			e1.printStackTrace();
		}

		while(true)
		{
			capture.read(cameraFeed);
			Imgproc.cvtColor(cameraFeed,HSV, Imgproc.COLOR_BGR2HSV);
			Core.inRange(HSV,new Scalar(H_min,S_min,V_min),new Scalar(H_max,S_max,V_max),threshold);
			
			if(useMorph)
				morphOps(threshold);
			
			if(trackObjects)
				trackFilteredObject(x,y,threshold,cameraFeed);
			
			//Zou afbeelding in venster moeten laten zien
			try
			{
				// deze laat een wit scherm zien
				MatOfByte matOfByte = new MatOfByte();
				Highgui.imencode(".jpg", threshold, matOfByte);
				byte[] byteArray = matOfByte.toArray();
				BufferedImage bufImage = null;
				InputStream in = new ByteArrayInputStream(byteArray);
				bufImage = ImageIO.read(in);
				screen1.SetImage(bufImage);
				// deze laat ongealterd plaatje zien
				Highgui.imencode(".jpg", cameraFeed, matOfByte);
				byteArray = matOfByte.toArray();
				in = new ByteArrayInputStream(byteArray);
				bufImage = ImageIO.read(in);
				screen2.SetImage(bufImage);
				// deze laat een plaatje met andere kleuren zien
				Highgui.imencode(".jpg", HSV, matOfByte);
				byteArray = matOfByte.toArray();
				in = new ByteArrayInputStream(byteArray);
				bufImage = ImageIO.read(in);
				screen3.SetImage(bufImage);
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
			try
			{
				Thread.sleep(20);
			}
			catch (InterruptedException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
}
