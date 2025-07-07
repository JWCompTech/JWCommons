package com.jwcomptech.commons.javafx.wizard.base;

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
