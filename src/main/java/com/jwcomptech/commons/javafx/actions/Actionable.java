package com.jwcomptech.commons.javafx.actions;

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

import com.jwcomptech.commons.javafx.FXEventType;
import javafx.event.ActionEvent;
import javafx.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.UnmodifiableView;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

@SuppressWarnings("unused")
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
