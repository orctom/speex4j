package com.orctom.speex4j;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SpeexEncoder {

  private static final Logger LOGGER = LoggerFactory.getLogger(SpeexEncoder.class);

  static {
    NativeUtils.loadLibrary("speexcodec");
  }

  protected SpeexEncoder(SpeexOptions options) {
    open(
        options.getSamplingRate().getCode(),
        options.getQuality(),
        options.getFrameSize()
    );
  }

  public static SpeexEncoder newInstance(SpeexOptions options) {
    return new SpeexEncoder(options);
  }

  public byte[] encode(byte[] pcm) {
    short[] _pcm = toShortArray(pcm);
    byte[] spx = new byte[pcm.length];
    int code = encode(_pcm, spx);
    if (0 != code) {
      throw new RuntimeException("Failed to decode");
    }

    return spx;
  }

  protected short[] toShortArray(byte[] inputData) {
    int len = inputData.length / 2;
    short[] ret = new short[len];

    for (int i = 0; i < len; i++) {
      ret[i] = (short) ((inputData[i * 2 + 1] << 8) & 0xffff | (inputData[i * 2] & 0x00ff));
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

  protected native void open(int mode, int quanlity, int frameSize);

  protected native int encode(short[] pcm, byte[] spx);

  public native void destroy();
}
