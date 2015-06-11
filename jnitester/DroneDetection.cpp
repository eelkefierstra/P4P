/*
 * DroneDetection.cpp
 *
 *  Created on: 30 mei 2015
 *      Author: Dudecake and Siroci
 */

#include <sstream>
#include <string>
#include <iostream>
#include <highgui.h>
#include <cv.h>
#include "RaspiCamCV.h"
#include "DroneDetection.h"

using namespace cv;
//initial min and max HSV filter values.
//these will be changed using trackbars
int H_MIN = 0;
int H_MAX = 256;
int S_MIN = 0;
int S_MAX = 256;
int V_MIN = 0;
int V_MAX = 256;

//default capture width and height
const int FRAME_WIDTH = 640;
const int FRAME_HEIGHT = 480;
//max number of objects to be detected in frame
const int MAX_NUM_OBJECTS=150;
//minimum and maximum object area
const int MIN_OBJECT_AREA = 10*10;
const int MAX_OBJECT_AREA = FRAME_HEIGHT*FRAME_WIDTH/10;
//names that will appear at the top of each window
const std::string windowName = "Original Image";
const std::string windowName1 = "HSV Image";
const std::string windowName2 = "Thresholded Image";
const std::string windowName3 = "After Morphological Operations";
const std::string trackbarWindowName = "Trackbars";

//some boolean variables for different functionality within this
//program
//bool trackObjects = true;
//bool useMorphOps = true;
//Matrix to store each frame of the webcam feed
Mat cameraFeed;
//matrix storage for HSV image
Mat HSV;
//matrix storage for binary threshold image
Mat thresh;
//x and y values for the location of the object
int x=0, y=0;
//create slider bars for HSV filtering
//createTrackbars();
//video capture object to acquire webcam feed
//VideoCapture capture;
RaspiCamCvCapture * camera;

bool first = true;

DroneDetection::DroneDetection()
{
	//open capture object at location zero (default location for webcam)
	//capture.open(0);
	//set height and width of capture frame
	//capture.set(CV_CAP_PROP_FRAME_WIDTH,FRAME_WIDTH);
	//capture.set(CV_CAP_PROP_FRAME_HEIGHT,FRAME_HEIGHT);

	//create slider bars for HSV filtering
	createTrackbars();

	camera = raspiCamCvCreateCameraCapture(0);
}


void on_trackbar( int, void* )
{//This function gets called whenever a
	// trackbar position is changed
}

void DroneDetection::createTrackbars()
{
	//create window for trackbars


    namedWindow(trackbarWindowName,0);
	//create memory to store trackbar name on window
	char TrackbarName[50];
	sprintf( TrackbarName, "H_MIN", H_MIN);
	sprintf( TrackbarName, "H_MAX", H_MAX);
	sprintf( TrackbarName, "S_MIN", S_MIN);
	sprintf( TrackbarName, "S_MAX", S_MAX);
	sprintf( TrackbarName, "V_MIN", V_MIN);
	sprintf( TrackbarName, "V_MAX", V_MAX);
	//create trackbars and insert them into window
	//3 parameters are: the address of the variable that is changing when the trackbar is moved(eg.H_LOW),
	//the max value the trackbar can move (eg. H_HIGH),
	//and the function that is called whenever the trackbar is moved(eg. on_trackbar)
	//                                  ---->    ---->     ---->
    createTrackbar( "H_MIN", trackbarWindowName, &H_MIN, H_MAX, on_trackbar );
    createTrackbar( "H_MAX", trackbarWindowName, &H_MAX, H_MAX, on_trackbar );
    createTrackbar( "S_MIN", trackbarWindowName, &S_MIN, S_MAX, on_trackbar );
    createTrackbar( "S_MAX", trackbarWindowName, &S_MAX, S_MAX, on_trackbar );
    createTrackbar( "V_MIN", trackbarWindowName, &V_MIN, V_MAX, on_trackbar );
    createTrackbar( "V_MAX", trackbarWindowName, &V_MAX, V_MAX, on_trackbar );


}

std::string DroneDetection::intToString(int number)
{
	std::stringstream ss;
	ss << number;
	return ss.str();
}

