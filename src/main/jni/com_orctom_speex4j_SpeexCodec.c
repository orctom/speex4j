#include <stdio.h>
#include "com_orctom_speex4j_SpeexCodec.h"
#include "speex/speex.h"
#include "speex-1.2.0/include/speex/speex.h"

__const SpeexMode *getSpeexMode(int mode) {
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

/*
 * Class:     com_orctom_speex4j_SpeexCodec
 * Method:    encode
 * Signature: (III[B)[B
 */
JNIEXPORT jint JNICALL Java_com_orctom_speex4j_SpeexCodec_encode
        (JNIEnv *env, jobject obj, jint mode, jint quality, jint frame_size, jshortArray pcm, jbyteArray spx) {

    short buffer[frame_size];
    jbyte output_buffer[frame_size];
    int code = 0;

    SpeexBits bits;
    void *state;
    const SpeexMode *speexMode = getSpeexMode((int)mode);
    int size = (*env)->GetArrayLength(env, spx);
    int n_frame = ((size - 1) / frame_size) + 1;
    state = speex_encoder_init(speexMode);

    speex_bits_init(&bits);
    for (int i = 0; i < n_frame; i++) {
        speex_bits_reset(&bits);
        (*env)->GetShortArrayRegion(env, pcm, i * frame_size, frame_size, buffer);
        speex_encode_int(state, buffer, &bits);
        int n_byte = speex_bits_write(&bits, (char *)output_buffer, (int)frame_size);
        (*env)->SetByteArrayRegion(env, spx, i * n_byte, n_byte, output_buffer);
    }

    speex_bits_destroy(&bits);
    speex_encoder_destroy(state);

    return (jint)code;
}

/*
 * Class:     com_orctom_speex4j_SpeexCodec
 * Method:    decode
 * Signature: (II[B)[B
 */
JNIEXPORT jint JNICALL Java_com_orctom_speex4j_SpeexCodec_decode
  (JNIEnv *env, jobject obj, jint mode, jbyteArray spx, jshortArray pcm) {

    SpeexBits bits;
    void *state;
    const SpeexMode *speexMode = getSpeexMode((int)mode);
    int enc_enh = 1;
    speex_bits_init(&bits);
    state = speex_decoder_init(speexMode);
    speex_decoder_ctl(state, SPEEX_SET_ENH, &enc_enh);
    printf("mode: %s\n", speexMode->modeName);
    int size = (*env)->GetArrayLength(env, spx);

    int frame_size;
    speex_decoder_ctl(state, SPEEX_GET_FRAME_SIZE, &frame_size);
    printf("frame size: %d\n", frame_size);
    printf("spx   size: %d\n", size);
    int n_frame = ((size - 1) / frame_size) + 1;

    jbyte buffer[frame_size];
    jshort output_buffer[1775];

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
    (*env)->GetByteArrayRegion(env, spx, 0, size, buffer);
    speex_bits_read_from(&bits, (char *)buffer, size);
    speex_decode_int(state, &bits, output_buffer);
    (*env)->SetShortArrayRegion(env, pcm, 0, frame_size, output_buffer);

    speex_bits_destroy(&bits);
    speex_encoder_destroy(state);

    return (jint)0;
}
