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

import com.jwcomptech.commons.resources.enums.ResourceType;
import com.jwcomptech.commons.utils.SingletonManager;
import com.jwcomptech.commons.resources.enums.ResourceDir;
import com.jwcomptech.commons.tuples.ImmutablePair;
import com.jwcomptech.commons.tuples.Pair;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.UnmodifiableView;
import com.jwcomptech.commons.resources.enums.Builtin;

import java.util.*;


import static com.jwcomptech.commons.consts.Literals.cannotBeNull;
import static com.jwcomptech.commons.consts.Literals.cannotBeNullOrEmpty;
import static com.jwcomptech.commons.validators.Preconditions.checkArgumentNotNull;
import static com.jwcomptech.commons.validators.Preconditions.checkArgumentNotNullOrEmpty;

/**
 * A resource manager for managing resources in the project "resources" directory.
 *
 * @since 0.0.1
 * @implNote To comply with separation of concerns (SoC), keep specific resource type
 * related methods and logic out of this class. All of those methods
 * should be solely contained in the {@link Resource} or {@link ResourceType} objects.
 */
@SuppressWarnings("unused")
@EqualsAndHashCode
@ToString
public final class ResourceManager {
    private final Map<ResourceDir, Set<Resource>> resources = new HashMap<>();

    public static ResourceManager getInstance() {
        return SingletonManager.getInstance(ResourceManager.class, ResourceManager::new);
    }

    /** Prevents public instantiation of this manager class. */
    @SuppressWarnings("ObjectAllocationInLoop")
    private ResourceManager() {
        for(final ResourceDir resourceDir : ResourceDir.values()) {
            resources.put(resourceDir, new HashSet<>());
        }

        for(final var resource : Builtin.values()) {
            addResource(resource);
        }
    }

    /**
     * Returns all resources currently tracked.
     * @return all resources currently tracked
     */
    @Contract(pure = true)
    public @NotNull @UnmodifiableView Map<ResourceDir, Set<Resource>> getResources() {
        final Map<ResourceDir, Set<Resource>> copy = new HashMap<>();
        for (final Map.Entry<ResourceDir, Set<Resource>> entry : resources.entrySet()) {
            copy.put(entry.getKey(), Collections.unmodifiableSet(entry.getValue()));
        }
        return Collections.unmodifiableMap(copy);
    }

    /**
     * Parses the specified resource path and returns the matching {@link ResourceDir}
     * and the path filename.
     *
     * @param resourcePath the path of the resource file
     * @return the matching {@link ResourceDir} and the path filename
     */
    @Contract("_ -> new")
    public @NotNull Pair<Optional<ResourceDir>, Optional<String>> parseResourceURL(@NotNull final String resourcePath) {
        checkArgumentNotNull(resourcePath, cannotBeNull("resourcePath"));

        ResourceDir resourceDir = null;
        String resourceName = null;

        for(final ResourceDir type : resources.keySet()) {
            if(resourcePath.startsWith(type.toString())) {
                resourceDir = type;
                resourceName = resourcePath.substring(type.toString().length());
                break;
            }
        }

        return new ImmutablePair<>(Optional.ofNullable(resourceDir),
                Optional.ofNullable(resourceName));
    }

    /**
     * Returns the specified {@link Builtin} resource.
     *
     * @param resource the resource to lookup
     * @return the specified {@link Builtin} resource
     */
    @SuppressWarnings("OptionalGetWithoutIsPresent") //Builtins will always be present
    public @NotNull Resource getResource(@NotNull final Builtin resource) {
        return getResource(resource.getType(), resource.getPath()).get();
    }

    /**
     * Returns the tracked {@link Resource} matching the specified {@link ResourceDir} and filename.
     *
     * @param resourceDir the resource directory to lookup
     * @param resourceName the resource filename to lookup
     * @return the tracked {@link Resource} matching the specified {@link ResourceDir} and filename
     */
    public Optional<Resource> getResource(@NotNull final ResourceDir resourceDir,
                                          @NotNull final String resourceName) {
        checkArgumentNotNull(resourceDir, cannotBeNull("resourceType"));
        checkArgumentNotNullOrEmpty(resourceName, cannotBeNullOrEmpty("resourceName"));

        final Set<Resource> resourceList = resources.get(resourceDir);
        if (resourceList != null) {
            for (final Resource resource : resourceList) {
                if(resource.getFileName().equals(resourceName)) {
                    return Optional.of(resource);
                }
            }
        }

        return Optional.empty();
    }

    /**
     * Returns the tracked {@link Resource} matching the specified path.
     *
     * @param resourcePath the path of the resource to lookup
     * @return the tracked {@link Resource} matching the specified path
     */
    public Optional<Resource> getResource(@NotNull final String resourcePath) {
        checkArgumentNotNull(resourcePath, cannotBeNull("resourcePath"));

        final Pair<Optional<ResourceDir>, Optional<String>> resourcePair = parseResourceURL(resourcePath);

        if(resourcePair.getLeft().isPresent() && resourcePair.getRight().isPresent()) {
            final ResourceDir resourceDir = resourcePair.getLeft().get();
            final String resourceName = resourcePair.getRight().get();

            if(!resourceName.isBlank()) {
                final Set<Resource> resourceList = resources.get(resourceDir);
                if (resourceList != null) {
                    for (final Resource resource : resourceList) {
                        if(resource.getURLString().equals(resourceName)) {
                            return Optional.of(resource);
                        }
                    }
                }
            }
        }

        return Optional.empty();
    }

