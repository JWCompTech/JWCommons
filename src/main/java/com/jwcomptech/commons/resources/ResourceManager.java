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

import com.jwcomptech.commons.SingletonManager;
import com.jwcomptech.commons.resources.enums.ResourceType;
import com.jwcomptech.commons.tuples.ImmutablePair;
import com.jwcomptech.commons.tuples.Pair;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.UnmodifiableView;

import java.util.*;

import static com.jwcomptech.commons.Literals.cannotBeNull;
import static com.jwcomptech.commons.Literals.cannotBeNullOrEmpty;
import static com.jwcomptech.commons.validators.CheckIf.checkArgumentNotNull;
import static com.jwcomptech.commons.validators.CheckIf.checkArgumentNotNullOrEmpty;

/**
 * A resource manager for managing resources in the project "resources" directory.
 *
 * @since 0.0.1
 */
@SuppressWarnings("unused")
@EqualsAndHashCode
@ToString
public final class ResourceManager {
    private final Map<ResourceType, Set<Resource>> resources = new HashMap<>();

    @Contract(pure = true)
    public @NotNull @UnmodifiableView Map<ResourceType, Set<Resource>> getResources() {
        final Map<ResourceType, Set<Resource>> copy = new HashMap<>();
        for (final Map.Entry<ResourceType, Set<Resource>> entry : resources.entrySet()) {
            copy.put(entry.getKey(), Collections.unmodifiableSet(entry.getValue()));
        }
        return Collections.unmodifiableMap(copy);
    }

    @Contract("_ -> new")
    public @NotNull Pair<Optional<ResourceType>, Optional<String>> parseResourceURL(@NotNull final String resourceURL) {
        checkArgumentNotNull(resourceURL, cannotBeNull("resourceURL"));

        ResourceType resourceType = null;
        String resourceName = null;

        for(final ResourceType type : resources.keySet()) {
            if(resourceURL.startsWith(type.toString())) {
                resourceType = type;
                resourceName = resourceURL.substring(type.toString().length());
                break;
            }
        }

        return new ImmutablePair<>(Optional.ofNullable(resourceType),
                Optional.ofNullable(resourceName));
    }

    public Optional<Resource> getResource(@NotNull final ResourceType resourceType,
                                          @NotNull final String resourceName) {
        checkArgumentNotNull(resourceType, cannotBeNull("resourceType"));
        checkArgumentNotNullOrEmpty(resourceName, cannotBeNullOrEmpty("resourceName"));

        final Set<Resource> resourceList = resources.get(resourceType);
        if (resourceList != null) {
            for (final Resource resource : resourceList) {
                if(resource.getURLString().equals(resourceName)) {
                    return Optional.of(resource);
                }
            }
        }

        return Optional.empty();
    }

    public Optional<Resource> getResource(@NotNull final String resourceURL) {
        checkArgumentNotNull(resourceURL, cannotBeNull("resourceURL"));

        final Pair<Optional<ResourceType>, Optional<String>> resourcePair = parseResourceURL(resourceURL);

        if(resourcePair.getLeft().isPresent() && resourcePair.getRight().isPresent()) {
            final ResourceType resourceType = resourcePair.getLeft().get();
            final String resourceName = resourcePair.getRight().get();

            if(!resourceName.isBlank()) {
                final Set<Resource> resourceList = resources.get(resourceType);
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

    public Optional<Set<Resource>> getResources(@NotNull final ResourceType resourceType) {
        checkArgumentNotNull(resourceType, cannotBeNull("resourceType"));
        final Set<Resource> list = resources.get(resourceType);

        if (list != null) {
            return Optional.of(Collections.unmodifiableSet(list));
        }

        //Shouldn't ever happen as all resource types are predefined
        return Optional.empty();
    }

    public @Nullable Resource addResource(@NotNull final ResourceType resourceType,
                                          @NotNull final String resourceName) {
        checkArgumentNotNull(resourceType, cannotBeNull("resourceType"));
        checkArgumentNotNullOrEmpty(resourceName, cannotBeNullOrEmpty("resourceName"));

        if(resources.containsKey(resourceType)) {
            final Optional<Resource> resource = getResource(resourceType, resourceName);
            if(resource.isEmpty()) {
                final Resource newResource = new Resource(resourceType, resourceName);
                resources.get(resourceType).add(newResource);
                return newResource;
            } else {
                return resource.get();
            }
        }

        return null;
    }

    public Optional<Resource> addResource(@NotNull final String resourceURL) {
        final Pair<Optional<ResourceType>, Optional<String>> resourcePair = parseResourceURL(resourceURL);

        if(resourcePair.getKey().isPresent() && resourcePair.getValue().isPresent()) {
            final ResourceType resourceType = resourcePair.getKey().get();
            final String resourceName = resourcePair.getValue().get();

            if(!resourceName.isBlank()) {
                final Set<Resource> resourceList = resources.get(resourceType);
                if (resourceList != null) {
                    Resource resource = null;
                    for (final Resource r : resourceList) {
                        if(r.getURLString().equals(resourceName)) {
                            resource = r;
                        }
                    }

                    if(resource != null) {
                        resource = addResource(resourceType, resourceName);
                    }

                    return Optional.ofNullable(resource);
                }
            }
        }

        return Optional.empty();
    }

    public boolean resourceExists(@NotNull final ResourceType resourceType,
                                  @NotNull final String resourceName) {
        checkArgumentNotNull(resourceType, cannotBeNull("resourceType"));
        checkArgumentNotNullOrEmpty(resourceName, cannotBeNullOrEmpty("resourceName"));

        if(resources.containsKey(resourceType)) {
            for(final Resource r : resources.get(resourceType)) {
                if(r.getURLString().equals(resourceName)) {
                    return true;
                }
            }
        }

        return false;
    }

    @SuppressWarnings("BooleanMethodNameMustStartWithQuestion")
    public boolean removeResource(@NotNull final ResourceType resourceType,
                                  @NotNull final String resourceName) {
        checkArgumentNotNull(resourceType, cannotBeNull("resourceType"));
        checkArgumentNotNullOrEmpty(resourceName, cannotBeNullOrEmpty("resourceName"));

        Resource resourceToRemove = null;

        if(resources.containsKey(resourceType)
                && resourceExists(resourceType, resourceName)) {
            for(final Resource r : resources.get(resourceType)) {
                if(r.getURLString().equals(resourceName)) {
                    resourceToRemove = r;
                    break;
                }
            }
        }

        if(resourceToRemove != null) {
            return resources.get(resourceType).remove(resourceToRemove);
        }

        return false;
    }

    public static ResourceManager getInstance() {
        return SingletonManager.getInstance(ResourceManager.class, ResourceManager::new);
    }

    /** Prevents public instantiation of this manager class. */
    @SuppressWarnings("ObjectAllocationInLoop")
    private ResourceManager() {
        for(final ResourceType resourceType : ResourceType.values()) {
            resources.put(resourceType, new HashSet<>());
        }
    }
}
