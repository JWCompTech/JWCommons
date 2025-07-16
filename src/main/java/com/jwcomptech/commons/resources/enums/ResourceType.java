package com.jwcomptech.commons.resources.enums;

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

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;
import com.jwcomptech.commons.exceptions.ParseException;
import com.jwcomptech.commons.javafx.FXMLEntity;
import com.jwcomptech.commons.javafx.dialogs.FXMLDialog;
import com.jwcomptech.commons.resources.Resource;
import com.jwcomptech.commons.tuples.ImmutablePair;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.image.Image;
import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.stage.Stage;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
//import org.apache.maven.api.model.Model;
//import org.apache.maven.model.v4.MavenStaxReader;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLStreamException;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

/**
 * The types of resources supported by the ResourceManager.
 *
 * @since 1.0.0-alpha
 */
@SuppressWarnings({"HardcodedFileSeparator", "unused"})
@RequiredArgsConstructor
@Getter
@ToString
public enum ResourceType {
    /**
     * Audio files stored in the "audio" directory.
     */
    AUDIO(ResourceDir.AUDIO,
            ResourceExtension.FLAC,
            ResourceExtension.MP3,
            ResourceExtension.M4A,
            ResourceExtension.OGG,
            ResourceExtension.WAV,
            ResourceExtension.WMA) {
        @Override
        public Optional<String> parseToString(final Resource resource) {
            throw new UnsupportedOperationException();
        }

        @Override
        public Optional<AudioClip> parseToAudio(final Resource resource) {
            try {
                return Optional.of(new AudioClip(resource.getURL().toExternalForm()));
            } catch (Exception e) {
                throw new ParseException(this.getClass(), e);
            }
        }

        @Override
        public Optional<Media> parseToMedia(final Resource resource) {
            try {
                return Optional.of(new Media(resource.getURL().toExternalForm()));
            } catch (Exception e) {
                throw new ParseException(this.getClass(), e);
            }
        }
    },
    /**
     * Compressed files stored in the "compressed" directory.
     */
    COMPRESSED(ResourceDir.COMPRESSED,
            ResourceExtension.GZ,
            ResourceExtension.JAR,
            ResourceExtension.RAR,
            ResourceExtension.SEVEN_Z,
            ResourceExtension.TAR,
            ResourceExtension.TAR_GZ,
            ResourceExtension.ZIP) {
        @Override
        public Optional<String> parseToString(final Resource resource) {
            throw new UnsupportedOperationException();
        }
    },
    /**
     * Config files stored in the "config" directory.
     */
    CONFIG(ResourceDir.CONFIG,
            ResourceExtension.CFG,
            ResourceExtension.CONFIG,
            ResourceExtension.INI) {
    },
    /**
     * CSS files stored in the "css" directory.
     */
    CSS(ResourceDir.CSS, ResourceExtension.CSS) {
    },
    /**
     * Data files stored in the "data" directory.
     */
    DATA(ResourceDir.DATA,
            ResourceExtension.DAT,
            ResourceExtension.DB,
            ResourceExtension.SQLITE) {
        @Override
        public Optional<String> parseToString(final Resource resource) {
            throw new UnsupportedOperationException();
        }
    },
    /**
     * Font files stored in the "font" directory.
     */
    FONT(ResourceDir.FONT,
            ResourceExtension.EOT,
            ResourceExtension.OTF,
            ResourceExtension.TTF) {
        @Override
        public Optional<String> parseToString(final Resource resource) {
            throw new UnsupportedOperationException();
        }
    },
    /**
     * FXML files stored in the "fxml" directory.
     */
    FXML(ResourceDir.FXML, ResourceExtension.FXML) {
        @Override
        public <C> Optional<FXMLEntity<C>> parseToFXML(final Resource resource) {
            return Optional.of(FXMLEntity.of(resource));
        }

        @Override
        public <C> Optional<FXMLEntity<C>> parseToFXML(final Resource resource, final C controller) {
            return Optional.of(FXMLEntity.of(resource, controller));
        }

        @SuppressWarnings("unchecked")
        @Override
        public <R, C> Optional<FXMLDialog<R, C>> parseToFXMLDialog(@NotNull final Resource resource,
                                                                   @Nullable final String title,
                                                                   @Nullable final List<Image> icons,
                                                                   @NotNull final Stage owner,
                                                                   @Nullable final C controller) {
            final Optional<FXMLEntity<C>> entity = parseToFXML(resource, controller);

            if (entity.isPresent() && entity.get().isDialog()) {
                return Optional.of(((FXMLDialog.Builder<R, C>) FXMLDialog.builder())
                        .withFxmlEntity(entity.get())
                        .withTitle(title)
                        .withIcon(icons)
                        .withOwner(owner)
                        .build());
            } else {
                throw new ParseException(this.getClass(), "Resource is not a dialog");
            }
        }

        @Override
        public <C> Optional<ImmutablePair<FXMLLoader, Parent>> parseToFXMLParent(final Resource resource,
                                                                                 final C controller) {
            final var entity = parseToFXML(resource, controller);

            if (entity.isPresent() && !entity.get().isDialog()) {
                FXMLLoader loader = entity.get().getLoader();
                Optional<Parent> parent = entity.get().getRoot();
                return parent.map(rParent -> new ImmutablePair<>(loader, rParent));
            } else {
                throw new ParseException(this.getClass(), "Resource is not a dialog");
            }
        }
    },
    /**
     * Image files stored in the "img" directory.
     */
    IMAGE(ResourceDir.IMAGE,
            ResourceExtension.BMP,
            ResourceExtension.GIF,
            ResourceExtension.ICO,
            ResourceExtension.JPEG,
            ResourceExtension.JPG,
            ResourceExtension.PNG,
            ResourceExtension.SVG,
            ResourceExtension.WEBP) {
        @Override
        public Optional<Image> parseToImage(final Resource resource) {
            try {
                return Optional.of(new Image(resource.getURLString()));
            } catch (Exception e) {
                throw new ParseException(this.getClass(), e);
            }
        }
    },
    /**
     * JSON files stored in the "json" directory.
     */
    JSON(ResourceDir.JSON, ResourceExtension.JSON) {
        @Override
        public <T> Optional<T> parseToJsonObject(final Resource resource,
                                                 final Class<T> clazz) {
            final ObjectMapper mapper = new ObjectMapper();
            try (InputStream input = resource.getInputStream()) {
                return Optional.of(mapper.readValue(input, clazz));
            } catch (IOException e) {
                throw new ParseException(this.getClass(), e);
            }
        }

        @Override
        public <T> Optional<List<T>> parseToJsonList(final Resource resource,
                                                     final Class<T> type) {
            ObjectMapper mapper = new ObjectMapper();
            try (InputStream input = resource.getInputStream()) {
                JavaType listType = mapper.getTypeFactory().constructCollectionType(List.class, type);
                List<T> list = mapper.readValue(input, listType);
                return Optional.of(list);
            } catch (IOException e) {
                throw new ParseException(this.getClass(), e);
            }
        }
    },
    /**
     * Maven POM files stored in the "pom" directory.
     */
    POM(ResourceDir.POM, ResourceExtension.POM) {
//        @Override
//        public Optional<Model> parseToPOMModel(final Resource resource) {
//            try (Reader reader = new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8)) {
//                Model model = new MavenStaxReader().read(reader);
//                return Optional.of(model);
//            } catch (IOException | XMLStreamException e) {
//                throw new ParseException(this.getClass(), e);
//            }
//        }
    },
    /**
     * Properties files stored in the "props" directory.
     */
    PROPERTIES(ResourceDir.PROPERTIES, ResourceExtension.PROPERTIES) {
        @Override
        public Optional<Properties> parseToProperties(final Resource resource) {
            try (InputStream is = resource.getInputStream()) {
                Properties props = new Properties();
                props.load(is);
                return Optional.of(props);
            } catch (IOException e) {
                throw new ParseException(this.getClass(), e);
            }
        }
    },
    /**
     * Script files stored in the "script" directory.
     */
    SCRIPT(ResourceDir.SCRIPT,
            ResourceExtension.BAT,
            ResourceExtension.BASH,
            ResourceExtension.JS,
            ResourceExtension.PS1,
            ResourceExtension.PY,
            ResourceExtension.SCRIPT,
            ResourceExtension.SH,
            ResourceExtension.SQL,
            ResourceExtension.VBS) {
    },
    /**
     * Text files stored in the "txt" directory.
     */
    TEXT(ResourceDir.TEXT,
            ResourceExtension.CSV,
            ResourceExtension.LOG,
            ResourceExtension.MAN,
            ResourceExtension.MD,
            ResourceExtension.README,
            ResourceExtension.TXT) {
    },
    /**
     * Video files stored in the "video" directory.
     */
    VIDEO(ResourceDir.VIDEO,
            ResourceExtension.AVI,
            ResourceExtension.MP4,
            ResourceExtension.MOV,
            ResourceExtension.MPEG,
            ResourceExtension.MPG,
            ResourceExtension.TS,
            ResourceExtension.WEBM,
            ResourceExtension.WMV) {
        @Override
        public Optional<String> parseToString(final Resource resource) {
            throw new UnsupportedOperationException();
        }

        @Override
        public Optional<Media> parseToMedia(final Resource resource) {
            try {
                return Optional.of(new Media(resource.getURL().toExternalForm()));
            } catch (Exception e) {
                throw new ParseException(this.getClass(), e);
            }
        }
    },
    /**
     * Web files stored in the "web" directory.
     */
    WEB(ResourceDir.WEB,
            ResourceExtension.HTM,
            ResourceExtension.HTML,
            ResourceExtension.PHP) {
    },
    /**
     * XML files stored in the "xml" directory.
     */
    XML(ResourceDir.XML, ResourceExtension.XML) {
        @Override
        public Optional<Document> parseToXML(final Resource resource) {
            final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setNamespaceAware(true);  // helps with XML namespaces if present

            try {
                DocumentBuilder builder = factory.newDocumentBuilder();
                try (var is = resource.getInputStream()) {
                    return Optional.ofNullable(builder.parse(is));
                }
            } catch (ParserConfigurationException | IOException | SAXException e) {
                throw new ParseException(this.getClass(), e);
            }
        }
    },
    /**
     * YAML files stored in the "yaml" directory.
     */
    YAML(ResourceDir.YAML, ResourceExtension.YAML, ResourceExtension.YML) {
        @Override
        public <T> Optional<T> parseToYamlObject(final Resource resource, final Class<T> type) {
            ObjectMapper yamlMapper = new YAMLMapper();
            try (InputStream input = resource.getInputStream()) {
                T result = yamlMapper.readValue(input, type);
                return Optional.of(result);
            } catch (IOException e) {
                throw new ParseException(this.getClass(), e);
            }
        }
    },
    MEDIA(ResourceDir.MEDIA, ResourceType.AUDIO.extensions, ResourceType.VIDEO.extensions) {
        @Override
        public Optional<String> parseToString(final Resource resource) {
            throw new UnsupportedOperationException();
        }

        @Override
        public Optional<Media> parseToMedia(final Resource resource) {
            try {
                return Optional.of(new Media(resource.getURL().toExternalForm()));
            } catch (Exception e) {
                throw new ParseException(this.getClass(), e);
            }
        }
    },
    ;

