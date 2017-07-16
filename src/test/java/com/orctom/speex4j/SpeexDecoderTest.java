package com.orctom.speex4j;

import com.google.common.io.ByteStreams;
import org.junit.*;

import java.io.InputStream;
import java.util.Arrays;

import static org.junit.Assert.*;

public class SpeexDecoderTest {

  @Test
  public void decode() throws Exception {
    InputStream in = getClass().getResourceAsStream("/sample.spx");
    byte[] bytes = ByteStreams.toByteArray(in);
    SpeexDecoder codec = SpeexDecoder.newInstance(SamplingRate.WIDE_BAND);

    int chunkSize = 1775;
    int startIndex = 0;
    int endIndex = chunkSize;
    while (startIndex < bytes.length) {
      if (endIndex > bytes.length) {
        endIndex = bytes.length;
      }
      byte[] data = Arrays.copyOfRange(bytes, startIndex, endIndex);
      startIndex += chunkSize;
      endIndex += chunkSize;
      byte[] pcm = codec.decode(data);
      System.out.println("----------");
      System.out.println(null == pcm);
      if (null != pcm) {
        System.out.println(pcm.length);
      }
    }
  }

}