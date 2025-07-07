package com.jwcomptech.commons.actions;

import com.jwcomptech.commons.base.Validated;
import com.jwcomptech.commons.javafx.FXEventType;
import com.jwcomptech.commons.validators.Condition;
import javafx.event.Event;
import javafx.event.EventHandler;

import java.lang.reflect.Method;
import java.util.*;
import java.util.function.Consumer;

import lombok.*;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.UnmodifiableView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@EqualsAndHashCode(callSuper = true)
@ToString
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class Action<T extends Event> extends Validated {
    public static class ActionExecutionException extends RuntimeException {
        public ActionExecutionException(String message, Throwable cause) {
            super(message, cause);
        }
    }

    private static final Map<Class<?>, Map<FXEventType, HandlerMethods>> handlerCache = new HashMap<>();
    @Getter @Setter
    private static boolean defaultSuppressExceptions = false;
    private static final Logger logger = LoggerFactory.getLogger(Action.class);

    private final List<Consumer<T>> consumers = new ArrayList<>();
    private final List<Runnable> successSubscribers = new ArrayList<>();
    private final List<Consumer<T>> successEventSubscribers = new ArrayList<>();
    private final List<Consumer<Exception>> failureSubscribers = new ArrayList<>();
    private final Set<Class<? extends Exception>> suppressedExceptionTypes = new HashSet<>();
    private Condition condition = Condition.TRUE;
    private boolean suppressExceptions = defaultSuppressExceptions;

    private Object boundTarget;
    private Method getHandlerMethod;
    private Method setHandlerMethod;
    private FXEventType eventType;

    @SuppressWarnings("unchecked")
    public static <T extends Event> @NotNull Action<T> of(final @NotNull FXEventType eventType,
                                                          final @NotNull Object target) {
        HandlerMethods methods = getHandlerMethods(target.getClass(), eventType);
        if (methods == null) {
            throw new UnsupportedOperationException("Target " + target.getClass().getSimpleName()
                    + " does not support handler for " + eventType.getValue().getSimpleName());
        }

        Action<T> action = new Action<T>();
        action.boundTarget = target;
        action.eventType = eventType;
        action.getHandlerMethod = methods.getter();
        action.setHandlerMethod = methods.setter();

        try {
            Object existingHandler = methods.getter().invoke(target);
            if (existingHandler instanceof EventHandler<?> handler) {
                action.consumers.add(e -> ((EventHandler<T>) handler).handle(e));
            }
            action.updateHandler();
        } catch (Exception e) {
            throw new RuntimeException("Failed to bind event handler", e);
        }

        return action;
    }

    public static boolean supports(final FXEventType eventType,
                                   final @NotNull Object target) {
        return getHandlerMethods(target.getClass(), eventType) != null;
    }

    public static @NotNull @UnmodifiableView Set<FXEventType> getSupportedEvents(final @NotNull Object target) {
        Map<FXEventType, HandlerMethods> map =
                handlerCache.computeIfAbsent(target.getClass(), Action::discoverEventHandlers);
        return Collections.unmodifiableSet(map.keySet());
    }

    public static @NotNull @UnmodifiableView List<Action<? extends Event>> ofAllSupported(final @NotNull Object target) {
        Map<FXEventType, HandlerMethods> handlers =
                handlerCache.computeIfAbsent(target.getClass(), Action::discoverEventHandlers);

        List<Action<? extends Event>> actions = new ArrayList<>();

        for (Map.Entry<FXEventType, HandlerMethods> entry : handlers.entrySet()) {
            FXEventType eventType = entry.getKey();
            try {
                Action<? extends Event> action = Action.of(eventType, target);
                actions.add(action);
            } catch (Exception e) {
                throw new RuntimeException("Could not create Action for event type: " + eventType, e);
            }
        }

        return Collections.unmodifiableList(actions);
    }

    @SuppressWarnings("unchecked")
    public static <T extends Event> @NotNull Optional<Action<T>> findForEventType(final Object target,
                                                                                  final FXEventType eventType) {
        return ofAllSupported(target).stream()
                .filter(a -> a.eventType.equals(eventType))
                .map(a -> (Action<T>) a)
                .findFirst();
    }

    private static HandlerMethods getHandlerMethods(Class<?> targetClass,
                                                    FXEventType eventType) {
        Map<FXEventType, HandlerMethods> eventMap =
                handlerCache.computeIfAbsent(targetClass, Action::discoverEventHandlers);
        return eventMap.get(eventType);
    }

    @SuppressWarnings("unchecked")
    private static @NotNull @UnmodifiableView Map<FXEventType, HandlerMethods> discoverEventHandlers(final @NotNull Class<?> cls) {
        Map<String, Method> setters = new HashMap<>();
        Map<String, Method> getters = new HashMap<>();
        Map<FXEventType, HandlerMethods> results = new HashMap<>();

        for (Method method : cls.getMethods()) {
            if (method.getName().startsWith("setOn") &&
                    method.getParameterCount() == 1 &&
                    EventHandler.class.isAssignableFrom(method.getParameterTypes()[0])) {
                String name = method.getName().substring(5);
                setters.put(name, method);
            } else if (method.getName().startsWith("getOn") &&
                    method.getParameterCount() == 0 &&
                    EventHandler.class.isAssignableFrom(method.getReturnType())) {
                String name = method.getName().substring(5);
                getters.put(name, method);
            }
        }

        for (String name : getters.keySet()) {
            Method getter = getters.get(name);
            Method setter = setters.get(name);
            if (setter != null) {
                try {
                    Class<?> returnType = getter.getReturnType();
                    Method handleMethod = returnType.getMethod("handle", Event.class);
                    FXEventType eventType = FXEventType.fromClass(
                            (Class<? extends Event>) handleMethod.getParameterTypes()[0]);
                    results.put(eventType, new HandlerMethods(getter, setter));
                } catch (Exception ignored) {
                }
            }
        }

        return Collections.unmodifiableMap(results);
    }

    @Contract("_ -> this")
    public Action<T> then(final @NotNull Runnable runnable) {
        logger.debug("Adding runnable: {}", runnable.getClass().getName());
        consumers.add(e -> runnable.run());
        updateHandler();
        return this;
    }

    @Contract("_ -> this")
    public Action<T> then(final @NotNull Consumer<T> consumer) {
        logger.debug("Adding consumer: {}", consumer.getClass().getName());
        consumers.add(consumer);
        updateHandler();
        return this;
    }

    @Contract("_ -> this")
    public Action<T> ifCondition(final @NotNull Condition condition) {
        logger.debug("Adding condition: {}", condition.getClass().getName());
        this.condition = condition;
        updateHandler();
        return this;
    }

    @Contract("_ -> this")
    public Action<T> onSuccess(final @NotNull Runnable onSuccess) {
        logger.debug("Adding onSuccess: {}", onSuccess.getClass().getName());
        this.successSubscribers.add(onSuccess);
        return this;
    }

    @Contract("_ -> this")
    public Action<T> onSuccess(final @NotNull Consumer<T> onSuccessWithEvent) {
        logger.debug("Adding onSuccessWithEvent: {}", onSuccessWithEvent.getClass().getName());
        this.successEventSubscribers.add(onSuccessWithEvent);
        return this;
    }

    @Contract("_ -> this")
    public Action<T> onFailure(final @NotNull Consumer<Exception> onFailureHandler) {
        logger.debug("Adding onFailureHandler: {}", onFailureHandler.getClass().getName());
        this.failureSubscribers.add(onFailureHandler);
        return this;
    }

    public Action<T> suppressExceptions(final boolean suppress) {
        logger.debug("Setting exception suppression: {}", suppress);
        this.suppressExceptions = suppress;
        return this;
    }

    @Contract("_ -> this")
    public Action<T> suppressIf(final @NotNull Class<? extends Exception> exceptionType) {
        logger.debug("Adding custom exception suppression: {}", exceptionType.getName());
        this.suppressedExceptionTypes.add(exceptionType);
        return this;
    }

    public boolean execute(final T event) {
        logger.debug("Attempting to execute Action");
        if (condition.reevaluate().hasEvaluatedTrue() && isValid()) {
            logger.debug("Validations passed, executing action...");
            try {
                for (Consumer<T> consumer : consumers) {
                    consumer.accept(event);
                }
                for (Runnable subscriber : successSubscribers) {
                    subscriber.run();
                }
                for (Consumer<T> successEventSubscriber : successEventSubscribers) {
                    successEventSubscriber.accept(event);
                }

                return true;
            } catch (Exception ex) {
                logger.debug("Exception thrown while executing action, " +
                        "notifying failure subscribers", ex);
                for (Consumer<Exception> failureSubscriber : failureSubscribers) {
                    failureSubscriber.accept(ex);
                }
                if (suppressExceptions || suppressedExceptionTypes.contains(ex.getClass())) {
                    logger.warn("Suppressed exception during action execution", ex);
                } else {
                    throw new ActionExecutionException("Action execution failed", ex);
                }
            }
        }

        return false;
    }

    private void updateHandler() {
        if (boundTarget != null && setHandlerMethod != null) {
            logger.debug("Updating event handler...");
            try {
                EventHandler<T> handler = this::execute;
                setHandlerMethod.invoke(boundTarget, handler);
            } catch (Exception e) {
                throw new RuntimeException("Failed to set handler for " + eventType.getValue().getSimpleName(), e);
            }
        }
    }

    record HandlerMethods(Method getter, Method setter) { }
}
