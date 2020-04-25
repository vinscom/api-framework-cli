package in.erail.cli.af;

import com.fasterxml.jackson.databind.ObjectMapper;
import in.erail.cli.af.model.Project;
import java.io.File;
import java.io.FileWriter;
import java.nio.file.Paths;
import java.util.concurrent.Callable;
import static picocli.CommandLine.*;

/**
 *
 * @author vinay
 */
@Command(name = "create")
public class Create implements Callable<Integer> {

  @Option(names = {"-g", "--groupid"}, defaultValue = "com.foo", description = "Maven project group id")
  private String groupId;
  @Option(names = {"-a", "--artifactid"}, defaultValue = "bar", description = "Maven project artifact id")
  private String artifactId;
  @Option(names = {"-v", "--version"}, defaultValue = "0.1", description = "Maven Project Version")
  private String version;
  @Option(names = {"-le", "--enableLambda"}, defaultValue = "false", description = "Enable Lambda Support")
  private boolean enableLambda;
  @Option(names = {"-o", "--output"}, defaultValue = ".", description = "Output Directory")
  private String outputDir;

  private ObjectMapper mapper = new ObjectMapper();

  @Override
  public Integer call() throws Exception {

    //Create POM
    Project config = mapper.readValue(Utils.getResource("/config/pom.json"), Project.class);
    config
            .setGroupId(groupId)
            .setArtifactId(artifactId)
            .setVersion(version)
            .setEnableLambda(enableLambda);
    
    new File(getOutputDir()).mkdirs();
    
    FileWriter out = new FileWriter(Paths.get(getOutputDir(),"pom.xml").toFile());
    Utils.getTemplate("pom.tpl").process(config, out);

    return 0;
  }

  public String getGroupId() {
    return groupId;
  }

  public Create setGroupId(String pGroupId) {
    this.groupId = pGroupId;
    return this;
  }

  public String getArtifactId() {
    return artifactId;
  }

  public Create setArtifactId(String pArtifactId) {
    this.artifactId = pArtifactId;
    return this;
  }

  public String getVersion() {
    return version;
  }

  public Create setVersion(String pVersion) {
    this.version = pVersion;
    return this;
  }

  public boolean isEnableLambda() {
    return enableLambda;
  }

  public Create setEnableLambda(boolean pEnableLambda) {
    this.enableLambda = pEnableLambda;
    return this;
  }

  public String getOutputDir() {
    return outputDir;
  }

  public void setOutputDir(String pOutputDir) {
    this.outputDir = pOutputDir;
  }

}
