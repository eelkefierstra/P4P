CXXFLAGS =	-O2 -g -Wall -fmessage-length=0

OBJS =		jnitester.o

LIBS =		opencv_imgproc2410
			opencv_ml2410
			opencv_core2410
			opencv_highgui2410

TARGET =	jnitester.exe

$(TARGET):	$(OBJS)
	$(CXX) -o $(TARGET) $(OBJS) $(LIBS)

all:	$(TARGET)

clean:
	rm -f $(OBJS) $(TARGET)
