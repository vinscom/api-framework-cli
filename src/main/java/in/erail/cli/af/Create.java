package in.erail.cli.af;

import com.fasterxml.jackson.databind.ObjectMapper;
import freemarker.template.TemplateException;
import in.erail.cli.af.model.Project;
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
  @Option(names = {"-awsl", "--enableAWSLambda"}, defaultValue = "false", description = "Enable Lambda Support")
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
            .setEnvironments(env)
            .setEnableAWSLambda(enableLambda);

    Path workspace = Paths.get(outputDir);

    createLayerFolders(workspace, config);
    createSourceFolders(workspace, config);

    generateLayerAssemblies(workspace, config);
    generateSource(workspace, config);
    generateConfigLayer(workspace, config);
    generateGlueConfig(workspace, config);
    generateLog4j(workspace, config);
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
    Path test = pPath.resolve("src/test/java").resolve(pProject.getGroupId().replace(".", "/")).resolve("api");
    Files.createDirectories(java);
    Files.createDirectories(test);

    Path javaSrc = java.resolve("SessionGetService.java");
    Utils.process("SessionGetService.java", Map.of("package", pProject.getGroupId() + ".api"), javaSrc);

    Path testSrc = test.resolve("SessionGetServiceTest.java");
    Utils.process("SessionGetServiceTest.java", Map.of("package", pProject.getGroupId() + ".api"), testSrc);
  }

  void generateConfigLayer(Path pPath, Project pProject) throws IOException, TemplateException {
    String folder = pProject.getGroupId().replace(".", "/");
    Path base = pPath.resolve("config-layers/common");
    Path service = base.resolve(folder).resolve("api");
    Path openapi = base.resolve("in/erail/route");
    Path eventbus = base.resolve("io/vertx/core/eventbus");

    Files.createDirectories(service);
    Files.createDirectories(openapi);
    Files.createDirectories(eventbus);

    String serviceCompPath = "/" + folder + "/api/SessionGetService";

    Utils.process("OpenAPI3RouteBuilder.properties",
            Map.of("path", serviceCompPath),
            openapi.resolve("OpenAPI3RouteBuilder.properties"));

    Utils.process("openapi3.json",
            Collections.EMPTY_MAP,
            openapi.resolve("openapi3.json"));

    Utils.process("SessionGetService.properties",
            Map.of("path", serviceCompPath, "package", pProject.getGroupId() + ".api"),
            service.resolve("SessionGetService.properties"));

    Utils.process("DeliveryOptions.properties", Collections.EMPTY_MAP, eventbus.resolve("DeliveryOptions.properties"));
    Utils.process("EventBusOptions.properties", Collections.EMPTY_MAP, eventbus.resolve("EventBusOptions.properties"));
  }

  void generateGlueConfig(Path pPath, Project pProject) throws IOException, TemplateException {
    for (String environment : pProject.getEnvironments()) {
      Utils.process("glue.config", Map.of("environment", environment), pPath.resolve("src/main/resources/glue-" + environment + ".config"));
    }
  }

  void generateLog4j(Path pPath, Project pProject) throws IOException, TemplateException {
    Utils.process("log4j2.xml", Collections.EMPTY_MAP, pPath.resolve("src/main/resources/log4j2.xml"));
  }
}
