package com.orctom.speex4j;

import com.google.common.io.ByteStreams;
import org.junit.*;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class SpeexCodecTest {

  @Test
  public void decode() throws Exception {
    InputStream in = getClass().getResourceAsStream("/sample.spx");
    byte[] bytes = ByteStreams.toByteArray(in);
    SpeexCodec codec = new SpeexCodec(SpeexOptions.DEFAULT);

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

  @Test
  public void encode() throws Exception {
    InputStream in = getClass().getResourceAsStream("/sample.pcm");
    byte[] bytes = ByteStreams.toByteArray(in);
    SpeexCodec codec = new SpeexCodec(SpeexOptions.DEFAULT);
    byte[] spx = codec.encode(bytes);
    System.out.println(null == spx);
    if (null != spx) {
      System.out.println(spx.length);
    }
  }

}