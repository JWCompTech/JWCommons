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

import com.jwcomptech.commons.javafx.controls.FXButton;
import com.jwcomptech.commons.javafx.wizard.enums.WizardPages;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unused")
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
