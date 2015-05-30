# Define a variable for classpath
CLASS_PATH = ../bin

# Define a virtual path for .class in the bin directory
vpath %.class $(CLASS_PATH)

all : jnitest.dll

# $@ matches the target, $< matches the first dependancy
jnitest.dll : JNITest.o
	gcc -Wl,--add-stdcall-alias -shared -o $@ $<

# $@ matches the target, $< matches the first dependancy
JNITest.o : JNITest.c JNITest.h
	gcc -I"C:\Program Files\Java\jdk1.8.0_45\include" -I"C:\Program Files\Java\jdk1.8.0_45\include\win32" -c $< -o $@

# $* matches the target filename without the extension
JNITest.h : JNITest.class
	javah -classpath $(CLASS_PATH) $*