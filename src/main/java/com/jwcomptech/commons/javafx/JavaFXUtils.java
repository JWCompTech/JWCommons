package com.jwcomptech.commons.javafx;

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

import com.jwcomptech.commons.resources.Resource;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Screen;
import javafx.stage.Window;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Optional;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Supplier;

import static com.jwcomptech.commons.consts.Literals.cannotBeNull;
import static com.jwcomptech.commons.exceptions.ExceptionUtils.throwUnsupportedExForUtilityCls;
import static com.jwcomptech.commons.validators.Preconditions.checkArgumentNotNull;

/**
 * Contains methods to do JavaFX related tasks.
 *
 * @since 1.0.0-alpha
 */
@SuppressWarnings("unused")
public final class JavaFXUtils {
    /**
     * Run the specified Runnable on the JavaFX Application Thread at some
     * unspecified
     * time in the future. This method, which may be called from any thread,
     * will post the Runnable to an event queue and then return immediately to
     * the caller. The Runnables are executed in the order they are posted.
     * A runnable passed into the runLater method will be
     * executed before any Runnable passed into a subsequent call to runLater.
     * If this method is called after the JavaFX runtime has been shutdown, the
     * call will be ignored: the Runnable will not be executed and no
     * exception will be thrown.
     *
     * <p>
     * NOTE: applications should avoid flooding JavaFX with too many
     * pending Runnables. Otherwise, the application may become unresponsive.
     * Applications are encouraged to batch up multiple operations into fewer
     * runLater calls.
     * Additionally, long-running operations should be done on a background
     * thread where possible, freeing up the JavaFX Application Thread for GUI
     * operations.
     * </p>
     *
     * <p>
     * This method must not be called before the FX runtime has been
     * initialized. For standard JavaFX applications that extend
     * {@link Application}, and use either the Java launcher or one of the
     * launch methods in the Application class to launch the application,
     * the FX runtime is initialized by the launcher before the Application
     * class is loaded.
     * For Swing applications that use JFXPanel to display FX content, the FX
     * runtime is initialized when the first JFXPanel instance is constructed.
     * For SWT application that use FXCanvas to display FX content, the FX
     * runtime is initialized when the first FXCanvas instance is constructed.
     * For applications that do not follow any of these approaches, then it is
     * necessary to manually start the JavaFX runtime by calling
     * {@link Platform#startup(Runnable)} once.
     * </p>
     *
     * <p>
     * Memory consistency effects: Actions in a thread prior to submitting a
     * {@code runnable} to this method <i>happen-before</i> actions performed
     * by the runnable in the JavaFX Application Thread.
     * </p>
     *
     * @param runnable the Runnable whose run method will be executed on the
     * JavaFX Application Thread
     * @throws IllegalStateException if the FX runtime has not been initialized
     * @see Application
     */
    @SuppressWarnings("GrazieInspection")
    public static void runLater(final Runnable runnable) {
        Platform.runLater(runnable);
    }

    /**
     * Ensures that the specified task is run using {@link Platform#runLater(Runnable)} and
     * if this method is called on the FX application thread.
     *
     * @param runnable the Runnable to run
     */
    public static void runLaterIfNeeded(final Runnable runnable) {
        if (Platform.isFxApplicationThread()) runnable.run();
        else Platform.runLater(runnable);
    }

    /**
     * Executes the given {@link Runnable} immediately if the current thread is the JavaFX Application Thread;
     * otherwise, submits the task to the JavaFX Application Thread and waits for it to complete.
     * <p>
     * This method ensures that the task is executed in a thread-safe manner and is especially useful
     * when a blocking call is acceptable or required to maintain synchronization.
     * </p>
     *
     * @param runnable the task to execute
     * @throws RuntimeException if the thread is interrupted while waiting
     */
    public static void runAndWait(final @NotNull Runnable runnable) {
        if (Platform.isFxApplicationThread()) {
            runnable.run();
        }

        final CountDownLatch latch = new CountDownLatch(1);

        Platform.runLater(() -> {
            try {
                runnable.run();
            } finally {
                latch.countDown();
            }
        });

        try {
            latch.await();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Interrupted while waiting for FX task", e);
        }
    }

