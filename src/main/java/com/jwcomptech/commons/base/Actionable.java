package com.jwcomptech.commons.base;

import com.jwcomptech.commons.actions.Action;
import com.jwcomptech.commons.javafx.FXEventType;
import javafx.event.ActionEvent;
import javafx.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.UnmodifiableView;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

public abstract class Actionable {
    private final Map<String, Map<FXEventType, Action<? extends Event>>> actions = new LinkedHashMap<>();

    @SuppressWarnings("unchecked")
    public <T extends Event> Optional<Action<T>> getEventAction(final String name, final FXEventType type) {
        if(actions.containsKey(name)) {
            final Map<FXEventType, Action<? extends Event>> map = actions.get(name);
            if(map.containsKey(type)) {
                return Optional.of((Action<T>) map.get(type));
            }
        }

        return Optional.empty();
    }

    public @UnmodifiableView Map<FXEventType, Action<? extends Event>> getEventActions(final String name) {
        if(actions.containsKey(name)) {
            return Collections.unmodifiableMap(actions.get(name));
        }

        else return Collections.unmodifiableMap(new LinkedHashMap<>());
    }

    @SuppressWarnings("unchecked")
    public <T extends Event> Action<T> addEventAction(
            final String name, final @NotNull FXEventType eventType, final @NotNull Object target) {

        final var subActions = actions.computeIfAbsent(name, k -> new LinkedHashMap<>());
        return (Action<T>) subActions.computeIfAbsent(eventType, et -> Action.of(eventType, target));
    }

    public Action<ActionEvent> addOnAction(final String name, final @NotNull Object target) {
        return addEventAction(name, FXEventType.ACTION, target);
    }
}
