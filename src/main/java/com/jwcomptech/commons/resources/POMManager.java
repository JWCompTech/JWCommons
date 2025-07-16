//package com.jwcomptech.commons.resources;
//
///*-
// * #%L
// * JWCT Commons
// * %%
// * Copyright (C) 2025 JWCompTech
// * %%
// * This program is free software: you can redistribute it and/or modify
// * it under the terms of the GNU Lesser General Public License as
// * published by the Free Software Foundation, either version 3 of the
// * License, or (at your option) any later version.
// *
// * This program is distributed in the hope that it will be useful,
// * but WITHOUT ANY WARRANTY; without even the implied warranty of
// * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// * GNU General Lesser Public License for more details.
// *
// * You should have received a copy of the GNU General Lesser Public
// * License along with this program.  If not, see
// * <http://www.gnu.org/licenses/lgpl-3.0.html>.
// * #L%
// */
//
//import com.jwcomptech.commons.utils.DateTimeUtils;
//import com.jwcomptech.commons.utils.SingletonManager;
//import com.jwcomptech.commons.values.ImmutablePropsValue;
//import lombok.EqualsAndHashCode;
//import lombok.Getter;
//import lombok.ToString;
//import org.apache.maven.api.model.Model;
//import org.apache.maven.model.v4.MavenStaxReader;
//
//import javax.xml.stream.XMLStreamException;
//import java.io.*;
//import java.net.URL;
//import java.nio.charset.StandardCharsets;
//import java.nio.file.Files;
//import java.nio.file.Path;
//import java.nio.file.Paths;
//import java.util.Optional;
//
//import static com.jwcomptech.commons.consts.Literals.cannotBeNullOrEmpty;
//import static com.jwcomptech.commons.validators.Preconditions.checkArgumentNotNullOrEmpty;
//import static java.nio.charset.StandardCharsets.UTF_8;
//
//@EqualsAndHashCode
//@ToString
//@SuppressWarnings({"unused", "HardcodedFileSeparator"})
//public class POMManager {
//    private final ImmutablePropsValue internalProps;
//    @Getter
//    private final Model internalPOM;
//
//    public static POMManager getInstance() {
//        return SingletonManager.getInstance(POMManager.class, POMManager::new);
//    }
//
//    public POMManager() {
//        final String internalPOMPath = "/pom.properties";
//        try(final InputStream internalPOMStream = POMManager.class.getResourceAsStream(internalPOMPath)) {
//            internalProps = new ImmutablePropsValue(internalPOMStream);
//            final String artifactId = internalProps.getProperty("artifactId");
//            checkArgumentNotNullOrEmpty(artifactId, cannotBeNullOrEmpty("artifact"));
//            final String rootPath = "pom.xml";
//            final Path path = Paths.get(rootPath);
//            final String currentPackage = POMManager.class.getPackage().getName();
//            final String metaPath = "/META-INF/maven/" + currentPackage + "/" + artifactId + "/pom.xml";
//
//            if(Files.exists(path)) {
//                internalPOM = new MavenStaxReader().read(Files.newBufferedReader(path, UTF_8));
//            } else {
//                internalPOM = new MavenStaxReader().read(DateTimeUtils.class.getResourceAsStream(metaPath));
//            }
//        } catch (final IOException | XMLStreamException e) {
//            throw new IllegalStateException("Cannot Access Project POM File!", e);
//        }
//    }
//
//    public static Model getPOM(final String path) throws IOException, XMLStreamException {
//        return getPOM(Paths.get(path));
//    }
//
//    public static Model getPOM(final Path path) throws IOException, XMLStreamException {
//        try (BufferedReader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {
//            return new MavenStaxReader().read(reader);
//        }
//    }
//
//    public static Model getPOM(final URL url) throws IOException, XMLStreamException {
//        try (InputStream stream = url.openStream()) {
//            return getPOM(stream);
//        }
//    }
//
//    public static Model getPOM(final InputStream stream) throws IOException, XMLStreamException {
//        try (Reader reader = new InputStreamReader(stream, StandardCharsets.UTF_8)) {
//            return new MavenStaxReader().read(reader);
//        }
//    }
//
//    public static Optional<Model> tryGetPOM(final String path) {
//        return tryGetPOM(Paths.get(path));
//    }
//
//    public static Optional<Model> tryGetPOM(final Path path) {
//        try (BufferedReader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {
//            return Optional.of(new MavenStaxReader().read(reader));
//        } catch (IOException | XMLStreamException e) {
//            return Optional.empty();
//        }
//    }
//
//    public static Optional<Model> tryGetPOM(final URL url) {
//        try (InputStream stream = url.openStream()) {
//            return tryGetPOM(stream);
//        } catch (IOException e) {
//            return Optional.empty();
//        }
//    }
//
//    public static Optional<Model> tryGetPOM(final InputStream stream) {
//        try (Reader reader = new InputStreamReader(stream, StandardCharsets.UTF_8)) {
//            return Optional.of(new MavenStaxReader().read(reader));
//        } catch (IOException | XMLStreamException e) {
//            return Optional.empty();
//        }
//    }
//}