void DroneDetection::drawObject(int x, int y,Mat &frame)
{

	//use some of the openCV drawing functions to draw crosshairs
	//on your tracked image!

    //UPDATE:JUNE 18TH, 2013
    //added 'if' and 'else' statements to prevent
    //memory errors from writing off the screen (ie. (-25,-25) is not within the window!)

	circle(frame,Point(x,y),20,Scalar(0,255,0),2);
    if(y-25>0)
    line(frame,Point(x,y),Point(x,y-25),Scalar(0,255,0),2);
    else line(frame,Point(x,y),Point(x,0),Scalar(0,255,0),2);
    if(y+25<FRAME_HEIGHT)
    line(frame,Point(x,y),Point(x,y+25),Scalar(0,255,0),2);
    else line(frame,Point(x,y),Point(x,FRAME_HEIGHT),Scalar(0,255,0),2);
    if(x-25>0)
    line(frame,Point(x,y),Point(x-25,y),Scalar(0,255,0),2);
    else line(frame,Point(x,y),Point(0,y),Scalar(0,255,0),2);
    if(x+25<FRAME_WIDTH)
    line(frame,Point(x,y),Point(x+25,y),Scalar(0,255,0),2);
    else line(frame,Point(x,y),Point(FRAME_WIDTH,y),Scalar(0,255,0),2);

	putText(frame,intToString(x)+","+intToString(y),Point(x,y+30),1,1,Scalar(0,255,0),2);
}

void DroneDetection::morphOps(Mat &thresh1)
{

	//create structuring element that will be used to "dilate" and "erode" image.
	//the element chosen here is a 3px by 3px rectangle

	Mat erodeElement = getStructuringElement( MORPH_RECT,Size(3,3));
    //dilate with larger element so make sure object is nicely visible
	Mat dilateElement = getStructuringElement( MORPH_RECT,Size(6,6));

	erode(thresh1,thresh1,erodeElement);
	erode(thresh1,thresh1,erodeElement);


	dilate(thresh1,thresh1,dilateElement);
	dilate(thresh1,thresh1,dilateElement);
}

int DroneDetection::trackFilteredObject(int &x, int &y, Mat threshold, Mat &cameraFeed)
{
	Mat temp;
	threshold.copyTo(temp);
	//these two vectors needed for output of findContours
	vector< vector<Point> > contours;
	vector<Vec4i> hierarchy;
	//find contours of filtered image using openCV findContours function
	findContours(temp,contours,hierarchy,CV_RETR_CCOMP,CV_CHAIN_APPROX_SIMPLE );
	//use moments method to find our filtered object
	bool objectFound = false;
	if (hierarchy.size() > 0)
	{
		int numObjects = hierarchy.size();
		//if number of objects greater than MAX_NUM_OBJECTS we have a noisy filter
		if(numObjects<MAX_NUM_OBJECTS)
		{
			for (int index = 0; index >= 0; index = hierarchy[index][0])
			{

				Moments moment = moments((Mat)contours[index]);
				double area = moment.m00;

				//if the area is less than 20 px by 20px then it is probably just noise
				//if the area is the same as the 3/2 of the image size, probably just a bad filter
				//we only want the object with the largest area so we safe a reference area each
				//iteration and compare it to the area in the next iteration.
				if(area>MIN_OBJECT_AREA && area<MAX_OBJECT_AREA)
				{
					x = moment.m10/area;
					y = moment.m01/area;
					objectFound = true;
					drawObject(x, y, cameraFeed);
				}
				else objectFound = false;
			}
			//let user know you found an object
			if(objectFound ==true)
			{
				return 0;
			}
			else return 1;
		}
		else return 2;
	}
	return 0;
}

int DroneDetection::loop()
{
	//store image to matrix
	//capture.read(cameraFeed);
	IplImage * temp = raspiCamCvQueryFrame(camera);
	cameraFeed = cvarrToMat(temp);

	if (first)
	{
		first = false;
		return 0;
	}
	//convert frame from BGR to HSV colorspace
	cvtColor(cameraFeed,HSV,COLOR_BGR2HSV);

	inRange(HSV, Scalar(H_MIN, S_MIN, V_MIN), Scalar(H_MAX, S_MAX, V_MAX), thresh);
	imshow("pre morph", thresh);
	int objects = 0, morphs = 0;
	bool objectsFound = false;
	bool cont = true;

		while (cont)
		{
			switch (trackFilteredObject(x, y, thresh, cameraFeed))
			{
				case 0:
					objectsFound = true;
					objects++;
					cont = false;
					break;
				case 1:
					if (morphs<3)
					{
						morphOps(thresh);
						morphs++;
					}
					else
					{
						cont = false;
					}
					break;
				case 2:
					cont = false;
					break;
			}

		}
	//imshow("Threshold Blue", thresh);
	imshow(windowName,cameraFeed);
	imshow(windowName1,HSV);
	return 0;
}


DroneDetection::~DroneDetection()
{
	//camera->raspiCamCvReleaseCapture();
	delete camera;
}

