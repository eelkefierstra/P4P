# Define a variable for classpath
CLASS_PATH = ../bin

#LIBS =		opencv_imgproc2410
#			opencv_ml2410
#			opencv_core2410
#			opencv_highgui2410

# Define a virtual path for .class in the bin directory
vpath %.class $(CLASS_PATH)

all : dronetracker.dll

# $@ matches the target, $< matches the first dependancy
dronetracker.dll : DroneTracker.o
	gcc -Wl,--add-stdcall-alias -shared -o $@ $<

# $@ matches the target, $< matches the first dependancy
DroneTracker.o : DroneTracker.cpp DroneTracker.h
	gcc -I"C:\Program Files\Java\jdk1.8.0_45\include" -I"C:\Program Files\Java\jdk1.8.0_45\include\win32" -I"D:\Dudecake\Downloads\opencv\build\include" -c $< -o $@

# $* matches the target filename without the extension
DroneTracker.h : DroneTracker.class
	javah -classpath $(CLASS_PATH) $*