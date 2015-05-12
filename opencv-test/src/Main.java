//import org.opencv.*;
//import org.opencv.core.*;
//import org.opencv.core.Point;
import java.util.Vector;

import org.opencv.highgui.*;
import org.opencv.imgproc.Imgproc;
import org.opencv.imgproc.Moments;
import org.opencv.core.Core;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.core.Mat;
import org.opencv.core.Size;

public class Main
{
	static int H_min = 0;
	static int H_max = 256;
	static int S_min = 0;
	static int S_max = 256;
	static int V_min = 0;
	static int V_max = 256;
	
	static final int frame_width = 640;
	static final int frame_height = 480;
	
	static int maxNumObjects = 50;
	static int minObjectArea = 15*15;
	static int maxObjectArea = (int)((double)frame_width*(double)frame_height/1.5);
	
	
	static final String windowName = "Original";
	static final String windowName1 = "HSV";
	static final String windowName2 = "Threshold";
	static final String windowName3 = "After Morph";
	static final String trackbarWindowName = "Trackbars";
	
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
	
	public static void trackFilteredObject(int x, int y, Mat threshold, Mat cameraFeed)
	{
		Mat temp;
		threshold.copyTo(temp);
		//these two vectors needed for output of findContours
		Vector<Point> contours = new Vector<Point>();
		Vector<Integer[][]> hierarchy = new Vector<Integer[][]>(4);
		//find contours of filtered image using openCV findContours function
		Imgproc.findContours(temp,contours,hierarchy, Imgproc.RETR_CCOMP, Imgproc.CHAIN_APPROX_SIMPLE );
		//use moments method to find our filtered object
		double refArea = 0;
		boolean objectFound = false;
		if (hierarchy.size() > 0) {
			int numObjects = hierarchy.size();
	        //if number of objects greater than MAX_NUM_OBJECTS we have a noisy filter
	        if(numObjects<maxNumObjects){
				for (int index = 0; index >= 0; index = hierarchy.toArray(new int[4][4])[index][0]) {

					Moments moment = new Moments((Mat)contours.toArray(new int[4][4])[index]);
					double area = moment.get_m00();

					//if the area is less than 20 px by 20px then it is probably just noise
					//if the area is the same as the 3/2 of the image size, probably just a bad filter
					//we only want the object with the largest area so we safe a reference area each
					//iteration and compare it to the area in the next iteration.
	                if(area>minObjectArea && area<maxObjectArea && area>refArea){
						x = (int) (moment.get_m10()/area);
						y = (int) (moment.get_m01()/area);
						objectFound = true;
						refArea = area;
					}else objectFound = false;


				}
				//let user know you found an object
				if(objectFound ==true){
					Core.putText(cameraFeed,"Tracking Object",new Point(0,50),2,1,new Scalar(0,255,0),2);
					//draw object location on screen
					drawObject(x,y,cameraFeed);}

			}else Core.putText(cameraFeed,"TOO MUCH NOISE! ADJUST FILTER",new Point(0,50),1,2,new Scalar(0,0,255),2);
		}
	}
	
	public static void main( String[] args )
	{
		boolean trackObjects = false;
		boolean useMorph = false;
		
		Mat cameraFeed;
		Mat HSV;
		Mat threshold;
		int x=0,y=0;
		
		VideoCapture capture;
		capture.open(0);
		capture.set(Highgui.CV_CAP_PROP_FRAME_WIDTH,frame_width);
		capture.set(Highgui.CV_CAP_PROP_FRAME_HEIGHT,frame_height);
		
		while(true)
		{
			capture.read(cameraFeed);
			Imgproc.cvtColor(cameraFeed,HSV,Imgproc.COLOR_BGR2HSV);
			Core.inRange(HSV,new Scalar(H_min,S_min,V_min),new Scalar(H_max,S_max,V_max),threshold);
			
			if(useMorph)
			morphOps(threshold);
			
			if(trackObjects)
				trackFilteredObject(x,y,threshold,cameraFeed);
			//Zou afbeelding in venster moeten laten zien
			Highgui.imshow(windowName2,threshold);
			Highgui.imshow(windowName,cameraFeed);
			Highgui.imshow(windowName1,HSV);
			
			wait(30);
		}
		
	}
}
