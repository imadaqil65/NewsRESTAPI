package nl.fontys.s3.individual.news.domain.dto.comment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import nl.fontys.s3.individual.news.domain.Comment;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GetAllCommentsResponse {
    private long countComment;
    private List<Comment> commentList;
}
