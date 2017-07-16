#include <stdio.h>
#include "speex/speex.h"
#include "com_orctom_speex4j_SpeexDecoder.h"
#include "commons.h"


SpeexBits bits;
void *state;
int frame_size;

JNIEXPORT void JNICALL Java_com_orctom_speex4j_SpeexDecoder_open
        (JNIEnv *env, jobject obj, jint mode) {
    speex_bits_init(&bits);
    const SpeexMode *speexMode = getSpeexMode((int) mode);
    state = speex_encoder_init(speexMode);

    speex_decoder_ctl(state, SPEEX_GET_FRAME_SIZE, &frame_size);
#ifdef DEBUG
    printf("decoder frame size: %d\n", frame_size);
#endif
}

JNIEXPORT jint JNICALL Java_com_orctom_speex4j_SpeexDecoder_decode
        (JNIEnv *env, jobject obj, jbyteArray spx, jshortArray pcm) {
    char buffer[frame_size];
    jshort output_buffer[1775];
    int code = 0;

    int size = (*env)->GetArrayLength(env, spx);
    speex_bits_reset(&bits);
//    for (int i = 0; i < n_frame; i++) {
//        speex_bits_reset(&bits);
//        (*env)->GetByteArrayRegion(env, spx, i * frame_size, frame_size, buffer);
//        speex_bits_read_from(&bits, (char *) buffer, frame_size);
//        int n_byte = speex_decode_int(state, &bits, output_buffer);
//        printf("decoded to %d bytes, vs. %d\n", n_byte, frame_size * 12);
//        (*env)->SetShortArrayRegion(env, pcm, i * n_byte, n_byte, output_buffer);
//    }
    printf("bits->buf_size: %d\n", bits.buf_size);
    (*env)->GetByteArrayRegion(env, spx, 0, size, (jbyte *) buffer);
    speex_bits_read_from(&bits, (char *)buffer, size);
    speex_decode_int(state, &bits, output_buffer);
    (*env)->SetShortArrayRegion(env, pcm, 0, frame_size, output_buffer);

    return (jint)code;
}

JNIEXPORT void JNICALL Java_com_orctom_speex4j_SpeexDecoder_destroy
        (JNIEnv *env, jobject obj) {
    speex_bits_destroy(&bits);
    speex_encoder_destroy(state);
}
