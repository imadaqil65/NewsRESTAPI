package nl.fontys.s3.individual.news.domain;

import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NewsArticle {
    private Long id;
    private String title;
    private User journalist;
    private Topic topic;
    private LocalDate dateTime;
    private String content;
    private Long numOfComments;
    private List<Comment> commentList;
}
