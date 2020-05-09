package in.erail.cli.af;

import picocli.CommandLine;
import picocli.CommandLine.Command;
import java.util.concurrent.Callable;

@Command(
        name = "af", 
        mixinStandardHelpOptions = true, 
        version = "1.1",
        subcommands = {
          Create.class
        }
)
public class APIFrameworkCLI implements Callable<Integer> {

  public static void main(String... args) {
    int exitCode = new CommandLine(new APIFrameworkCLI())
            .setUsageHelpLongOptionsMaxWidth(30)
            .execute(args);
    System.exit(exitCode);
  }

  @Override
  public Integer call() throws Exception {
    CommandLine.usage(this, System.out);
    return 0;
  }
}
