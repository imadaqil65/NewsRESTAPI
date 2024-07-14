package nl.fontys.s3.individual.news.business;

import nl.fontys.s3.individual.news.configuration.security.token.AccessToken;
import nl.fontys.s3.individual.news.configuration.security.token.AccessTokenEncoder;
import nl.fontys.s3.individual.news.domain.User;
import nl.fontys.s3.individual.news.domain.Usertype;
import nl.fontys.s3.individual.news.business.implementation.UserServiceImpl;
import nl.fontys.s3.individual.news.domain.dto.user.*;
import nl.fontys.s3.individual.news.exception.InvalidCredentialsException;
import nl.fontys.s3.individual.news.exception.UnauthorizedDataAccessException;
import nl.fontys.s3.individual.news.persistence.UserRepository;
import nl.fontys.s3.individual.news.persistence.entities.UserEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private AccessTokenEncoder accessTokenEncoder;
    @Mock
    private AccessToken requestAccessToken;
    @InjectMocks
    private UserServiceImpl userService;

    private CreateUserRequest mockUser() {
        return CreateUserRequest.builder()
                .email("j.doe@email.com")
                .name("John Doe")
                .password("password")
                .bio("person biography")
                .usertype(Usertype.READER)
                .build();
    }

    private UpdateUserRequest mockUpdateUser(Long id) {
        return UpdateUserRequest.builder()
                .id(id)
                .email("j.doe@email.com")
                .name("John Doe")
                .bio("person biography")
                .build();
    }

    private ResetPasswordRequest resetPassword(){
        return ResetPasswordRequest.builder()
                .email("j.doe@email.com")
                .password("password")
                .build();
    }

    private UserEntity mockUserEntity(Long id) {
        return UserEntity.builder()
                .id(id)
                .email("j.doe@email.com")
                .name("John Doe")
                .password("password")
                .bio("person biography")
                .usertype(Usertype.READER)
                .build();
    }

    @Test
    void createNewItem_success() {
        CreateUserRequest user = mockUser();
        UserEntity savedEntity = mockUserEntity(1L);
        when(userRepository.save(any(UserEntity.class))).thenReturn(savedEntity);
        when(passwordEncoder.encode(user.getPassword())).thenReturn("encodedPassword");

        Long userId = userService.createItem(user).getUserid();
        assertNotNull(userId);
        assertEquals(1L, userId);
        verify(userRepository, times(1)).save(any(UserEntity.class));
    }

    @Test
    void updateItem_success() {
        UpdateUserRequest user = mockUpdateUser(1L);
        UserEntity existingEntity = mockUserEntity(1L);

        when(userRepository.getReferenceById(user.getId())).thenReturn(existingEntity);
        when(requestAccessToken.getUserId()).thenReturn(1L);

        userService.updateItem(user);
        verify(userRepository, times(1)).getReferenceById(user.getId());
        verify(userRepository, times(1)).save(any(UserEntity.class));
    }

    @Test
    void ResetPassword_success() {
        ResetPasswordRequest request = resetPassword();
        UserEntity existingEntity = mockUserEntity(1L);

        when(userRepository.existsByEmail(request.getEmail())).thenReturn(true);
        when(userRepository.findByEmail(request.getEmail())).thenReturn(existingEntity);

        userService.resetPassword(request);
        verify(userRepository, times(1)).findByEmail(request.getEmail());
        verify(userRepository, times(1)).save(any(UserEntity.class));
    }

    @Test
    void updateItem_throws_UnauthorizedDataAccessException() {
        UpdateUserRequest user = mockUpdateUser(1L);

        assertThrows(UnauthorizedDataAccessException.class, ()->{userService.updateItem(user);});
    }

    @Test
    void deleteItem_success() {
        long userId = 1L;
        doNothing().when(userRepository).deleteById(userId);
        when(requestAccessToken.getUserId()).thenReturn(1L);

        userService.deleteItem(userId);
        verify(userRepository, times(1)).deleteById(userId);
    }

    @Test
    void getEditUser_success() {
        long userId = 1L;
        Optional<UserEntity> returnedEntity = Optional.of(mockUserEntity(userId));
        when(userRepository.findById(userId)).thenReturn(returnedEntity);
        when(requestAccessToken.getUserId()).thenReturn(1L);

        Optional<User> result = userService.getUser(userId);
        assertTrue(result.isPresent());
        assertEquals("John Doe", result.get().getName());
        verify(userRepository, times(1)).findById(userId);
    }

    @Test
    void getEditUser_throws_UnauthorizedDataAccessException(){
        long userId = 2L;
        when(requestAccessToken.getUserId()).thenReturn(1L);

        assertThrows(UnauthorizedDataAccessException.class, ()->{userService.getUser(userId);});
    }

    @Test
    void login_validCredentials_returnAccessToken(){
        String email = "test@example.com";
        String password = "password";
        String encodedPassword = "encodedPassword";
        String accessToken = "accessToken";

        UserEntity user = UserEntity.builder()
                .email(email)
                .password(encodedPassword)
                .usertype(Usertype.READER)
                .build();

        when(userRepository.findByEmail(email)).thenReturn(user);
        when(passwordEncoder.matches(password, encodedPassword)).thenReturn(true);
        when(accessTokenEncoder.encode(any(AccessToken.class))).thenReturn(accessToken);

        LoginRequest request = LoginRequest.builder()
                                .email(email)
                                .password(password)
                                .build();

        LoginResponse response = userService.login(request);

        assertNotNull(response);
        assertEquals(accessToken, response.getAccessToken());
    }

    @Test
    void login_InvalidEmail_ThrowsInvalidCredentialsException() {
        String email = "invalid@example.com";
        String password = "password";

        when(userRepository.findByEmail(email)).thenReturn(null);

        LoginRequest request = new LoginRequest(email, password);

        assertThrows(InvalidCredentialsException.class, () -> userService.login(request));
    }

    @Test
    void login_InvalidPassword_ThrowsInvalidCredentialsException() {
        String email = "test@example.com";
        String password = "invalidPassword";
        String encodedPassword = "encodedPassword";

        UserEntity user = UserEntity.builder()
                .email(email)
                .password(encodedPassword)
                .usertype(Usertype.READER)
                .build();

        when(userRepository.findByEmail(email)).thenReturn(user);
        when(passwordEncoder.matches(password, encodedPassword)).thenReturn(false);

        LoginRequest request = new LoginRequest(email, password);

        assertThrows(InvalidCredentialsException.class, () -> userService.login(request));
    }
}