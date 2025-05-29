package com.jwcomptech.commons;

/*-
 * #%L
 * JWCT Commons
 * %%
 * Copyright (C) 2025 JWCompTech
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Lesser Public License for more details.
 * 
 * You should have received a copy of the GNU General Lesser Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/lgpl-3.0.html>.
 * #L%
 */

import ch.qos.logback.classic.Level;
import com.jwcomptech.commons.javafx.dialogs.*;
import com.jwcomptech.commons.logging.Loggers;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import org.apache.maven.api.model.Model;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.stream.XMLStreamException;
import java.io.IOException;

import static com.jwcomptech.commons.utils.DebugUtils.print;

@SuppressWarnings("ClassWithoutConstructor")
public final class Main extends Application {
    /**
     * Application entry point.
     * @param args application command line arguments
     */
    public static void main(String... args) { launch(); }

    @Override
    public void start(@NotNull Stage stage) throws XMLStreamException, IOException, InterruptedException {
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

        String javaVersion = System.getProperty("java.version");
        String javafxVersion = System.getProperty("javafx.version");
        Label l = new Label("Hello, JavaFX " + javafxVersion + ", running on Java " + javaVersion + ".");
        Scene scene = new Scene(new StackPane(l), 640, 480);
        stage.setScene(scene);
        stage.show();

        var result1 = MessageBox.show("Proceed to next step?", "Confirm",
                "", MessageBoxButtons.YesNo,
                MessageBoxIcon.CONFIRMATION,
                MessageBoxDefaultButton.Button1);

        var result2 = MessageBox.builder()
                        .text("Proceed to next step?")
                        .title("Confirm")
                        .buttons(MessageBoxButtons.YesNo)
                        .icon(MessageBoxIcon.CONFIRMATION)
                        .defaultButton(MessageBoxDefaultButton.Button1)
                        .build()
                        .showAndWait();

        print(result1.toString());
        print(result2.toString());

        //        System.exit(exitCode);
    }
}
