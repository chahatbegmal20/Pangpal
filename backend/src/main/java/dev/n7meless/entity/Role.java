package dev.n7meless.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.PersistenceCreator;

import java.io.Serializable;


@Getter
@Setter
@ToString
@Entity
@Table(name = "roles")
@NoArgsConstructor
public class Role implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;
    @Column(name = "name")
    private String name;

    public static Role of(String name) {
        return new Role(name);
    }

    @PersistenceCreator
    private Role(String name) {
        this.name = name;
    }
}
