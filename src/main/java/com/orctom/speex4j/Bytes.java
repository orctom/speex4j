package com.orctom.speex4j;

public class Bytes {

  public static byte[] shortToByte(short number) {
    int temp = number;
    byte[] b = new byte[2];
    for (int i = 0; i < b.length; i++) {
      b[i] = new Integer(temp & 0xff).byteValue();// 将最低位保存在最低位
      temp = temp >> 8; // 向右移8位
    }
    return b;
  }

  public static short byteToShort(byte[] b) {
    short s = 0;
    short s0 = (short) (b[0] & 0xff);// 最低位
    short s1 = (short) (b[1] & 0xff);
    s1 <<= 8;
    s = (short) (s0 | s1);
    return s;
  }

  public static short[] byteArray2ShortArray(byte[] b) {
    int len = b.length / 2;
    int index = 0;
    short[] re = new short[len];
    byte[] buf = new byte[2];
    for (int i = 0; i < b.length;) {
      buf[0] = b[i];
      buf[1] = b[i + 1];
      short st = byteToShort(buf);
      re[index] = st;
      index++;
      i += 2;
    }
    return re;
  }

  public static byte[] shortArray2ByteArray(short[] b) {
    byte[] rebt = new byte[b.length * 2];
    int index = 0;
    for (int i = 0; i < b.length; i++) {
      short st = b[i];
      byte[] bt = shortToByte(st);
      rebt[index] = bt[0];
      rebt[index + 1] = bt[1];
      index += 2;
    }
    return rebt;
  }
}
