package com.jwcomptech.commons.javafx;

/*-
 * #%L
 * JWCommons
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

import com.jwcomptech.commons.annotations.FeatureComplete;
import com.jwcomptech.commons.annotations.VisibleForTesting;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import lombok.Data;
import lombok.SneakyThrows;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

import static com.jwcomptech.commons.consts.Literals.cannotBeNull;
import static com.jwcomptech.commons.exceptions.ExceptionUtils.throwUnsupportedExForUtilityCls;
import static com.jwcomptech.commons.validators.Preconditions.checkArgumentNotNull;

/**
 * Initializes and manages the JavaFX runtime in environments where no {@link javafx.application.Application}
 * subclass is explicitly used.
 * <p>
 * This utility is useful for headless or library-mode scenarios where JavaFX dialogs or controls
 * are needed without a standalone JavaFX application entry point.
 *
 * @since 1.0.0-alpha
 */
@SuppressWarnings("unused")
@Data
@FeatureComplete(since = "1.0.0-alpha")
public class HeadlessFX {
    private static final AtomicBoolean started = new AtomicBoolean(false);
    private static final AtomicReference<CountDownLatch> shutdownLatch = new AtomicReference<>(new CountDownLatch(1));
    private static final AtomicReference<CountDownLatch> appLatch = new AtomicReference<>(new CountDownLatch(1));
    private static final AtomicReference<FXApplication> application = new AtomicReference<>();
    private static final AtomicReference<Consumer<Stage>> appStartupRunnable = new AtomicReference<>();

    /**
     * Initializes the JavaFX runtime in headless mode (without a {@link javafx.stage.Stage} or scene).
     * <p>
     * This method is safe to call multiple times and will only initialize JavaFX once.
     * <p>
     * Use this when you don't need to display any GUI elements, but still require JavaFX runtime access.
     */
    public static void initialize() {
        if (!isInitialized()) {
            Thread fxInit = new Thread(() -> {
                try {
                    Application.launch(HeadlessFXApp.class);
                } catch (IllegalStateException ignored) {
                    // Already initialized in another context
                }
            }, "JavaFX FXLauncher Thread");

            fxInit.setDaemon(true);
            fxInit.start();

            try {
                HeadlessFXApp.waitForStart(); // waits until FX is ready
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new RuntimeException("FX initialization interrupted", e);
            }

            started.set(true);
        }
    }

    public static class HeadlessFXApp extends Application {
        private static final CountDownLatch initLatch = new CountDownLatch(1);

        @Override
        public void start(Stage primaryStage) {
            System.setProperty("jwcommons.javafx.name", getClass().getSimpleName());
            Thread.currentThread().setName("JavaFX-HeadlessFX-Thread");

            Platform.setImplicitExit(false);

            // Create invisible owner stage so dialogs have a window
            Stage hiddenOwner = new Stage();
            hiddenOwner.initStyle(StageStyle.UTILITY);
            hiddenOwner.setOpacity(0);
            hiddenOwner.setWidth(0);
            hiddenOwner.setHeight(0);
            hiddenOwner.setX(-10000); // offscreen
            hiddenOwner.setY(-10000);
            hiddenOwner.show();

            // Mark FX as initialized
            initLatch.countDown();
        }

        public static void waitForStart() throws InterruptedException {
            initLatch.await();
        }
    }

    /**
     * Initializes the JavaFX runtime in application mode, providing access to a {@link javafx.stage.Stage}
     * through a startup callback.
     * <p>
     * This method will launch the JavaFX application thread, call the provided startup consumer with the
     * primary stage, and then return.
     * <p>
     * If JavaFX is already initialized, this method will do nothing.
     *
     * @param startup A consumer that receives the primary {@link Stage} once JavaFX starts. Can be {@code null}.
     * @param args Optional command-line arguments passed to the JavaFX runtime.
     */
    @SneakyThrows
    public static void initializeWithUI(final @Nullable Consumer<Stage> startup, final String... args) {
        if (isInitialized()) return;

        if (startup == null) {
            initialize();
        } else {
            appStartupRunnable.set(startup);
            Application.launch(FXApplication.class, args);

            try {
                appLatch.get().await();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new RuntimeException(e);
            }

            // Sets the name of the name of this application based on the Application class.
            Platform.runLater(() -> {
                System.setProperty("jwcommons.javafx.name", HeadlessFX.class.getSimpleName());
                Thread.currentThread().setName("JavaFX-HeadlessFX-Thread");
            });
        }
    }

