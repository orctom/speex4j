package com.orctom.speex4j;

import com.google.common.io.ByteStreams;
import org.junit.*;

import java.io.InputStream;

public class SpeexCodecTest {

  @Test
  public void decode() throws Exception {
    InputStream in = getClass().getResourceAsStream("/sample.spx");
    byte[] bytes = ByteStreams.toByteArray(in);
    SpeexCodec codec = new SpeexCodec(SpeexOptions.DEFAULT);
    byte[] pcm = codec.decode(bytes);
    System.out.println(null == pcm);
    if (null != pcm) {
      System.out.println(pcm.length);
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