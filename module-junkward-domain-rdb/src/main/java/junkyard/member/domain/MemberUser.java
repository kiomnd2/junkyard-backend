package junkyard.member.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@Entity
@Table(name = "member_users")
public class MemberUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "auth_id", nullable = false, unique = true)
    private Long authId;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "phone_no", nullable = false, length = 20)
    private String phoneNo;

    @Column(name = "email", length = 100)
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(name = "join_method" ,length=20, nullable = false)
    private JoinMethod joinMethod;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "last_visit_at", nullable = false)
    private LocalDateTime lastVisitAt;

    public enum JoinMethod {
        KAKAO, TOSS
    }

    @Builder
    public MemberUser(Long authId, String name, String phoneNo, String email, JoinMethod joinMethod,
                      LocalDateTime createdAt, LocalDateTime lastVisitAt) {
        this.authId = authId;
        this.name = name;
        this.phoneNo = phoneNo;
        this.email = email;
        this.joinMethod = joinMethod;
        this.createdAt = createdAt;
        this.lastVisitAt = lastVisitAt;
    }
}