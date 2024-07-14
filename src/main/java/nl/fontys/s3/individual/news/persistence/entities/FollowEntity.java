package nl.fontys.s3.individual.news.persistence.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "follow")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FollowEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @NotNull
    @OneToOne
    @JoinColumn(name = "user_id")
    private UserEntity userId;
    @NotNull
    @OneToOne
    @JoinColumn(name = "topic_id")
    private TopicEntity topicId;
}
