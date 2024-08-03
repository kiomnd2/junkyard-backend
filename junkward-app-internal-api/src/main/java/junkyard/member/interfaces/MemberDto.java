package junkyard.member.interfaces;

import junkyard.member.domain.MemberRegisterCommand;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class MemberDto {

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RequestJoin {
        private Long id;
        private String method;
        private String name;
        private String phoneNo;
        private String email;

        public MemberRegisterCommand toCommand() {
            return MemberRegisterCommand.builder()
                    .id(id)
                    .method(method)
                    .name(name)
                    .phoneNo(phoneNo)
                    .email(email)
                    .build();
        }
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ResponseJoin {
        private String token;
    }
}
