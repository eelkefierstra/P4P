#include <jni.h>
#include <vector>
#include <sstream>
#include <string>
#include <stdio.h>
#include <sys/types.h>
#include <sys/socket.h>
#include <netinet/in.h>
#include <arpa/inet.h>
#include <iostream>
#include <opencv/highgui.h>
#include <opencv/cv.h>
#include "RaspiCamCV.h"
#include "DroneTracker.h"

using namespace cv;
//initial min and max HSV filter values.
int H_MIN = 108; //red leds
int H_MAX = 143; // Numbers found through trial and error(more error than trial :P)
int S_MIN = 64;
int S_MAX = 200;
int V_MIN = 108;
int V_MAX = 224;

int H_MIN2 = 77; //Dit zijn denk ik de blauwe leds
int H_MAX2 = 256; //Ben niet zeker
int S_MIN2 = 44;
int S_MAX2 = 142;
int V_MIN2 = 200;
int V_MAX2 = 256;

//default capture width and height
const int FRAME_WIDTH = 854;
const int FRAME_HEIGHT = 480;
//max number of objects to be detected in frame
const int MAX_NUM_OBJECTS=1500;
//minimum and maximum object area
const int MIN_OBJECT_AREA = 10*10;
const int MAX_OBJECT_AREA = FRAME_HEIGHT*FRAME_WIDTH/10;

//Matrix to store each frame of the webcam feed
Mat cameraFeed;
//matrix storage for HSV image
Mat HSV;
//matrix storage for binary threshold image
Mat thresh;
//x and y values for the location of the object
Mat betweenMat;
int x=0, y=0;
//video capture object to acquire webcam feed
//VideoCapture capture;
RaspiCamCvCapture * camera;

vector<int> xList; // list with x coordinates of found objects
vector<int> yList; // list with y coordinates of found objects

vector<int> param = vector<int>(2);

bool first = true;

// Used for initial setup
JNIEXPORT void JNICALL Java_DroneTracker_Setup(JNIEnv *, jobject)
{
	//open capture object at location zero (default location for webcam)
	//capture.open(0);
	//capture.set(CV_CAP_PROP_FRAME_WIDTH,FRAME_WIDTH);
	//capture.set(CV_CAP_PROP_FRAME_HEIGHT,FRAME_HEIGHT);
	camera = raspiCamCvCreateCameraCapture(0);

	param[0] = IMWRITE_PXM_BINARY;
	param[1] = 0;
}

// Convert integers to strings
std::string intToString(int number)
{
	std::stringstream ss;
	ss << number;
	return ss.str();
}

// Used in debugging to draw circels on the screen at the x,y
void drawObject(int x, int y,Mat &frame)
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

// Use this to suppress noise in the threshold and enlarge the remaining objects
// We adjusted this to only enlarge objects, because the leds we follow are only small dots on the screen
void morphOps(Mat &thresh1)
{

	//create structuring element that will be used to "dilate" and "erode" image.
	//the element chosen here is a 3px by 3px rectangle

	//Mat erodeElement = getStructuringElement( MORPH_RECT,Size(3,3));
    //dilate with larger element so make sure object is nicely visible
	Mat dilateElement = getStructuringElement( MORPH_RECT,Size(6,6));

	//erode(thresh1,thresh1,erodeElement);
	//erode(thresh1,thresh1,erodeElement);


	dilate(thresh1,thresh1,dilateElement);
	//dilate(thresh1,thresh1,dilateElement);
}

