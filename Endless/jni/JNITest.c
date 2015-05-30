#include <jni.h>
#include <stdio.h>
#include "JNITest.h"

JNIEXPORT void JNICALL Java_JNITest_jnitest(JNIEnv *env, jobject thisObj) {
   printf("Hello World!\n");
   return;
}
