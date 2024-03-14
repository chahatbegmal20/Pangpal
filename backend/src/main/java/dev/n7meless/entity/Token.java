package dev.n7meless.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Entity
@Getter
@ToString
@Setter
@AllArgsConstructor
@Table(name = "tokens")
public class Token {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "refresh_token")
    private String refreshToken;
    @Column(name = "issue_at")
    @CreatedDate
    private LocalDateTime issueAt;
    @Column(name = "expired_at")
    private LocalDateTime expiredAt;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;


    public Token(String refreshToken, LocalDateTime issueAt, LocalDateTime expiredAt) {
        this.refreshToken = refreshToken;
        this.issueAt = issueAt;
        this.expiredAt = expiredAt;
    }

    public Token() {
    }
}