// Used to find objects in the threshold
int trackFilteredObject(int &x, int &y, Mat threshold, Mat &cameraFeed)
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

				//if the area is less than 10 px by 10px then it is probably just noise					//We adjusted these parameters to our own needs
				//if the area is the same as the 3/2 of the image size, probably just a bad filter		
                if(area>MIN_OBJECT_AREA && area<MAX_OBJECT_AREA)
				{
					x = moment.m10/area;
					y = moment.m01/area;
					objectFound = true;
					xList.push_back(x);
					yList.push_back(y);
					//drawObject(x, y, cameraFeed); // Use this to get a graphical feedback on what objects are found
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
//Just a bunch of unused code, since we don't need the screens anymore

vector<uchar> ConvertMat(Mat &img)
{
	vector<uchar> buff;//buffer for coding
	imencode(".bmp", img, buff, param);
	return buff;
}

JNIEXPORT jbyteArray JNICALL Java_DroneTracker_GetFeed(JNIEnv *env, jobject)
{
	vector<uchar> tempvec = ConvertMat(betweenMat);
	jbyte* temp = new jbyte[tempvec.size()];
	jbyteArray res = env->NewByteArray(tempvec.size());
	for (int i = 0; i < tempvec.size(); i++)
	{
		temp[i] = (jbyte)tempvec[i];
	}
	env->SetByteArrayRegion(res, 0, tempvec.size(), temp);
	delete temp, tempvec;
	//temp = nullptr, tempvec = nullptr;
	return res;
}


// Coen, What happened here to naming stuff??
jint dinges;

JNIEXPORT jint JNICALL Java_DroneTracker_SendFeed(JNIEnv *, jobject)
{
	return dinges;
}

//Sends image over the network
int sendImage(Mat frame)
{
	int  imgSize = frame.total()*frame.elemSize();
	int  bytes = 0;
	int clientSock;
	char temp[] = { 192, 168, 1, 36 };
	const char* server_ip = temp;
	delete temp;
	int server_port = 2000;
	struct sockaddr_in serverAddr;
	socklen_t serverAddrLen = sizeof(serverAddr);
	dinges = -1;
    if ((clientSock = socket(PF_INET, SOCK_STREAM, 0)) < 0)
    {
        //printf("\n--> socket() failed.");
        return -1;
    }

    serverAddr.sin_family = PF_INET;
    serverAddr.sin_addr.s_addr = inet_addr(server_ip);
    serverAddr.sin_port = htons(server_port);

    if (connect(clientSock, (sockaddr*)&serverAddr, serverAddrLen) < 0)
    {
    	//printf("\n--> connect() failed.");
    	return -1;
    }

    frame = (frame.reshape(0,1)); // to make it continuous

    /* start sending images */
    if ((bytes = send(clientSock, frame.data, imgSize, 0)) < 0)
    {
        //printf("\n--> send() failed");
        return -1;
     }

    /* if something went wrong, restart the connection */
	if (bytes != imgSize)
    {
    	std::cout << "\n-->  Connection closed " << std::endl;
    	close(clientSock);
    	return -1;
    }
dinges = 0;
return 0;

}

// Gets average x coordinate, this is done, to make the point that is returned more accurate
JNIEXPORT jint JNICALL Java_DroneTracker_GetX(JNIEnv *env, jobject)
{
	jint sum = 0;
	jint aantal = xList.size();
	// Simple loop to calculate an average
	while(xList.size()>0)
	{
		sum += xList.back();
		xList.pop_back();
	}
	jint res = sum/aantal;
	return res;
}

// Gets the average y coordinate, just like with the x
JNIEXPORT jint JNICALL Java_DroneTracker_GetY(JNIEnv *env, jobject)
{
	jint sum = 0;
	jint aantal = yList.size();
	while(yList.size()>0)
	{
		sum += yList.back();
		yList.pop_back();
	}
	jint res = sum/aantal;
	return res;
}

// The heart of the tracking, this is the method with our detection 'algorithm'
JNIEXPORT jboolean JNICALL Java_DroneTracker_Track(JNIEnv *env, jobject)
{
	jboolean tracker = true;
	//store image to matrix
	//capture.read(cameraFeed);
	IplImage * temp = raspiCamCvQueryFrame(camera);
	cameraFeed = cvarrToMat(temp);
	//delete temp;

	if (first)
	{
		first = false, tracker = false;
		return tracker;
	}
	//convert frame from BGR to HSV colorspace
	cvtColor(cameraFeed,HSV,COLOR_BGR2HSV);
	
	for(int i = 0;i<2;i++)
	{
		if(i==0)
		{
			inRange(HSV, Scalar(H_MIN2, S_MIN2, V_MIN2), Scalar(H_MAX2, S_MAX2, V_MAX2), thresh);
		}
		else inRange(HSV, Scalar(H_MIN, S_MIN, V_MIN), Scalar(H_MAX, S_MAX, V_MAX), thresh);
		int morphs = 0;
		bool objectsFound = false;
		bool cont = true;

		while (cont)
		{
			switch (trackFilteredObject(x, y, thresh, cameraFeed))
			{
				case 0:
					objectsFound = true;
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
					cont = false, tracker = false;
					break;
			}
			return 0;
		}
	}
	
	sendImage(cameraFeed);
	return tracker;
}

// Some automatic generated junk
/*
~DroneTracker()
{
	// TODO Auto-generated destructor stub
}
*/
