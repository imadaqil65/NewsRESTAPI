package nl.fontys.s3.individual.news.domain.dto.topic;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import nl.fontys.s3.individual.news.domain.Topic;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GetAllTopicResponse {
    private List<Topic> topicList;
}
