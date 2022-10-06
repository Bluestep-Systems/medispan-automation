package net.bluestep.medispan.downloader;

import java.util.Arrays;
import java.util.EnumSet;
import java.util.regex.Pattern;

/*
 * @author James Schriever
 */
public enum DownloadFilter {
  SOFTWARE(Pattern.compile(ConfigurationManager.getAppSettings("download_filter_softwareName"))),
  INCREMANTAL_DB(Pattern.compile(ConfigurationManager.getAppSettings("download_filter_incrementalDbName"))),
  FULL_DB(Pattern.compile(ConfigurationManager.getAppSettings("download_filter_fullDbName")));

  private static final Pattern fileNameFilter = Pattern
      .compile(ConfigurationManager.getAppSettings("download_filter_name"));
  private static EnumSet<DownloadFilter> downloadFilterTypes = EnumSet.copyOf(
      Arrays.stream(ConfigurationManager.getAppSettings("download_types").split(","))
          .map(String::trim)
          .map(DownloadFilter::valueOf)
          .toList());

  private final Pattern pattern;

  private DownloadFilter(final Pattern pattern) {
    this.pattern = pattern;
  }

  public boolean filter(final String fileName) {
    return fileNameFilter.matcher(fileName).matches()
        && pattern.matcher(fileName).matches();
  }

  public static boolean filterAll(final String fileName) {
    return downloadFilterTypes.stream()
        .filter(downloadType -> downloadType.filter(fileName))
        .findAny()
        .isPresent();
  }
}
