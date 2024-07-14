package nl.fontys.s3.individual.news.business;

import nl.fontys.s3.individual.news.business.implementation.TopicServiceImpl;
import nl.fontys.s3.individual.news.domain.Topic;
import nl.fontys.s3.individual.news.domain.dto.topic.CreateNewTopicResponse;
import nl.fontys.s3.individual.news.domain.dto.topic.CreateTopicRequest;
import nl.fontys.s3.individual.news.domain.dto.topic.GetAllTopicResponse;
import nl.fontys.s3.individual.news.domain.dto.topic.UpdateTopicRequest;
import nl.fontys.s3.individual.news.persistence.TopicRepository;
import nl.fontys.s3.individual.news.persistence.entities.TopicEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TopicServiceImplTest {

    @Mock
    private TopicRepository topicRepository;
    @InjectMocks
    private TopicServiceImpl topicService;

    private CreateTopicRequest mockTopic() {
        return CreateTopicRequest.builder()
                .topicName("sports")
                .build();
    }

    private UpdateTopicRequest mockUpdateTopic(Long id) {
        return UpdateTopicRequest.builder()
                .id(id)
                .topicName("sports")
                .build();
    }

    private TopicEntity mockTopicEntity(Long id) {
        return TopicEntity.builder()
                .id(id)
                .topicName("sports")
                .build();
    }

    @Test
    void createNewItem_success() {
        TopicEntity savedEntity = mockTopicEntity(1L);
        when(topicRepository.save(any(TopicEntity.class))).thenReturn(savedEntity);

        CreateNewTopicResponse topicId = topicService.createItem(mockTopic());
        assertNotNull(topicId);
        assertEquals(1L, topicId.getId());
        verify(topicRepository, times(1)).save(any(TopicEntity.class));
    }

    @Test
    void updateItem_success() {
        UpdateTopicRequest topic = mockUpdateTopic(1L);
        TopicEntity existingEntity = mockTopicEntity(1L);

        when(topicRepository.getReferenceById(topic.getId())).thenReturn(existingEntity);

        topicService.updateItem(topic);
        verify(topicRepository, times(1)).getReferenceById(topic.getId());
        verify(topicRepository, times(1)).save(any(TopicEntity.class));
    }

    @Test
    void deleteItem_success() {
        long TopicId = 1L;
        doNothing().when(topicRepository).deleteById(TopicId);

        topicService.deleteItem(TopicId);
        verify(topicRepository, times(1)).deleteById(TopicId);
    }

    @Test
    void getTopic_success() {
        long TopicId = 1L;
        Optional<TopicEntity> returnedEntity = Optional.of(mockTopicEntity(TopicId));
        when(topicRepository.findById(TopicId)).thenReturn(returnedEntity);

        Optional<Topic> result = topicService.getItem(TopicId);
        assertTrue(result.isPresent());
        assertEquals("sports", result.get().getTopicName());
        verify(topicRepository, times(1)).findById(TopicId);
    }
    
    @Test
    void getAllTopic_success(){
        List<TopicEntity> entityList = List.of(mockTopicEntity(1L), mockTopicEntity(2L));
        when(topicRepository.findAll()).thenReturn(entityList);

        GetAllTopicResponse result = topicService.getAllItems();
        assertFalse(result.getTopicList().isEmpty());
        verify(topicRepository, times(1)).findAll();
    }
}