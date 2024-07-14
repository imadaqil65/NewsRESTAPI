package nl.fontys.s3.individual.news.controller;

import lombok.RequiredArgsConstructor;
import nl.fontys.s3.individual.news.business.FollowManager;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/socials")
@RequiredArgsConstructor
public class FollowController {

    private final FollowManager followManager;
    @PostMapping("/{userId}/follow/{topicId}")
    public ResponseEntity<Object> followTopic(@PathVariable Long userId, @PathVariable Long topicId) {
        try{
            followManager.followTopic(userId, topicId);
            return ResponseEntity.noContent().build();
        } catch(Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error! Unable to follow topic." + e.getMessage());
        }
    }

    @PostMapping("/{userId}/unfollow/{topicId}")
    public ResponseEntity<Object> unfollowRestaurant(@PathVariable Long userId, @PathVariable Long topicId) {
        try {
            followManager.unfollowTopic(userId, topicId);
            return ResponseEntity.noContent().build();
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error! Unable to unfollow topic." + e.getMessage());
        }
    }
}
