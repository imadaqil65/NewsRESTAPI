package nl.fontys.s3.individual.news.domain.dto.topic;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateTopicRequest {
    private Long id;
    @NotBlank
    @Size(min = 3, max = 50)
    private String topicName;
}
