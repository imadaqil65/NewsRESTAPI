package nl.fontys.s3.individual.news.domain.dto.news;

import jakarta.validation.constraints.*;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateNewsArticleRequest {

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
