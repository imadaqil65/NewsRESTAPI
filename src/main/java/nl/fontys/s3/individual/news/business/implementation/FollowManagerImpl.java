package nl.fontys.s3.individual.news.business.implementation;

import lombok.RequiredArgsConstructor;
import nl.fontys.s3.individual.news.business.FollowManager;
import nl.fontys.s3.individual.news.exception.InvalidDataException;
import nl.fontys.s3.individual.news.persistence.FollowRepository;
import nl.fontys.s3.individual.news.persistence.TopicRepository;
import nl.fontys.s3.individual.news.persistence.UserRepository;
import nl.fontys.s3.individual.news.persistence.entities.FollowEntity;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FollowManagerImpl implements FollowManager {

    private final UserRepository userRepository;
    private final TopicRepository topicRepository;
    private final FollowRepository followRepository;

    void validateIds(Long userId, Long topicId){
        if(!userRepository.existsById(userId) && !topicRepository.existsById(topicId)){
            throw new InvalidDataException(HttpStatus.NOT_FOUND, "INVALID_ID");
        }
    }
    @Override
    public void followTopic(Long userId, Long topicId) {
        validateIds(userId, topicId);
        FollowEntity follow = FollowEntity.builder()
                                .userId(userRepository.getReferenceById(userId))
                                .topicId(topicRepository.getReferenceById(topicId))
                                .build();
        followRepository.save(follow);
    }

    @Override
    public void unfollowTopic(Long userId, Long topicId) {
        validateIds(userId, topicId);
        followRepository.deleteByTopicIdAndUserId(topicId, userId);
    }
}
