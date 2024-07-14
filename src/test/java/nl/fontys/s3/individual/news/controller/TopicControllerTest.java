package nl.fontys.s3.individual.news.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import nl.fontys.s3.individual.news.business.TopicService;
import nl.fontys.s3.individual.news.domain.Topic;
import nl.fontys.s3.individual.news.domain.dto.topic.CreateNewTopicResponse;
import nl.fontys.s3.individual.news.domain.dto.topic.CreateTopicRequest;
import nl.fontys.s3.individual.news.domain.dto.topic.GetAllTopicResponse;
import nl.fontys.s3.individual.news.domain.dto.topic.UpdateTopicRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
class TopicControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TopicService topicService;

    @Test
    void getTopics_shouldReturn200ResponseWithUsersArray() throws Exception{
        List<Topic> topics = List.of(
                Topic.builder().id(1L).topicName("sports").build(),
                Topic.builder().id(2L).topicName("travels").build()
        );

        when(topicService.getAllItems()).thenReturn(GetAllTopicResponse.builder().topicList(topics).build());

        mockMvc.perform(get("/topics"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Type", APPLICATION_JSON_VALUE))
                .andExpect(content().json("""
                    {"topicList":[{"id":1,"topicName":"sports"},{"id":2,"topicName":"travels"}]}
                    """));

        verify(topicService).getAllItems();
    }

    @Test
    void getTopicById_shouldReturn200WithTopic() throws Exception {
        Topic topic = Topic.builder().id(1L).topicName("sports").build();

        when(topicService.getItem(1L)).thenReturn(Optional.of(topic));

        mockMvc.perform(get("/topics/1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Type", APPLICATION_JSON_VALUE))
                .andExpect(content().json("""
                    {"id":1,"topicName":"sports"}
                    """));

        verify(topicService).getItem(1L);
    }

    @Test
    void getTopicById_shouldReturn404IfTopicNotFound() throws Exception {
        when(topicService.getItem(1L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/topics/1"))
                .andDo(print())
                .andExpect(status().isNotFound());

        verify(topicService).getItem(1L);
    }

    @Test
    @WithMockUser(username = "admin@fontys.nl", roles = "ADMIN")
    void createTopic_shouldReturn201_whenRequestValid() throws Exception {
        CreateTopicRequest expectedRequest =
                CreateTopicRequest.builder()
                        .topicName("sports")
                        .build();

        String requestJson = new ObjectMapper().writeValueAsString(expectedRequest);

        when(topicService.createItem(any(CreateTopicRequest.class)))
                .thenReturn(CreateNewTopicResponse.builder()
                        .id(1L)
                        .build());

        mockMvc.perform(post("/topics")
                        .contentType(APPLICATION_JSON_VALUE)
                        .content(requestJson))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(header().string("Content-Type", APPLICATION_JSON_VALUE))
                .andExpect(content().json("""
                {"id":1}
                """));

        verify(topicService).createItem(any(CreateTopicRequest.class));
    }

    @Test
    @WithMockUser(username = "admin@fontys.nl", roles = "ADMIN")
    void createTopic_shouldNotCreateAndReturn400_WhenMissingField() throws Exception{
        mockMvc.perform(post("/topics")
                        .contentType(APPLICATION_JSON_VALUE)
                        .content("""
                            {
                                "topicName":""
                            }
                            """))
                .andDo(print())
                .andExpect(status().isBadRequest());

        verifyNoInteractions(topicService);
    }

    @Test
    @WithMockUser(username = "admin@fontys.nl", roles = "ADMIN")
    void updateTopic_shouldReturn204_WhenRequestValid() throws Exception {
        UpdateTopicRequest request =
                UpdateTopicRequest.builder()
                        .topicName("updatedTopicName")
                        .build();
        String requestJson = new ObjectMapper().writeValueAsString(request);


        // Mocking the behavior of updateItem to perform the update operation
        doAnswer(invocation -> {
            return null; // Since the method returns void
        }).when(topicService).updateItem(any(UpdateTopicRequest.class));

        mockMvc.perform(put("/topics/1")
                        .contentType(APPLICATION_JSON_VALUE)
                        .content(requestJson))
                .andDo(print())
                .andExpect(status().isNoContent());


        verify(topicService).updateItem(any(UpdateTopicRequest.class));
    }

    @Test
    @WithMockUser(username = "admin@fontys.nl", roles = {"ADMIN"})
    void deleteTopic_shouldReturn204_WhenRequestValid() throws Exception {
        mockMvc.perform(delete("/topics/1"))
                .andDo(print())
                .andExpect(status().isNoContent());

        verify(topicService).deleteItem(1L);
    }

}
