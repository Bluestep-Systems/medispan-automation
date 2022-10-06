package net.bluestep.medispan.downloader;

import java.io.StringReader;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpClient.Redirect;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

/**
 *
 * @author CHale
 * @authur James Schriever
 */
public class DeliveryTools {

  private static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy.MM.dd");

  public DeliveryTools() {
  }

  // <editor-fold defaultstate="collapsed" desc="Send User For Validation">
  // <summary>
  // Send a user request for validation based on UserName and Password
  // on App.config. Save authentication token in local file.
  // </summary>
  // <returns></returns>
  public static boolean sendUserForValidation() throws Exception {
    boolean bIsUserValid = false;
    final String strUserName = ConfigurationManager.getAppSettings("authentication_username");
    final String strPassword = ConfigurationManager.getAppSettings("authentication_password");

    final String authURL = ConfigurationManager.getAppSettings("url_base")
        + ConfigurationManager.getAppSettings("authentication_url");

    try {
      final HttpClient httpclient = HttpClient.newBuilder()
          .connectTimeout(Duration.ofSeconds(30))
          .followRedirects(Redirect.NORMAL)
          .build();

      final HttpRequest webRequest = HttpRequest.newBuilder()
          .uri(URI.create(authURL))
          .timeout(Duration.ofMinutes(2))
          .header("X-UserName", strUserName)
          .header("X-Password", strPassword)
          .GET()
          .build();

      final HttpResponse<String> response = httpclient.send(webRequest, HttpResponse.BodyHandlers.ofString());
      final String resultXML = Localization.loadString("XML_HEADER") + response.body();

      final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
      final DocumentBuilder builder = factory.newDocumentBuilder();
      final Document xmlDoc = builder.parse(new InputSource(new StringReader(resultXML)));

      final NodeList nodeList = xmlDoc.getElementsByTagName(Localization.loadString("NODE_TOKEN"));
      if (nodeList.getLength() > 0) {
        ImpLogger.getInstance().writeLog(Localization.loadString("AUTH_SUCCESS"));
        final var strToken = nodeList.item(0).getTextContent();
        Tools.writeTokenToFile(strToken);
        bIsUserValid = true;
      } else {
        final NodeList error = xmlDoc.getElementsByTagName(Localization.loadString("NODE_ERROR"));

        if (error.getLength() > 0) {
          ImpLogger.getInstance().writeLog(error.item(0).getTextContent());
        } else {
          ImpLogger.getInstance().writeLog(Localization.loadString("ERROR_MESSAGE_HEADER"));
        }
      }

    } catch (final Exception e) {
      ImpLogger.getInstance().writeLogException(e);
    }
    return bIsUserValid;
  }

  public static record FileInfo(String name, String url, String id, LocalDate issueDate) {
  }

  // </editor-fold>

