package nl.fontys.s3.individual.news.business;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import nl.fontys.s3.individual.news.business.implementation.FollowManagerImpl;
import nl.fontys.s3.individual.news.exception.InvalidDataException;
import nl.fontys.s3.individual.news.persistence.FollowRepository;
import nl.fontys.s3.individual.news.persistence.TopicRepository;
import nl.fontys.s3.individual.news.persistence.UserRepository;
import nl.fontys.s3.individual.news.persistence.entities.FollowEntity;
import nl.fontys.s3.individual.news.persistence.entities.TopicEntity;
import nl.fontys.s3.individual.news.persistence.entities.UserEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class FollowManagerImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private TopicRepository topicRepository;

    @Mock
    private FollowRepository followRepository;

    @InjectMocks
    private FollowManagerImpl followManager;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void followTopic_ValidIds_FollowSaved() {
        Long userId = 1L;
        Long topicId = 2L;

        when(userRepository.existsById(userId)).thenReturn(true);
        when(topicRepository.existsById(topicId)).thenReturn(true);
        when(userRepository.getReferenceById(userId)).thenReturn(UserEntity.builder().build());
        when(topicRepository.getReferenceById(topicId)).thenReturn(TopicEntity.builder().build());

        followManager.followTopic(userId, topicId);

        verify(followRepository, times(1)).save(any(FollowEntity.class));
    }

    @Test
    void followTopic_InvalidIds_ThrowsInvalidDataException() {
        Long userId = 1L;
        Long topicId = 2L;

        when(userRepository.existsById(userId)).thenReturn(false);
        when(topicRepository.existsById(topicId)).thenReturn(false);

        assertThrows(InvalidDataException.class, () -> {
            followManager.followTopic(userId, topicId);
        });
    }

    @Test
    void unfollowTopic_ValidIds_FollowDeleted() {
        Long userId = 1L;
        Long topicId = 2L;

        when(userRepository.existsById(userId)).thenReturn(true);
        when(topicRepository.existsById(topicId)).thenReturn(true);

        followManager.unfollowTopic(userId, topicId);

        verify(followRepository, times(1)).deleteByTopicIdAndUserId(topicId, userId);
    }

    @Test
    void unfollowTopic_InvalidIds_ThrowsInvalidDataException() {
        Long userId = 1L;
        Long topicId = 2L;

        when(userRepository.existsById(userId)).thenReturn(false);
        when(topicRepository.existsById(topicId)).thenReturn(false);

        assertThrows(InvalidDataException.class, () -> {
            followManager.unfollowTopic(userId, topicId);
        });
    }
}

