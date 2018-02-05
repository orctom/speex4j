package com.orctom.speex4j;

import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.Pointer;

public interface Speex extends Library {

  Speex INSTANCE = Native.loadLibrary("spx", Speex.class);

  Pointer create_encoder(int mode, int quality, int sampling_rate);
  Pointer create_decoder(int mode);

  int encode(Pointer state, short[] in, int size, byte[] out);
  int decode(Pointer state, byte[] in, int size, short[] out);

  void destroy_encoder(Pointer state);
  void destroy_decoder(Pointer state);
}
