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
import java.util.Arrays;
import java.util.Collections;
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

    createLayerFolders(workspace, config);
    createSourceFolders(workspace, config);

    generateLayerAssemblies(workspace, config);
    generateSource(workspace, config);
    generateConfigLayer(workspace, config);

    createPOM(workspace, config);
    return 0;
  }

  void createPOM(Path pPath, Project pProject) throws URISyntaxException, IOException, TemplateException {
    Files.createDirectories(pPath);
    Utils.process("pom.xml", pProject, pPath.resolve("pom.xml"));
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

  void generateLayerAssemblies(Path pPath, Project pProject) throws IOException, TemplateException {

    Map<Path, Map<String, String>> assemblies = new HashMap<>();
    assemblies.put(pPath.resolve("src/assembly/common-config.xml"), Map.of("env", "common", "path", "common"));

    for (String environment : pProject.getEnvironments()) {
      Path p = pPath.resolve("src/assembly/env-" + environment + "-config.xml");
      assemblies.put(p, Map.of("env", "env-" + environment, "path", "env/" + environment));
    }

    for (Map.Entry<Path, Map<String, String>> entry : assemblies.entrySet()) {
      Utils.process("config-assembly.xml", entry.getValue(), entry.getKey());
    }

    Utils.process("deployment.xml",
            Map.of("envs", Arrays.asList(pProject.getEnvironments())),
            pPath.resolve("src/assembly/deployment.xml"));
  }

  void generateSource(Path pPath, Project pProject) throws IOException, TemplateException {
    Path java = pPath.resolve("src/main/java").resolve(pProject.getGroupId().replace(".", "/")).resolve("api");
    Path test = pPath.resolve("src/test/java").resolve(pProject.getGroupId().replace(".", "/"));
    Files.createDirectories(java);
    Files.createDirectories(test);

    Path path = java.resolve("SessionGetService.java");
    Utils.process("SessionGetService.java", Collections.EMPTY_MAP, path);
  }

  void generateConfigLayer(Path pPath, Project pProject) throws IOException, TemplateException {
    Path base = pPath.resolve("config-layers/common");
    Path service = base.resolve(pProject.getGroupId().replace(".", "/")).resolve("api");
    Path erail = base.resolve("in/erail/route");
    Files.createDirectories(service);
    Files.createDirectories(erail);

    Utils.process("OpenAPI3RouteBuilder.properties", Collections.EMPTY_MAP, erail.resolve("OpenAPI3RouteBuilder.properties"));
    Utils.process("openapi3.json", Collections.EMPTY_MAP, erail.resolve("openapi3.json"));
    Utils.process("SessionGetService.properties", Collections.EMPTY_MAP, service.resolve("SessionGetService.properties"));
  }
}
