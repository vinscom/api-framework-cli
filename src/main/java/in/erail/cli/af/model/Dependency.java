package in.erail.cli.af.model;

/**
 *
 * @author vinay
 */
public class Dependency {

  private String groupid;
  private String artifactid;
  private String version;
  private String type;
  private String scope;
  private String classifier;

  public String getGroupid() {
    return groupid;
  }

  public void setGroupid(String pGroupid) {
    this.groupid = pGroupid;
  }

  public String getArtifactid() {
    return artifactid;
  }

  public void setArtifactid(String pArtifactid) {
    this.artifactid = pArtifactid;
  }

  public String getVersion() {
    return version;
  }

  public void setVersion(String pVersion) {
    this.version = pVersion;
  }

  public String getType() {
    return type;
  }

  public void setType(String pType) {
    this.type = pType;
  }

  public String getScope() {
    return scope;
  }

  public void setScope(String pScope) {
    this.scope = pScope;
  }

  public String getClassifier() {
    return classifier;
  }

  public void setClassifier(String pClassifier) {
    this.classifier = pClassifier;
  }

}
