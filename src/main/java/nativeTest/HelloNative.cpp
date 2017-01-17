
#include <iostream>
#include "nativeTest_HelloNative.h"
using namespace std;

JNIEXPORT void JNICALL Java_nativeTest_HelloNative_greeting(JNIEnv *env, jclass c1) {
    cout << "Hello Native!!" << endl;
}
