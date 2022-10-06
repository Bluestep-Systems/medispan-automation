/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.bluestep.medispan.downloader;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 Everything is done in helper classes to help you find what you're seeking.
    This app breaks the process up into three chunks :
    1) Validates the user, saves the the authentication token to a file
    2) Gets the available file list (using the token now in the file)
    3) Downloads available files

 * The configuration manager libraries provided with this example are covered by
 * the Apache License, version 2 .0
 * http://www.apache.org/licenses/LICENSE-2.0
 */
public class App {

  /**
   * @param args the command line arguments
   */

  public static void main(final String[] args) {
    // check output directory.
    // load the properties

    try {

      final String baseDir = ConfigurationManager.getAppSettings("output_path");

      if (!Files.isDirectory(Paths.get(baseDir))) {
        System.out.println(
            String.format(Localization.loadString("BAD_DIR"), baseDir, System.lineSeparator()));
        System.exit(1);
      }

      final String logfile = baseDir + File.separator
          + ConfigurationManager.getAppSettings("output_logFilePrefix")
          + DateTimeFormatter.ofPattern(".ddMMyyyy.HHmm").format(ZonedDateTime.now())
          + ".log";
      ImpLogger.getInstance().Init(logfile, false);

      if (DeliveryTools.sendUserForValidation()) {
        final var fileList = DeliveryTools.getAvailableFiles();
        if (!fileList.isEmpty()) {
          DeliveryTools.doDownloadFiles(fileList);
          ImpLogger.getInstance().writeLog(Localization.loadString("DOWNLOAD_SUCCESS"));
        } else {
          ImpLogger.getInstance().writeLog(Localization.loadString("DOWNLOAD_FAIL"));
        }
      } else {
        ImpLogger.getInstance().writeLog(Localization.loadString("ERROR_MESSAGE_HEADER"));
      }
    } catch (final Exception ex) {
      Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
    }

  }

}
