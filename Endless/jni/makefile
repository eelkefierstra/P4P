# Define a variable for classpath
CLASS_PATH = ../bin

# Define a virtual path for .class in the bin directory
vpath %.class $(CLASS_PATH)

all : dronetracker.dll

# $@ matches the target, $< matches the first dependancy
dronetracker.dll : DroneTracker.o
	g++ -shared -o ../resources/lib/$@ $< -L"/usr/lib" -L"/home/pi/git/raspberrypi/userland/build/lib" -L"/home/pi/git/robidouille/raspicam_cv" -LC:\opencv\build\x64\MinGW\lib -LC:\Users\Dudecake\Downloads\opencv\build\x64\MinGW\lib -LD:\Dudecake\Downloads\opencv\build\x64\MinGW\lib -LC:\Users\Eelke\Downloads\utorrent\opencv\build\x64\MinGW\lib -lopencv_core2410 -lopencv_ml2410 -lopencv_video2410 -lopencv_features2d2410 -lopencv_calib3d2410 -lopencv_objdetect2410 -lopencv_contrib2410 -lopencv_flann2410 -lopencv_highgui2410 -lopencv_imgproc2410
# -lmmal_core -lmmal -lmmal_util -lvcos -lbcm_host -lraspicamcv
# --add-stdcall-alias 

# $@ matches the target, $< matches the first dependancy
DroneTracker.o : DroneTracker.cpp DroneTracker.h
	g++ -I"/usr/lib/jvm/jdk-8-oracle-arm-vfp-hflt/include" -I"/usr/lib/jvm/jdk-8-oracle-arm-vfp-hflt/include/linux" -I"/home/pi/opencv-2.4.10/include/opencv" -I"/home/pi/git/robidouille/raspicam_cv" -I"C:\Program Files\Java\jdk1.8.0_45\include" -I"C:\Program Files\Java\jdk1.8.0_45\include\win32" -I"C:\Program Files\mingw-w64\x86_64-4.9.2-win32-seh-rt_v4-rev2\mingw64\include" -I"C:\Program Files\mingw-w64\x86_64-4.9.2-posix-seh-rt_v4-rev3\mingw64\include" -I"C:\Program Files\mingw-w64\x86_64-5.1.0-posix-seh-rt_v4-rev0\mingw64\include" -I"C:\opencv\build\include" -I"D:\Dudecake\Downloads\opencv\build\include" -I"C:\Users\Dudecake\Downloads\opencv\build\include" -I"C:\Users\Eelke\Downloads\utorrent\opencv\build\include" -c $< -o $@ $(LDFLAGS)

# $* matches the target filename without the extension
DroneTracker.h : DroneTracker.class
	javah -classpath $(CLASS_PATH) $*

clean :
	rm DroneTracker.h DroneTracker.o DroneTracker.a ../resources/lib/dronetracker.dll ../resources/lib/libdronetracker.so
