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
  private Dependency[] dependencies;

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

  public Dependency[] getDependencies() {
    return dependencies;
  }

  public Project setDependencies(Dependency[] pDependencies) {
    this.dependencies = pDependencies;
    return this;
  }

  public String getVersion() {
    return version;
  }

  public Project setVersion(String pVersion) {
    this.version = pVersion;
    return this;
  }

}
