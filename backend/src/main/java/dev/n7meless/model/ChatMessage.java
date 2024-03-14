package dev.n7meless.model;


import com.fasterxml.jackson.annotation.JsonProperty;
import dev.n7meless.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user1_id")
    @JsonProperty("user1")
    private User firstUser;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user2_id")
    @JsonProperty("user2")
    private User secondUser;

    @Column(name = "text")
    private String text;
    @Column(name = "created_date")
    @CreatedDate
    private LocalDateTime date;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "status")
    private MessageStatus messageStatus;

    public enum MessageStatus {
        ACCEPTED, NOT_ACCEPTED

    }

    public static ChatMessage of(User firstUser, User secondUser, String text) {
        return new ChatMessage(firstUser, secondUser, text, MessageStatus.NOT_ACCEPTED);
    }

    private ChatMessage(User firstUser, User secondUser, String text, MessageStatus status) {
        this.firstUser = firstUser;
        this.secondUser = secondUser;
        this.text = text;
        this.messageStatus = status;
    }
}
