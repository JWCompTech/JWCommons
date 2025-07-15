package com.jwcomptech.commons.resources.enums;

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

import com.jwcomptech.commons.resources.Resource;
import com.jwcomptech.commons.resources.ResourceManager;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.jetbrains.annotations.NotNull;

@RequiredArgsConstructor
@Getter
@ToString
public enum Builtin {
    Red_Lock_IMG(ResourceDir.IMAGE, "Lock-Red.png"),
    Data_Secure_IMG(ResourceDir.IMAGE, "Data-Secure.png"),
    Key_IMG(ResourceDir.IMAGE, "Key.png"),
    User_Login_IMG(ResourceDir.IMAGE, "User-Login.png"),
    Login_Dialog_CSS(ResourceDir.CSS, "Login-Dialog.css"),

    ;

    private final ResourceDir type;
    private final String path;

    public @NotNull Resource getResource() {
        final ResourceManager resources = ResourceManager.getInstance();
        return resources.getResource(this);
    }
}
