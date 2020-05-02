package in.erail.cli.af;

import picocli.CommandLine;
import picocli.CommandLine.Command;
import java.util.concurrent.Callable;

@Command(
        name = "af", 
        mixinStandardHelpOptions = true, 
        version = "API Framework 4.0",
        subcommands = {
          Create.class
        }
)
public class APIFrameworkCLI implements Callable<Integer> {

  public static void main(String... args) {
    int exitCode = new CommandLine(new APIFrameworkCLI()).execute(args);
    System.exit(exitCode);
  }

  @Override
  public Integer call() throws Exception {
    return 0;
  }
}
