package com.jwcomptech.commons;

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

import com.jwcomptech.commons.utils.DateTimeUtils;
import com.jwcomptech.commons.values.ImmutablePropsValue;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.apache.maven.api.model.Model;
import org.apache.maven.model.v4.MavenStaxReader;

import javax.xml.stream.XMLStreamException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static com.jwcomptech.commons.Literals.cannotBeNullOrEmpty;
import static com.jwcomptech.commons.validators.Preconditions.checkArgumentNotNullOrEmpty;
import static java.nio.charset.StandardCharsets.UTF_8;

@EqualsAndHashCode
@ToString
@SuppressWarnings({"unused", "HardcodedFileSeparator"})
public class POMManager {
    private final ImmutablePropsValue internalProps;
    @Getter
    private final Model internalPOM;

    public static POMManager getInstance() {
        return SingletonManager.getInstance(POMManager.class, POMManager::new);
    }

    public POMManager() {
        final String internalPOMPath = "/pom.properties";
        try(final InputStream internalPOMStream = POMManager.class.getResourceAsStream(internalPOMPath)) {
            internalProps = new ImmutablePropsValue(internalPOMStream);
            final String artifactId = internalProps.getProperty("artifactId");
            checkArgumentNotNullOrEmpty(artifactId, cannotBeNullOrEmpty("artifact"));
            final String rootPath = "pom.xml";
            final Path path = Paths.get(rootPath);
            final String currentPackage = POMManager.class.getPackage().getName();
            final String metaPath = "/META-INF/maven/" + currentPackage + "/" + artifactId + "/pom.xml";

            if(Files.exists(path)) {
                internalPOM = new MavenStaxReader().read(Files.newBufferedReader(path, UTF_8));
            } else {
                try {
                    internalPOM = new MavenStaxReader().read(DateTimeUtils.class.getResourceAsStream(metaPath));
                } catch(final XMLStreamException e) {
                    throw new IllegalStateException("Cannot Access Project POM File!");
                }
            }
        } catch (final IOException e) {
            throw new IllegalStateException("Cannot Access Project POM File!", e);
        } catch (final XMLStreamException e) {
            throw new IllegalStateException("Cannot Process Project POM File!", e);
        }
    }

    public static Model getPOM(final String path) throws IOException, XMLStreamException {
        return getPOM(Paths.get(path));
    }

    public static Model getPOM(final Path path) throws IOException, XMLStreamException {
        return new MavenStaxReader().read(Files.newBufferedReader(path, UTF_8));
    }
}
