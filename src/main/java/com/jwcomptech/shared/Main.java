package com.jwcomptech.shared;

import ch.qos.logback.classic.Level;
import com.jwcomptech.shared.info.HWInfo;
import com.jwcomptech.shared.info.OS;
import com.jwcomptech.shared.info.OSInfo;
import com.jwcomptech.shared.logging.Loggers;
import org.apache.maven.api.model.Model;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.stream.XMLStreamException;
import java.io.IOException;
import java.util.Properties;

import static com.jwcomptech.shared.utils.Misc.getMavenModel;

@SuppressWarnings("ClassWithoutConstructor")
public final class Main {
    /**
     * Application entry point.
     * @param args application command line arguments
     */
    public static void main(String[] args) throws XMLStreamException, IOException, InterruptedException {
        final Logger logger = LoggerFactory.getLogger(Main.class);
        Loggers.RootPackage
                .setName(Main.class.getPackage().getName())
                .enableLimitedConsole(Level.INFO);

        Properties properties = new Properties();
        properties.load(Main.class.getResourceAsStream("/pom.properties"));
        String artifactId = (String) properties.get("artifactId");
        String groupId = (String) properties.get("groupId");
        String version = (String) properties.get("version");

        Model model = getMavenModel(artifactId);

        logger.info("JWCT Shared Library v{}", model.getVersion());
        //logger.info("JWCT Shared Library v{}", APP_VERSION);
        logger.info("");
        logger.info("-----OS Info Test-----");
//        logger.info(runNewCmd("ipconfig").getFirstResult());
        logger.info("Name: {}", OSInfo.OS.getName());
        logger.info("Name Expanded: {}", OSInfo.OS.getNameExpanded());
        logger.info("OS Type: {}", OSInfo.getOSType());
        logger.info("Manufacturer: {}", OSInfo.getManufacturer());
        logger.info("Version: {}", OSInfo.getVersion());
        logger.info("Arch: {}", OSInfo.getBitNumber());
        logger.info("Arch String: {}", OSInfo.getBitString());
        logger.info(HWInfo.OEM.ProductName().get());
        //TODO: FIXME
        //logger.info(WindowsOSEx.Activation.getStatusFromSLMGR().get());
        Condition condition = Condition.of(() -> OS.isWindows);
        logger.info("Result: {}", condition.evaluate().getResult());


//        final Arguments app = new Arguments();
//        final CommandLine cmd = new CommandLine(app).setUsageHelpAutoWidth(true);
//        app.setCmd(cmd);
//        final int exitCode = cmd.execute(args);
//        System.exit(exitCode);
    }
}