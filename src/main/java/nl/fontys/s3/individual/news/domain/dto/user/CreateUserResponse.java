package nl.fontys.s3.individual.news.domain.dto.user;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CreateUserResponse {
    private Long userid;
}