    /**
     * Returns all tracked {@link Resource} objects from the specified {@link ResourceDir}.
     *
     * @param resourceDir the directory to lookup
     * @return all tracked {@link Resource} objects from the specified {@link ResourceDir}
     */
    public Optional<Set<Resource>> getResources(@NotNull final ResourceDir resourceDir) {
        checkArgumentNotNull(resourceDir, cannotBeNull("resourceType"));
        final Set<Resource> list = resources.get(resourceDir);

        if (list != null) {
            return Optional.of(Collections.unmodifiableSet(list));
        }

        //Shouldn't ever happen as all resource types are predefined
        return Optional.empty();
    }

    /**
     * Adds the specified {@link Builtin} resource to be tracked.
     *
     * @param resource the resource to add
     * @return the resource passed in for method chaining
     */
    @SuppressWarnings("UnusedReturnValue")
    private @NotNull Resource addResource(final @NotNull Builtin resource) {
        return addResource(resource.getType(), resource.getPath());
    }

    /**
     * Adds the specified resource to be tracked.
     *
     * @param resourceDir the resource directory
     * @param resourceName the resource name
     * @return the newly created {@link Resource} instance
     */
    public @NotNull Resource addResource(@NotNull final ResourceDir resourceDir,
                                         @NotNull final String resourceName) {
        checkArgumentNotNull(resourceDir, cannotBeNull("resourceType"));
        checkArgumentNotNullOrEmpty(resourceName, cannotBeNullOrEmpty("resourceName"));

        if(!resources.containsKey(resourceDir)) {
            resources.put(resourceDir, new HashSet<>());
        }

        final Optional<Resource> resource = getResource(resourceDir, resourceName);
        if(resource.isEmpty()) {
            final Resource newResource = Resource.of(resourceDir, resourceName);
            resources.get(resourceDir).add(newResource);
            return newResource;
        } else {
            return resource.get();
        }
    }

    /**
     * Adds the specified resource to be tracked.
     *
     * @param resourceDir the resource directory
     * @param resource the resource
     * @return the {@link Resource} passed in for method chaining
     */
    @SuppressWarnings("UnusedReturnValue")
    public Resource addResource(@NotNull final ResourceDir resourceDir,
                                @NotNull final Resource resource) {
        checkArgumentNotNull(resourceDir, cannotBeNull("resourceType"));
        checkArgumentNotNull(resource, cannotBeNull("resource"));

        if(!resources.containsKey(resourceDir)) {
            resources.put(resourceDir, new HashSet<>());
        }

        return resource;
    }

    /**
     * Adds the specified resource to be tracked.
     *
     * @param resourcePath the path to the resource
     * @return the newly created {@link Resource} instance
     */
    public Optional<Resource> addResource(@NotNull final String resourcePath) {
        final Pair<Optional<ResourceDir>, Optional<String>> resourcePair = parseResourceURL(resourcePath);

        if(resourcePair.getKey().isPresent() && resourcePair.getValue().isPresent()) {
            final ResourceDir resourceDir = resourcePair.getKey().get();
            final String resourceName = resourcePair.getValue().get();

            if(!resourceName.isBlank()) {
                final Set<Resource> resourceList = resources.get(resourceDir);
                if (resourceList != null) {
                    if(!resourceExists(resourceDir, resourceName)) {
                        return Optional.of(addResource(resourceDir, resourceName));
                    }
                }
            } else {
                throw new IllegalArgumentException("Resource url must be a file path not a directory path.");
            }
        }

        return Optional.empty();
    }

    /**
     * Checks if the specified resource is currently being tracked.
     *
     * @param resourceDir the resource directory
     * @param resourceFileName the resource filename
     * @return true if the specified resource is currently being tracked
     */
    public boolean resourceExists(@NotNull final ResourceDir resourceDir,
                                  @NotNull final String resourceFileName) {
        checkArgumentNotNull(resourceDir, cannotBeNull("resourceType"));
        checkArgumentNotNullOrEmpty(resourceFileName, cannotBeNullOrEmpty("resourceFileName"));

        if(resources.containsKey(resourceDir)) {
            for(final Resource r : resources.get(resourceDir)) {
                if(r.getURLString().equals(resourceFileName)) {
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * Checks if the specified resource is currently being tracked.
     *
     * @param resourceDir the resource directory
     * @param resource the resource
     * @return true if the specified resource is currently being tracked
     */
    public boolean resourceExists(@NotNull final ResourceDir resourceDir,
                                  @NotNull final Resource resource) {
        checkArgumentNotNull(resourceDir, cannotBeNull("resourceType"));
        checkArgumentNotNull(resource, cannotBeNull("resource"));

        if(resources.containsKey(resourceDir)) {
            for(final Resource r : resources.get(resourceDir)) {
                if(r.equals(resource)) {
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * Removes the specified resource from being tracked.
     *
     * @param resourceDir the resource directory
     * @param resourceFileName the resource filename
     * @return true if the resource was removed successfully
     */
    @SuppressWarnings("BooleanMethodNameMustStartWithQuestion")
    public boolean removeResource(@NotNull final ResourceDir resourceDir,
                                  @NotNull final String resourceFileName) {
        checkArgumentNotNull(resourceDir, cannotBeNull("resourceType"));
        checkArgumentNotNullOrEmpty(resourceFileName, cannotBeNullOrEmpty("resourceFileName"));

        Resource resourceToRemove = null;

        if(resources.containsKey(resourceDir)
                && resourceExists(resourceDir, resourceFileName)) {
            for(final Resource r : resources.get(resourceDir)) {
                if(r.getURLString().equals(resourceFileName)) {
                    resourceToRemove = r;
                    break;
                }
            }
        }

        if(resourceToRemove != null) {
            return resources.get(resourceDir).remove(resourceToRemove);
        }

        return false;
    }
}
