/*-
 * #%L
 * jwct.commons.javafx
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
module jwct.commons.javafx {
    requires ch.qos.logback.classic;
    requires java.xml;
    requires javafx.base;
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires jwct.commons.core;
    requires org.jetbrains.annotations;
    requires org.slf4j;
    requires static lombok;

    exports com.jwcomptech.commons.javafx.dialogs;
    exports com.jwcomptech.commons.javafx.resources.enums;
    exports com.jwcomptech.commons.javafx.resources;
    exports com.jwcomptech.commons.javafx;

    opens com.jwcomptech.commons.javafx.dialogs to javafx.fxml;
    opens com.jwcomptech.commons.javafx.resources to javafx.fxml;
    opens com.jwcomptech.commons.javafx.resources.enums to javafx.fxml;
}
