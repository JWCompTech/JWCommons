package com.jwcomptech.shared;

import ch.qos.logback.classic.Level;
import com.jwcomptech.shared.logging.Loggers;
import org.apache.maven.api.model.Model;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.stream.XMLStreamException;
import java.io.IOException;

import static com.jwcomptech.shared.Settings.APP_VERSION;

@SuppressWarnings("ClassWithoutConstructor")
public final class Main {
    /**
     * Application entry point.
     * @param args application command line arguments
     */
    public static void main(String... args) throws XMLStreamException, IOException, InterruptedException {
        final Logger logger = LoggerFactory.getLogger(Main.class);
        Loggers.RootPackage
                .setName(Main.class.getPackage().getName())
                .enableLimitedConsole(Level.INFO);

        Model pom = POMManager.getInstance().getInternalPOM();

        logger.info("--------------------------");
        logger.info("{} v{}", pom.getName(), pom.getVersion());
        logger.info("--------------------------");
        logger.info("");
//        logger.info("-----OS Info Test-----");
////        logger.info(runNewCmd("ipconfig").getFirstResult());
//        logger.info("Name: {}", OSInfo.OS.getName());
//        logger.info("Name Expanded: {}", OSInfo.OS.getNameExpanded());
//        logger.info("OS Type: {}", OSInfo.getOSType());
//        logger.info("Manufacturer: {}", OSInfo.getManufacturer());
//        logger.info("Version: {}", OSInfo.getVersion());
//        logger.info("Arch: {}", OSInfo.getBitNumber());
//        logger.info("Arch String: {}", OSInfo.getBitString());
//        logger.info(HWInfo.OEM.ProductName().get());
//        //TODO: FIXME
//        logger.info(WindowsOSEx.Activation.getStatusFromSLMGR().get());
//        Condition condition = Condition.of(() -> OS.isWindows);
//        logger.info("Result: {}", condition.evaluate().getResult());

//        APIManager apiManager = APIManager.getInstance();
//        GitHubApi github = apiManager.GITHUB.login();
//        GHUser user = github.getUser("jlwisedev");
//        logger.info(user.getName());
//        logger.info(DateTimeUtils.formatted(user.getUpdatedAt()));
//        logger.info(String.valueOf(github.getMyRepositories()));

//        final Arguments app = new Arguments();
//        final CommandLine cmd = new CommandLine(app).setUsageHelpAutoWidth(true);
//        app.setCmd(cmd);
//        final int exitCode = cmd.execute(args);
//        System.exit(exitCode);
    }
}