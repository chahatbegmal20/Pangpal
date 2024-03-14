package dev.n7meless.controller;

import dev.n7meless.dto.login.LoginRequest;
import dev.n7meless.dto.login.LoginResponse;
import dev.n7meless.dto.register.RegisterRequest;
import dev.n7meless.dto.register.RegisterResponse;
import dev.n7meless.dto.reset.ResetRequest;
import dev.n7meless.dto.reset.ResetResponse;
import dev.n7meless.entity.User;
import dev.n7meless.service.AuthService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@Slf4j
@RequestMapping("/api/v1")
public class AuthController {

    private final AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }


    @PostMapping("/register")
    public RegisterResponse registration(@RequestBody RegisterRequest registerRequest) {
        User user = authService.register(
                registerRequest.email(),
                registerRequest.firstName(),
                registerRequest.lastName(),
                registerRequest.password(),
                registerRequest.passwordConfirm()
        );
        return new RegisterResponse(user.getEmail(), user.getFirstName(), user.getLastName());
    }

    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest loginRequest, HttpServletResponse response) {
        var login = authService.login(loginRequest.email(), loginRequest.password());
        Cookie cookie = new Cookie("refresh_token", login.refreshToken().getToken());
        cookie.setMaxAge(3600);
        cookie.setHttpOnly(true);
        cookie.setPath("/api/v1");
        response.addCookie(cookie);
        response.setHeader(HttpHeaders.AUTHORIZATION, "Bearer " + login.accessToken().getToken());
        return new LoginResponse(login.accessToken().getToken());
    }

//    @GetMapping("/user")
//    public dev.n7meless.dto.user.UserResponse user(HttpServletRequest request) {
//        var user = (User) request.getAttribute("user");
//        return new dev.n7meless.dto.user.UserResponse(user.getId(), user.getEmail(), user.getFirstName(),
//                user.getLastName());
//    }

    @PostMapping("/logout")
    public String logout(@CookieValue("refresh_token") String refreshToken,
                         HttpServletResponse response) {

        authService.logout(refreshToken);

        Cookie cookie = new Cookie("refresh_token", null);
        cookie.setMaxAge(0);
        cookie.setHttpOnly(true);

        response.addCookie(cookie);
        return "success";
    }

    @PostMapping("/refresh")
    public ResponseEntity<String> refresh(@CookieValue("refresh_token") String refreshToken) {
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(authService.refreshAccess(refreshToken).accessToken().getToken());
    }


    @PostMapping(value = "/forgot")
    public ResponseEntity<String> forgot(@RequestParam("email") String email, HttpServletRequest request) {
        var originUrl = request.getHeader(HttpHeaders.ORIGIN);

        authService.forgot(email, originUrl);

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body("success");
    }


    @PostMapping(value = "/reset")
    public ResetResponse reset(@RequestBody ResetRequest request) {
        System.out.println(request);
        if (!authService.reset(request.password(), request.passwordConfirm(), request.token())) {
            throw new ResponseStatusException(HttpStatus.BAD_GATEWAY, "cannot reset password");
        }
        return new ResetResponse("success");
    }

}