    /**
     * Gracefully shuts down the JavaFX platform using {@link Platform#exit()}.
     * <p>
     * This method blocks until shutdown is complete (up to 5 seconds) and resets the internal state
     * so the platform can be reinitialized if needed.
     * <p><b>Note:</b> JavaFX is not guaranteed to support restart in all environments.
     *
     * @return {@code true} if shutdown completed, {@code false} if JavaFX was not initialized.
     */
    public static boolean exit() {
        if (!isInitialized()) return false;
        Platform.exit();

        try {
            //noinspection ResultOfMethodCallIgnored
            shutdownLatch.get().await(5, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException(e);
        }

        shutdownLatch.set(new CountDownLatch(1));
        appLatch.set(new CountDownLatch(1));
        started.set(false);
        appStartupRunnable.set(null);
        return true;
    }

    /**
     * Checks if the JavaFX runtime has been initialized.
     *
     * @return {@code true} if JavaFX has started, {@code false} otherwise.
     */
    public static boolean isInitialized() {
        return started.get();
    }

    /**
     * Checks whether the current thread is the JavaFX Application Thread.
     * <p>
     * Useful for verifying whether UI-related operations can be performed directly.
     *
     * @return {@code true} if running on the FX Application Thread, {@code false} otherwise.
     */
    public static boolean isFxThread() {
        return Platform.isFxApplicationThread();
    }

    /**
     * Schedules the provided task to run on the JavaFX Application Thread using {@link Platform#runLater(Runnable)}.
     * <p>
     * If JavaFX is not yet initialized, it will be initialized automatically in headless mode.
     *
     * @param task The task to run. Must not be {@code null}.
     */
    public static void runLater(Runnable task) {
        checkArgumentNotNull(task, cannotBeNull("task"));
        initialize(); // Ensure FX is started

        Platform.runLater(task);
    }

    /**
     * Runs the provided task immediately if already on the JavaFX Application Thread, otherwise schedules
     * it via {@link Platform#runLater(Runnable)}.
     * <p>
     * If JavaFX is not initialized, it will be started in headless mode automatically.
     *
     * @param task The task to execute. Must not be {@code null}.
     */
    public static void runLaterIfNeeded(final Runnable task) {
        checkArgumentNotNull(task, cannotBeNull("task"));
        initialize(); // Ensure FX is started

        if (isFxThread()) {
            System.out.println("Running directly on FX thread");
            task.run();
        }
        else {
            System.out.println("Queuing on FX thread via runLater");
            Platform.runLater(() -> {
                System.out.println("Running on FX thread");
                task.run();
            });
        }
    }

    /**
     * Runs the given task on the JavaFX Application Thread and blocks the current thread until it's complete.
     * <p>
     * If called on the FX thread, the task runs immediately. Otherwise, it is scheduled and the method waits.
     * <p>
     * JavaFX will be initialized in headless mode if not already started.
     *
     * @param task The task to run. Must not be {@code null}.
     */
    public static void runAndWait(final Runnable task) {
        checkArgumentNotNull(task, cannotBeNull("task"));
        initialize(); // Ensure FX is started

        if (isFxThread()) task.run();
        else {
            CountDownLatch latch = new CountDownLatch(1);
            Platform.runLater(() -> {
                try {
                    task.run();
                } finally {
                    latch.countDown();
                }
            });
            try {
                latch.await();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * Returns the underlying {@link FXApplication} instance, if JavaFX was initialized in application mode.
     * <p>
     * This is primarily intended for internal or testing use.
     *
     * @return The FXApplication instance or {@code null} if not initialized with a startup {@link Stage}.
     */
    @VisibleForTesting
    @SuppressWarnings("ClassEscapesDefinedScope")
    public static @Nullable FXApplication getApplication() {
        return application.get();
    }

    public static abstract class FXController {
        // Generic lifecycle and stage support
    }

    public static abstract class FXMLController extends FXController {
        // Auto FXML loading magic
    }

    private static final class FXApplication extends Application {

        /**
         * Called by the JavaFX runtime after initialization. Stores the application reference and invokes
         * the user-defined startup routine with the provided {@link Stage}.
         *
         * @param primaryStage The primary stage of the JavaFX application.
         */
        @Override
        public void start(@NotNull final Stage primaryStage) {
            application.set(this);
            try {
                Optional.ofNullable(appStartupRunnable.get())
                        .ifPresent(r -> r.accept(primaryStage));
            } finally {
                appLatch.get().countDown();
            }
        }

        /**
         * Called by the JavaFX runtime during shutdown. Triggers internal cleanup logic.
         *
         */
        @Override
        public void stop() {
            // This is called when the JavaFX runtime is about to shut down.
            shutdownLatch.get().countDown();
        }
    }

    /** Prevents instantiation of this utility class. */
    private HeadlessFX() { throwUnsupportedExForUtilityCls(); }
}
