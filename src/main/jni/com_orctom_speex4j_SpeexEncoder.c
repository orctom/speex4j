#include <stdio.h>
#include "speex/speex.h"
#include "com_orctom_speex4j_SpeexEncoder.h"
#include "commons.h"


SpeexBits bits;
void *state;
int frame_size;

JNIEXPORT void JNICALL Java_com_orctom_speex4j_SpeexEncoder_open
        (JNIEnv *env, jobject obj, jint mode, jint quality, jint j_frame_size) {
    speex_bits_init(&bits);
    const SpeexMode *speexMode = getSpeexMode((int)mode);
    state = speex_encoder_init(speexMode);
    frame_size = j_frame_size;

    int _quality = quality;
    speex_encoder_ctl(state, SPEEX_SET_QUALITY, &_quality);

    int enc_enh = 1;
    speex_encoder_ctl(state, SPEEX_SET_ENH, &enc_enh);

    int _frame_size;
    speex_encoder_ctl(state, SPEEX_GET_FRAME_SIZE, &_frame_size);
#ifdef DEBUG
    printf("encoder: retrieved frame_size: %d\n", _frame_size);
#endif
}

JNIEXPORT jint JNICALL Java_com_orctom_speex4j_SpeexEncoder_encode
        (JNIEnv *env, jobject obj, jshortArray pcm, jbyteArray spx) {
    short buffer[frame_size];
    jbyte output_buffer[frame_size];
    int code = 0;

    int size = (*env)->GetArrayLength(env, pcm);
    int n_frame = ((size - 1) / frame_size) + 1;

    speex_bits_init(&bits);
    for (int i = 0; i < n_frame; i++) {
        speex_bits_reset(&bits);
        (*env)->GetShortArrayRegion(env, pcm, i * frame_size, frame_size, buffer);
        speex_encode_int(state, buffer, &bits);
        int n_byte = speex_bits_write(&bits, (char *)output_buffer, frame_size);
        (*env)->SetByteArrayRegion(env, spx, i * n_byte, n_byte, output_buffer);
    }

    return (jint)code;
}

JNIEXPORT void JNICALL Java_com_orctom_speex4j_SpeexEncoder_destroy
        (JNIEnv *env, jobject obj) {
    speex_bits_destroy(&bits);
    speex_encoder_destroy(state);
}
