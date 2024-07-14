package nl.fontys.s3.individual.news.persistence;

import nl.fontys.s3.individual.news.persistence.entities.FollowEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface FollowRepository extends JpaRepository<FollowEntity, Long> {
    @Modifying
    @Transactional
    @Query("DELETE FROM FollowEntity f WHERE f.topicId.id = :topicId AND f.userId.id = :userId")
    void deleteByTopicIdAndUserId(Long topicId, Long userId);
}
