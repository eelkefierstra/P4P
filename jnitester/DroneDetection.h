/*
 * DroneDetection.h
 *
 *  Created on: 30 mei 2015
 *      Author: Dudecake
 */

#ifndef DRONEDETECTION_H_
#define DRONEDETECTION_H_
#include <highgui.h>
#include <cv.h>

class DroneDetection {
public:
	DroneDetection();
	virtual ~DroneDetection();
	int loop();
	std::string intToString(int number);
	void drawObject(int x, int y,cv::Mat &frame);
	void morphOps(cv::Mat &thresh);
	int trackFilteredObject(int &x, int &y, cv::Mat threshold, cv::Mat &cameraFeed);
};

#endif /* DRONEDETECTION_H_ */
