package nl.fontys.s3.individual.news.domain.dto.topic;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TopicDetails {
    private Long id;
    private String name;
    private Long totalCommentsPerTopic;
}
