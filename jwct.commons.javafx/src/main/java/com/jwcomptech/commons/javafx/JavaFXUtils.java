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

import javafx.application.Application;
import javafx.application.Platform;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.function.Supplier;

import static com.jwcomptech.commons.exceptions.ExceptionUtils.throwUnsupportedExForUtilityCls;

/**
 * Contains methods to do JavaFX related tasks.
 * @since 0.0.1
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
     *
     * @throws IllegalStateException if the FX runtime has not been initialized
     *
     * @see Application
     */
    @SuppressWarnings("GrazieInspection")
    public static void runLater(final Runnable runnable) {
        Platform.runLater(runnable);
    }

    /**
     * Ensures that the specified task is run using {@link Platform#runLater(Runnable)} and
     * if this method is called on the FX application thread.
     * @param runnable the Runnable to run
     */
    public static void runLaterCheck(final Runnable runnable) {
        if (Platform.isFxApplicationThread()) runnable.run();
        else Platform.runLater(runnable);
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
     * Ensures that the specified task is run using {@link Platform#runLater(Runnable)} and
     * if this method is called on the FX application thread, runs an additional Runnable.
     * @param task the task to run
     * @param ifRunnable a Runnable to run if this method is called on the FX application thread
     */
    public static void ifFxThreadCheck(final Runnable task,
                                       final Runnable ifRunnable) {
        runLaterCheck(task);
        if(Platform.isFxApplicationThread()) ifRunnable.run();
    }

    /**
     * Runs the specified Runnable if this method is called on the FX application thread.
     * @param runnable the Runnable to run
     */
    public static void ifFxThread(final Runnable runnable) {
        if(Platform.isFxApplicationThread()) runnable.run();
    }

    /**
     * Runs the specified Runnable if this method is called on the FX application thread
     * and if not runs another Runnable instead.
     * @param ifRunnable the Runnable to run if this method is called on the FX application thread
     * @param elseRunnable the Runnable to run if this method is NOT called on the FX application thread
     */
    public static void ifFxThreadElse(final Runnable ifRunnable,
                                      final Runnable elseRunnable) {
        if(Platform.isFxApplicationThread()) ifRunnable.run();
        else elseRunnable.run();
    }

    /**
     * Runs the specified Runnable if this method is called on the FX application thread,
     * runs the specified Runnable if this method is called on the FX application thread,
     * and if not runs another Runnable instead
     * @param task the task to run
     * @param ifRunnable the Runnable to run if this method is called on the FX application thread
     * @param elseRunnable the Runnable to run if this method is NOT called on the FX application thread
     */
    public static void ifFxThreadElseCheck(final Runnable task,
                                           final Runnable ifRunnable,
                                           final Runnable elseRunnable) {
        runLaterCheck(task);
        if(Platform.isFxApplicationThread()) ifRunnable.run();
        else elseRunnable.run();
    }

    /**
     * Returns the value from the specified Supplier if this method is called on the FX application thread.
     * @param <R> the return type of the Supplier
     * @param supplier the Supplier to run if this method is called on the FX application thread
     */
    public static <R> @NotNull Optional<R> ifFxThreadGet(final Supplier<R> supplier) {
        if(isFxThread()) return Optional.of(supplier.get());
        return Optional.empty();
    }

    /**
     * Returns the value from the specified Supplier if this method is called on the FX application thread
     * and if not returns the value from another Supplier instead.
     * @param <R> the return type of the Suppliers
     * @param ifSupplier the Supplier to run if this method is called on the FX application thread
     * @param elseSupplier the Supplier to run if this method is NOT called on the FX application thread
     * @return the value from the specified Supplier if this method is called on the FX application thread
     * and if not returns the value from another Supplier instead
     */
    public static <R> R ifFxThreadElseGet(final Supplier<R> ifSupplier,
                                          final Supplier<R> elseSupplier) {
        if(isFxThread()) return ifSupplier.get();
        else return elseSupplier.get();
    }

    /** Prevents instantiation of this utility class. */
    private JavaFXUtils() { throwUnsupportedExForUtilityCls(); }
}
