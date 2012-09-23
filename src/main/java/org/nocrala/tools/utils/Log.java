package org.nocrala.tools.utils;

public class Log {

  private static final boolean DEBUG_ENABLED = false;
  private static final boolean INFO_ENABLED = false; // Should be false for prod
  private static final boolean ERROR_ENABLED = true; // Should remain true

  private static final boolean SHOW_CALLER = false;
  private static final boolean SHOW_CLASS_PACKAGE = false;

  private static final String ASTERION_PROMPT = "[Asterion] ";

  public static void debug(final String txt) {
    if (DEBUG_ENABLED) {
      System.out.println(ASTERION_PROMPT + "DEBUG " + renderCaller() + " - "
          + txt);
    }
  }

  public static void info(final String txt) {
    if (INFO_ENABLED) {
      System.out.println(ASTERION_PROMPT + "INFO  " + renderCaller() + " - "
          + txt);
    }
  }

  public static void error(final String txt) {
    if (ERROR_ENABLED) {
      System.out.println(ASTERION_PROMPT + "ERROR " + renderCaller() + " - "
          + txt);
    }
  }

  public static void error(final Throwable t) {
    if (ERROR_ENABLED) {
      System.out.println(ASTERION_PROMPT + "ERROR " + renderCaller() + " - "
          + t);
      t.printStackTrace(System.out);
    }
  }

  public static boolean isDebugEnabled() {
    return DEBUG_ENABLED;
  }

  public static boolean isInfoEnabled() {
    return INFO_ENABLED;
  }

  public static boolean isErrorEnabled() {
    return ERROR_ENABLED;
  }

  private static String renderCaller() {
    if (!SHOW_CALLER) {
      return "";
    }
    StackTraceElement caller = null;
    StackTraceElement[] se = Thread.currentThread().getStackTrace();
    if (se != null && se.length >= 4) {
      caller = se[3];
    }
    return renderCallerDetail(caller);
  }

  private static String renderCallerDetail(final StackTraceElement caller) {
    if (caller == null) {
      return "";
    }
    String className = caller.getClassName();
    if (!SHOW_CLASS_PACKAGE) {
      int idx = className.lastIndexOf('.');
      if (idx != -1) {
        className = className.substring(idx + 1);
      }
    }

    return className + "." + caller.getMethodName() + "("
        + caller.getLineNumber() + ") ";
  }

}
