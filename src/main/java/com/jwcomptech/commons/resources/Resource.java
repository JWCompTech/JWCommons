package com.jwcomptech.commons.resources;

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

import com.jwcomptech.commons.exceptions.ParseException;
import com.jwcomptech.commons.javafx.FXMLEntity;
import com.jwcomptech.commons.javafx.dialogs.FXMLDialog;
import com.jwcomptech.commons.resources.enums.ResourceDir;
import com.jwcomptech.commons.resources.enums.ResourceExtension;
import com.jwcomptech.commons.resources.enums.ResourceType;
import com.jwcomptech.commons.tuples.ImmutablePair;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.image.Image;
import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.stage.Stage;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Value;
import org.apache.maven.api.model.Model;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.w3c.dom.Document;

import java.io.*;
import java.net.URI;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

import static com.jwcomptech.commons.consts.Literals.cannotBeNull;
import static com.jwcomptech.commons.validators.Preconditions.checkArgumentNotNull;
import static com.jwcomptech.commons.validators.Preconditions.checkArgumentNotNullOrEmpty;

/**
 * An object representation of an internal resource file in the
 * project resource directory.
 *
 * @since 1.0.0-alpha
 */
@SuppressWarnings("unused")
@Value
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Resource {
    ResourceDir type;
    String fileName;
    String fullPath;

    @Contract(pure = true)
    private Resource(@NotNull final ResourceDir type, final String fileName) {
        checkArgumentNotNull(type, cannotBeNull("type"));
        checkArgumentNotNullOrEmpty(fileName, "filename");
        this.type = type;
        this.fileName = fileName;

        //noinspection HardcodedFileSeparator
        fullPath = type.getPath() + "/" + fileName;
    }

    /**
     * Creates a new Resource representing the file at the specified path.
     *
     * @param filePath the path to the file
     * @return the new Resource instance
     */
    public static @NotNull Resource of(final String filePath) {
        final var result = ResourceManager.getInstance().parseResourceURL(filePath);

        if(result.getLeft().isEmpty() || result.getRight().isEmpty()) {
            throw new IllegalArgumentException("FXML file is invalid");
        }
        return new Resource(result.getLeft().get(), result.getRight().get());
    }


    /**
     * Creates a new Resource representing the file in the specified directory,
     * using the specified filename.
     *
     * @param dir the directory the file is stored in
     * @param filename the filename of the file
     * @return the new Resource instance
     */
    @Contract(value = "_, _ -> new", pure = true)
    public static @NotNull Resource of(@NotNull final ResourceDir dir, final String filename) {
        return new Resource(dir, filename);
    }

    public static @NotNull URL requireResource(final String path) {
        URL resource = Resource.class.getResource(path);
        if (resource == null) {
            throw new IllegalStateException("Resource not found: " + path);
        }
        return resource;
    }

    /**
     * Returns the resource path location as a {@link URL} object.
     *
     * @return the resource path location as a {@link URL} object
     */
    public URL getURL() {
        return requireResource(fullPath);
    }

    /**
     * Returns the resource path location wrapped in a JavaFX CSS url string.
     *
     * @return the resource path location wrapped in a JavaFX CSS url string
     */
    public String getCSSPath() {
        return "url('%s')".formatted(fullPath);
    }

    /**
     * Returns the resource path location as a String.
     *
     * @return the resource path location as a String
     */
    public String getURLString() {
        return getURL().toString();
    }

    /**
     * Returns the resource filename from the provided url.
     *
     * @return the resource filename from the provided url
     */
    public String getFileName() {
        try {
            return Paths.get(getURL().toURI()).getFileName().toString();
        } catch (Exception e) {
            return ""; // fallback if URI is malformed
        }
    }

    /**
     * Returns the resource file extension as a string.
     *
     * @return the resource file extension as a string
     */
    public String getFileExtension() {
        final String fileName = getFileName();
        return fileName.substring(fileName.lastIndexOf('.'));
    }

    /**
     * Returns the resource filename from the provided url as lowercase.
     *
     * @return the resource filename from the provided url as lowercase
     */
    public String getFileNameLower() {
        try {
            return Paths.get(getURL().toURI()).getFileName().toString().toLowerCase(Locale.ROOT);
        } catch (Exception e) {
            return ""; // fallback if URI is malformed
        }
    }

    /**
     * Returns the resource filename from the provided url as lowercase
     * using the specified locale.
     *
     * @return the resource filename from the provided url as lowercase
     * using the specified locale.
     */
    public String getFileNameLower(final Locale locale) {
        checkArgumentNotNull(locale, cannotBeNull("locale"));
        try {
            return Paths.get(getURL().toURI()).getFileName().toString().toLowerCase(locale);
        } catch (Exception e) {
            return ""; // fallback if URI is malformed
        }
    }

    /**
     * Opens a connection to this resource and returns an
     * {@link InputStream} for reading from that connection.
     *
     * @return     an input stream for reading from the URL connection.
     * @throws     UncheckedIOException  if an I/O exception occurs.
     * @see        java.net.URL#openConnection()
     * @see        java.net.URLConnection#getInputStream()
     */
    public InputStream getInputStream() {
        try {
            URL url = getURL();
            if (url == null) {
                throw new IllegalStateException("Resource URL is null");
            }
            return url.openStream();
        } catch (IOException e) {
            throw new UncheckedIOException("Failed to open stream for resource: " + this, e);
        }
    }

    /**
     * Returns the file content type of the resource.
     *
     * @return the file content type of the resource
     */
    public String getContentType() {
        try {
            URI uri = getURL().toURI();
            Path path = Paths.get(uri);
            String contentType = Files.probeContentType(path);

            if (contentType != null) {
                return contentType;
            }

            // Fallback: guess based on file extension
            String filename = path.getFileName().toString();
            Optional<ResourceExtension> resourceExtension = getResourceExtension();
            if (resourceExtension.isPresent()) {
                return resourceExtension.get().getMimeType();
            } else {
                // Final fallback
                return "application/octet-stream";
            }
        } catch (final Exception e) {
            // Final fallback
            return "application/octet-stream";
        }
    }

    /**
     * Returns the resource file extension as a {@link ResourceExtension} object.
     *
     * @return the resource file extension as a {@link ResourceExtension} object
     */
    public Optional<ResourceExtension> getResourceExtension() {
        return Optional.ofNullable(ResourceExtension.fromExtension(getFileExtension()));
    }

    /**
     * Returns the ResourceType matching the resource file extension.
     *
     * @return the ResourceType matching the resource file extension,
     * if a match is not found returns {@link Optional#empty()}
     */
    public @NotNull Optional<ResourceType> getResourceType() {
        return getResourceExtension().flatMap(ResourceType::parseByExtension);
    }

    /**
     * Returns true if the resource content type equals the specified content type.
     * @param contentType the content type to match against the resource
     * @return true if the resource content type equals the specified content type
     */
    public boolean isContentType(final String contentType) {
        return getContentType().equals(contentType);
    }

    /**
     * Checks if this instance is of the specified type.
     *
     * @param type the type to check
     * @return true if this instance is of the specified type
     */
    public boolean isOfType(final ResourceType type) {
        return getResourceType().map(t -> t == type).orElse(false);
    }

    //region isX() Methods

    /**
     * Returns true if the file content type of the resource is audio.
     *
     * @return true if the file content type of the resource is audio
     */
    public boolean isAudio() {
        return isOfType(ResourceType.AUDIO);
    }

    /**
     * Returns true if the file content type of the resource is FXML.
     *
     * @return true if the file content type of the resource is FXML
     */
    public boolean isFXML() {
        return isOfType(ResourceType.FXML);
    }

    /**
     * Returns true if the file content type of the resource is an image.
     *
     * @return true if the file content type of the resource is an image
     */
    public boolean isImage() {
        return isOfType(ResourceType.IMAGE);
    }

    /**
     * Returns true if the file content type of the resource is json.
     *
     * @return true if the file content type of the resource is json
     */
    public boolean isJson() {
        return isOfType(ResourceType.JSON);
    }

    /**
     * Returns true if the file content type of the resource is audio or video.
     *
     * @return true if the file content type of the resource is audio or video
     */
    public boolean isMedia() {
        return isOfType(ResourceType.MEDIA);
    }

    /**
     * Returns true if the resource is a pom.xml file.
     *
     * @return true if the resource is a pom.xml file
     */
    public boolean isPom() {
        return isOfType(ResourceType.POM);
    }

    /**
     * Returns true if the resource is a properties file.
     *
     * @return true if the resource is a properties file
     */
    public boolean isProperties() {
        return isOfType(ResourceType.PROPERTIES);
    }

    /**
     * Returns true if the file content type of the resource is text.
     *
     * @return true if the file content type of the resource is text
     */
    public boolean isText() {
        return isOfType(ResourceType.TEXT);
    }

    /**
     * Returns true if the file content type of the resource is video.
     *
     * @return true if the file content type of the resource is video
     */
    public boolean isVideo() {
        return isOfType(ResourceType.VIDEO);
    }

    /**
     * Returns true if the file content type of the resource is xml.
     *
     * @return true if the file content type of the resource is xml
     */
    public boolean isXml() {
        return isOfType(ResourceType.XML)
                || getContentType().endsWith("+xml")
                || isOfType(ResourceType.POM);
    }

    /**
     * Returns true if the file content type of the resource is yaml or
     * if the file extension is either ".yaml" or ".yml".
     *
     * @return true if the file content type of the resource is yaml or
     * if the file extension is either ".yaml" or ".yml"
     */
    public boolean isYaml() {
        return isOfType(ResourceType.YAML);
    }

    //endregion isX() Methods

    //region asX() Methods

    /**
     * Parses the resource as a JavaFX AudioClip object only if {@link #isAudio()}
     * evaluates to true, otherwise {@link Optional#empty()}.
     *
     * @return the resource as a JavaFX AudioClip object only if {@link #isAudio()}
     * evaluates to true, otherwise {@link Optional#empty()}
     * @throws ParseException if any exception is thrown during parsing
     */
    public Optional<AudioClip> asAudio() {
        return isAudio() ? ResourceType.AUDIO.parseToAudio(this) : Optional.empty();
    }

    /**
     * Parses the resource as a FXML file and returns a {@link FXMLEntity} object.
     *
     * @param <C> the class type of the controller
     * @return the resource as a {@link FXMLEntity} object
     */
    public <C> Optional<FXMLEntity<C>> asFXML() {
        return isFXML() ? ResourceType.FXML.parseToFXML(this) : Optional.empty();
    }

    /**
     * Parses the resource as a FXML file and returns a {@link FXMLEntity} object.
     *
     * @param controller the controller to inject, can be null
     * @param <C> the controller type
     * @return the resource as a {@link FXMLEntity} object
     * @apiNote If controller is null then this method will attempt to load the controller from
     * the fxml file via the fx:controller tag. If this tag is already defined and controller
     * is not null then an IllegalStateException will be thrown. One or the other must be used
     * but not both.
     * @throws IllegalStateException if both controller is not null and fx:controller is defined
     */
    public <C> Optional<FXMLEntity<C>> asFXML(@Nullable final C controller) {
        return isFXML() ? ResourceType.FXML.parseToFXML(this, controller) : Optional.empty();
    }

    /**
     * Parses the resource to a {@link FXMLDialog} with the specified parameters.
     *
     * @param title the title to set, can be null
     * @param icon the icon to set, can be null
     * @param owner the owner to set
     * @param controller the controller to inject, can be null
     * @param <R> the class type to be returned when the dialog is closed
     * @param <C> the controller type
     * @return the resource as a {@link FXMLEntity} object
     * @apiNote If controller is null then this method will attempt to load the controller from
     * the fxml file via the fx:controller tag. If this tag is already defined and controller
     * is not null then an IllegalStateException will be thrown. One or the other must be used
     * but not both.
     */
    public <R, C> Optional<FXMLDialog<R, C>> asFXMLDialog(@Nullable final String title,
                                                          @Nullable final Image icon,
                                                          @NotNull final Stage owner,
                                                          @Nullable final C controller) {
        return isFXML()
                ? ResourceType.FXML.parseToFXMLDialog(this, title, icon, owner, controller)
                : Optional.empty();
    }

    /**
     * Parses the resource to a {@link FXMLDialog} with the specified parameters.
     *
     * @param title the title to set, can be null
     * @param icons the icons to set, can be null or empty
     * @param owner the owner to set
     * @param controller the controller to inject, can be null
     * @param <R> the class type to be returned when the dialog is closed
     * @param <C> the controller type
     * @return the resource as a {@link FXMLDialog} object
     * @apiNote If controller is null then this method will attempt to load the controller from
     * the fxml file via the fx:controller tag. If this tag is already defined and controller
     * is not null then an IllegalStateException will be thrown. One or the other must be used
     * but not both.
     */
    public <R, C> Optional<FXMLDialog<R, C>> asFXMLDialog(@Nullable final String title,
                                                          @Nullable final List<Image> icons,
                                                          @NotNull final Stage owner,
                                                          @Nullable final C controller) {
        return isFXML()
                ? ResourceType.FXML.parseToFXMLDialog(this, title, icons, owner, controller)
                : Optional.empty();
    }

    /**
     * Parses the resource as a Parent object and returns the {@link Parent} and {@link FXMLLoader} objects.
     *
     * @return the new {@link Parent} object and the {@link FXMLLoader} that was used to load the resource
     */
    public Optional<ImmutablePair<FXMLLoader, Parent>> asFXMLParent() {
        return isFXML() ? ResourceType.FXML.parseToFXMLParent(this) : Optional.empty();
    }

    /**
     * Parses the resource as a {@link Parent} object, attempts to inject the specified controller,
     * and returns the new {@link Parent} and {@link FXMLLoader} objects.
     *
     * @param controller the controller to inject, can be null
     * @param <C> the controller type
     * @return the new Dialog object and the {@link FXMLLoader} that was used to load the resource
     * @apiNote If controller is null then this method will attempt to load the controller from
     * the fxml file via the fx:controller tag. If this tag is already defined and controller
     * is not null then an IllegalStateException will be thrown. One or the other must be used
     * but not both.
     */
    public <C> Optional<ImmutablePair<FXMLLoader, Parent>> asFXMLParent(@Nullable final C controller) {
        return isFXML() ? ResourceType.FXML.parseToFXMLParent(this, controller) : Optional.empty();
    }

    /**
     * Parses the resource as a JavaFX Image object only if {@link #isImage()}
     * evaluates to true, otherwise {@link Optional#empty()}.
     *
     * @return the resource as a JavaFX Image object only if {@link #isImage()}
     * evaluates to true, otherwise {@link Optional#empty()}
     * @throws ParseException if any exception is thrown during parsing
     */
    public @NotNull Optional<Image> asImage() {
        return isImage() ? ResourceType.IMAGE.parseToImage(this) : Optional.empty();
    }

    /**
     * Parses the resource as a Json object if the content type is json,
     * otherwise returns {@link Optional#empty()}.
     *
     * @param type the class to attempt to return an instance of
     * @param <T> the class type of the expected object to be returned
     * @return Returns the resource as a Json object if the content type is json,
     * otherwise returns {@link Optional#empty()}
     * @throws ParseException if any exception is thrown during parsing
     */
    public <T> Optional<T> asJson(Class<T> type) {
        return isJson() ? ResourceType.JSON.parseToJsonObject(this, type) : Optional.empty();

    }

    /**
     * Parses the resource as a Json {@link List<T>} if the content type is json,
     * otherwise returns {@link Optional#empty()}.
     *
     * @param type the class to attempt to return instances of
     * @param <T> the class type of the expected objects to be returned
     * @return Returns the resource as a Json {@link List<T>} if the content type is json,
     * otherwise returns {@link Optional#empty()}
     * @throws ParseException if any exception is thrown during parsing
     */
    public <T> Optional<List<T>> asJsonList(Class<T> type) {
        return isJson() ? ResourceType.JSON.parseToJsonList(this, type) : Optional.empty();

    }

    /**
     * Parses the resource as a JavaFX Media object only if {@link #isAudio()}
     * evaluates to true, otherwise {@link Optional#empty()}.
     *
     * @return the resource as a JavaFX Media object only if {@link #isAudio()}
     * evaluates to true, otherwise {@link Optional#empty()}
     * @throws ParseException if any exception is thrown during parsing
     */
    public Optional<Media> asMedia() {
        return isMedia()
                ? ResourceType.MEDIA.parseToMedia(this)
                : Optional.empty();
    }

    /**
     * Parses the resource as a Maven {@link Model} object if the content type is pom,
     * otherwise returns {@link Optional#empty()}.
     *
     * @return Returns the resource as a Maven {@link Model} object if the content type is pom,
     * otherwise returns {@link Optional#empty()}
     * @throws ParseException if any exception is thrown during parsing
     */
    public Optional<Model> asPOM() {
        return isPom() ? ResourceType.POM.parseToPOMModel(this) : Optional.empty();
    }


    /**
     * Parses the resource as a Java {@link Properties} object.
     *
     * @return the resource as a Java {@link Properties} object
     * @throws ParseException if any exception is thrown during parsing
     */
    public Optional<Properties> asProperties(){
        return isProperties()
                ? ResourceType.PROPERTIES.parseToProperties(this)
                : Optional.empty();
    }

    /**
     * Parses the resource as a text string if the content type is
     * text or can be converted to text, otherwise returns {@link Optional#empty()}.
     *
     * @return the resource as a text string if the content type is
     * text or can be converted to text, otherwise returns {@link Optional#empty()}
     * @throws ParseException if any exception is thrown during parsing
     */
    public Optional<String> asText() {
        return isText()
                ? ResourceType.TEXT.parseToString(this)
                : Optional.empty();
    }

    /**
     * Parses the resource as an XML {@link Document} object if the content type is xml,
     * otherwise returns {@link Optional#empty()}.
     *
     * @return Returns the resource as an XML {@link Document} object if the content type is xml,
     * otherwise returns {@link Optional#empty()}
     * @throws ParseException if any exception is thrown during parsing
     */
    public Optional<Document> asXML() {
        return isXml() ? ResourceType.XML.parseToXML(this) : Optional.empty();
    }

    /**
     * Parses the resource as a Yaml object if the content type is yaml,
     * otherwise returns {@link Optional#empty()}.
     *
     * @param type the class to attempt to return an instance of
     * @param <T> the class type of the expected object to be returned
     * @return Returns the resource as a Yaml object if the content type is yaml,
     * otherwise returns {@link Optional#empty()}
     * @throws ParseException if any exception is thrown during parsing
     */
    public <T> Optional<T> asYaml(Class<T> type) {
        return isYaml() ? ResourceType.YAML.parseToYamlObject(this, type) : Optional.empty();
    }

    //endregion asX() Methods
}
