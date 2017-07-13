#!/usr/bin/env bash

echo "Compiling..."
gcc -c com_orctom_speex4j_SpeexDecoder.h com_orctom_speex4j_SpeexDecoder.c slots.h slots.c \
  -framework JavaVM \
  -I /System/Library/Frameworks/JavaVM.framework/Headers \
  -I speex-1.2.0/include/ \
  -L speex-1.2.0/lib \
  -I .

echo "Linking..."
gcc  -dynamiclib -o libspeexcodec.dylib \
  com_orctom_speex4j_SpeexDecoder.o slots.o \
  -framework JavaVM \
  -I /System/Library/Frameworks/JavaVM.framework/Headers \
  -I speex-1.2.0/include/ \
  -L speex-1.2.0/lib \
  -l speex \
  -I .

mv libspeexcodec.dylib ../resources/

echo "Done."
