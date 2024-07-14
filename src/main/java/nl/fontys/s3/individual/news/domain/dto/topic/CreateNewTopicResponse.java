package nl.fontys.s3.individual.news.domain.dto.topic;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CreateNewTopicResponse {
    private Long id;
}
