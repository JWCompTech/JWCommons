package com.jwcomptech.commons.javafx.dialogs;

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

import com.jwcomptech.commons.enums.BaseEnum;
import javafx.scene.control.Alert;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;


/**
 * Used to specify which icon is displayed on a message box.
 *
 * @since 0.0.1
 */
@AllArgsConstructor
@Getter
@ToString
public enum MessageBoxIcon implements BaseEnum<Alert.AlertType> {
    /**
     * The NONE alert type has the effect of not setting any default properties
     * in the MessageBox.
     */
    NONE(Alert.AlertType.NONE),

    /**
     * The INFORMATION alert type configures the MessageBox to appear in a
     * way that suggests the content of the MessageBox is informing the user of
     * a piece of information. This includes an 'information' image, an
     * appropriate title and header, and just an OK button for the user to
     * click on to dismiss the MessageBox.
     */
    INFORMATION(Alert.AlertType.INFORMATION),

    /**
     * The WARNING alert type configures the MessageBox to appear in a
     * way that suggests the content of the MessageBox is warning the user about
     * some fact or action. This includes a 'warning' image, an
     * appropriate title and header, and just an OK button for the user to
     * click on to dismiss the MessageBox.
     */
    WARNING(Alert.AlertType.WARNING),

    /**
     * The CONFIRMATION alert type configures the MessageBox to appear in a
     * way that suggests the content of the MessageBox is seeking confirmation from
     * the user. This includes a 'confirmation' image, an
     * appropriate title and header, and both OK and Cancel buttons for the
     * user to click on to dismiss the MessageBox.
     */
    CONFIRMATION(Alert.AlertType.CONFIRMATION),

    /**
     * The ERROR alert type configures the MessageBox to appear in a
     * way that suggests that something has gone wrong. This includes an
     * 'error' image, an appropriate title and header, and just an OK button
     * for the user to click on to dismiss the MessageBox.
     */
    ERROR(Alert.AlertType.ERROR);

    private final Alert.AlertType value;
}
