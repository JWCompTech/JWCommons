package com.jwcomptech.commons.events;

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

import com.jwcomptech.commons.utils.SingletonManager;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Allows global event handler registration and forwards received
 * events to the appropriate registered event handlers.
 * @since 0.0.1
 */
@SuppressWarnings("unused")
public final class EventManager {
    private final Map<String, Event> events = new HashMap<>();

    private EventManager() { }

    public static EventManager getInstance() {
        return SingletonManager.getInstance(EventManager.class, EventManager::new);
    }

    @Contract("null, _, _ -> fail")
    public <T extends Event> @NotNull T registerNewEvent(final String eventName,
                                                         final Class<T> classRef,
                                                         final EventType<? extends T> eventType)
            throws Exception {
        return registerNewEvent(eventName, classRef, new EventTarget<>(), eventType);
    }

    @Contract("null, _, _, _ -> fail")
    public <T extends Event> @NotNull T registerNewEvent(final String eventName,
                                                         final Class<T> classRef,
                                                         final EventType<? extends T> eventType,
                                                         final List<Object> args)
            throws Exception {
        return registerNewEvent(eventName, classRef, new EventTarget<>(), eventType, args);
    }

    @Contract("null, _, _, _ -> fail")
    public <T extends Event> @NotNull T registerNewEvent(final String eventName,
                                                         final Class<T> classRef,
                                                         final EventTarget<? extends T> target,
                                                         final EventType<? extends T> eventType)
            throws Exception {
        return registerNewEvent(eventName, classRef, target, eventType, new ArrayList<>());
    }

    @Contract("null, _, _, _, _ -> fail")
    @SuppressWarnings("MethodWithTooExceptionsDeclared")
    public <T extends Event> @NotNull T registerNewEvent(final String eventName,
                                                         final Class<T> classRef,
                                                         final EventTarget<? extends T> target,
                                                         final EventType<? extends T> eventType,
                                                         final List<Object> args)
            throws IllegalAccessException, InvocationTargetException, InstantiationException, NoSuchMethodException {
        if (eventName == null || eventName.trim().isEmpty()) {
            throw new IllegalArgumentException("Event name cannot be null!");
        }
        if (classRef == null) {
            throw new IllegalArgumentException("Event class ref cannot be null!");
        }
        if (target == null) {
            throw new IllegalArgumentException("Event target cannot be null!");
        }
        if (eventType == null) {
            throw new IllegalArgumentException("Event type cannot be null!");
        }
        if(args == null) {
            throw new IllegalArgumentException("Event Args cannot be null!");
        }

        final Constructor<T> constructor = classRef.getConstructor(EventTarget.class, EventType.class, List.class);

        final T event = constructor.newInstance(target, eventType, args);

        events.put(eventName, event);

        return event;
    }

    @SuppressWarnings("unchecked")
    public <T extends Event> T getEvent(final String eventName) { return (T) events.get(eventName); }

    /**
     * Fires the specified event with the specified source.
     * @param eventName the event to fire
     * @param source the event source which sent the event
     */
    public void fireEvent(final String eventName,
                          final Object source) {
        getEvent(eventName).fireEvent(source);
    }

    /**
     * Fires the specified event with the specified source and args.
     * @param eventName the event to fire
     * @param source the event source which sent the event
     * @param args a list of parameters to pass to the EventHandler
     */
    public void fireEvent(final String eventName,
                          final Object source,
                          final Object... args) {
        getEvent(eventName).fireEvent(source, args);
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) return true;

        if (obj == null || getClass() != obj.getClass()) return false;

        final EventManager that = (EventManager) obj;

        return new EqualsBuilder()
                .append(events, that.events)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(events)
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("events", events)
                .toString();
    }
}
