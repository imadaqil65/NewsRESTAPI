package nl.fontys.s3.individual.news.persistence.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "article")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NewsEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotBlank
    @Size(min = 3, max = 150)
    @Column(name = "title")
    private String title;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity journalist;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "topic_id")
    private TopicEntity topic;

    @Column(name = "date")
    private LocalDate dateTime;

    @Size(min=10)
    @Column(name="content")
    private String content;

    @OneToMany(mappedBy = "news", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CommentEntity> comments;
}
