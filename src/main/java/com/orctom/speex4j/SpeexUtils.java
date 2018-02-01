package com.orctom.speex4j;

import java.util.Random;

public abstract class SpeexUtils {

  public static byte[] generateWaveHeader(int channels, int sampleRate, int sampleCount) {
    int headerSize = 44;
    byte[] header = new byte[headerSize];
    writeString(header, 0, "RIFF");
    writeInt(header, 4, 2 * channels * sampleCount + headerSize - 8);
    writeString(header, 8, "WAVE");
    writeString(header, 12, "fmt ");
    writeInt(header, 16, 16);                         // Size of format chunk
    writeShort(header, 20, (short) 0x01);                 // Format tag: PCM
    writeShort(header, 22, (short) channels);             // Number of channels
    writeInt(header, 24, sampleRate);                     // Sampling frequency
    writeInt(header, 28, 2 * sampleRate * channels);  // Average bytes per second
    writeShort(header, 32, (short) 2 * channels);     // Blocksize of data
    writeShort(header, 34, (short) 16);                   // Bits per sample
    writeString(header, 36, "data");
    writeInt(header, 40, 2 * sampleCount * channels); // Data Size
    return header;
  }

  private static void writeString(byte[] data, int offset, String val) {
    byte[] str = val.getBytes();
    System.arraycopy(str, 0, data, offset, str.length);
  }

  private static void writeInt(byte[] data, int offset, int val) {
    data[offset] = (byte) (0xff & val);
    data[offset + 1] = (byte) (0xff & (val >>> 8));
    data[offset + 2] = (byte) (0xff & (val >>> 16));
    data[offset + 3] = (byte) (0xff & (val >>> 24));
  }

  private static void writeShort(byte[] data, int offset, int val) {
    data[offset] = (byte) (0xff & val);
    data[offset + 1] = (byte) (0xff & (val >>> 8));
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
      if (signal[i] > 32767)
        signal[i] = 32767;
      if (signal[i] < -32768)
        signal[i] = -32768;
    }
    return signal;
  }


}
