package dev.n7meless.repository;

import dev.n7meless.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;


@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    Optional<User> findByPasswordRecoveriesToken(String token);

    Optional<User> findByIdAndTokensRefreshTokenAndTokensExpiredAtGreaterThan(Long id,
                                                                              String refreshToken,
                                                                              LocalDateTime expiredAt);
}