    /**
     * The resource directory in the project resource folder.
     */
    private final ResourceDir dir;
    /**
     * The extensions that match the resource type
     */
    private final Set<ResourceExtension> extensions;

    /**
     * @param dir the directory in the project resource folder.
     * @param extension the extension that matches the resource type
     */
    ResourceType(ResourceDir dir, final ResourceExtension... extension) {
        this.dir = dir;
        this.extensions = Set.of(extension);
    }

    /**
     * @param dir the directory in the project resource folder.
     * @param extensions the extensions that match the resource type
     */
    @SafeVarargs
    ResourceType(ResourceDir dir, final Set<ResourceExtension>... extensions) {
        this.dir = dir;
        this.extensions = Arrays.stream(extensions)
                .flatMap(Set::stream)
                .collect(Collectors.toSet());
    }

    /**
     * Returns the default path of the directory.
     *
     * @return the default path of the directory
     */
    public String getDefaultPath() {
        return dir.getPath();
    }

    /**
     * Checks if the specified extension supports this resource type.
     *
     * @param extension the extension to check
     * @return true if the specified extension supports this resource type
     */
    public boolean supportsExtension(final String extension) {
        return extensions.stream()
                .anyMatch(ext -> ext.name().equalsIgnoreCase(extension));
    }

    /**
     * Checks if the specified extension supports this resource type.
     *
     * @param extension the extension to check
     * @return true if the specified extension supports this resource type
     */
    public boolean supportsExtension(final ResourceExtension extension) {
        return extensions.stream()
                .anyMatch(ext -> ext.equals(extension));
    }

