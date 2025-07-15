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

import java.io.Serial;
import java.util.List;

/**
 * An {@link Event} representing some type of action.
 * @since 1.0.0-alpha
 */
@SuppressWarnings("unused")
public class ActionEvent extends Event {
    /** The only valid EventType for the ActionEvent. */
    public static final EventType<ActionEvent> ACTION = new EventType<>(Event.ANY, "ACTION");

    /** Common supertype for all action event types. */
    public static final EventType<ActionEvent> ANY = ACTION;

    @Serial
    private static final long serialVersionUID = -839917085012249791L;

    /**
     * Construct a new {@code ActionEvent} with the specified event target.
     * All ActionEvents have their type set to {@code ACTION}.
     * @param target    the event target to associate with the event
     */
    public ActionEvent(final EventTarget<? extends ActionEvent> target) { super(target, ACTION); }

    /**
     * Construct a new {@code ActionEvent} with the specified event target and type.
     * All ActionEvents have their type set to {@code ACTION}.
     * @param target    the event target to associate with the event
     * @param eventType the event type
     */
    public ActionEvent(final EventTarget<? extends ActionEvent> target,
                       final EventType<? extends Event> eventType) {
        super(target, eventType);
    }

    /**
     * Construct a new {@code ActionEvent} with the specified event target, type and args.
     * All ActionEvents have their type set to {@code ACTION}.
     * @param target    the event target to associate with the event
     * @param eventType the event type
     * @param args arguments to make available to the EventHandler
     */
    public ActionEvent(final EventTarget<? extends ActionEvent> target,
                       final EventType<? extends Event> eventType,
                       final List<Object> args) {
        super(target, eventType, args);
    }

    @Override
    @SuppressWarnings("unchecked")
    public EventType<? extends ActionEvent> getEventType() {
        return (EventType<? extends ActionEvent>) super.getEventType();
    }

    @Override
    public ActionEvent copyFor(final Object newSource, final EventTarget<? extends Event> newTarget) {
        return (ActionEvent) super.copyFor(newSource, newTarget);
    }
}
