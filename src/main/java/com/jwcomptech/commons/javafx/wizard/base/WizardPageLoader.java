package com.jwcomptech.commons.javafx.wizard.base;

/*-
 * #%L
 * JWCommons
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

import com.jwcomptech.commons.javafx.FXMLEntity;
import com.jwcomptech.commons.javafx.wizard.enums.WizardPages;
import com.jwcomptech.commons.resources.Resource;
import com.jwcomptech.commons.resources.ResourceManager;
import com.jwcomptech.commons.resources.enums.ResourceDir;
import javafx.scene.Parent;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.UnmodifiableView;

import java.util.*;

@SuppressWarnings("unused")
@Data
@Setter(AccessLevel.PRIVATE)
public class WizardPageLoader<T extends AbstractWizard<T>> {
    private AbstractWizard<T> parent = null;
    private final List<WizardPages> pages = new ArrayList<>();
    private final Map<WizardPages, WizardPage> pageMap = new LinkedHashMap<>();
    protected final Map<WizardPages, AbstractWizardPageController> pageControllersMap = new LinkedHashMap<>();

    public WizardPageLoader<T> setParent(final AbstractWizard<T> parent) {
        if(this.parent == null) {
            this.parent = parent;
        }

        return this;
    }

    public void throwIfParentNotSet() {
        if(parent == null) {
            throw new IllegalStateException("Parent wizard page has not been set");
        }
    }

    public final WizardPageLoader<T> addPage(WizardPages page) {
        throwIfParentNotSet();
        pages.add(page);
        pageMap.put(page, loadPage(page));

        return this;
    }

    private @NotNull WizardPage loadPage(@NotNull WizardPages page) {
        throwIfParentNotSet();
        final ResourceManager manager = ResourceManager.getInstance();
        Optional<Resource> resource = manager.getResource(
                ResourceDir.FXML, page.getFXMLPath());

        if (resource.isEmpty()) {
            resource = Optional.of(manager.addResource(
                    ResourceDir.FXML, page.getFXMLPath()));
        }

        FXMLEntity<AbstractWizardPageController> entity = FXMLEntity.of(resource.get());
        Parent root;

        if(!entity.isDialog()) {
            //noinspection OptionalGetWithoutIsPresent
            root = entity.getRoot().get();
        } else {
            throw new IllegalArgumentException("Provided FXML should not be a Dialog!");
        }

        AbstractWizardPageController controller = entity.getController();
        pageControllersMap.put(page, controller);
        controller.setContext(parent.getContext());
        controller.setRoot(root);
        return controller;
    }

    public final AbstractWizardPageController getPageController(WizardPages page) {
        return pageControllersMap.get(page);
    }

    @Contract(pure = true)
    public final @NotNull @UnmodifiableView List<WizardPages> getPages() {
        return Collections.unmodifiableList(pages);
    }

    @Contract(pure = true)
    public final @NotNull @UnmodifiableView Map<WizardPages, WizardPage> getPageMap() {
        return Collections.unmodifiableMap(pageMap);
    }

    @Contract(pure = true)
    public final @NotNull @UnmodifiableView Map<WizardPages, AbstractWizardPageController> getPageControllersMap() {
        return Collections.unmodifiableMap(pageControllersMap);
    }
}
