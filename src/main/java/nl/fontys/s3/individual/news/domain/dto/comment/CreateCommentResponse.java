package nl.fontys.s3.individual.news.domain.dto.comment;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CreateCommentResponse {
    private Long commentId;
}
