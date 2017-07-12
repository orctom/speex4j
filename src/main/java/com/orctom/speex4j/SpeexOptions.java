package com.orctom.speex4j;

public class SpeexOptions {

  public static final SpeexOptions DEFAULT = new SpeexOptions(SamplingRate.WIDE_BAND, 8, 320);

  private SamplingRate samplingRate;
  private int quality;
  private int frameSize;

  SpeexOptions(SamplingRate samplingRate, int quality, int frameSize) {
    this.samplingRate = samplingRate;
    this.quality = quality;
    this.frameSize = frameSize;
  }

  public SamplingRate getSamplingRate() {
    return samplingRate;
  }

  public int getQuality() {
    return quality;
  }

  public int getFrameSize() {
    return frameSize;
  }

  public static Builder builder() {
    return new Builder();
  }

  public static class Builder {

    private SamplingRate samplingRate;
    private int quality;
    private int frameSize;

    public Builder samplingRate(SamplingRate samplingRate) {
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
