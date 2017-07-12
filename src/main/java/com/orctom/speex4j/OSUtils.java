package com.orctom.speex4j;

public abstract class OSUtils {

  private static String os = System.getProperty("os.name").toLowerCase();

  public static OS getOS() {
    if (os.contains("nux") || os.contains("nix")) {
      return OS.LINUX;
    }
    if (os.contains("mac")) {
      return OS.MAC;
    }
    if (os.contains("win")) {
      return OS.WIN;
    }

    return OS.OTHER;
  }
}
