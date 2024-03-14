package dev.n7meless.controller;

import dev.n7meless.dto.friend.FriendRequest;
import dev.n7meless.dto.friend.FriendResponse;
import dev.n7meless.entity.User;
import dev.n7meless.exception.UserNotFoundError;
import dev.n7meless.service.AuthService;
import dev.n7meless.service.FriendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/friends")
public class FriendController {

    private final AuthService authService;
    private final FriendService friendService;

    @Autowired
    public FriendController(AuthService authService, FriendService friendService) {
        this.authService = authService;
        this.friendService = friendService;
    }


    @PostMapping("/send")
    public void send(@RequestBody FriendRequest friendRequest) {
        Optional<User> user =
                Optional.of(authService.getUserFromToken(friendRequest.token()));
        if (!user.isEmpty()) friendService.saveFriend(user.get(), friendRequest.friend());
        else throw new UserNotFoundError();
    }


    @PostMapping
    public List<FriendResponse> getFriendList(FriendRequest friendRequest) {
        if (Optional.of(authService.getUserFromToken(friendRequest.token())).isEmpty())
            throw new UserNotFoundError();

        List<FriendResponse> myFriends = friendService.getFriendList(friendRequest.friend()).stream()
                .map(friend -> new FriendResponse(friend.getId(), friend.getCreatedDt(),
                        friend.getFirstUser().getId() == friendRequest.friend() ? friend.getSecondUser() : friend.getFirstUser(),
                        friend.getStatus()))
                .collect(Collectors.toList());
        return myFriends;
    }

    @PostMapping("/accept")
    public void accept(@RequestBody FriendRequest friendRequest) {
        Optional<User> user =
                Optional.of(authService.getUserFromToken(friendRequest.token()));
        if (!user.isEmpty()) friendService.accept(user.get(), friendRequest.friend());
        else throw new UserNotFoundError();
    }

}
