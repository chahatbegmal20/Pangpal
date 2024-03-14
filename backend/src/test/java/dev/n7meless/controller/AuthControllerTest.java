package dev.n7meless.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.n7meless.annotation.UnsecuredWebMvcTest;
import dev.n7meless.dto.login.LoginRequest;
import dev.n7meless.dto.register.RegisterRequest;
import dev.n7meless.entity.User;
import dev.n7meless.jwt.Jwt;
import dev.n7meless.jwt.Login;
import dev.n7meless.service.AuthService;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@UnsecuredWebMvcTest(controllers = {AuthController.class})
public class AuthControllerTest {


    @MockBean
    private AuthService authService;


    @Value("${application.security.access-token-secret}")
    private String accessToken;

    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;

    @Test
    void shouldReturn200WhenLogin() throws Exception {
        //given
        String email = "user@gmail.com";
        String password = "user";

        LoginRequest loginRequest =
                new LoginRequest(email, password);

        Login login = new Login(Jwt.of(1L, 1L, accessToken),
                Jwt.of(1L, 1L, accessToken));

        BDDMockito.given(authService.login(email, password)).willReturn(login);

        //when + then
        this.mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/login")
                        .content(objectMapper.writeValueAsString(loginRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("token")))
                .andExpect(MockMvcResultMatchers.cookie().exists("refresh_token"));
    }

    @Test
    void shouldReturn200WhenRegister() throws Exception {
        //given
        String firstName = "user";
        String lastName = "user";
        String email = "user@gmail.com";
        String password = "user";
        String passwordConfirm = "user";

        RegisterRequest registerRequest =
                new RegisterRequest(email, firstName, lastName, password, passwordConfirm);

        User user = new User();
        user.setLastName(lastName);
        user.setFirstName(firstName);
        user.setPassword(password);
        user.setEmail(email);

        BDDMockito.given(authService.register(email, firstName, lastName, password, passwordConfirm))
                .willReturn(user);

        //when + then
        this.mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/register")
                        .content(objectMapper.writeValueAsString(registerRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.first_name").value(firstName))
                .andExpect(MockMvcResultMatchers.jsonPath("$.last_name").value(lastName))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value(email));
    }
}
