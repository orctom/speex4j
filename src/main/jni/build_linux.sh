#!/usr/bin/env bash

echo "Compiling..."
gcc  -shared -fPIC \
  -c com_orctom_speex4j_SpeexDecoder.h com_orctom_speex4j_SpeexDecoder.c slots.h slots.c \
  -I/$JAVA_HOME/include \
  -I/$JAVA_HOME/include/linux/ \
  -I speex-1.2.0/include/ \
  -I .

echo "Linking..."
gcc  -shared -fPIC  -o libspeexcodec.so \
  -c com_orctom_speex4j_SpeexDecoder.h com_orctom_speex4j_SpeexDecoder.c slots.h slots.c \
  -I/$JAVA_HOME/include \
  -I/$JAVA_HOME/include/linux/ \
  -I speex-1.2.0/include/ \
  -L speex-1.2.0/lib \
  -I .

cp libspeexcodec.so ../resources/
echo "Done".