package junkyard.member.infrastructure.caller;

public interface UserInfoCaller {
    boolean supports(String method);
    UserInfoResponse call(String accessToken);
}
