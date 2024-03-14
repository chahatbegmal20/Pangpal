package dev.n7meless.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import dev.n7meless.model.FriendStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "friends")
public class Friend {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "created_dt")
    @JsonProperty("created_dt")
    private LocalDate createdDt;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user1_id")
    @JsonProperty("first_user")
    private User firstUser;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user2_id")
    @JsonProperty("second_user")
    private User secondUser;

    @Column(name = "initiator_id")
    @JsonProperty("initiator_id")
    private long initiatorId;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "status")
    private FriendStatus status;


    public Friend(User firstUser, User secondUser, long initiatorId, FriendStatus status, LocalDate createdDt) {
        this.firstUser = firstUser;
        this.secondUser = secondUser;
        this.initiatorId = initiatorId;
        this.status = status;
        this.createdDt = createdDt;
    }
}
