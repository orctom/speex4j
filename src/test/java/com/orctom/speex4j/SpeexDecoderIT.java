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
import java.util.Arrays;

public class SpeexDecoderIT {

  private static final Logger LOGGER = LoggerFactory.getLogger(SpeexDecoderIT.class);

  @Test
  public void decode() throws Exception {
    InputStream in = getClass().getResourceAsStream("/sample.spx");
    byte[] bytes = ByteStreams.toByteArray(in);
    try (SpeexDecoder decoder = new SpeexDecoder()) {
      byte[] pcm = decoder.decode(bytes);
      System.out.println("total length: " + pcm.length);
      File decodedFile = new File("/Users/orctom/workspace/voice/speex4j-decoded.pcm");
      if (decodedFile.exists()) {
        decodedFile.delete();
      }
      OutputStream out = new FileOutputStream(decodedFile);
      out.write(pcm);
      out.close();
    }
  }

  @Test
  public void chunkDecode() throws Exception {
    InputStream in = getClass().getResourceAsStream("/sample.spx");
    byte[] bytes = ByteStreams.toByteArray(in);
    try (SpeexDecoder decoder = new SpeexDecoder()) {
      int chunkSize = 1775; // 500ms
      LOGGER.info("chunk size: {}, total: {}", chunkSize, bytes.length);
      int startIndex = 0;
      int endIndex = chunkSize;
      Stopwatch stopwatch = Stopwatch.createStarted();
      while (startIndex < bytes.length) {
        if (endIndex > bytes.length) {
          endIndex = bytes.length;
        }
        byte[] data = Arrays.copyOfRange(bytes, startIndex, endIndex);
        LOGGER.info("decode took: {} coping data", stopwatch.reset().start());
        byte[] spx = decoder.decode(data);
        LOGGER.info("decode took: {}, length: {}", stopwatch.reset().start(), spx.length);
        startIndex += chunkSize;
        endIndex += chunkSize;
      }
    }
  }

}