    /**
     * Returns the ResourceType matching the specified directory.
     *
     * @param dir the directory to lookup
     * @return the ResourceType matching the specified directory,
     * if a match is not found returns {@link Optional#empty()}
     */
    public static @NotNull Optional<ResourceType> parseByDir(final ResourceDir dir) {
        return Arrays.stream(values())
                .filter(type -> type.getDir() == dir)
                .findFirst();
    }

    /**
     * Returns the ResourceType matching the specified filename.
     *
     * @param fileName the filename to lookup
     * @return the ResourceType matching the specified filename,
     * if a match is not found returns {@link Optional#empty()}
     */
    public static @NotNull Optional<ResourceType> parseByFileName(final String fileName) {
        return Arrays.stream(values())
                .filter(type -> type.getExtensions().stream()
                        .anyMatch(ext -> fileName.toLowerCase()
                                .endsWith("." + ext.getExtension().toLowerCase())))
                .findFirst();
    }

    /**
     * Returns the ResourceType matching the specified extension.
     *
     * @param extension the extension to lookup
     * @return the ResourceType matching the specified extension,
     * if a match is not found returns {@link Optional#empty()}
     */
    public static @NotNull Optional<ResourceType> parseByExtension(final ResourceExtension extension) {
        return Arrays.stream(values())
                .filter(type -> type.supportsExtension(extension))
                .findFirst();
    }

