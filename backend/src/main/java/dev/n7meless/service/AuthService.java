package dev.n7meless.service;

import dev.n7meless.entity.PasswordRecovery;
import dev.n7meless.entity.Token;
import dev.n7meless.entity.User;
import dev.n7meless.exception.*;
import dev.n7meless.jwt.Jwt;
import dev.n7meless.jwt.Login;
import dev.n7meless.entity.Role;
import dev.n7meless.model.Status;
import dev.n7meless.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.Set;
import java.util.UUID;

@Service
public class AuthService {
    private final UserRepository userRepository;
    private final String accessTokenSecret;
    private final String refreshTokenSecret;
    private final Long accessTokenValidity;
    private final Long refreshTokenValidity;
    private final PasswordEncoder passwordEncoder;
    private final MailService mailService;

    @Autowired
    public AuthService(@Value("${application.security.access-token-secret}") String accessTokenSecret,
                       @Value("${application.security.refresh-token-secret}") String refreshTokenSecret,
                       @Value("${application.security.access-token-validity}") Long accessTokenValidity,
                       @Value("${application.security.refresh-token-validity}") Long refreshTokenValidity,
                       UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       MailService mailService) {
        this.userRepository = userRepository;
        this.accessTokenSecret = accessTokenSecret;
        this.refreshTokenSecret = refreshTokenSecret;
        this.passwordEncoder = passwordEncoder;
        this.accessTokenValidity = accessTokenValidity;
        this.refreshTokenValidity = refreshTokenValidity;
        this.mailService = mailService;
    }

    public User register(String email, String firstName, String lastName,
                         String password, String passwordConfirm) {

        if (!userRepository.findByEmail(email).isEmpty()) {
            throw new EmailAlreadyExistsError();
        }
        if (!password.equals(passwordConfirm))
            throw new PasswordDoNotMatchError();
        return userRepository.save(
                User.of(email, firstName, lastName,
                        passwordEncoder.encode(password),
                        Set.of(Role.of("ROLE_USER")), Status.ACTIVE)
        );
    }

    @Transactional
    public Login login(String email, String password) {
        User user = userRepository.findByEmail(email).orElseThrow(InvalidCredentialsError::new);
        if (!passwordEncoder.matches(password, user.getPassword()))
            throw new InvalidCredentialsError();
        var login = Login.of(
                user.getId(),
                accessTokenSecret, accessTokenValidity,
                refreshTokenSecret, refreshTokenValidity);
        var refreshJwt = login.refreshToken();
        Token token = new Token(refreshJwt.getToken(), refreshJwt.getIssueAt(), refreshJwt.getExpiration());
        token.setUser(user);
        user.addToken(token);
//        userService.save(user);
        return login;
    }

    public User getUserFromToken(String token) {
        return userRepository.findById(Jwt.from(token, accessTokenSecret).getUserId())
                .orElseThrow(UserNotFoundError::new);
    }

    public Login refreshAccess(String refreshToken) {
        var refreshJwt = Jwt.from(refreshToken, refreshTokenSecret);

        userRepository.findByIdAndTokensRefreshTokenAndTokensExpiredAtGreaterThan(refreshJwt.getUserId(),
                        refreshJwt.getToken(), refreshJwt.getExpiration())
                .orElseThrow(UnauthenticatedError::new);

        return Login.of(refreshJwt.getUserId(), accessTokenSecret, accessTokenValidity, refreshJwt);
    }

    public boolean logout(String refreshToken) {
        var refreshJwt = Jwt.from(refreshToken, refreshTokenSecret);

        var user = userRepository.findById(refreshJwt.getUserId())
                .orElseThrow(UnauthenticatedError::new);

        var tokenIsRemoved =
                user.removeTokenIf(token -> Objects.equals(token.getRefreshToken(), refreshToken));

        if (tokenIsRemoved) {
            userRepository.save(user);
        }
        return tokenIsRemoved;

    }
    @Transactional
    public void forgot(String email, String originUrl) {
        var token = UUID.randomUUID().toString().replace("-", "");
        var user = userRepository.findByEmail(email)
                .orElseThrow(UserNotFoundError::new);
        PasswordRecovery passwordRecovery = new PasswordRecovery(token);
        passwordRecovery.setUser(user);
        user.addPasswordRecovery(passwordRecovery);

//        mailService.sendForgotMessage(email, token, originUrl);
        try {
            userRepository.save(user);
        } catch (RuntimeException e) {
            e.printStackTrace();
        }
    }
    @Transactional
    public Boolean reset(String password, String passwordConfirm, String token) {
        if (!Objects.equals(password, passwordConfirm)) {
            throw new PasswordDoNotMatchError();
        }

        User user = userRepository.findByPasswordRecoveriesToken(token).orElseThrow(InvalidLinkError::new);


        var passwordRecoveryIsRemoved = user.removePasswordRecoveryIf(recovery ->
                Objects.equals(recovery.getToken(), token));

        if (passwordRecoveryIsRemoved) {
            user.setPassword(passwordEncoder.encode(password));
            userRepository.save(user);
        }

        return passwordRecoveryIsRemoved;
    }
}
