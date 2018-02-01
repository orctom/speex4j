package com.orctom.speex4j;

public class SpeexOptions {

  public static final SpeexOptions DEFAULT = new SpeexOptions(Mode.WIDE_BAND, 8, 16000);

  private Mode mode;
  private int quality;
  private int samplingRate;

  SpeexOptions(Mode mode, int quality, int samplingRate) {
    this.mode = mode;
    this.quality = quality;
    this.samplingRate = samplingRate;
  }

  public Mode getMode() {
    return mode;
  }

  public int getQuality() {
    return quality;
  }

  public int getSamplingRate() {
    return samplingRate;
  }

  public static Builder builder() {
    return new Builder();
  }

  public static class Builder {

    private Mode samplingRate;
    private int quality;
    private int frameSize;

    public Builder samplingRate(Mode samplingRate) {
      this.samplingRate = samplingRate;
      return this;
    }

    public Builder quality(int quality) {
      this.quality = quality;
      return this;
    }

    public Builder frameSize(int frameSize) {
      this.frameSize = frameSize;
      return this;
    }

    public SpeexOptions build() {
      return new SpeexOptions(samplingRate, quality, frameSize);
    }
  }
}
