package nl.fontys.s3.individual.news.domain.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateUserRequest {
    private Long id;
    @NotBlank
    @NotNull
    @Size(min = 3)
    private String name;
    @NotNull
    @NotBlank
    @Email
    private String email;
    @Size(max = 300)
    private String bio;
}
