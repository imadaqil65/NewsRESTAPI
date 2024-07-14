package nl.fontys.s3.individual.news.business;

import nl.fontys.s3.individual.news.domain.dto.user.*;
import nl.fontys.s3.individual.news.business.interfaces.DataCreationInterface;
import nl.fontys.s3.individual.news.domain.User;
import java.util.Optional;

public interface UserService extends DataCreationInterface<CreateUserResponse, CreateUserRequest, UpdateUserRequest> {
    Optional<User> getUser(long id);
    void resetPassword(ResetPasswordRequest request);
    LoginResponse login(LoginRequest request);

}
