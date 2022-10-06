/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.bluestep.medispan.downloader;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 *
 * @author CHale
 * @authur James Schriever
 */
class Localization {
  private static Localization _instance;
  private final ResourceBundle _resmanager;

  private Localization() {
    _resmanager = ResourceBundle.getBundle("bluestep-medispan-downloader", Locale.ENGLISH);
  }

  public ResourceBundle getResourceBundle() {
    return _resmanager;
  }

  public static String loadString(final String p_strResourceID) {
    if (_instance == null) {
      _instance = new Localization();
    }
    return _instance.getResourceBundle().getString(p_strResourceID);
  }

}
