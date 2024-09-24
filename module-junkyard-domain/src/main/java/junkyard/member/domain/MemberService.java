package junkyard.member.domain;

public interface MemberService {
    MemberInfo registerMember(MemberRegisterCommand registerCommand);
    boolean checkMember(Long authId);
    MemberInfo findMember(Long authId);
}