    protected <T> Optional<T> unsupportedParse() {
        throw new UnsupportedOperationException("This resource type does not support this parse operation.");
    }

    /**
     * Returns the resource as a JavaFX {@link AudioClip} object only if {@link Resource#isAudio()}
     * evaluates to true, otherwise {@link Optional#empty()}.
     *
     * @param resource the resource to load
     * @return the resource as a JavaFX {@link AudioClip} object only if {@link Resource#isAudio()}
     * evaluates to true, otherwise {@link Optional#empty()}
     * @throws ParseException if any exception is thrown during parsing
     */
    public Optional<AudioClip> parseToAudio(final Resource resource) {
        return unsupportedParse();
    }

    /**
     * Loads the resource as a FXML file and returns a {@link FXMLEntity} object.
     *
     * @param resource the resource to load
     * @param <C> the class type of the controller
     * @return the resource as a {@link FXMLEntity} object
     */
    public <C> Optional<FXMLEntity<C>> parseToFXML(final Resource resource) {
        throw new UnsupportedOperationException();
    }

    /**
     * Loads the resource as a FXML file and returns a {@link FXMLEntity} object.
     *
     * @param resource the resource to load
     * @param controller the controller to inject, can be null
     * @param <C> the controller type
     * @return the resource as a {@link FXMLEntity} object
     * @apiNote If controller is null then this method will attempt to load the controller from
     * the fxml file via the fx:controller tag. If this tag is already defined and controller
     * is not null then an IllegalStateException will be thrown. One or the other must be used
     * but not both.
     * @throws IllegalStateException if both controller is not null and fx:controller is defined
     */
    public <C> Optional<FXMLEntity<C>> parseToFXML(final Resource resource,
                                                   final C controller) {
        throw new UnsupportedOperationException();
    }

