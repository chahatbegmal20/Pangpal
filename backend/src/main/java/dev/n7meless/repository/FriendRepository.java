package dev.n7meless.repository;

import dev.n7meless.entity.Friend;
import dev.n7meless.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FriendRepository extends JpaRepository<Friend, Long> {
    boolean existsByFirstUserAndSecondUser(User first, User second);

    List<Friend> findByFirstUser(User user);

    List<Friend> findBySecondUser(User user);

    List<Friend> findByFirstUserOrSecondUser(User first, User second);

    Optional<Friend> findByFirstUserAndSecondUser(User first, User second);

    @Query(value = """
            select a.id, a.created_date, a.first_account, a.second_account, a.initiator_id, a.status
            from sc_network.friend a
                     inner join sc_network.user acc
                                on a.first_account = acc.id
            where a.initiator_id != :first
              and (a.first_account = :first or a.second_account = :first)
              and (a.first_account = :second or a.second_account = :second)
              and a.status = 0
                      """, nativeQuery = true)
    Optional<Friend> findToAccept(@Param("first") long first, @Param("second") long second);

    Friend save(Friend friend);
}
