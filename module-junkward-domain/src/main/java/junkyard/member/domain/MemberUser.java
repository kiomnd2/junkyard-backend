package junkyard.member.domain;

import jakarta.persistence.*;
import junkyard.BaseEntity;
import junkyard.common.response.exception.InvalidTypeException;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "member_users")
public class MemberUser extends BaseEntity {

    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "auth_id", nullable = false, unique = true)
    private Long authId;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "profile_url", length = 200)
    private String profileUrl;

    @Column(name = "phone_no", nullable = false, length = 20)
    private String phoneNo;

    @Column(name = "email", length = 100)
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(name = "join_method" ,length=20, nullable = false)
    private JoinMethod joinMethod;

    @RequiredArgsConstructor
    @Getter
    public enum JoinMethod {
        KAKAO("kakao"), TOSS("toss");

        private final String description;
        public static JoinMethod get(String method) {
            JoinMethod[] values = values();
            for (JoinMethod value : values) {
                if (method.equals(value.getDescription())) {
                    return value;
                }
            }
            throw new InvalidTypeException(method);
        }
    }

    @Builder
    public MemberUser(Long authId, String name, String phoneNo, String email, JoinMethod joinMethod) {
        this.authId = authId;
        this.name = name;
        this.phoneNo = phoneNo;
        this.email = email;
        this.joinMethod = joinMethod;
    }

    public MemberInfo toInfo() {
        return MemberInfo.builder()
                .authId(authId)
                .name(name)
                .profileUrl(profileUrl)
                .phoneNo(phoneNo)
                .email(email)
                .build();
    }
}
