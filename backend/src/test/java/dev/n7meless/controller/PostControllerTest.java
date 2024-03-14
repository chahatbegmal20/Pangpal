package dev.n7meless.controller;

import dev.n7meless.annotation.UnsecuredWebMvcTest;
import dev.n7meless.entity.Post;
import dev.n7meless.service.PostService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Collections;
import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@UnsecuredWebMvcTest(controllers = PostController.class)
public class PostControllerTest {
    public static final String URL = "/api/v1/posts";
    @MockBean
    private PostService postService;
    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldReturn200WhenGetPostsByUserId() throws Exception {
        //given
        List<Post> posts = Collections.emptyList();

        //when
        Mockito.when(postService.getPostsByUserId(1L)).thenReturn(posts);

        //then
        this.mockMvc.perform(MockMvcRequestBuilders.get(URL)
                        .param("id", "1")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());

    }
}
