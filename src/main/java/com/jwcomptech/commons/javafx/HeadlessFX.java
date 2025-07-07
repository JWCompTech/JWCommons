package com.jwcomptech.commons.javafx;

import com.jwcomptech.commons.annotations.FeatureComplete;
import com.jwcomptech.commons.annotations.VisibleForTesting;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;
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
            Platform.startup(() -> {
                started.set(true);
                System.setProperty("jwcommons.javafx.name", HeadlessFX.class.getSimpleName());
                Thread.currentThread().setName("JavaFX-HeadlessFX-Thread");
            });
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

        if (isFxThread()) task.run();
        else Platform.runLater(task);
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