    /**
     * Executes the given {@link Supplier} and returns its result, running immediately if on the JavaFX Application Thread,
     * or blocking and waiting for execution on the JavaFX Application Thread if called from another thread.
     * <p>
     * Useful when code outside the FX thread needs a computed result from an FX-safe operation.
     * </p>
     *
     * @param supplier the task to run
     * @param <T> the result type
     * @return the computed result from the supplier
     * @throws RuntimeException if the thread is interrupted while waiting
     */
    public static <T> T runAndWait(final @NotNull Supplier<T> supplier) {
        if (Platform.isFxApplicationThread()) {
            return supplier.get();
        }

        final AtomicReference<T> result = new AtomicReference<>();
        final CountDownLatch latch = new CountDownLatch(1);

        Platform.runLater(() -> {
            try {
                result.set(supplier.get());
            } finally {
                latch.countDown();
            }
        });

        try {
            latch.await();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Interrupted while waiting for FX task", e);
        }

        return result.get();
    }

    /**
     * Returns true if the calling thread is the JavaFX Application Thread.
     * Use this call to ensure that a given task is being executed
     * (or not being executed) on the JavaFX Application Thread.
     *
     * @return true if running on the JavaFX Application Thread
     */
    public static boolean isFxThread() {
        return Platform.isFxApplicationThread();
    }

    /**
     * Runs the specified Runnable if this method is called on the FX application thread.
     *
     * @param runnable the Runnable to run
     */
    public static void ifFxThread(final Runnable runnable) {
        if(Platform.isFxApplicationThread()) runnable.run();
    }

    /**
     * Runs the specified Runnable if this method is called on the FX application thread
     * and if not runs another Runnable instead.
     *
     * @param ifRunnable the Runnable to run if this method is called on the FX application thread
     * @param elseRunnable the Runnable to run if this method is NOT called on the FX application thread
     */
    public static void ifFxThreadElse(final Runnable ifRunnable,
                                      final Runnable elseRunnable) {
        if(Platform.isFxApplicationThread()) ifRunnable.run();
        else elseRunnable.run();
    }

    /**
     * Returns the value from the specified Supplier if this method is called on the FX application thread.
     *
     * @param supplier the Supplier to run if this method is called on the FX application thread
     * @param <R> the return type of the Supplier
     * @return the result from the Supplier
     */
    public static <R> @NotNull Optional<R> getIfFxThread(final Supplier<R> supplier) {
        if(isFxThread()) return Optional.of(supplier.get());
        return Optional.empty();
    }

    /**
     * Returns the value from the specified Supplier if this method is called on the FX application thread
     * and if not returns the value from another Supplier instead.
     *
     * @param ifSupplier the Supplier to run if this method is called on the FX application thread
     * @param elseSupplier the Supplier to run if this method is NOT called on the FX application thread
     * @param <R> the return type of the Suppliers
     * @return the value from the specified Supplier if this method is called on the FX application thread
     * and if not returns the value from another Supplier instead
     */
    public static <R> R ifFxThreadElseGet(final Supplier<R> ifSupplier,
                                          final Supplier<R> elseSupplier) {
        if(isFxThread()) return ifSupplier.get();
        else return elseSupplier.get();
    }

    /**
     * Checks if the specified FXML file has the "fx:controller" tag defined.
     *
     * @param fxmlPath the path to the file to check
     * @return true if the specified FXML file has the "fx:controller" tag defined
     */
    public static boolean hasFxControllerTag(final @NotNull String fxmlPath) {
        final Resource fxmlFile = Resource.of(fxmlPath);

        return hasFxControllerTag(fxmlFile);
    }

