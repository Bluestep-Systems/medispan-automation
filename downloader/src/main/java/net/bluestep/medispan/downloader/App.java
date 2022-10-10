
package net.bluestep.medispan.downloader;

import java.nio.file.Files;
import java.nio.file.Paths;
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

  private static final Logger logger = Logger.getLogger(App.class.getName());

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

      if (DeliveryTools.sendUserForValidation()) {
        final var fileList = DeliveryTools.getAvailableFiles();
        if (!fileList.isEmpty()) {
          DeliveryTools.doDownloadFiles(fileList);
          logger.info(Localization.loadString("DOWNLOAD_SUCCESS"));
        } else {
          logger.info(Localization.loadString("DOWNLOAD_FAIL"));
        }
      } else {
        logger.info(Localization.loadString("ERROR_MESSAGE_HEADER"));
      }
    } catch (final Exception ex) {
      logger.log(Level.SEVERE, null, ex);
    }

  }

}
