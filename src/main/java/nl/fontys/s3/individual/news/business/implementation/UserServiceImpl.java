package nl.fontys.s3.individual.news.business.implementation;

import lombok.RequiredArgsConstructor;
import nl.fontys.s3.individual.news.configuration.security.token.AccessToken;
import nl.fontys.s3.individual.news.configuration.security.token.AccessTokenEncoder;
import nl.fontys.s3.individual.news.configuration.security.token.impl.AccessTokenImpl;
import nl.fontys.s3.individual.news.domain.dto.user.*;
import nl.fontys.s3.individual.news.exception.InvalidCredentialsException;
import nl.fontys.s3.individual.news.exception.InvalidDataException;
import nl.fontys.s3.individual.news.exception.UnauthorizedDataAccessException;
import nl.fontys.s3.individual.news.persistence.entities.UserEntity;
import nl.fontys.s3.individual.news.persistence.UserRepository;
import nl.fontys.s3.individual.news.business.UserService;
import nl.fontys.s3.individual.news.business.converters.UserConverter;
import nl.fontys.s3.individual.news.domain.User;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepo;
    private final PasswordEncoder passwordEncoder;
    private final AccessTokenEncoder accessTokenEncoder;
    private final AccessToken requestAccessToken;

    private static final String ERROR_1 = "EDIT_NOT_ALLOWED";
    private UserEntity saveNewUser(CreateUserRequest user) {
        String encodedPassword = passwordEncoder.encode(user.getPassword());

        return userRepo.save(UserEntity.builder()
                .email(user.getEmail())
                .name(user.getName())
                .password(encodedPassword)
                .usertype(user.getUsertype())
                .bio(user.getBio())
                .build());
    }

    @Override
    public CreateUserResponse createItem(CreateUserRequest item) {


        return CreateUserResponse.builder()
                .userid(saveNewUser(item).getId())
                .build();
    }

    private void updateFields(UpdateUserRequest user, UserEntity entity) {

        entity.setEmail(user.getEmail());
        entity.setName(user.getName());
        entity.setBio(user.getBio());

        userRepo.save(entity);
    }

    @Override
    public void updateItem(UpdateUserRequest item) {
        if(!Objects.equals(requestAccessToken.getUserId(), item.getId())){
            throw new UnauthorizedDataAccessException(ERROR_1);
        }
        UserEntity entity = userRepo.getReferenceById(item.getId());
        updateFields(item, entity);
    }

    @Override
    public void deleteItem(long id) {
        if(!Objects.equals(requestAccessToken.getUserId(), id)){
            throw new UnauthorizedDataAccessException(ERROR_1);
        }
        userRepo.deleteById(id);
    }


    @Override
    public Optional<User> getUser(long id) {
        if(requestAccessToken.getUserId() != id){
            throw new UnauthorizedDataAccessException(ERROR_1);
        }
        return userRepo.findById(id).map(UserConverter::convert);
    }

    @Override
    public void resetPassword(ResetPasswordRequest request) {
        if(!userRepo.existsByEmail(request.getEmail())){
            throw new InvalidDataException(HttpStatus.NOT_FOUND, "EMAIL_NOT_FOUND");
        }

        UserEntity entity = userRepo.findByEmail(request.getEmail());
        entity.setPassword(passwordEncoder.encode(request.getPassword()));

        userRepo.save(entity);
    }

    @Override
    public LoginResponse login(LoginRequest request) {
        UserEntity user = userRepo.findByEmail(request.getEmail());
        if (user == null) {
            throw new InvalidCredentialsException();
        }

        if (!matchesPassword(request.getPassword(), user.getPassword())) {
            throw new InvalidCredentialsException();
        }

        String accessToken = generateAccessToken(user);
        return LoginResponse.builder().accessToken(accessToken).build();
    }

    private boolean matchesPassword(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }

    private String generateAccessToken(UserEntity user) {
        assert user != null;
        Long userId = user.getId();
        String role = user.getUsertype().toString();

        return accessTokenEncoder.encode(
                new AccessTokenImpl(user.getEmail(), userId, role));
    }
}