    /**
     * Checks if the specified FXML file has the "fx:controller" tag defined.
     *
     * @param fxmlFile the file to check
     * @return true if the specified FXML file has the "fx:controller" tag defined
     */
    public static boolean hasFxControllerTag(final @NotNull Resource fxmlFile) {
        try (InputStream inputStream = fxmlFile.getInputStream();
             BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {

            String line;
            boolean inComment = false;

            while ((line = reader.readLine()) != null) {
                String trimmed = line.trim();

                // Handle multi-line comment blocks
                if (trimmed.contains("<!--")) {
                    inComment = true;
                }

                if (!inComment && trimmed.contains("fx:controller")) {
                    return true;
                }

                if (trimmed.contains("-->")) {
                    inComment = false;
                }
            }
        } catch (IOException e) {
            throw new UncheckedIOException("Failed to read FXML resource: " + fxmlFile, e);
        }

        return false;
    }

    /**
     * Sets the {@code fx:id} (ID) for the specified JavaFX node.
     *
     * @param node the JavaFX {@link Node}
     * @param id the string ID to set
     */
    public static void setFxId(final @NotNull Node node, final @NotNull String id) {
        node.setId(id);
    }

    /**
     * Looks up a child {@link Node} by its {@code fx:id} within the given {@link Parent}.
     * This performs a lookup using the standard {@code "#id"} syntax.
     *
     * @param root the root node to search from
     * @param fxId the fx:id to find (without the '#' prefix)
     * @param <T> the expected node type
     * @return an {@link Optional} containing the node if found and castable to type T
     */
    @SuppressWarnings("unchecked")
    public static <T extends Node> @NotNull Optional<T> lookupById(final @NotNull Parent root,
                                                                   final @NotNull String fxId) {
        return Optional.ofNullable((T) root.lookup("#" + fxId));
    }

    /**
     * Sets the anchor constraints on the specified node within an {@link AnchorPane}.
     * Any negative or null values are ignored, allowing partial anchor application.
     *
     * @param node the node to anchor
     * @param top the distance from the top edge, or -1 to ignore
     * @param right the distance from the right edge, or -1 to ignore
     * @param bottom the distance from the bottom edge, or -1 to ignore
     * @param left the distance from the left edge, or -1 to ignore
     */
    public static void setAnchors(final @NotNull Node node,
                                  final Double top,
                                  final Double right,
                                  final Double bottom,
                                  final Double left) {
        checkArgumentNotNull(node, cannotBeNull("node"));
        if (top != null && top >= 0) AnchorPane.setTopAnchor(node, top);
        if (right != null && right >= 0) AnchorPane.setRightAnchor(node, right);
        if (bottom != null && bottom >= 0) AnchorPane.setBottomAnchor(node, bottom);
        if (left != null && left >= 0) AnchorPane.setLeftAnchor(node, left);
    }

    /**
     * Attempts to close the window associated with the given {@link Node}'s {@link Scene}.
     * This is useful for closing dialogs, stages, or windows from inside UI logic like button handlers.
     *
     * @param node the node from which to derive the window to close
     */
    public static void closeWindow(final @NotNull Node node) {
        final Window window = node.getScene() != null ? node.getScene().getWindow() : null;
        if (window != null) {
            window.hide();
        }
    }

    /**
     * Centers the given {@link Window} on the primary screen's visible bounds.
     * Can be used to manually position dialogs or stages.
     *
     * @param window the window to center on screen
     */
    public static void centerOnScreen(final @NotNull Window window) {
        Screen screen = Screen.getPrimary();
        Rectangle2D bounds = screen.getVisualBounds();

        window.setX(bounds.getMinX() + (bounds.getWidth() - window.getWidth()) / 2);
        window.setY(bounds.getMinY() + (bounds.getHeight() - window.getHeight()) / 2);
    }

    /** Prevents instantiation of this utility class. */
    private JavaFXUtils() { throwUnsupportedExForUtilityCls(); }
}
