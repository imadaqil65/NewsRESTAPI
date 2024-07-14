package nl.fontys.s3.individual.news.controller;


import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import nl.fontys.s3.individual.news.business.UserService;
import nl.fontys.s3.individual.news.domain.Usertype;
import nl.fontys.s3.individual.news.domain.dto.user.CreateUserRequest;
import nl.fontys.s3.individual.news.domain.dto.user.CreateUserResponse;
import nl.fontys.s3.individual.news.domain.User;
import nl.fontys.s3.individual.news.domain.dto.user.ResetPasswordRequest;
import nl.fontys.s3.individual.news.domain.dto.user.UpdateUserRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;


    @GetMapping("/profile/{id}")
    public ResponseEntity<User> getUser(@PathVariable(value="id") long id){
        try{
            final Optional<User> userOptional = userService.getUser(id);

            return userOptional.map(user -> ResponseEntity.ok().body(user))
                    .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
        }
        catch (Exception e){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }

    @PostMapping
    public ResponseEntity<Object> createNewUser(@RequestBody @Valid CreateUserRequest request){
        request.setUsertype(Usertype.READER);
        try{
            CreateUserResponse response = userService.createItem(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        }
        catch(Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PostMapping("journalist")
    @RolesAllowed("ADMIN")
    public ResponseEntity<Object> createNewJournalist(@RequestBody @Valid CreateUserRequest request){
        request.setUsertype(Usertype.JOURNALIST);
        try{
            CreateUserResponse response = userService.createItem(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        }
        catch(Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PostMapping("admin")
    @RolesAllowed("ADMIN")
    public ResponseEntity<Object> createNewAdmin(@RequestBody @Valid CreateUserRequest request){
        request.setUsertype(Usertype.ADMIN);
        try{
            CreateUserResponse response = userService.createItem(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        }
        catch(Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable int id){
        userService.deleteItem(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("{id}")
    public ResponseEntity<Object> updateUser(@PathVariable("id") long id,
                                             @RequestBody @Valid UpdateUserRequest request){
        request.setId(id);
        try{
            userService.updateItem(request);
            return ResponseEntity.noContent().build();
        }
        catch(Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PutMapping("password")
    public ResponseEntity<Object> resetPassword(@RequestBody @Valid ResetPasswordRequest request){
        try{
            userService.resetPassword(request);
            return ResponseEntity.noContent().build();
        }
        catch(Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}
