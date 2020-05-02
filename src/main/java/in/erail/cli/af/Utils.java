package in.erail.cli.af;

import freemarker.cache.ClassTemplateLoader;
import freemarker.core.ParseException;
import freemarker.template.Configuration;
import freemarker.template.MalformedTemplateNameException;
import freemarker.template.Template;
import freemarker.template.TemplateExceptionHandler;
import java.io.IOException;
import java.net.URISyntaxException;

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

  public static Template getTemplate(String pName) throws MalformedTemplateNameException, ParseException, IOException {
    return fmConfig.getTemplate(pName);
  }

  public static String getResource(String pPath) throws URISyntaxException, IOException {
    return new String(Utils.class.getResourceAsStream(pPath).readAllBytes());
  }
}