    /**
     * Parses the resource to a {@link FXMLDialog} with the specified parameters.
     *
     * @param resource the resource to load
     * @param title the title to set, can be null
     * @param icon the icon to set, can be null or empty
     * @param owner the owner to set
     * @param controller the controller to inject, can be null
     * @param <R> the return type of the dialog
     * @param <C> the class type of the controller
     * @return the resource as a {@link FXMLDialog} object
     */
    public <R, C> Optional<FXMLDialog<R, C>> parseToFXMLDialog(@NotNull final Resource resource,
                                                               @Nullable final String title,
                                                               @Nullable final Image icon,
                                                               @NotNull final Stage owner,
                                                               @Nullable final C controller) {
        final List<Image> icons = new ArrayList<>();
        icons.add(icon);
        return parseToFXMLDialog(resource, title, icons, owner, controller);
    }

    /**
     * Parses the resource to a {@link FXMLDialog} with the specified parameters.
     *
     * @param resource the resource to load
     * @param title the title to set, can be null
     * @param icons the icons to set, can be null or empty
     * @param owner the owner to set
     * @param controller the controller to inject, can be null
     * @param <R> the return type of the dialog
     * @param <C> the class type of the controller
     * @return the resource as a {@link FXMLDialog} object
     */
    public <R, C> Optional<FXMLDialog<R, C>> parseToFXMLDialog(@NotNull final Resource resource,
                                                               @Nullable final String title,
                                                               @Nullable final List<Image> icons,
                                                               @NotNull final Stage owner,
                                                               @Nullable final C controller) {
        throw new UnsupportedOperationException();
    }

    /**
     * Parses the resource as a {@link Parent} object and returns the {@link Parent} and {@link FXMLLoader} objects.
     *
     * @param resource the resource to load
     * @return the new {@link Parent} object and the {@link FXMLLoader} that was used to load the specified resource
     */
    public Optional<ImmutablePair<FXMLLoader, Parent>> parseToFXMLParent(final Resource resource) {
        return parseToFXMLParent(resource, null);
    }

    /**
     * Parses the resource as a {@link Parent} object, attempts to inject the specified controller,
     * and returns the {@link Parent} and {@link FXMLLoader} objects.
     *
     * @param resource the resource to load
     * @param controller the controller to inject, can be null
     * @param <C> the controller type
     * @return the new {@link Parent} object and the {@link FXMLLoader} that was used to load the resource
     * @apiNote If controller is null then this method will attempt to load the controller from
     * the fxml file via the fx:controller tag. If this tag is already defined and controller
     * is not null then an IllegalStateException will be thrown. One or the other must be used
     * but not both.
     */
    public <C> Optional<ImmutablePair<FXMLLoader, Parent>> parseToFXMLParent(final Resource resource,
                                                                             final C controller) {
        throw new UnsupportedOperationException();
    }

    /**
     * Parses the resource as a JavaFX Image object only if {@link Resource#isImage()}
     * evaluates to true, otherwise {@link Optional#empty()}.
     *
     * @return the resource as a JavaFX Image object only if {@link Resource#isImage()}
     * evaluates to true, otherwise {@link Optional#empty()}
     * @throws ParseException if any exception is thrown during parsing
     */
    public Optional<Image> parseToImage(final Resource resource) {
        return unsupportedParse();
    }

