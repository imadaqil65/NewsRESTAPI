package nl.fontys.s3.individual.news.domain.dto.news;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import nl.fontys.s3.individual.news.domain.NewsArticle;
import java.util.List;
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor

public class GetAllNewsResponse {
    private List<NewsArticle> newsArticleList;
}
