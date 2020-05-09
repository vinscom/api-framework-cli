package in.erail.cli.af;

import freemarker.cache.ClassTemplateLoader;
import freemarker.core.ParseException;
import freemarker.template.Configuration;
import freemarker.template.MalformedTemplateNameException;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;
import java.io.CharArrayWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 *
 * @author vinay
 */
public class Utils {

  public static final Configuration fmConfig;

  static {
    fmConfig = new Configuration(Configuration.VERSION_2_3_30);
    fmConfig.setDefaultEncoding("UTF-8");
    fmConfig.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
    fmConfig.setLogTemplateExceptions(false);
    fmConfig.setWrapUncheckedExceptions(true);
    fmConfig.setFallbackOnNullLoopVariable(false);
    fmConfig.setTemplateLoader(new ClassTemplateLoader(Utils.class, "/template"));
    fmConfig.setInterpolationSyntax(Configuration.SQUARE_BRACKET_INTERPOLATION_SYNTAX);
  }

  private static Template getTemplate(String pName) throws MalformedTemplateNameException, ParseException, IOException {
    return fmConfig.getTemplate(pName + ".tpl");
  }

  public static void process(String pTemplate, Object pData, Path pPath) throws IOException, TemplateException {
    try (FileWriter out = new FileWriter(pPath.toFile())) {
      getTemplate(pTemplate).process(pData, out);
    }
  }

  public static String processStr(String pTemplate, Object pData) throws IOException, TemplateException {
    CharArrayWriter out = new CharArrayWriter();
    getTemplate(pTemplate).process(pData, out);
    return out.toString();
  }

  public static String getResource(String pPath) throws URISyntaxException, IOException {
    try (InputStream stream = Utils.class.getResourceAsStream(pPath)) {
      return new String(stream.readAllBytes());
    }
  }

  public static String unzip(String pZipFileResource, Path pDestinationPath) throws IOException {
    try (ZipInputStream zipIn = new ZipInputStream(Utils.class.getResourceAsStream(pZipFileResource))) {

      ZipEntry entry = zipIn.getNextEntry();
      // iterates over entries in the zip file
      while (entry != null) {
        String uri = pDestinationPath + File.separator + entry.getName();
        Path path = Paths.get(uri);
        if (entry.isDirectory()) {
          // if the entry is a directory, make the directory
          if (!Files.exists(path)) {
            Files.createDirectories(path);
          }
        } else {

          Path parentPath = path.getParent();

          if (!Files.exists(parentPath)) {
            Files.createDirectories(parentPath);
          }

          // if the entry is a file, extracts it
          Files.copy(zipIn, path);
        }
        zipIn.closeEntry();
        entry = zipIn.getNextEntry();
      }

      return pDestinationPath.toString();
    }
  }
}
