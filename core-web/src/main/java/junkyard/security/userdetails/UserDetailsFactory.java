package junkyard.security.userdetails;

public interface UserDetailsFactory {
    boolean trigger(UserRoles roles);
}
