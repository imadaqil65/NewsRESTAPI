package nl.fontys.s3.individual.news.persistence.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import nl.fontys.s3.individual.news.domain.Usertype;

import java.util.List;

@Entity
@Table(name = "user")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserEntity {
    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotBlank
    @Email
    @Column(name = "email")
    private String email;

    @NotBlank
    @Size(min = 2)
    @Column(name = "name")
    private String name;

    @NotBlank
    @Column(name = "password")
    private String password;

    @Size(max = 300)
    @Column(name = "bio")
    private String bio;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "usertype")
    private Usertype usertype;

    @OneToMany(mappedBy = "journalist", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<NewsEntity> articles;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CommentEntity> comments;
}
