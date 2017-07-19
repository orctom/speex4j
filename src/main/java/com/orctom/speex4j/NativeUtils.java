package com.orctom.speex4j;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

public class NativeUtils {

  private static final Logger LOGGER = LoggerFactory.getLogger(NativeUtils.class);
  private static final String PREFIX = "/lib";

  /**
   * Load the native library from classpath.
   *
   * @param name The name of the library, do NOT include the file extension
   */
  public static void loadLibrary(String name) {
    try {
      System.loadLibrary(name);
    } catch (UnsatisfiedLinkError ignored) {
      try {
        loadLibraryFromJar(name);
      } catch (Exception e) {
        throw new RuntimeException(e.getMessage(), e);
      }
    }
  }

  private static void loadLibraryFromJar(String name) throws IOException {
    String extension = resolveLibraryExtension();
    final File temp = File.createTempFile("jni-" + name, extension);

    if (!temp.exists()) {
      throw new RuntimeException("File " + temp.getAbsolutePath() + " does not exist.");
    } else {
      temp.deleteOnExit();
    }

    String path = PREFIX + name + extension;

    try (final InputStream is = NativeUtils.class.getResourceAsStream(path)) {
      if (is == null) {
        throw new RuntimeException(path + " was not found inside JAR.");
      } else {
        Files.copy(is, temp.toPath(), StandardCopyOption.REPLACE_EXISTING);
      }
    }

    String libraryPath = temp.getPath();
    LOGGER.debug("Loading native library: {}", libraryPath);
    System.load(libraryPath);
  }

  private static String resolveLibraryExtension() {
    OS os = OSUtils.getOS();
    switch (os) {
      case MAC:
        return ".dylib";
      case WIN:
        return ".dll";
      case LINUX:
        return ".so";
      default:
        return ".so";
    }
  }
}
