
#include <stdio.h>
#include "nativeTest_HelloNative.h"

JNIEXPORT void JNICALL Java_nativeTest_HelloNative_greeting(JNIEnv *env, jclass c1) {
    printf("Hello Native!!\n");
}