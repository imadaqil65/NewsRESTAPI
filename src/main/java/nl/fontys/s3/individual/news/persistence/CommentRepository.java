package nl.fontys.s3.individual.news.persistence;

import nl.fontys.s3.individual.news.persistence.entities.CommentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
@Repository
public interface CommentRepository extends JpaRepository<CommentEntity, Long> {
    List<CommentEntity> findAllByNewsId_IdOrderByNewsIdDesc(Long newsId);
    @Query("SELECT COUNT(c) FROM CommentEntity c WHERE c.news.id = :newsId")
    long countByNewsId(@Param("newsId") Long newsId);

}
