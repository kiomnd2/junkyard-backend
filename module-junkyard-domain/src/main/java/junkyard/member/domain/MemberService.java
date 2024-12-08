package junkyard.member.domain;

public interface MemberService {
    MemberInfo registerMember(MemberCommand.MemberRegisterCommand registerCommand);
    boolean checkMember(Long authId);
    MemberInfo findMember(Long authId);
    void checkAdmin(MemberCommand.AdminLoginCommand command);

}
