package in.erail.cli.af.model;

/**
 *
 * @author vinay
 */
public class Project {

  private String[] environments;
  private String groupId;
  private String artifactId;
  private String version;
  private boolean enableAWSLambda;
  private boolean createHelmChart;

  public String[] getEnvironments() {
    return environments;
  }

  public Project setEnvironments(String[] pEnvironments) {
    this.environments = pEnvironments;
    return this;
  }

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

  public boolean isEnableAWSLambda() {
    return enableAWSLambda;
  }

  public Project setEnableAWSLambda(boolean pEnableAWSLambda) {
    this.enableAWSLambda = pEnableAWSLambda;
    return this;
  }

  public boolean isCreateHelmChart() {
    return createHelmChart;
  }

  public Project setCreateHelmChart(boolean pCreateHelmChart) {
    this.createHelmChart = pCreateHelmChart;
    return this;
  }

}
