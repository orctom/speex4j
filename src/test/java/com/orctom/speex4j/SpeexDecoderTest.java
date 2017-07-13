package com.orctom.speex4j;

import com.google.common.io.ByteStreams;
import org.junit.*;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SpeexDecoderTest {

  @Test
  public void decode() throws Exception {
    InputStream in = getClass().getResourceAsStream("/sample.spx");
    byte[] bytes = ByteStreams.toByteArray(in);
    SpeexDecoder codec = new SpeexDecoder(SamplingRate.WIDE_BAND);
    int chunkSize = 1775;
    int startIndex = 0;
    int endIndex = chunkSize;
    int index = 1;
    while (startIndex < bytes.length) {
      if (endIndex > bytes.length) {
        endIndex = bytes.length;
      }
      byte[] data = Arrays.copyOfRange(bytes, startIndex, endIndex);
      byte[] pcm = codec.decode(data);
      System.out.println(null == pcm);
      if (null != pcm) {
        System.out.println(pcm.length);
      }
      startIndex += chunkSize;
      endIndex += chunkSize;
      index++;
    }
  }

}