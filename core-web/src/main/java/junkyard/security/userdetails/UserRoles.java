package junkyard.security.userdetails;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum UserRoles {
    ADMIN("ROLE_ADMIN"), USER("ROLE_USER");

    private final String description;

    public static UserRoles get(final String type) {
        UserRoles[] values = values();
        for (UserRoles value : values) {
            if (type.equals(value.name())) {
                return value;
            }
        }
        throw new IllegalArgumentException("해당 type 은 잘못된 타입입니다 " + type);
    }
}
