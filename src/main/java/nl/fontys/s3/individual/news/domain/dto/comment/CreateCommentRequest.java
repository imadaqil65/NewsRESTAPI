package nl.fontys.s3.individual.news.domain.dto.comment;

import jakarta.validation.constraints.NotBlank;
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
public class CreateCommentRequest {
    @NotNull
    private Long userId;
    @NotNull
    private Long newsId;
    @NotBlank
    @Size(min = 1, max = 500)
    private String content;
}
