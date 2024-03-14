package dev.n7meless.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.annotation.CreatedDate;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Setter
@Getter
@ToString
@Table(name = "posts")
@AllArgsConstructor
@NoArgsConstructor
public class Post implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String title;
    private String text;
    @CreatedDate
    @Column(name = "created_dt")
    private LocalDateTime createdDt;
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;
    @OneToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "album_id", referencedColumnName = "id")
    private Album album;
}
