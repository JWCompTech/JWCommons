package com.jwcomptech.commons.resources.enums;

import com.jwcomptech.commons.resources.Resource;
import com.jwcomptech.commons.resources.ResourceManager;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import org.jetbrains.annotations.NotNull;

@AllArgsConstructor
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
