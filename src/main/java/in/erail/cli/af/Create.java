package in.erail.cli.af;

import com.fasterxml.jackson.databind.ObjectMapper;
import freemarker.template.TemplateException;
import in.erail.cli.af.model.Project;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

  @Option(names = {"-e", "--env"}, split = ",", defaultValue = "test,prod", description = "Additional Environments")
  private String[] env;

  private final ObjectMapper mapper = new ObjectMapper();

  @Override
  public Integer call() throws Exception {

    //Create POM
    Project config = mapper.readValue(Utils.getResource("/files/pom.json"), Project.class);
    config
            .setGroupId(groupId)
            .setArtifactId(artifactId)
            .setVersion(version)
            .setEnableLambda(enableLambda)
            .setEnvironments(env);

    Path workspace = Paths.get(outputDir);

    createPOM(workspace, config);
    createLayerFolders(workspace, config);
    createSourceFolders(workspace, config);
    createLayerAssemblies(workspace, config);
    
    return 0;
  }

  void createPOM(Path pPath, Project pProject) throws URISyntaxException, IOException, TemplateException {
    Files.createDirectories(pPath);
    FileWriter out = new FileWriter(pPath.resolve("pom.xml").toFile());
    Utils.getTemplate("pom.tpl").process(pProject, out);
  }

  void createLayerFolders(Path pPath, Project pProject) throws IOException {
    List<Path> dirs = new ArrayList<>();
    dirs.add(pPath.resolve("config-layers/common"));
    dirs.add(pPath.resolve("config-layers/test"));
    dirs.add(pPath.resolve("config-layers/local"));

    for (String environment : pProject.getEnvironments()) {
      dirs.add(pPath.resolve("config-layers/env/" + environment));
    }

    for (Path dir : dirs) {
      Files.createDirectories(dir);
    }

    for (Path dir : dirs) {
      Files.createFile(dir.resolve("placeholder.txt"));
    }
  }

  void createSourceFolders(Path pPath, Project pProject) throws IOException {
    List<Path> dirs = new ArrayList<>();
    dirs.add(pPath.resolve("src/assembly"));
    dirs.add(pPath.resolve("src/main/java"));
    dirs.add(pPath.resolve("src/main/resources"));
    dirs.add(pPath.resolve("src/test/java"));
    dirs.add(pPath.resolve("src/test/resources"));

    for (Path dir : dirs) {
      Files.createDirectories(dir);
    }
  }

  void createLayerAssemblies(Path pPath, Project pProject) throws IOException, TemplateException {

    Map<Path, Map<String, String>> assemblies = new HashMap<>();
    assemblies.put(pPath.resolve("src/assembly/common-config.xml"), Map.of("env", "common", "path", "common"));

    for (String environment : pProject.getEnvironments()) {
      Path p = pPath.resolve("src/assembly/env-" + environment + "-config.xml");
      assemblies.put(p, Map.of("env", "env-" + environment, "path", "env/" + environment));
    }

    for (Map.Entry<Path, Map<String, String>> entry : assemblies.entrySet()) {
      Path f = entry.getKey();
      FileWriter out = new FileWriter(f.toFile());
      Utils.getTemplate("config-assembly.tpl").process(entry.getValue(), out);
    }

  }
}
