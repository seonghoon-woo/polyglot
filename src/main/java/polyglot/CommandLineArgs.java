package polyglot;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;
import com.google.common.net.HostAndPort;

/** Provides easy access to the arguments passed on the commmand line. */
public class CommandLineArgs {
  private static final Logger logger = LoggerFactory.getLogger(CommandLineArgs.class);
  private static final String USAGE = "polyglot call <host> <port> <protoclass> <service> <method>";

  @Option(name = "--proto_class")
  private String protoClassArg;

  @Option(name = "--service")
  private String serviceArg;

  @Option(name = "--method")
  private String methodArg;

  @Option(name = "--full_method")
  private String fullMethodArg;

  @Option(name = "--endpoint")
  private String endpointArg;

  @Option(name = "--proto_root")
  private String protoRootArg;

  private HostAndPort hostAndPort;
  private ProtoMethodName grpcMethodName;

  public static CommandLineArgs parse(String[] args) {
    CommandLineArgs result = new CommandLineArgs();
    CmdLineParser parser = new CmdLineParser(result);
    try {
      parser.parseArgument(args);
    } catch (CmdLineException e) {
      throw new IllegalArgumentException("Unable to parse command line flags", e);
    }
    result.initialize();
    return result;
  }

  private CommandLineArgs() {
  }

  private void initialize() {
    Preconditions.checkNotNull(endpointArg);
    Preconditions.checkNotNull(fullMethodArg);

    hostAndPort = HostAndPort.fromString(endpointArg);
    grpcMethodName = ProtoMethodName.parseFullGrpcMethodName(fullMethodArg);
  }

  public String host() {
    return hostAndPort.getHostText();
  }

  public int port() {
    return hostAndPort.getPort();
  }

  public String service() {
    return serviceArg;
  }

  public String method() {
    return methodArg;
  }

  public String protoClass() {
    return protoClassArg;
  }

  public String fullMethod() {
    return fullMethodArg;
  }

  public Path protoRoot() {
    return Paths.get(protoRootArg).toAbsolutePath();
  }

  public ProtoMethodName grpcMethodName() {
    return grpcMethodName;
  }
}