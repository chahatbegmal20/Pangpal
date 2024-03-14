package dev.n7meless.service;

import dev.n7meless.entity.Friend;
import dev.n7meless.entity.User;
import dev.n7meless.exception.UserNotFoundError;
import dev.n7meless.model.FriendStatus;
import dev.n7meless.repository.FriendRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;

@Service
public class FriendService {

    private final FriendRepository friendRepository;
    private final UserService userService;

    @Autowired
    public FriendService(FriendRepository friendRepository, UserService userService) {
        this.friendRepository = friendRepository;
        this.userService = userService;
    }

    public void saveFriend(User initiator, Long friendId) {
        User second = userService.getUserById(friendId).orElseThrow(UserNotFoundError::new);

        Friend friend = new Friend();

        User first = initiator;
        if (initiator.getId() > friendId) {
            first = second;
            second = initiator;
        }

        if (!friendRepository.existsByFirstUserAndSecondUser(initiator, second)) {
            friend.setFirstUser(first);
            friend.setSecondUser(second);
            friend.setCreatedDt(LocalDate.now());
            friend.setStatus(FriendStatus.SENT);
            friend.setInitiatorId(initiator.getId());
            friendRepository.save(friend);
        }

    }

    public List<Friend> getFriendList(long id) {
        User currUser = userService
                .getUserById(id)
                .orElseThrow(UserNotFoundError::new);
        return friendRepository
                .findByFirstUserOrSecondUser(currUser, currUser);
    }

    public void accept(User first, long friendId) {
        User second = userService.getUserById(friendId).orElseThrow(UserNotFoundError::new);
        Friend friend = friendRepository.findToAccept(first.getId(), second.getId()).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "u not have friend requests")
        );
        System.out.println(friendRepository.findToAccept(first.getId(), second.getId()));
        friend.setStatus(FriendStatus.ACCEPT);
        friendRepository.save(friend);
    }
}
