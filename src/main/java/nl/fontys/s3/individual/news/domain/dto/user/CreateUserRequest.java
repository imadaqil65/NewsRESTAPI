package nl.fontys.s3.individual.news.domain.dto.user;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import nl.fontys.s3.individual.news.domain.Usertype;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateUserRequest {
    private Usertype usertype;
    @NotBlank
    @NotNull
    @Size(min = 3)
    private String name;
    @NotNull
    @NotBlank
    @Email
    private String email;
    @NotNull
    @NotBlank
    @Size(min = 8, max = 23)
    private String password;
    @Size(max = 300)
    private String bio;
}
