package nl.fontys.s3.individual.news.business;

public interface FollowManager {
    void followTopic(Long userId, Long topicId);
    void unfollowTopic(Long userId, Long topicId);

}
