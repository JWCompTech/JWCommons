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
import com.jwcomptech.commons.info.HWInfo;
import com.jwcomptech.commons.info.OS;
import com.jwcomptech.commons.info.OSInfo;
import com.jwcomptech.commons.info.os.WindowsOSEx;
import com.jwcomptech.commons.javafx.HeadlessFX;
import com.jwcomptech.commons.javafx.controls.FXButtonTypeGroup;
import com.jwcomptech.commons.javafx.dialogs.MessageBox;
import com.jwcomptech.commons.javafx.dialogs.MessageBoxDefaultButton;
import com.jwcomptech.commons.javafx.dialogs.MessageBoxIcon;
import com.jwcomptech.commons.logging.JWLogger;
//import com.jwcomptech.commons.resources.POMManager;
import com.jwcomptech.commons.utils.DateTimeUtils;
import com.jwcomptech.commons.validators.Condition;
import com.jwcomptech.commons.webapis.APIManager;
import com.jwcomptech.commons.webapis.GitHubAPI;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
//import org.apache.maven.api.model.Model;
import org.kohsuke.github.GHUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

import static com.jwcomptech.commons.utils.osutils.ExecCommand.runNewCmd;

@SuppressWarnings("ClassWithoutConstructor")
public final class Main extends Application {
    /**
     * Application entry point.
     * @param args application command line arguments
     */
    public static void main(final String... args) throws IOException, InterruptedException {
        final Logger logger = LoggerFactory.getLogger(Main.class);
        JWLogger.of(Main.class.getPackage()).config()
                .useLimitedConsole(Level.INFO);

//        final Model pom = POMManager.getInstance().getInternalPOM();

//        logger.info("--------------------------");
//        logger.info("{} v{}", pom.getName(), pom.getVersion());
//        logger.info("--------------------------");
//        logger.info("");
//        logger.info("-----OS Info Test-----");
//        logger.info(runNewCmd("ipconfig").getFirstResult());
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
//        GitHubAPI github = apiManager.GITHUB.login();
//        GHUser user = github.getUser("jlwisedev");
//        logger.info(user.getName());
//        logger.info(DateTimeUtils.formatted(user.getUpdatedAt()));
//        logger.info(String.valueOf(github.getMyRepositories()));

//        final Arguments app = new Arguments();
//        final CommandLine cmd = new CommandLine(app).setUsageHelpAutoWidth(true);
//        app.setCmd(cmd);
//        final int exitCode = cmd.execute(args);

//        final String javaVersion = System.getProperty("java.version");
//        final String javafxVersion = System.getProperty("javafx.version");
//        final Label label = new Label("Hello, JavaFX " + javafxVersion + ", running on Java " + javaVersion + ".");
//        final Scene scene = new Scene(new StackPane(label), 640, 480);
//        stage.setScene(scene);
//        primaryStage.show();

        //launch(args);

        HeadlessFX.runLaterIfNeeded(() -> {
            System.out.println("Showing Message Box...");
//            final var result1 = MessageBox.show("Proceed to next step?", "Confirm",
//                    "", FXButtonTypeGroup.YesNo,
//                    MessageBoxIcon.CONFIRMATION,
//                    MessageBoxDefaultButton.Button1);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.initModality(Modality.NONE);
            alert.setTitle("Test");
            alert.setContentText("Dialog with no modality");
            alert.show();
        });


//
//        final var result2 = MessageBox.builder()
//                        .withText("Proceed to next step?")
//                        .withTitle("Confirm")
//                        .withButtons(FXButtonTypeGroup.YesNo)
//                        .withIcon(MessageBoxIcon.CONFIRMATION)
//                        .withDefaultButton(MessageBoxDefaultButton.Button1)
//                        .build()
//                        .showAndWait();
//
//        print(result1.toString());
//        print(result2.toString());

//        ExceptionUtils.assertThrownException(() -> {
//            throw new IllegalStateException();
//        }, IllegalStateException.class);
//
//        test(1);

//        ServerInstallWizard wizard = new ServerInstallWizard();
//        wizard.initialize();
//
//        stage.setTitle("Installation Wizard");
//        stage.setScene(new Scene(wizard.getContent()));
//        stage.show();

//        LoginDialog.Builder builder = LoginDialog.builder();
//        LoginDialog dialog = builder.build();
//
//        ResourceManager manager = ResourceManager.getInstance();
//        var resources = manager.getResources();
//        var resultObj = dialog.showAndWait();
//        if(resultObj.isPresent()) {
//            var result = resultObj.get();
//            var username = result.getDataValue(String.class, "username");
//            var password = result.getDataValue(String.class, "password");
//            if(username.isPresent() && password.isPresent()) {
//                print(username.get());
//                print(password.get());
//            }
//
//        } else {
//            print("Login Canceled!");
//        }

//        ServerInstallWizard wizard = new ServerInstallWizard();
//        wizard.show();

        int exitCode = 0;
        System.exit(exitCode);
    }

    @Override
    public void start(final Stage primaryStage) throws Exception {
        final var result1 = MessageBox.show("Proceed to next step?", "Confirm",
                "", FXButtonTypeGroup.YesNo,
                MessageBoxIcon.CONFIRMATION,
                MessageBoxDefaultButton.Button1);
        System.out.println(result1.getButtonType().getText());
    }

//    private int test(final Integer myNumber) {
//
//        DebugUtils.print("");
//        return myNumber + 1;
//    }
}
