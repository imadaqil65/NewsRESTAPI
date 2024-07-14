package nl.fontys.s3.individual.news.domain.dto.news;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateNewsArticleRequest {
    private long id;

    @NotBlank
    @Size(min = 3, max = 150)
    private String title;

    @NotNull
    private Long journalistId;

    @NotNull
    private Long topic;

    @NotNull
    @NotBlank
    @Size(min = 10)
    private String content;
}
