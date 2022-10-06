package net.bluestep.medispan.downloader;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author CHale
 * @authur James Schriever
 */
enum LogState {
  ERR, NeedsInitialized, Ready
}

public class ImpLogger {
  private static Logger _instanceLogger = Logger.getLogger("WebDownloader");;
  private static ImpLogger _instance = new ImpLogger();
  private String _log = "";
  private FileHandler _logStream;
  private boolean _logToConsole = false;
  private final String _logSep = " | ";
  private static LogState _state = LogState.NeedsInitialized;

  public static ImpLogger getInstance() {
    if (_instance == null) {
      _instance = new ImpLogger();
    }
    return _instance;
  }

  private ImpLogger() {
  }

  public boolean Init(final String logFile, final boolean alwaysLogToConsole) {
    boolean bRet = false;
    try {
      _logToConsole = alwaysLogToConsole;
      _log = logFile;
      _logStream = new FileHandler(_log, true);
      _instanceLogger.addHandler(_logStream);
      bRet = true;
      _state = LogState.Ready;
    } catch (final IOException ex) {
      java.util.logging.Logger.getLogger(Logger.class.getName()).log(Level.SEVERE, null, ex);
    } catch (final SecurityException ex) {
      java.util.logging.Logger.getLogger(Logger.class.getName()).log(Level.SEVERE, null, ex);
    }
    return bRet;

  }

  public void writeLog(final String LogText) throws Exception {
    if (!(_state == LogState.Ready)) {
      throw new Exception(
          String.format("Logger has NOT been initialized, or logfile '%s' is in a bad state. ERR: %s", _log,
              _state.toString()));
    } else {
      _log = LogText;
    }
    _instanceLogger.log(Level.INFO, _log);
  }

  public void writeLogException(final Exception ex) throws Exception {
    if (!_state.equals(LogState.Ready)) {
      throw new Exception(
          String.format("Logger has NOT been initialized, or logfile '%s' is in a bad state. ERR: %s", _log,
              _state.toString()));

    }
    final String errTime = logTime();
    if (_logToConsole) {
      System.out.println(
          errTime + _logSep + ex.getMessage() + System.lineSeparator() + ex.getStackTrace());
    }
    try {
      _instanceLogger.log(Level.SEVERE, errTime + _logSep + "*****EXCEPTION*****");
      _instanceLogger.log(Level.SEVERE, String.format(_logSep + "%1$-22s", "Message : ") + ex.toString());
      final Writer result = new StringWriter();
      final PrintWriter printWriter = new PrintWriter(result);
      ex.printStackTrace(printWriter);
      _instanceLogger.log(Level.SEVERE, String.format(_logSep + "%1$-22s", "Stack Trace : ") + result.toString());
    } catch (final Exception ode) {
      _state = LogState.ERR;
      if (_logToConsole) {
        System.out.println("EXCEPTION TRYING TO WRITE TO LOGFILE!");
        System.out.println(ode.getMessage());
        System.out.println(ode.getStackTrace());
      } else {
        throw (ode);
      }
    }
  }

  private String logTime() {
    final SimpleDateFormat sdf = new SimpleDateFormat("yyyy:MM:dd:HH:mm:ss");
    final Calendar cal = Calendar.getInstance();
    final String datetimenow = sdf.format(cal.getTime());
    return datetimenow;
  }

}
