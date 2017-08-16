package com.orctom.speex4j;

public class SpeexDecoder {

  private final int slot;

  public SpeexDecoder(SamplingRate samplingRate) {
    slot = allocate(samplingRate.getCode());
  }

  public byte[] decode(byte[] spx) {
    return decode(slot, spx, spx.length, null, -1);
  }

  public void close() {
    deallocate(slot);
  }

  private native static byte[] decode(int slot, byte[] frame, int length, byte[] dst_buf, int index);
  protected native static int allocate(int wideband);
  protected native static void deallocate(int slot);

  static {
    NativeUtils.loadLibrary("speexcodec");
  }
}
