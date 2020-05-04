package in.erail.cli.af.model;

/**
 *
 * @author vinay
 */
public class Project {

  private String groupId;
  private String artifactId;
  private String version;
  private String versionAPIFramework;
  private String versionVertx;
  private String versionLog4j;
  private String versionJunit;
  private String versionPluginMavenAssemblyPlugin;
  private String versionPluginMavenSurefirePlugin;
  private String versionPluginMavenJarPlugin;
  private String versionPluginJacocoMavenPlugin;
  private String versionPluginMavenCompilerPlugin;
  private String versionPluginExecMavenPlugin;

  private boolean enableLambda;

  private String[] environments;

  public String getGroupId() {
    return groupId;
  }

  public Project setGroupId(String pGroupId) {
    this.groupId = pGroupId;
    return this;
  }

  public String getArtifactId() {
    return artifactId;
  }

  public Project setArtifactId(String pArtifactId) {
    this.artifactId = pArtifactId;
    return this;
  }

  public String getVersion() {
    return version;
  }

  public Project setVersion(String pVersion) {
    this.version = pVersion;
    return this;
  }

  public String getVersionAPIFramework() {
    return versionAPIFramework;
  }

  public Project setVersionAPIFramework(String pVersionAPIFramework) {
    this.versionAPIFramework = pVersionAPIFramework;
    return this;
  }

  public String getVersionVertx() {
    return versionVertx;
  }

  public Project setVersionVertx(String pVersionVertx) {
    this.versionVertx = pVersionVertx;
    return this;
  }

  public String getVersionLog4j() {
    return versionLog4j;
  }

  public Project setVersionLog4j(String pVersionLog4j) {
    this.versionLog4j = pVersionLog4j;
    return this;
  }

  public String getVersionJunit() {
    return versionJunit;
  }

  public Project setVersionJunit(String pVersionJunit) {
    this.versionJunit = pVersionJunit;
    return this;
  }

  public String getVersionPluginMavenAssemblyPlugin() {
    return versionPluginMavenAssemblyPlugin;
  }

  public Project setVersionPluginMavenAssemblyPlugin(String pVersionPluginMavenAssemblyPlugin) {
    this.versionPluginMavenAssemblyPlugin = pVersionPluginMavenAssemblyPlugin;
    return this;
  }

  public String getVersionPluginMavenSurefirePlugin() {
    return versionPluginMavenSurefirePlugin;
  }

  public Project setVersionPluginMavenSurefirePlugin(String pVersionPluginMavenSurefirePlugin) {
    this.versionPluginMavenSurefirePlugin = pVersionPluginMavenSurefirePlugin;
    return this;
  }

  public String getVersionPluginMavenJarPlugin() {
    return versionPluginMavenJarPlugin;
  }

  public Project setVersionPluginMavenJarPlugin(String pVersionPluginMavenJarPlugin) {
    this.versionPluginMavenJarPlugin = pVersionPluginMavenJarPlugin;
    return this;
  }

  public String getVersionPluginJacocoMavenPlugin() {
    return versionPluginJacocoMavenPlugin;
  }

  public Project setVersionPluginJacocoMavenPlugin(String pVersionPluginJacocoMavenPlugin) {
    this.versionPluginJacocoMavenPlugin = pVersionPluginJacocoMavenPlugin;
    return this;
  }

  public String getVersionPluginMavenCompilerPlugin() {
    return versionPluginMavenCompilerPlugin;
  }

  public Project setVersionPluginMavenCompilerPlugin(String pVersionPluginMavenCompilerPlugin) {
    this.versionPluginMavenCompilerPlugin = pVersionPluginMavenCompilerPlugin;
    return this;
  }

  public String getVersionPluginExecMavenPlugin() {
    return versionPluginExecMavenPlugin;
  }

  public Project setVersionPluginExecMavenPlugin(String pVersionPluginExecMavenPlugin) {
    this.versionPluginExecMavenPlugin = pVersionPluginExecMavenPlugin;
    return this;
  }

  public boolean isEnableLambda() {
    return enableLambda;
  }

  public Project setEnableLambda(boolean pEnableLambda) {
    this.enableLambda = pEnableLambda;
    return this;
  }

  public String[] getEnvironments() {
    return environments;
  }

  public Project setEnvironments(String[] pEnvironments) {
    this.environments = pEnvironments;
    return this;
  }

}
