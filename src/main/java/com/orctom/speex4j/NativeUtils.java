package com.orctom.speex4j;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;

public class NativeUtils {

  private static final Logger LOGGER = LoggerFactory.getLogger(NativeUtils.class);
  private static final String PREFIX = "/lib";

  /**
   * Load the native library from classpath.
   *
   * @param name The name of the library, do NOT include the file extension
   */
  public static void loadLibrary(String name) {
    String libraryPath = resolveLibraryPath(name);
    try {
      URL url = NativeUtils.class.getResource(libraryPath);
      File file = new File(url.toURI());
      String path = file.getAbsoluteFile().getPath();
      LOGGER.debug("Loading native library: {}", path);
      System.load(path);
    } catch (URISyntaxException e) {
      LOGGER.error(e.getMessage(), e);
    }
  }

  private static String resolveLibraryPath(String name) {
    OS os = OSUtils.getOS();
    switch (os) {
      case MAC:
        return PREFIX + name + ".dylib";
      case WIN:
        return PREFIX + name + ".dll";
      case LINUX:
        return PREFIX + name + ".so";
      default:
        return PREFIX + name + ".so";
    }
  }
}
