package junkyard.member.domain;

public interface MemberService {
    Long registerMember(MemberRegisterCommand registerCommand);
    boolean checkMember(Long authId);
}
