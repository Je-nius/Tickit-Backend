package jenius.userservice.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@NoArgsConstructor
@Table(name = "users")
@Getter
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String loginId;
    private String password;
    private String username;
    private String email;
    private LocalDate birth;
    private String phoneNumber;
    @Enumerated(EnumType.STRING)
    private UserRole role;

    @Builder
    public User(String loginId, String password, String username, String email, LocalDate birth, String phoneNumber,
                UserRole role) {
        this.loginId = loginId;
        this.password = password;
        this.username = username;
        this.email = email;
        this.birth = birth;
        this.phoneNumber = phoneNumber;
        this.role = role;
    }

}
