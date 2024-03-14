package dev.n7meless.security;

import dev.n7meless.container.AbstractTestcontainersDB;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
@SpringBootTest
@AutoConfigureMockMvc
public class SecurityTest extends AbstractTestcontainersDB {
    @Autowired
    MockMvc mockMvc;
    @Test
    void shouldReturn403WhenGetRequestWithoutRoleUser() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/users/"))
                .andDo(print())
                .andExpect(status().isForbidden());
    }
}
