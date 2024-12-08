package junkyard.member.domain;


import jakarta.persistence.*;
import junkyard.BaseEntity;
import junkyard.common.response.codes.Codes;
import junkyard.common.response.exception.member.InvalidAdminPasswordException;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Entity
@Getter
@Table(name = "member_admins")
public class MemberAdmin extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "login_id", nullable = false, length = 100)
    private String loginId;

    @Column(name = "password", nullable = false, length = 255)
    private String password;

    @Builder
    public MemberAdmin(String loginId, String phoneNo, String email, String password) {
        this.loginId = loginId;
        this.password = password;
    }

    public void checkPassword(String password) {
        if (!this.password.equals(password)) {
            throw new InvalidAdminPasswordException(Codes.COMMON_INVALID_ADMIN, this.loginId, password);
        }
    }
}
