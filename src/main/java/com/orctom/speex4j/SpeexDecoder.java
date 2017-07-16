package com.orctom.speex4j;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SpeexDecoder {

  private static final Logger LOGGER = LoggerFactory.getLogger(SpeexDecoder.class);

  static {
    NativeUtils.loadLibrary("speexcodec");
  }

  protected SpeexDecoder(SamplingRate samplingRate) {
    open(samplingRate.getCode());
  }

  public static SpeexDecoder newInstance(SamplingRate samplingRate) {
    return new SpeexDecoder(samplingRate);
  }

  public byte[] decode(byte[] spx) {
    short[] pcm = new short[spx.length];
    int code = decode(spx, pcm);
    if (0 != code) {
      throw new RuntimeException("Failed to encode");
    }

    return toByteArray(pcm);
  }

  protected byte[] toByteArray(short[] inputData) {
    int len = inputData.length * 2;
    byte[] ret = new byte[len];

    for (int i = 0; i < len; i += 2) {
      ret[i] = (byte) (inputData[i / 2] & 0xff);
      ret[i + 1] = (byte) ((inputData[i / 2] >> 8) & 0xff);
    }
    return ret;
  }

  public void close() {
    destroy();
  }

  @Override
  protected void finalize() throws Throwable {
    destroy();
  }

  protected native void open(int mode);

  protected native int decode(byte[] spx, short[] pcm);

  public native void destroy();
}
