package com.orctom.speex4j;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SpeexCodec {

  private static final Logger LOGGER = LoggerFactory.getLogger(SpeexCodec.class);

  static {
    NativeUtils.loadLibrary("speex");
  }

  private SpeexOptions options;

  public SpeexCodec(SpeexOptions options) {
    this.options = options;
  }

  public byte[] encode(byte[] pcm) {
    short[] _pcm = toShortArray(pcm);
    byte[] spx = new byte[pcm.length];
    int code = encode(options.getSamplingRate().getCode(), options.getQuality(), options.getFrameSize(), _pcm, spx);
    if (0 != code) {
      throw new RuntimeException("Failed to decode");
    }

    return spx;
  }

  public byte[] decode(byte[] spx) {
    short[] pcm = new short[spx.length];
    int code = decode(options.getSamplingRate().getCode(), spx, pcm);
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

  protected short[] toShortArray(byte[] inputData) {
    int len = inputData.length / 2;
    short[] ret = new short[len];

    for (int i = 0; i < len; i++) {
      ret[i] = (short) ((inputData[i * 2 + 1] << 8) & 0xffff | (inputData[i * 2] & 0x00ff));
    }
    return ret;
  }

  private native int encode(int mode, int quality, int frameSize, short[] pcm, byte[] spx);

  private native int decode(int mode, byte[] spx, short[] pcm);

}
