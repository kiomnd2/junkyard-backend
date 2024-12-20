package junkyard.member.domain;

import lombok.Builder;

public class MemberCommand {
    @Builder
    public record MemberRegisterCommand(Long id, String method, String name, String profileUrl, String phoneNo, String email) {

        public MemberUser toEntity() {
            return MemberUser.builder()
                    .authId(id)
                    .joinMethod(MemberUser.JoinMethod.get(method))
                    .name(name)
                    .phoneNo(phoneNo)
                    .email(email)
                    .build();
        }
    }

    @Builder
    public record AdminLoginCommand(String loginId, String password) {

        public MemberAdmin toEntity() {
            return MemberAdmin.builder()
                    .loginId(loginId)
                    .password(password)
                    .build();
        }
    }

}
