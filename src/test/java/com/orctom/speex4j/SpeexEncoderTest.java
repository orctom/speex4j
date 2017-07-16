package com.orctom.speex4j;

import com.google.common.io.ByteStreams;
import org.junit.*;

import java.io.InputStream;

public class SpeexEncoderTest {

  @Test
  public void encode() throws Exception {
    InputStream in = getClass().getResourceAsStream("/sample.pcm");
    byte[] bytes = ByteStreams.toByteArray(in);
    SpeexEncoder codec = SpeexEncoder.newInstance(SpeexOptions.DEFAULT);
    byte[] spx = codec.encode(bytes);
    System.out.println(null == spx);
    if (null != spx) {
      System.out.println(spx.length);
    }
  }

}