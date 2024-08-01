package junkyard.member.infrastructure.caller;

public interface AccessTokenCaller {
    boolean supports(String method);
    AccessTokenResponse trigger(String code);
}
