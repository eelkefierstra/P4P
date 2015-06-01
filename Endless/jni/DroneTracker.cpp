#include <jni.h>
#include <sstream>
#include <string>
#include <iostream>
#include <opencv\highgui.h>
#include <opencv\cv.h>
#include "DroneTracker.h"

using namespace cv;
//initial min and max HSV filter values.
//these will be changed using trackbars
int H_MIN = 0;
int H_MAX = 256;
int S_MIN = 0;
int S_MAX = 256;
int V_MIN = 0;
int V_MAX = 256;

int H_MIN2 = 77;
int H_MAX2 = 256;
int S_MIN2 = 44;
int S_MAX2 = 142;
int V_MIN2 = 200;
int V_MAX2 = 256;

//default capture width and height
const int FRAME_WIDTH = 640;
const int FRAME_HEIGHT = 480;
//max number of objects to be detected in frame
const int MAX_NUM_OBJECTS=1500;
//minimum and maximum object area
const int MIN_OBJECT_AREA = 10*10;
const int MAX_OBJECT_AREA = FRAME_HEIGHT*FRAME_WIDTH/1.5;
//Matrix to store each frame of the webcam feed
Mat cameraFeed;
//matrix storage for HSV image
Mat HSV;
//matrix storage for binary threshold image
Mat thresh;
//x and y values for the location of the object
int x=0, y=0;
//video capture object to acquire webcam feed
VideoCapture capture;

bool first = true;

JNIEXPORT void JNICALL Java_DroneTracker_Setup(JNIEnv *, jobject)
{
	//open capture object at location zero (default location for webcam)
	capture.open(0);
	//set height and width of capture frame
	capture.set(CV_CAP_PROP_FRAME_WIDTH,FRAME_WIDTH);
	capture.set(CV_CAP_PROP_FRAME_HEIGHT,FRAME_HEIGHT);
}

std::string intToString(int number){
	std::stringstream ss;
	ss << number;
	return ss.str();
}

void drawObject(int x, int y,Mat &frame){

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

void morphOps(Mat &thresh1){

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

void trackFilteredObject(int &x, int &y, Mat threshold, Mat &cameraFeed){

	Mat temp;
	threshold.copyTo(temp);
	//these two vectors needed for output of findContours
	vector< vector<Point> > contours;
	vector<Vec4i> hierarchy;
	//find contours of filtered image using openCV findContours function
	findContours(temp,contours,hierarchy,CV_RETR_CCOMP,CV_CHAIN_APPROX_SIMPLE );
	//use moments method to find our filtered object
	double refArea = 0;
	bool objectFound = false;
	if (hierarchy.size() > 0) {
		int numObjects = hierarchy.size();
        //if number of objects greater than MAX_NUM_OBJECTS we have a noisy filter
        if(numObjects<MAX_NUM_OBJECTS){
			for (int index = 0; index >= 0; index = hierarchy[index][0]) {

				Moments moment = moments((Mat)contours[index]);
				double area = moment.m00;

				//if the area is less than 20 px by 20px then it is probably just noise
				//if the area is the same as the 3/2 of the image size, probably just a bad filter
				//we only want the object with the largest area so we safe a reference area each
				//iteration and compare it to the area in the next iteration.
                if(area>MIN_OBJECT_AREA && area<MAX_OBJECT_AREA && area>refArea){
					x = moment.m10/area;
					y = moment.m01/area;
					objectFound = true;
					refArea = area;
				}else objectFound = false;
			}
			//let user know you found an object
			if(objectFound ==true){
				putText(cameraFeed,"Tracking Object",Point(0,50),2,1,Scalar(0,255,0),2);
				//draw object location on screen
				drawObject(x,y,cameraFeed);}

		}else putText(cameraFeed,"TOO MUCH NOISE! ADJUST FILTER",Point(0,50),1,2,Scalar(0,0,255),2);
	}
}

vector<uchar> ConvertMat(Mat &img)
{
	vector<uchar> buff;//buffer for coding
	vector<int> param = vector<int>(2);
	param[0] = IMWRITE_PNG_COMPRESSION;
	param[1] = 3;
	imencode(".jpeg", img, buff, param);
	return buff;
}

JNIEXPORT jbyteArray JNICALL Java_DroneTracker_GetFeed(JNIEnv *env, jobject)
{
	vector<uchar> tempvec = ConvertMat(cameraFeed);
	jbyte* temp = new jbyte[tempvec.size()];
	jbyteArray res = env->NewByteArray(tempvec.size());
	for (int i = 0; i < tempvec.size(); i++)
	{
		temp[i] = (jbyte)tempvec[i];
	}
	env->SetByteArrayRegion(res, 0, tempvec.size(), temp);
	return res;
}

JNIEXPORT jbyteArray JNICALL Java_DroneTracker_GetThresh(JNIEnv *env, jobject)
{
	vector<uchar> tempvec = ConvertMat(thresh);
	jbyte* temp = new jbyte[tempvec.size()];
	jbyteArray res = env->NewByteArray(tempvec.size());
	for (int i = 0; i < tempvec.size(); i++)
	{
		temp[i] = (jbyte)tempvec[i];
	}
	env->SetByteArrayRegion(res, 0, tempvec.size(), temp);
	return res;
}

JNIEXPORT jbyteArray JNICALL Java_DroneTracker_GetHSV(JNIEnv *env, jobject)
{
	vector<uchar> tempvec = ConvertMat(HSV);
	jbyte* temp = new jbyte[tempvec.size()];
	jbyteArray res = env->NewByteArray(tempvec.size());
	for (int i = 0; i < tempvec.size(); i++)
	{
		temp[i] = (jbyte)tempvec[i];
	}
	env->SetByteArrayRegion(res, 0, tempvec.size(), temp);
	return res;
}

JNIEXPORT void JNICALL Java_DroneTracker_Track(JNIEnv *env, jobject)
{
	//store image to matrix
	capture.read(cameraFeed);
	if (first)
	{
		first = false;
		return ;
	}
	//convert frame from BGR to HSV colorspace
	cvtColor(cameraFeed,HSV,COLOR_BGR2HSV);

	inRange(HSV, Scalar(H_MIN, S_MIN, V_MIN), Scalar(H_MAX, S_MAX, V_MAX), thresh);
	//imshow("pre morph", thresh);
	morphOps(thresh);
	trackFilteredObject(x, y, thresh, cameraFeed);
	//imshow("Threshold Blue", thresh);

	//show frames
	/*
	imshow(windowName,cameraFeed);
	imshow(windowName1,HSV);
	*/
	//delay 30ms so that screen can refresh.
	//image will not appear without this waitKey() command
	return ;
}

/*
~DroneTracker()
{
	// TODO Auto-generated destructor stub
}
*/
