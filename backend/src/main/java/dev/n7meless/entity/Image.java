package dev.n7meless.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import dev.n7meless.entity.enums.ImageType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.annotation.CreatedDate;

import java.io.Serializable;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "images")
public class Image implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @CreatedDate
    @Column(name = "created_dt")
    private LocalDate createdDt;
    private String fileName;
    private Long size;
    private String path;
    private String description;
    private String contentType;
    @Enumerated(EnumType.STRING)
    private ImageType imageType;
    @Transient
    private byte[] bytes;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "album_id", referencedColumnName = "id")
    @JsonBackReference
    private Album album;
}
