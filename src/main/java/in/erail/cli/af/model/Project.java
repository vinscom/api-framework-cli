package in.erail.cli.af.model;

import lombok.Builder;
import lombok.Data;

/**
 *
 * @author vinay
 */
@Data
@Builder
public class Project {

  private String[] environments;
  private String groupId;
  private String artifactId;
  private String version;
  private boolean enableAWSLambda;
  private boolean createHelmChart;

}
