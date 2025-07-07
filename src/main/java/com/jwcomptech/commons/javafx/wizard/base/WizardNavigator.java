package com.jwcomptech.commons.javafx.wizard.base;

import com.jwcomptech.commons.javafx.controls.FXButton;
import com.jwcomptech.commons.javafx.wizard.enums.WizardPages;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Data
@Setter(AccessLevel.PRIVATE)
public class WizardNavigator<T extends AbstractWizard<T>> {
    private AbstractWizard<T> parent = null;
    private WizardPage currentPage = null;
    private int currentPageIndex = 0;

    private final List<WizardPages> pages = new ArrayList<>();

    public static String NEXT_BUTTON = "wizard_next";
    public static String BACK_BUTTON = "wizard_back";
    public static String CANCEL_BUTTON = "wizard_cancel";

    public WizardNavigator<T> setParent(final AbstractWizard<T> parent) {
        if(this.parent == null) {
            this.parent = parent;
        }

        return this;
    }

    public final void start() {
        showPage(0);
    }

    public final void nextPage() {
        if(currentPageIndex + 1 < pages.size()) {
            showPage(currentPageIndex);
        }
    }

    public final void previousPage() {
        if(currentPageIndex - 1 >= 0) {
            showPage(currentPageIndex);
        }
    }

    public final void navigateTo(final WizardPages page) {
        if(page != null) {
            int pageIndex = pages.indexOf(page);
            if (pageIndex != -1) {
                showPage(pageIndex);
            }
        }
    }

    public final void showPage(int index) {
        if (index >= 0 && index < pages.size()) {
            currentPageIndex = index;  // Update the current index!
            currentPage = parent.getPageLoader().getPageMap().get(pages.get(index));
            parent.getContent().setCenter(((AbstractWizardPageController) currentPage).getRoot());
        }
    }

    public final void onNext() {
        currentPage.beforeNext();
        nextPage();
        currentPage.afterNext();
    }

    public final void onPrevious() {
        currentPage.beforePrevious();
        previousPage();
        currentPage.afterPrevious();
    }

    public final void onCancel() {
        currentPage.onCancel();
    }

    public final void onFinish() {
        currentPage.afterFinished();
    }

    public final FXButton getBackButton() {
        return parent.getContext().getBackButton();
    }

    public final FXButton getNextButton() {
        return parent.getContext().getNextButton();
    }

    public final FXButton getCancelButton() {
        return parent.getContext().getCancelButton();
    }
}
