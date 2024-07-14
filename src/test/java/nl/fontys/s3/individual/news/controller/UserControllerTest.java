package nl.fontys.s3.individual.news.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import nl.fontys.s3.individual.news.business.UserService;
import nl.fontys.s3.individual.news.domain.dto.user.CreateUserRequest;
import nl.fontys.s3.individual.news.domain.User;
import nl.fontys.s3.individual.news.domain.Usertype;
import nl.fontys.s3.individual.news.domain.dto.user.CreateUserResponse;
import nl.fontys.s3.individual.news.domain.dto.user.UpdateUserRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Test
    @WithMockUser(username = "j.doe@email.com", roles = "READER")
    void getUserById_shouldReturn200WithTopic() throws Exception {
        User user = User.builder()
                .id(1L)
                .email("j.doe@email.com")
                .name("John Doe")
                .password("password")
                .bio("person biography")
                .usertype(Usertype.READER)
                .build();

        when(userService.getUser(1L)).thenReturn(Optional.of(user));

        mockMvc.perform(get("/users/profile/1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Type", APPLICATION_JSON_VALUE))
                .andExpect(content().json("""
                    
                        {
                        "id": 1,
                        "email": "j.doe@email.com",
                        "name": "John Doe",
                        "password": "password",
                        "bio": "person biography",
                        "usertype": "READER"
                    }
                    """));

        verify(userService).getUser(1L);
    }

    @Test
    @WithMockUser(username = "j.doe@email.com", roles = "READER")
    void getUserById_shouldReturn404IfTopicNotFound() throws Exception {
        when(userService.getUser(1L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/users/profile/1"))
                .andDo(print())
                .andExpect(status().isNotFound());

        verify(userService).getUser(1L);
    }

    @Test
    void createUser_shouldReturn201_whenRequestValid() throws Exception {
        CreateUserRequest expectedRequest =
                CreateUserRequest.builder()
                .email("j.doe@email.com")
                .name("John Doe")
                .password("password")
                .bio("person biography")
                .usertype(Usertype.READER)
                .build();

        String requestJson = new ObjectMapper().writeValueAsString(expectedRequest);

        when(userService.createItem(any(CreateUserRequest.class)))
                .thenReturn(CreateUserResponse.builder()
                        .userid(1L)
                        .build());
        mockMvc.perform(post("/users")
                        .contentType(APPLICATION_JSON_VALUE)
                        .content(requestJson))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(header().string("Content-Type"
                        ,
                        APPLICATION_JSON_VALUE))
                .andExpect(content().json("""
                {"userid":1}
                """));

        verify(userService).createItem(any(CreateUserRequest.class));
    }

    @Test
    @WithMockUser(username = "j.doe@email.com", roles = "ADMIN")
    void createJournalist_shouldReturn201_whenRequestValid() throws Exception {
        CreateUserRequest expectedRequest =
                CreateUserRequest.
                        builder()
                        .email("j.doe@email.com")
                        .name("John Doe")
                        .password("password")
                        .bio("person biography")
                        .build();

        String requestJson = new ObjectMapper().writeValueAsString(expectedRequest);

        when(userService.createItem(any(CreateUserRequest.class)))
                .thenReturn(CreateUserResponse.builder()
                        .userid(1L)
                        .build());
        mockMvc.perform(post("/users/journalist")
                        .contentType(APPLICATION_JSON_VALUE)
                        .content(requestJson))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(header().string("Content-Type"
                        ,
                        APPLICATION_JSON_VALUE))
                .andExpect(content().json("""
                {"userid":1}
                """));

        verify(userService).createItem(any(CreateUserRequest.class));
    }

    @Test
    @WithMockUser(username = "j.doe@email.com", roles = "ADMIN")
    void createAdmin_shouldReturn201_whenRequestValid() throws Exception {
        CreateUserRequest expectedRequest =
                CreateUserRequest.
                        builder()
                        .email("j.doe@email.com")
                        .name("John Doe")
                        .password("password")
                        .bio("person biography")
                        .build();

        String requestJson = new ObjectMapper().writeValueAsString(expectedRequest);

        when(userService.createItem(any(CreateUserRequest.class)))
                .thenReturn(CreateUserResponse.builder()
                        .userid(1L)
                        .build());
        mockMvc.perform(post("/users/admin")
                        .contentType(APPLICATION_JSON_VALUE)
                        .content(requestJson))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(header().string("Content-Type"
                        ,
                        APPLICATION_JSON_VALUE))
                .andExpect(content().json("""
                {"userid":1}
                """));

        verify(userService).createItem(any(CreateUserRequest.class));
    }

    @Test
    void createUser_shouldNotCreateAndReturn400_WhenMissingField() throws Exception{
        mockMvc.perform(post("/users")
                        .contentType(APPLICATION_JSON_VALUE)
                        .content("""
                            {
                                "topicName":""
                            }
                            """))
                .andDo(print())
                .andExpect(status().isBadRequest());

        verifyNoInteractions(userService);
    }

    @Test
    @WithMockUser(username = "j.doe@email.com", roles = "READER")
    void updateUser_shouldReturn204_WhenRequestValid() throws Exception {
        UpdateUserRequest expectedRequest =
                UpdateUserRequest.builder()
                        .id(1L)
                        .email("j.doe@email.com")
                        .name("John Doe")
                        .bio("person biography")
                        .build();

        String requestJson = new ObjectMapper().writeValueAsString(expectedRequest);

        // Mocking the behavior of updateItem to perform the update operation
        doAnswer(invocation -> {
            return null; // Since the method returns void
        }).when(userService).updateItem(any(UpdateUserRequest.class));

        mockMvc.perform(put("/users/1")
                        .contentType(APPLICATION_JSON_VALUE)
                        .content(requestJson))
                .andDo(print())
                .andExpect(status().isNoContent());


        verify(userService).updateItem(any(UpdateUserRequest.class));
    }

    @Test
    @WithMockUser(username = "j.doe@email.com", roles = "READER")
    void deleteUser_shouldReturn204_WhenRequestValid() throws Exception {
        mockMvc.perform(delete("/users/1"))
                .andDo(print())
                .andExpect(status().isNoContent());

        verify(userService).deleteItem(1L);
    }

}
