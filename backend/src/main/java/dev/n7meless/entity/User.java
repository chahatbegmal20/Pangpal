package dev.n7meless.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import dev.n7meless.model.Status;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.PersistenceCreator;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Predicate;

@Entity
@Getter
@Setter
@Table(name = "users")
@NoArgsConstructor
public class User implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;
    private String email;
    @Column(name = "first_name")
    @JsonProperty("first_name")
    private String firstName;
    @Column(name = "last_name")
    @JsonProperty("last_name")
    private String lastName;
    private String password;
    @CreatedDate
    @Column(name = "created_dt")
    private LocalDateTime createdDate;

    @ToString.Include(name = "password")
    private String maskPassword() {
        return "********";
    }

    @ManyToMany
    @JoinTable(
            name = "user_role",
            joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id")
    )
    private Set<Role> roles;

    @OrderBy("createdDt ASC")
    @OneToMany(mappedBy = "user",
            cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Post> posts;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "status")
    private Status status;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Album> albums;

    @JsonIgnore
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private final Set<Token> tokens = new HashSet<>();

    @JsonIgnore
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private final Set<PasswordRecovery> passwordRecoveries = new HashSet<>();

    public static User of(String email, String firstName, String lastName,
                          String password, Set<Role> role, Status status) {
        return new User(email, firstName, lastName,
                password, role, status, Collections.emptyList(), Collections.emptyList());
    }

    @PersistenceCreator
    private User(String email, String firstName,
                 String lastName, String password, Set<Role> role, Status status,
                 Collection<Token> tokens, Collection<PasswordRecovery> passwordRecoveries) {
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
        this.roles = role;
        this.status = status;
        this.tokens.addAll(tokens);
        this.passwordRecoveries.addAll(passwordRecoveries);
    }

    public void addToken(Token token) {
        this.tokens.add(token);
    }

    public Boolean removeTokenIf(Predicate<? super Token> predicate) {
        return this.tokens.removeIf(predicate);
    }

    public void addPasswordRecovery(PasswordRecovery passwordRecovery) {
        this.passwordRecoveries.add(passwordRecovery);
    }

    public Boolean removePasswordRecovery(PasswordRecovery passwordRecovery) {
        return this.passwordRecoveries.remove(passwordRecovery);
    }

    public Boolean removePasswordRecoveryIf(Predicate<? super PasswordRecovery> predicate) {
        return this.passwordRecoveries.removeIf(predicate);
    }
}
