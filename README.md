# speex4j [![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.orctom/speex4j/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.orctom/speex4j)

## Purpose

Provide a speex encoder and decoder in Java.

**Notice**
* Using the the way that in speex sample (sampleenc.c / sampledec.c) to encode and decode
* 16 bits per sample

## Usage

```
<dependency>
    <groupId>com.orctom</groupId>
    <artifactId>speex4j</artifactId>
    <version>1.0</version>
</dependency>
```

```
try (SpeexEncoder encoder = new SpeexEncoder()) {
  byte[] pcm = ...
  byte[] spx = encoder.encode(pcm);
}

try (SpeexDecoder decoder = new SpeexDecoder()) {
  byte[] spx = ...
  byte[] pcm = encoder.encode(spx);
}
```

## Change log

#### 1.0.3
* Renamed share lib name, so you don't have to have the libspeex.so/libspeex.dylib deleted from your `LD_LIBRARY_PATH`.
* Fixed `pcm2wav` issue in `SpeexUtils`.

#### 1.0.2
* Added safety check for `close` method.
#### 1.0.2
* Added safety check for `close` method.

#### 1.0.1
* Added SpeexUtils to convert between pcm and wav.

#### 1.0
* Speex encoder and decoder with fixed `frame size` of 320.
* Changed static methods to prototypes.
* Changed to use JNA.

#### 0.0.1
* Decode spx with hard-coded `wide-band` and frame size of `320`
  