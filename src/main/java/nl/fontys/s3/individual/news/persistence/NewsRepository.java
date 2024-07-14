package nl.fontys.s3.individual.news.persistence;

import nl.fontys.s3.individual.news.persistence.entities.NewsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface NewsRepository extends JpaRepository<NewsEntity, Long> {
    List<NewsEntity> findAllByTopic_TopicNameOrderByDateTimeDesc(String topic);
    List<NewsEntity> findAllByTitleContaining(String search);
    List<NewsEntity> findFirst5ByOrderByDateTimeDesc();
    List<NewsEntity> findAllByTopicTopicNameAndTitleContaining(String topic, String title);
    List<NewsEntity> findAllByJournalist_Id(Long id);
}
