#!/usr/bin/env bash

echo "Compiling..."
gcc -c commons.h commons.c \
  com_orctom_speex4j_SpeexEncoder.h com_orctom_speex4j_SpeexEncoder.c \
  com_orctom_speex4j_SpeexDecoder.h com_orctom_speex4j_SpeexDecoder.c \
  -I /System/Library/Frameworks/JavaVM.framework/Headers \
  -I speex-1.2.0/include/ \
  -I .

echo "Linking..."
gcc  -dynamiclib -o libspeexcodec.dylib \
  commons.o \
  com_orctom_speex4j_SpeexEncoder.o \
  com_orctom_speex4j_SpeexDecoder.o \
  -framework JavaVM \
  -I /System/Library/Frameworks/JavaVM.framework/Headers \
  -I speex-1.2.0/include/ \
  -L speex-1.2.0/lib \
  -l speex \
  -I .

echo "Done."
