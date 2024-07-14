package nl.fontys.s3.individual.news.domain.dto.comment;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateCommentRequest {
    private Long id;
    @NotNull
    private Long userId;
    @NotNull
    private Long newsId;
    @NotEmpty
    @Size(min = 1, max = 500)
    private String content;
}
