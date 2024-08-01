package junkyard.domain.member;


import jakarta.persistence.*;
import junkyard.domain.BaseEntity;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@Entity
@Table(name = "member_admins")
public class MemberAdmin extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "phone_no", nullable = false, length = 20)
    private String phoneNo;

    @Column(name = "email", unique = true, length = 100)
    private String email;

    @Column(name = "password", nullable = false, length = 255)
    private String password;

    @Builder
    public MemberAdmin(String name, String phoneNo, String email, String password) {
        this.name = name;
        this.phoneNo = phoneNo;
        this.email = email;
        this.password = password;
    }
}
