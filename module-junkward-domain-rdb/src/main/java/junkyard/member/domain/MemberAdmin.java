package junkyard.member.domain;


import jakarta.persistence.*;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@Entity
@Table(name = "member_admins")
public class MemberAdmin {

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

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "last_visit_at", nullable = false)
    private LocalDateTime lastVisitAt;

    @Builder
    public MemberAdmin(String name, String phoneNo, String email, String password
            , LocalDateTime createdAt, LocalDateTime lastVisitAt) {
        this.name = name;
        this.phoneNo = phoneNo;
        this.email = email;
        this.password = password;
        this.createdAt = createdAt;
        this.lastVisitAt = lastVisitAt;
    }
}
