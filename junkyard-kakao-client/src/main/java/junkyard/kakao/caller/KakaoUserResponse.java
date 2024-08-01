package junkyard.kakao.caller;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.Map;

@ToString
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class KakaoUserResponse {
    private long id;
    private boolean hasSignedUp;
    private boolean isConnected;
    private LocalDateTime connectedAt;
    private LocalDateTime synchedAt;
    private Map<String, Object> properties;

    @JsonProperty("kakao_account")
    private KakaoAccount kakaoAccount;

    @JsonProperty("for_partner")
    private Partner forPartner;


    @ToString
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    static class KakaoAccount {
        @JsonProperty("profile_needs_agreement")
        private boolean profileNeedsAgreement;

        @JsonProperty("profile_nickname_needs_agreement")
        private boolean profileNicknameNeedsAgreement;

        @JsonProperty("profile_image_needs_agreement")
        private boolean profileImageNeedsAgreement;

        private Profile profile;

        @JsonProperty("name_needs_agreement")
        private boolean nameNeedsAgreement;

        private String name;

        @JsonProperty("email_needs_agreement")
        private boolean emailNeedsAgreement;

        @JsonProperty("is_email_valid")
        private boolean isEmailValid;

        @JsonProperty("is_email_verified")
        private boolean isEmailVerified;

        private String email;

        @JsonProperty("age_range_needs_agreement")
        private boolean ageRangeNeedsAgreement;

        @JsonProperty("age_range")
        private String ageRange;

        @JsonProperty("birthyear_needs_agreement")
        private boolean birthyearNeedsAgreement;

        private String birthyear;

        @JsonProperty("birthday_needs_agreement")
        private boolean birthdayNeedsAgreement;

        private String birthday;

        @JsonProperty("birthday_type")
        private String birthdayType;

        @JsonProperty("gender_needs_agreement")
        private boolean genderNeedsAgreement;

        private String gender;

        @JsonProperty("phone_number_needs_agreement")
        private boolean phoneNumberNeedsAgreement;

        @JsonProperty("phone_number")
        private String phoneNumber;

        @JsonProperty("ci_needs_agreement")
        private boolean ciNeedsAgreement;

        private String ci;

        @JsonProperty("ci_authenticated_at")
        private LocalDateTime ciAuthenticatedAt;
    }



    @ToString
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    static class Profile {
        private String nickname;

        @JsonProperty("thumbnail_image_url")
        private String thumbnailImageUrl;

        @JsonProperty("profile_image_url")
        private String profileImageUrl;

        @JsonProperty("is_default_image")
        private boolean isDefaultImage;

        @JsonProperty("is_default_nickname")
        private boolean isDefaultNickname;
    }

    @ToString
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    static class Partner {
        private String uuid;
    }
}