  // <editor-fold defaultstate="collapsed" desc="Get available files">
  // <summary>
  // Get files available from the download site, and save list to xml file
  // specified in App.config
  // </summary>
  // <param name="requestURL">Site URL</param>
  // <param name="token">Authentication Token</param>
  // <returns></returns>
  public static List<FileInfo> getAvailableFiles() throws Exception {
    final var fileInfos = new ArrayList<FileInfo>();

    ImpLogger.getInstance().writeLog(Localization.loadString("GET_FILE_LIST"));
    final String strRetrieveMode = ConfigurationManager.getAppSettings("download_fullRetrieveMode");
    final String token = Tools.readTokenFromFile();
    final String fileList = Boolean.parseBoolean(strRetrieveMode)
        ? ConfigurationManager.getAppSettings("download_fullFileListUrl")
        : ConfigurationManager.getAppSettings("download_fileListUrl");
    final String requestURL = ConfigurationManager.getAppSettings("url_base") + fileList;

    try {

      final HttpClient httpclient = HttpClient.newBuilder()
          .connectTimeout(Duration.ofSeconds(30))
          .followRedirects(Redirect.NORMAL)
          .build();

      final HttpRequest webRequest = HttpRequest.newBuilder()
          .uri(URI.create(requestURL))
          .timeout(Duration.ofMinutes(2))
          .header("X-Token", token)
          .GET()
          .build();

      final HttpResponse<String> response = httpclient.send(webRequest, HttpResponse.BodyHandlers.ofString());
      final String resultXML = Localization.loadString("XML_HEADER") + response.body();
      final DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
      final Document xmlDoc = builder.parse(new InputSource(new StringReader(resultXML)));

      final NodeList errorNodes = xmlDoc.getElementsByTagName(Localization.loadString("NODE_ERROR"));

      if (errorNodes.getLength() > 0) {
        ImpLogger.getInstance().writeLog(errorNodes.item(0).getTextContent());
      } else {
        final NodeList fileListNodes = xmlDoc.getElementsByTagName(Localization.loadString("NODE_FILELIST"));

        LocalDate latestIssueDate = LocalDate.EPOCH;
        if (fileListNodes.getLength() > 0) {
          final var stringBuilder = new StringBuilder(Localization.loadString("LIST_OF_FILES_READY")
              + System.lineSeparator());
          for (int x = 0; x < fileListNodes.getLength(); x++) {
            final var fileNode = fileListNodes.item(x);
            final var kids = fileNode.getChildNodes();
            String appendedURL = "";
            String fileName = "";
            String fileId = "";
            LocalDate issueDate = LocalDate.now();
            String md5checksum = "";
            for (int i = 0; i < kids.getLength(); i++) {
              final var kid = kids.item(i);

              if (kid.getNodeName().indexOf(Localization.loadString("NODE_FILENAME")) > -1) {
                fileName = kid.getTextContent();
              }
              if (kid.getNodeName().indexOf(Localization.loadString("NODE_LINK")) > -1) {
                appendedURL = kid.getAttributes()
                    .getNamedItem(Localization.loadString("ATTRIBUTE_HREF"))
                    .getTextContent();
              }
              if (kid.getNodeName().indexOf(Localization.loadString("NODE_ID")) > -1) {
                fileId = kid.getTextContent();
              }
              if (kid.getNodeName().indexOf(Localization.loadString("NODE_ISSUEDATE")) > -1) {
                issueDate = LocalDate.parse(kid.getTextContent(), dateFormatter);
              }
              if (kid.getNodeName().indexOf(Localization.loadString("NODE_CHECKSUM")) > -1) {
                md5checksum = kid.getTextContent();
              }
            }
            if (DownloadFilter.filterAll(fileName)) {
              if (issueDate.isAfter(latestIssueDate)) {
                latestIssueDate = issueDate;
              }
              if (Tools.checkSum(filePath(fileName), md5checksum)) {
                stringBuilder.append("Already downloaded --> ");
              } else {
                fileInfos.add(new FileInfo(fileName, appendedURL, fileId, issueDate));
                stringBuilder.append("Passed filters --> ");
              }
            } else {
              stringBuilder.append("Failed filters --> ");
            }
            stringBuilder.append(fileNode.getTextContent()).append(System.lineSeparator());
          }
          ImpLogger.getInstance().writeLog(stringBuilder.toString());
        } else {
          ImpLogger.getInstance().writeLog(Localization.loadString("ERROR_MESSAGE_HEADER"));
        }
        if (Boolean.parseBoolean(ConfigurationManager.getAppSettings("download_latestOnly"))) {
          final var latestIssueDate2 = latestIssueDate;
          fileInfos.removeIf(fileInfo -> fileInfo.issueDate().isBefore(latestIssueDate2));
        }
      }

    } catch (final Exception e) {
      ImpLogger.getInstance().writeLogException(e);
    }

    return fileInfos;
  }
  // </editor-fold>

  // <editor-fold defaultstate="collapsed" desc="Download Files">
  //
  //
  public static void doDownloadFiles(final List<FileInfo> fileInfos) throws Exception {
    final String token = Tools.readTokenFromFile();
    ImpLogger.getInstance().writeLog(Localization.loadString("DOWNLOADING"));

    final HttpClient httpclient = HttpClient.newBuilder()
        .connectTimeout(Duration.ofSeconds(30))
        .followRedirects(Redirect.NORMAL)
        .build();
    final AtomicInteger fileDownloadCount = new AtomicInteger();
    final ForkJoinPool customThreadPool = new ForkJoinPool(
        Integer.parseInt(ConfigurationManager.getAppSettings("download_concurrent")));
    final var task = customThreadPool.submit(() -> fileInfos.parallelStream()
        .forEach(fileInfo -> {
          try {
            ImpLogger.getInstance()
                .writeLog(String.format(Localization.loadString("FILE_ATTEMPT"), fileInfo.name()));

            final HttpRequest webRequest = HttpRequest.newBuilder()
                .uri(URI.create(fileInfo.url()))
                .timeout(Duration.ofMinutes(2))
                .header("X-Token", token)
                .header("X-FileID", fileInfo.id())
                .GET()
                .build();

            // use httpclient to download file
            httpclient.send(
                webRequest,
                HttpResponse.BodyHandlers.ofFile(filePath(fileInfo.name())));

            fileDownloadCount.incrementAndGet();
          } catch (final Exception e) {
            Logger.getLogger(DeliveryTools.class.getName()).log(Level.SEVERE, null, e);
          }
        }));

    task.get();
    customThreadPool.shutdownNow();
    if (fileDownloadCount.get() < fileInfos.size()) {
      ImpLogger.getInstance().writeLog(String.format(Localization.loadString("ONLY_X_FILES"),
          String.valueOf(fileDownloadCount), String.valueOf(fileInfos.size())));
    }
  }

  private static Path filePath(final String fileName) {
    return Paths.get(ConfigurationManager.getAppSettings("output_path"), fileName);
  }

  // </editor-fold>
}
