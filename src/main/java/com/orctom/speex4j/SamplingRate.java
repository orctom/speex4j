package com.orctom.speex4j;

/**
 * KHz
 */
public enum SamplingRate {
  NARROW_BAND(0),
  WIDE_BAND(1),
  ULTRA_WIDE_BAND(2);

  private int code;

  SamplingRate(int code) {
    this.code = code;
  }

  public int getCode() {
    return code;
  }
}
