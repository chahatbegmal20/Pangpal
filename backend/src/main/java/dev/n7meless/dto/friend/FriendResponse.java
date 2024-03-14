package dev.n7meless.dto.friend;

import com.fasterxml.jackson.annotation.JsonProperty;
import dev.n7meless.entity.User;
import dev.n7meless.model.FriendStatus;

import java.time.LocalDate;

public record FriendResponse(Long id,
                             LocalDate date,
                             @JsonProperty("my_friend") User myFriend,
                             FriendStatus status) {
}
