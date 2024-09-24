package junkyard.member.domain;


import lombok.Builder;

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
