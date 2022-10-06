
package net.bluestep.medispan.downloader;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author CHale
 * @authur James Schriever
 */
public class Tools {
  private Tools() {
  }

  /// <summary>
  /// Write the authentication token to a local file specified in
  /// App.config
  /// </summary>
  /// <param name="Token"></param>
  public static void writeTokenToFile(final String token) {
    final var tokenFile = Paths.get(ConfigurationManager.getAppSettings("output_path"),
        ConfigurationManager.getAppSettings("output_tokenFile"))
        .toFile();
    if (tokenFile.exists()) {
      tokenFile.delete();
    }
    try (final var out = new FileWriter(tokenFile);
        final var outputToken = new BufferedWriter(out)) {
      outputToken.write(token);
      outputToken.flush();
    } catch (final IOException ex) {
      Logger.getLogger(Tools.class.getName()).log(Level.SEVERE, null, ex);
    }
    tokenFile.deleteOnExit();
  }

  /// <summary>
  /// Read authentication token from file specified in App.config
  /// </summary>
  /// <returns></returns>
  public static String readTokenFromFile() {
    final var tokenFile = Paths
        .get(ConfigurationManager.getAppSettings("output_path"),
            ConfigurationManager.getAppSettings("output_tokenFile"))
        .toFile();
    try (final var in = new FileReader(tokenFile);
        final var tokenreader = new BufferedReader(in)) {
      final var token = new StringBuilder();
      String tokenline = null;
      while ((tokenline = tokenreader.readLine()) != null) {
        token.append(tokenline);
      }
      return token.toString();
    } catch (final IOException ex) {
      Logger.getLogger(Tools.class.getName()).log(Level.SEVERE, null, ex);
    }
    return null;
  }

  public static boolean checkSum(final Path path, final String md5) {
    final var file = path.toFile();
    if (!file.exists()) {
      return false;
    }

    // Check the MD5 from path against md5
    try (final var fis = new FileInputStream(file)) {
      final var md = MessageDigest.getInstance("MD5");
      final var dataBytes = new byte[1024];
      int nread = 0;
      while ((nread = fis.read(dataBytes)) != -1) {
        md.update(dataBytes, 0, nread);
      }
      final var mdbytes = md.digest();
      final var sb = new StringBuilder();
      for (final var mdbyte : mdbytes) {
        sb.append(Integer.toString((mdbyte & 0xff) + 0x100, 16).substring(1));
      }
      return sb.toString().equals(md5);
    } catch (final Exception ex) {
      Logger.getLogger(Tools.class.getName()).log(Level.SEVERE, null, ex);
    }
    return false;
  }
}
