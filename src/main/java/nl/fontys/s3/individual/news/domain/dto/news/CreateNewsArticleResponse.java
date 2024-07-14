package nl.fontys.s3.individual.news.domain.dto.news;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CreateNewsArticleResponse {
    private Long articleId;
}
