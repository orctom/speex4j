package com.orctom.speex4j;

import com.google.common.base.Stopwatch;
import com.google.common.io.ByteStreams;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class SpeexEncoderIT {

  private static final Logger LOGGER = LoggerFactory.getLogger(SpeexEncoderIT.class);

  @Test
  public void encode() throws Exception {
    InputStream in = getClass().getResourceAsStream("/sample.pcm");
    byte[] bytes = ByteStreams.toByteArray(in);
    System.out.println("pcm length: " + bytes.length);
    try (SpeexEncoder encoder = new SpeexEncoder()) {
      Stopwatch stopwatch = Stopwatch.createStarted();
      byte[] spx = encoder.encode(bytes);
      LOGGER.info("decode took: {}, length: {}", stopwatch, spx.length);
      File encodedFile = new File("/Users/orctom/workspace/voice/speex4j-encoded.spx");
      if (encodedFile.exists()) {
        encodedFile.delete();
      }
      OutputStream out = new FileOutputStream(encodedFile);
      out.write(spx);
      out.close();
      in.close();
    }
  }

  @Test
  public void concurrentEncode() throws Exception {
    ExecutorService es = Executors.newFixedThreadPool(4);
    for (int i = 0; i < 300; i++) {
      for (int j = 0; j < 20; j++) {
        es.submit(() -> {
          try (SpeexEncoder encoder = new SpeexEncoder()) {
            InputStream in = getClass().getResourceAsStream("/audio.pcm");
            byte[] bytes = ByteStreams.toByteArray(in);
            Stopwatch stopwatch = Stopwatch.createStarted();
            byte[] spx = encoder.encode(bytes);
            LOGGER.info("decode took: {}, length: {}", stopwatch, spx.length);
          } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
          }
        });
      }
      TimeUnit.MILLISECONDS.sleep(500);
    }

    TimeUnit.MILLISECONDS.sleep(1000);
    es.shutdown();
    es.awaitTermination(10, TimeUnit.SECONDS);
    es.shutdownNow();
  }
}