    /**
     * Parses the resource as a Json object if the content type is json,
     * otherwise returns {@link Optional#empty()}.
     *
     * @param resource the resource to load
     * @param clazz the class to attempt to return an instance of
     * @param <T> the class type of the expected object to be returned
     * @return Returns the resource as a Json object if the content type is json,
     * otherwise returns {@link Optional#empty()}
     * @throws ParseException if any exception is thrown during parsing
     */
    public <T> Optional<T> parseToJsonObject(final Resource resource,
                                             final Class<T> clazz) {
        return unsupportedParse();
    }

    /**
     * Parses the resource as a Json {@link List<T>} if the content type is json,
     * otherwise returns {@link Optional#empty()}.
     *
     * @param resource the resource to load
     * @param type the class to attempt to return instances of
     * @param <T> the class type of the expected objects to be returned
     * @return Returns the resource as a Json {@link List<T>} if the content type is json,
     * otherwise returns {@link Optional#empty()}
     * @throws ParseException if any exception is thrown during parsing
     */
    public <T> Optional<List<T>> parseToJsonList(final Resource resource,
                                                 final Class<T> type) {
        return unsupportedParse();
    }

    /**
     * Parses the resource as a JavaFX {@link Media} object only if {@link Resource#isAudio()}
     * evaluates to true, otherwise {@link Optional#empty()}.
     *
     * @param resource the resource to load
     * @return the resource as a JavaFX {@link Media} object only if {@link Resource#isAudio()}
     * evaluates to true, otherwise {@link Optional#empty()}
     * @throws ParseException if any exception is thrown during parsing
     */
    public Optional<Media> parseToMedia(final Resource resource) {
        throw new UnsupportedOperationException();
    }

//    /**
//     * Parses the resource as a Maven {@link Model} object if the content type is pom,
//     * otherwise returns {@link Optional#empty()}.
//     *
//     * @param resource the resource to load
//     * @return Returns the resource as a Maven {@link Model} object if the content type is pom,
//     * otherwise returns {@link Optional#empty()}
//     * @throws ParseException if any exception is thrown during parsing
//     */
//    public Optional<Model> parseToPOMModel(final Resource resource) {
//        return unsupportedParse();
//    }

    /**
     * Parses the resource as a Java {@link Properties} object.
     *
     * @param resource the resource to load
     * @return the resource as a Java {@link Properties} object
     * @throws ParseException if any exception is thrown during parsing
     */
    public Optional<Properties> parseToProperties(final Resource resource) {
        return unsupportedParse();
    }

    /**
     * Parses the resource as a text string if the content type is
     * text or can be converted to text, otherwise returns {@link Optional#empty()}.
     *
     * @param resource the resource to load
     * @return the resource as a text string if the content type is
     * text or can be converted to text, otherwise returns {@link Optional#empty()}
     * @throws ParseException if any exception is thrown during parsing
     */
    public Optional<String> parseToString(final Resource resource) {
        try (final InputStream input = resource.getInputStream();
             final BufferedReader reader = new BufferedReader(
                     new InputStreamReader(input, StandardCharsets.UTF_8))) {
            return Optional.of(reader.lines().collect(Collectors.joining("\n")));
        } catch (IOException e) {
            throw new ParseException(this.getClass(), e);
        }
    }

    /**
     * Parses the resource as an XML Document object if the content type is xml,
     * otherwise returns {@link Optional#empty()}.
     *
     * @param resource the resource to load
     * @return Returns the resource as an XML Document object if the content type is xml,
     * otherwise returns {@link Optional#empty()}
     * @throws ParseException if any exception is thrown during parsing
     */
    public Optional<Document> parseToXML(final Resource resource) {
        return unsupportedParse();
    }

    /**
     * Parses the resource as a Yaml object if the content type is yaml,
     * otherwise returns {@link Optional#empty()}.
     *
     * @param resource the resource to load
     * @param <T> the class type of the expected object to be returned
     * @return Returns the resource as a Yaml object if the content type is yaml,
     * otherwise returns {@link Optional#empty()}
     * @throws ParseException if any exception is thrown during parsing
     */
    public <T> Optional<T> parseToYamlObject(final Resource resource,
                                             final Class<T> type) {
        return unsupportedParse();
    }
}
