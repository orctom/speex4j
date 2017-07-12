#!/usr/bin/env bash

#gcc -o libHelloImpl.jnilib -lc -shared \
#    -I/System/Library/Frameworks/JavaVM.framework/Headers com_marakana_jniexamples_Hello.c
echo "Compiling..."
gcc -c com_orctom_speex4j_SpeexCodec.c \
  -framework JavaVM \
  -I /System/Library/Frameworks/JavaVM.framework/Headers

echo "Linking..."
gcc  -dynamiclib -o speex.dylib \
  com_orctom_speex4j_SpeexCodec.o \
  -framework JavaVM \
  -I /System/Library/Frameworks/JavaVM.framework/Headers \
  -l speex

cp speex.dylib /Users/xm/workspace-hao/speex4j/src/test/resources/
#gcc  -dynamiclib \
#  -o libspeexdecoder.jnilib \
#  com_kikakeyboard_backend_voice_service_speex_SpeexDecoder.o \
#  com_kikakeyboard_backend_voice_service_speex_SpeexEncoder.o \
#  slots.o \
#  -framework JavaVM \
#  -I /System/Library/Frameworks/JavaVM.framework/Headers \
#  -I /usr/local/Cellar/speex/1.2.0/include/ \
#  -L /usr/local/Cellar/speex/1.2.0/lib \
#  -l speex