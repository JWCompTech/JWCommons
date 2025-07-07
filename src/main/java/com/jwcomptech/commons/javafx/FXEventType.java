package com.jwcomptech.commons.javafx;

import com.jwcomptech.commons.enums.BaseEnum;
import javafx.event.*;
import javafx.scene.input.*;
import javafx.scene.media.MediaErrorEvent;
import javafx.scene.web.WebEvent;
import javafx.stage.WindowEvent;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

/**
 * Enum representing common JavaFX event types, each associated with its
 * corresponding {@link javafx.event.Event} subclass.
 * <p>
 * This can be used to work with event types in a type-safe and centralized way,
 * especially when dynamically handling events.
 */

@AllArgsConstructor
@Getter
@ToString
public enum FXEventType implements BaseEnum<Class<? extends Event>> {

    /**
     * Represents {@link ActionEvent}, typically used by buttons, menus, etc.
     */
    ACTION(ActionEvent.class),

    /**
     * Represents {@link WindowEvent}, used for window showing, hiding, closing, etc.
     */
    WINDOW(WindowEvent.class),

    /**
     * Represents {@link MouseEvent}, used for mouse clicks, movements, enters, exits, etc.
     */
    MOUSE(MouseEvent.class),

    /**
     * Represents {@link ScrollEvent}, typically used for scroll gestures or mouse wheel movement.
     */
    SCROLL(ScrollEvent.class),

    /**
     * Represents {@link SwipeEvent}, used for touchscreen swipe gestures.
     */
    SWIPE(SwipeEvent.class),

    /**
     * Represents {@link RotateEvent}, used for rotation gestures.
     */
    ROTATE(RotateEvent.class),

    /**
     * Represents {@link ZoomEvent}, used for pinch-zoom gestures.
     */
    ZOOM(ZoomEvent.class),

    /**
     * Represents {@link DragEvent}, used during drag-and-drop operations.
     */
    DRAG(DragEvent.class),

    /**
     * Represents {@link KeyEvent}, used for keyboard key presses/releases.
     */
    KEY(KeyEvent.class),

    /**
     * Represents {@link InputEvent}, base class for mouse, keyboard, scroll events, etc.
     */
    INPUT(InputEvent.class),

    /**
     * Represents {@link InputMethodEvent}, used for input method editing (e.g. IME).
     */
    INPUT_METHOD(InputMethodEvent.class),

    /**
     * Represents {@link TouchEvent}, used for multitouch and touch screen interaction.
     */
    TOUCH(TouchEvent.class),

    /**
     * Represents {@link ContextMenuEvent}, used for context menu requests (e.g. right-click).
     */
    CONTEXT_MENU(ContextMenuEvent.class),

    /**
     * Represents {@link GestureEvent}, base class for high-level gesture events.
     */
    GESTURE(GestureEvent.class),

    /**
     * Represents {@link MediaErrorEvent}, used when media playback fails.
     */
    MEDIA_ERROR(MediaErrorEvent.class),

    /**
     * Represents {@link WebEvent}, used by JavaFX WebView for events like alert().
     */
    WEB(WebEvent.class),

    /**
     * Fallback event type representing the generic {@link Event} class.
     */
    GENERIC(Event.class);

    /**
     * The JavaFX event class associated with this type.
     */
    private final Class<? extends Event> value;

    /**
     * Attempts to find the matching {@code FXEventType} enum constant
     * for the given event class.
     *
     * @param type the event class to match
     * @return the matching {@code JavaFXEventType}, or {@link #GENERIC} if no match is found
     */
    public static FXEventType fromClass(Class<? extends Event> type) {
        for (FXEventType eventType : values()) {
            if (type.isAssignableFrom(eventType.getValue())) {
                return eventType;
            }
        }
        return GENERIC;
    }
}

