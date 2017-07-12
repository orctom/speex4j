#include <stdio.h>
#include "com_orctom_speex4j_SpeexCodec.h"
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

int decode_spx(const char *in_buf, const int in_size, char *decoded_data, int *out_size) {
#define FRAME_SIZE 320
    short speex_output[320];
    int nbBytes = 70;
    void *state;
    SpeexBits m_bits;
    int i, tmp;

    //初始化
    state = speex_decoder_init(&speex_wb_mode);
    int enc_enh = 1;
    speex_decoder_ctl(state, SPEEX_SET_ENH, &enc_enh);
    speex_bits_init(&m_bits);

    int in_start_pos = 0;
    int out_start_pos = 0;
    while (1) {
        //读一个字节
        int length = in_buf[in_start_pos];
        in_start_pos += 1;
        //fprintf(stdout, "%s.%d\n", __FILE__, __LINE__);
        //读一个Frame
        speex_bits_read_from(&m_bits, in_buf + in_start_pos, length);
        in_start_pos += length;

        //fprintf(stdout, "%s.%d\n", __FILE__, __LINE__);
        //解码
        int ret = speex_decode_int(state, &m_bits, speex_output);

        //fprintf(stdout, "%s.%d\n", __FILE__, __LINE__);
        //转为short
        for (i=0;i<FRAME_SIZE;i++) {
            decoded_data[out_start_pos + 2*i+1]  = (char)((speex_output[i] >> 8) & 0xff);
            decoded_data[out_start_pos + 2*i] = (char)(speex_output[i] & 0xff);
        }

        //fprintf(stdout, "%s.%d\n", __FILE__, __LINE__);
        out_start_pos += FRAME_SIZE*2;
        if (in_start_pos == in_size) {
            //fprintf(stdout, "decode over good!");
            break;
        } else if (in_start_pos > in_size) {
            //fprintf(stdout, "decode bad!");
        }
        //fprintf(stdout, "%s.%d\n", __FILE__, __LINE__);
    }
    *out_size = out_start_pos;
    fprintf(stdout, "out_start_pos:%d\n", out_start_pos);

    //fprintf(stdout, "%s.%d\n", __FILE__, __LINE__);
    //析构函数
    speex_decoder_destroy(state);
    speex_bits_destroy(&m_bits);

    //fprintf(stdout, "%s.%d\n", __FILE__, __LINE__);
    return 0;
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
    speex_decoder_ctl(state, SPEEX_SET_ENH, &enc_enh);
    state = speex_decoder_init(speexMode);
    printf("mode: %s\n", speexMode->modeName);
    int size = (*env)->GetArrayLength(env, spx);

    int frame_size;
    speex_decoder_ctl(state, SPEEX_GET_FRAME_SIZE, &frame_size);
    printf("frame size: %d\n", frame_size);
    int n_frame = ((size - 1) / frame_size) + 1;

    jbyte buffer[frame_size];
    short output_buffer[1775];

    speex_bits_init(&bits);
    for (int i = 0; i < n_frame; i++) {
        speex_bits_reset(&bits);
        (*env)->GetByteArrayRegion(env, spx, i * frame_size, frame_size, buffer);
        speex_bits_read_from(&bits, (char *) buffer, frame_size);
        int n_byte = speex_decode_int(state, &bits, output_buffer);
        printf("decoded to %d bytes, vs. %d\n", n_byte, frame_size * 12);
        (*env)->SetShortArrayRegion(env, pcm, i * n_byte, n_byte, output_buffer);
    }

    speex_bits_destroy(&bits);
    speex_encoder_destroy(state);

    return (jint)0;
}
