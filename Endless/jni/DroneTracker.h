/* DO NOT EDIT THIS FILE - it is machine generated */
#include <jni.h>
/* Header for class DroneTracker */

#ifndef _Included_DroneTracker
#define _Included_DroneTracker
#ifdef __cplusplus
extern "C" {
#endif
/*
 * Class:     DroneTracker
 * Method:    Setup
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_DroneTracker_Setup
  (JNIEnv *, jobject);

/*
 * Class:     DroneTracker
 * Method:    Track
 * Signature: ()Z
 */
JNIEXPORT jboolean JNICALL Java_DroneTracker_Track
  (JNIEnv *, jobject);

/*
 * Class:     DroneTracker
 * Method:    GetFeed
 * Signature: ()[B
 */
JNIEXPORT jbyteArray JNICALL Java_DroneTracker_GetFeed
  (JNIEnv *, jobject);

/*
 * Class:     DroneTracker
 * Method:    GetThresh
 * Signature: ()[B
 */
JNIEXPORT jbyteArray JNICALL Java_DroneTracker_GetThresh
  (JNIEnv *, jobject);

/*
 * Class:     DroneTracker
 * Method:    GetHSV
 * Signature: ()[B
 */
JNIEXPORT jbyteArray JNICALL Java_DroneTracker_GetHSV
  (JNIEnv *, jobject);

/*
 * Class:     DroneTracker
 * Method:    GetX
 * Signature: ()I
 */
JNIEXPORT jint JNICALL Java_DroneTracker_GetX
  (JNIEnv *, jobject);

/*
 * Class:     DroneTracker
 * Method:    GetY
 * Signature: ()I
 */
JNIEXPORT jint JNICALL Java_DroneTracker_GetY
  (JNIEnv *, jobject);

#ifdef __cplusplus
}
#endif
#endif
