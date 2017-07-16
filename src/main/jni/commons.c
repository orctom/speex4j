#include "speex/speex.h"
#import "commons.h"

const SpeexMode *getSpeexMode(int mode) {
    switch (mode) {
        case 0:
            return &speex_nb_mode;
        case 2:
            return &speex_uwb_mode;
        default:
            return &speex_wb_mode;
    }
}


void JNU_ThrowByName(JNIEnv *env, const char *name, const char *msg) {
    jclass cls = (*env)->FindClass(env, name);
    if (cls != NULL) {
        (*env)->ThrowNew(env, cls, msg);
    }
    (*env)->DeleteLocalRef(env, cls);
}
