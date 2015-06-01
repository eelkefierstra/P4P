# Define a variable for classpath
CLASS_PATH = ../bin

# Define a virtual path for .class in the bin directory
vpath %.class $(CLASS_PATH)

all : dronetracker.dll

# $@ matches the target, $< matches the first dependancy
dronetracker.dll : DroneTracker.o
	g++ -Wl,--add-stdcall-alias -shared -o ..\resources\lib\$@ $< -LC:\Users\Dudecake\Downloads\opencv\build\x64\MinGW\lib -lopencv_core2410 -lopencv_ml2410 -lopencv_video2410 -lopencv_features2d2410 -lopencv_calib3d2410 -lopencv_objdetect2410 -lopencv_contrib2410 -lopencv_flann2410 -lopencv_highgui2410 -lopencv_imgproc2410

# $@ matches the target, $< matches the first dependancy
DroneTracker.o : DroneTracker.cpp DroneTracker.h
	g++ -I"C:\Program Files\Java\jdk1.8.0_45\include" -I"C:\Program Files\Java\jdk1.8.0_45\include\win32" -I"C:\Program Files\mingw-w64\x86_64-4.9.2-win32-seh-rt_v4-rev2\mingw64\include" -I"D:\Dudecake\Downloads\opencv\build\include" -I"C:\Users\Dudecake\Downloads\opencv\build\include" -c $< -o $@ $(LDFLAGS)

# $* matches the target filename without the extension
DroneTracker.h : DroneTracker.class
	javah -classpath $(CLASS_PATH) $*

clean :
	rm DroneTracker.h DroneTracker.o ../resources/lib/dronetracker.dll
# g++ "-ID:\\Dudecake\\Downloads\\opencv\\build\\include" "-IC:\\Program Files\\Java\\jdk1.8.0_45\\include" "-IC:\\Users\\Dudecake\\Documents\\GitHub\\P4P\\jnitester" "-IC:\\Program Files\\mingw-w64\\x86_64-4.9.2-posix-seh-rt_v4-rev2\\mingw64\\include" "-IC:\\Users\\Dudecake\\Downloads\\opencv\\build\\include" -O2 -g -Wall -c -fmessage-length=0 -o jnitester.o "..\\jnitester.cpp"
# g++ "-ID:\\Dudecake\\Downloads\\opencv\\build\\include" "-IC:\\Program Files\\Java\\jdk1.8.0_45\\include" "-IC:\\Users\\Dudecake\\Documents\\GitHub\\P4P\\jnitester" "-IC:\\Program Files\\mingw-w64\\x86_64-4.9.2-posix-seh-rt_v4-rev2\\mingw64\\include" "-IC:\\Users\\Dudecake\\Downloads\\opencv\\build\\include" -O2 -g -Wall -c -fmessage-length=0 -o DroneDetection.o "..\\DroneDetection.cpp"
# g++ "-LD:\\Dudecake\\Downloads\\opencv\\build\\x64\\MinGW\\lib" "-LC:\\Users\\Dudecake\\Downloads\\opencv\\build\\x64\\MinGW\\lib" -o jnitester jnitester.o DroneDetection.o -lopencv_core2410 -lopencv_ml2410 -lopencv_video2410 -lopencv_features2d2410 -lopencv_calib3d2410 -lopencv_objdetect2410 -lopencv_contrib2410 -lopencv_flann2410 -lopencv_highgui2410 -lopencv_imgproc2410