#include "com_orctom_speex4j_SpeexDecoder.h"

#include <stdlib.h>
#include <string.h>

#include "slots.h"

static struct SlotVector slots = {
    0,0
};

JNIEXPORT jint JNICALL Java_com_orctom_speex4j_SpeexDecoder_allocate
  (JNIEnv *env, jclass cls, jint wideband)
{
    int slot = allocate_slot(&slots);

    //

    slots.slots[slot] = malloc(sizeof(struct Slot));

    struct Slot* gob = slots.slots[slot];

    //

    speex_bits_init(&gob->bits);


    const SpeexMode * mode;
    switch (wideband) {
    case 1:
	mode = &speex_wb_mode;
	break;
    case 2:
	mode = &speex_uwb_mode;
	break;
    default:
	mode = &speex_nb_mode;
	break;
    }

    gob->state = speex_decoder_init(mode);

    return slot;
}

//

static void throwIllegalArgumentException(JNIEnv *env, char * msg)
{
    jclass newExcCls = (*env)->FindClass(env, "java/lang/IllegalArgumentException");
    if (newExcCls == 0) /* Unable to find the new exception class, give up. */
	return;
    (*env)->ThrowNew(env, newExcCls, msg);
}

static void throwOutOfMemoryError(JNIEnv *env, char * msg)
{
    jclass newExcCls = (*env)->FindClass(env, "java/lang/OutOfMemoryError");
    if (newExcCls == 0) /* Unable to find the new exception class, give up. */
	return;
    (*env)->ThrowNew(env, newExcCls, msg);
}

//

static int throwIfBadSlot(JNIEnv *env, jint slot)
{
    if (slot>=slots.nslots) {
	throwIllegalArgumentException(env, "bogus slot");
	return 1;
    }

    if ((void*)0 == slots.slots[slot]) {
	jclass newExcCls = (*env)->FindClass(env, "java/lang/IllegalArgumentException");
	if (newExcCls == 0) /* Unable to find the new exception class, give up. */
	    return 1;
	(*env)->ThrowNew(env, newExcCls, "slot is already empty");
	return 1;
    }

    return 0; // the slot is good
}

//

JNIEXPORT void JNICALL Java_com_orctom_speex4j_SpeexDecoder_deallocate
  (JNIEnv *env, jclass cls, jint slot)
{
    if (throwIfBadSlot(env, slot))
	return;

    speex_bits_destroy(&slots.slots[slot]->bits);
    speex_decoder_destroy(slots.slots[slot]->state);

    free( slots.slots[slot] );
    slots.slots[slot] = (void*)0;
}

int decode_spx(const char *in_buf, const int in_size, char *decoded_data, int *out_size) {
    #define FRAME_SIZE 320
    short speex_output[320];
    int nbBytes = 70;
    void *state;
    SpeexBits m_bits;
    int i, tmp;

    state = speex_decoder_init(&speex_wb_mode);
    int enc_enh = 1;
    speex_decoder_ctl(state, SPEEX_SET_ENH, &enc_enh);
    speex_bits_init(&m_bits);

    int in_start_pos = 0;
    int out_start_pos = 0;
    while (1) {
        int length = in_buf[in_start_pos];
        in_start_pos += 1;
        speex_bits_read_from(&m_bits, in_buf + in_start_pos, length);
        in_start_pos += length;

        int ret = speex_decode_int(state, &m_bits, speex_output);

        for (i=0;i<FRAME_SIZE;i++) {
            decoded_data[out_start_pos + 2*i+1]  = (char)((speex_output[i] >> 8) & 0xff);
            decoded_data[out_start_pos + 2*i] = (char)(speex_output[i] & 0xff);
        }

        out_start_pos += FRAME_SIZE*2;
        if (in_start_pos == in_size) {
            break;
        } else if (in_start_pos > in_size) {
            //fprintf(stdout, "decode bad!");
        }
        //fprintf(stdout, "%s.%d\n", __FILE__, __LINE__);
    }
    *out_size = out_start_pos;

    speex_decoder_destroy(state);
    speex_bits_destroy(&m_bits);

    return 0;
}

JNIEXPORT jbyteArray JNICALL Java_com_orctom_speex4j_SpeexDecoder_decode
  (JNIEnv *env, jclass cls, jint slot, jbyteArray input_frame_, jint length, jbyteArray output_frame_, jint index)
{
    if (throwIfBadSlot(env, slot))
    	return 0;

    struct Slot * gob = slots.slots[slot];
    int frame_length = (*env)->GetArrayLength(env, input_frame_);
    char* input_frame = (*env)->GetByteArrayElements(env, input_frame_, 0);
    char rval[16000];
    int dst_length = 0;
    int ret = decode_spx(input_frame, length, rval, &dst_length);

    (*env)->ReleaseByteArrayElements(env, input_frame_, input_frame, 0);
    jbyteArray array = (*env)->NewByteArray(env, dst_length);
    (*env)->SetByteArrayRegion(env, array, 0, dst_length, rval);
    return array;
}