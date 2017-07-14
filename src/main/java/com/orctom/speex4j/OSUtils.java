package com.orctom.speex4j;

public abstract class OSUtils {

  private static final String OS_NAME = System.getProperty("os.name").toLowerCase();
  private static final String ARCH = System.getProperty("os.arch").toLowerCase();

  public static OS getOS() {
    if (OS_NAME.contains("nux") || OS_NAME.contains("nix")) {
      return OS.LINUX;
    }
    if (OS_NAME.contains("mac")) {
      return OS.MAC;
    }
    if (OS_NAME.contains("win")) {
      return OS.WIN;
    }

    return OS.OTHER;
  }
}
