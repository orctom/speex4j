package com.orctom.speex4j;

import com.google.common.io.ByteStreams;
import com.google.common.io.Files;
import org.junit.Test;

import java.io.File;
import java.io.InputStream;

public class SpeexUtilsIT {

  @Test
  public void testPcm2Wav() throws Exception {
    InputStream in = getClass().getResourceAsStream("/sample.pcm");
    byte[] pcm = ByteStreams.toByteArray(in);
    byte[] wav = SpeexUtils.pcm2wav(pcm);
    File target = new File("target.wav");
    Files.write(wav, target);
    System.out.println(target.getAbsolutePath());
  }
}
