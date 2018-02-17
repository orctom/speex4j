package com.orctom.speex4j;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;
import java.util.Random;

public abstract class SpeexUtils {

  public static byte[] pcm2spx(byte[] pcm) {
    try (SpeexEncoder encoder = new SpeexEncoder()) {
      return encoder.encode(pcm);
    }
  }

  public static byte[] pcm2wav(byte[] pcm) {
    return addHeader(pcm, 16000, 16);
  }

  public static byte[] spx2pcm(byte[] spx) {
    try (SpeexDecoder decoder = new SpeexDecoder()) {
      return decoder.decode(spx);
    }
  }

  public static byte[] spx2wav(byte[] spx) {
    return pcm2wav(spx2pcm(spx));
  }

  public static byte[] wav2pcm(byte[] wav) {
    return Arrays.copyOfRange(wav, 44, wav.length);
  }

  public static byte[] wav2spx(byte[] wav) {
    return pcm2spx(wav2pcm(wav));
  }

  public static byte[] pcm2wav(byte[] pcm, int samplingRate, int bitsPerSample) {
    return addHeader(pcm, samplingRate, bitsPerSample);
  }

  private static byte[] addHeader(byte[] pcm, int samplingRate, int bitsPerSample) {
    byte[] header = generateWavHeader(pcm.length, samplingRate, bitsPerSample);
    return Bytes.concat(header, pcm);
  }

  /**
   * convert pcm from sampling rate of 160000 to 8000
   */
  public static byte[] pcmLowerSamplingRate(byte[] pcm) {
    byte[] data = new byte[pcm.length / 2];
    for (int i = 0; i < pcm.length; i++) {
      if (i % 2 == 0) {
        continue;
      }
      data[i / 2] = pcm[i];
    }
    return data;
  }

  /**
   * convert pcm from bits per sample of 16 to 8
   */
  public static byte[] pcmLowerBitRate(byte[] pcm) {
    short[] shorts = new short[pcm.length / 2];
    ByteBuffer.wrap(pcm).order(ByteOrder.LITTLE_ENDIAN).asShortBuffer().get(shorts);

    byte[] data = new byte[shorts.length];
    for (int i = 0; i < shorts.length; i++) {
      short s = shorts[i];
      byte byteData = (byte) (((s + 32768) >> 8) & 0xFF);
      data[i] = byteData;
    }
    return data;
  }

  public static byte[] generateWavHeader(int pcmLength, int sampleRate, int bitsPerSample) {
    byte[] header = new byte[44];

    long totalDataLen = pcmLength + 36;
    int channel = 1;
    long bitrate = sampleRate * channel * bitsPerSample;

    header[0] = 'R';
    header[1] = 'I';
    header[2] = 'F';
    header[3] = 'F';
    header[4] = (byte) (totalDataLen & 0xff);
    header[5] = (byte) ((totalDataLen >> 8) & 0xff);
    header[6] = (byte) ((totalDataLen >> 16) & 0xff);
    header[7] = (byte) ((totalDataLen >> 24) & 0xff);
    header[8] = 'W';
    header[9] = 'A';
    header[10] = 'V';
    header[11] = 'E';
    header[12] = 'f';
    header[13] = 'm';
    header[14] = 't';
    header[15] = ' ';
    header[16] = (byte) 16;
    header[17] = 0;
    header[18] = 0;
    header[19] = 0;
    header[20] = 1;
    header[21] = 0;
    header[22] = (byte) channel;
    header[23] = 0;
    header[24] = (byte) (sampleRate & 0xff);
    header[25] = (byte) ((sampleRate >> 8) & 0xff);
    header[26] = (byte) ((sampleRate >> 16) & 0xff);
    header[27] = (byte) ((sampleRate >> 24) & 0xff);
    header[28] = (byte) ((bitrate / 8) & 0xff);
    header[29] = (byte) (((bitrate / 8) >> 8) & 0xff);
    header[30] = (byte) (((bitrate / 8) >> 16) & 0xff);
    header[31] = (byte) (((bitrate / 8) >> 24) & 0xff);
    header[32] = (byte) ((channel * bitsPerSample) / 8);
    header[33] = 0;
    header[34] = (byte) bitsPerSample;
    header[35] = 0;
    header[36] = 'd';
    header[37] = 'a';
    header[38] = 't';
    header[39] = 'a';
    header[40] = (byte) (pcmLength & 0xff);
    header[41] = (byte) ((pcmLength >> 8) & 0xff);
    header[42] = (byte) ((pcmLength >> 16) & 0xff);
    header[43] = (byte) ((pcmLength >> 24) & 0xff);

    return header;
  }

  public static int[] generateWhiteNoise(int sampleCount, int stddev) {
    if (sampleCount < 0) {
      return new int[0];
    }

    Random random = new Random();
    // Generate White Noise.
    int[] signal = new int[sampleCount];
    for (int i = 0; i < sampleCount; i++) {
      signal[i] = (int) (random.nextGaussian() * stddev);
      if (signal[i] > 32767) {
        signal[i] = 32767;
      }
      if (signal[i] < -32768) {
        signal[i] = -32768;
      }
    }
    return signal;
  }
}
