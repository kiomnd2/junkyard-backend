package junkyard.security.userdetails;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum UserRoles {
    ADMIN("ROLE_ADMIN"), USER("ROLE_USER");

    private final String description;
}
