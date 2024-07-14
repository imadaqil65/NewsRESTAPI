package nl.fontys.s3.individual.news.persistence;

import nl.fontys.s3.individual.news.domain.dto.topic.TopicDetails;
import nl.fontys.s3.individual.news.persistence.entities.TopicEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TopicRepository extends JpaRepository<TopicEntity, Long> {

    @Query("SELECT new nl.fontys.s3.individual.news.domain.dto.topic.TopicDetails(t.id, t.topicName, COUNT(c.id)) " +
            "FROM TopicEntity t " +
            "LEFT JOIN t.articles a " +
            "LEFT JOIN a.comments c " +
            "GROUP BY t.id, t.topicName " +
            "ORDER BY t.id ASC")
    List<TopicDetails> findTopicDetails();
}
