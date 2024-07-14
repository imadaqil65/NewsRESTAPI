package nl.fontys.s3.individual.news.controller;

import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import nl.fontys.s3.individual.news.business.TopicService;
import nl.fontys.s3.individual.news.domain.Topic;
import nl.fontys.s3.individual.news.domain.dto.topic.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/topics")
@RequiredArgsConstructor
//@CrossOrigin(origins = "*", allowedHeaders = "*")
public class TopicController {
    private final TopicService topicService;

    @GetMapping("{id}")
    public ResponseEntity<Topic> getTopic(@PathVariable(value = "id") final long id){
        final Optional<Topic> topicOptional = topicService.getItem(id);

        return topicOptional.map(topic -> ResponseEntity.ok().body(topic))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/details")
    @RolesAllowed("ADMIN")
    public ResponseEntity<List<TopicDetails>> getTopicDetails() {
        List<TopicDetails> topicDetails = topicService.getTopicDetails();
        return ResponseEntity.ok(topicDetails);
    }

    @GetMapping()
    public ResponseEntity<GetAllTopicResponse> getAllTopic(){
        GetAllTopicResponse topicList = topicService.getAllItems();

        return ResponseEntity.ok(topicList);
    }

    @PostMapping()
    @RolesAllowed("ADMIN")
    public ResponseEntity<Object> createNewTopic(@RequestBody @Valid CreateTopicRequest request){
        try{
            CreateNewTopicResponse response = topicService.createItem(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @DeleteMapping("{id}")
    @RolesAllowed("ADMIN")
    public ResponseEntity<Void> deleteTopic(@PathVariable int id){
        topicService.deleteItem(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("{id}")
    @RolesAllowed("ADMIN")
    public ResponseEntity<Object> updateTopic(@PathVariable("id") long id,
                                              @RequestBody @Valid UpdateTopicRequest request){
        request.setId(id);
        try{
            topicService.updateItem(request);
            return ResponseEntity.noContent().build();
        }
        catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}
