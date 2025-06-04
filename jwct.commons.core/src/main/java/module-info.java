/*-
 * #%L
 * jwct.commmons.core
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
module jwct.commons.core {
    requires ch.qos.logback.classic;
    requires ch.qos.logback.core;
    requires com.google.errorprone.annotations;
    requires com.google.gson;
    requires com.sun.jna.platform;
    requires com.sun.jna;
    requires io.vavr;
    requires java.desktop;
    requires java.management;
    requires java.xml;
    requires jBCrypt;
    requires jdk.management;
    requires okhttp3;
    requires okhttp3.logging;
    requires org.apache.commons.codec;
    requires org.apache.commons.io;
    requires org.apache.commons.lang3;
    requires org.jetbrains.annotations;
    requires org.kohsuke.github.api;
    requires org.slf4j;
    requires paranamer;
    requires plexus.utils;
    requires retrofit2;
    requires retrofit2.converter.gson;
    requires static lombok;

    exports com.jwcomptech.commons.annotations;
    exports com.jwcomptech.commons.base;
    exports com.jwcomptech.commons.consts;
    exports com.jwcomptech.commons.download;
    exports com.jwcomptech.commons.enums;
    exports com.jwcomptech.commons.events;
    exports com.jwcomptech.commons.exceptions;
    exports com.jwcomptech.commons.functions.checked;
    exports com.jwcomptech.commons.functions;
    exports com.jwcomptech.commons.info.enums;
    exports com.jwcomptech.commons.info.os;
    exports com.jwcomptech.commons.info;
    exports com.jwcomptech.commons.interfaces;
    exports com.jwcomptech.commons.internal;
    exports com.jwcomptech.commons.logging;
    exports com.jwcomptech.commons.tuples;
    exports com.jwcomptech.commons.utils.osutils.windows.enums;
    exports com.jwcomptech.commons.utils.osutils.windows.pshell;
    exports com.jwcomptech.commons.utils.osutils.windows;
    exports com.jwcomptech.commons.utils.osutils;
    exports com.jwcomptech.commons.utils;
    exports com.jwcomptech.commons.validators;
    exports com.jwcomptech.commons.values;
    exports com.jwcomptech.commons.webapis.generators;
    exports com.jwcomptech.commons.webapis.services;
    exports com.jwcomptech.commons.webapis;

    opens com.jwcomptech.commons.consts to javafx.fxml;
    opens com.jwcomptech.commons.internal to javafx.fxml;
    opens com.jwcomptech.commons.utils to javafx.fxml;
    opens com.jwcomptech.commons.validators to javafx.fxml;
}
