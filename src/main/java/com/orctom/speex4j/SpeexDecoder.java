package com.orctom.speex4j;

import com.sun.jna.Pointer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Closeable;

public class SpeexDecoder implements Closeable {

  private static final Logger LOGGER = LoggerFactory.getLogger(SpeexDecoder.class);

  private Pointer state;

  public SpeexDecoder() {
    this(Mode.WIDE_BAND);
  }

  public SpeexDecoder(Mode mode) {
    state = Speex.INSTANCE.create_decoder(mode.getCode());
  }

  public byte[] decode(byte[] spx) {
    int len = spx.length;
    int size = ((len - 1) / 71 + 1) * 320;
    short[] pcm = new short[size];
    int pcmSize = Speex.INSTANCE.decode(state, spx, len, pcm);
    LOGGER.debug("size: {}, pcmSize: {}", size, pcmSize);
    return Bytes.toByteArray(pcm);
  }

  @Override
  public void close() {
    LOGGER.debug("close");
    Speex.INSTANCE.destroy_decoder(state);
  }
}
