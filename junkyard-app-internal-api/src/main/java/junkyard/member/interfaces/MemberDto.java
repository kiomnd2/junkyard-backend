package junkyard.member.interfaces;

import jakarta.validation.constraints.*;
import junkyard.member.application.TokenInfo;
import junkyard.member.domain.MemberCommand;
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

        @NotNull(message = "ID는 필수 값입니다.")
        private Long id;

        @NotBlank(message = "Method는 필수 값입니다.")
        private String method;

        @NotBlank(message = "Name은 필수 값입니다.")
        @Size(max = 100, message = "Name은 최대 100자까지 허용됩니다.")
        private String name;

        private String profileUrl;

        @NotBlank(message = "Phone number는 필수 값입니다.")
        @Pattern(regexp = "^\\+?[0-9. ()-]{7,25}$", message = "유효한 전화번호를 입력해주세요.")
        private String phoneNo;

        @NotBlank(message = "Email은 필수 값입니다.")
        @Email(message = "유효한 이메일 주소를 입력해주세요.")
        private String email;

        public MemberCommand.MemberRegisterCommand toCommand() {
            return MemberCommand.MemberRegisterCommand.builder()
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
        private TokenInfo token;
    }



    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ResponseRefresh {
        private TokenInfo token;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RequestAdminLogin {
        private String loginId;
        private String password;

        public MemberCommand.AdminLoginCommand toCommand() {
            return MemberCommand.AdminLoginCommand.builder()
                    .loginId(loginId)
                    .password(password)
                    .build();
        }
    }
}
