package nl.fontys.s3.individual.news.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import nl.fontys.s3.individual.news.business.CommentService;
import nl.fontys.s3.individual.news.domain.dto.comment.CreateCommentRequest;
import nl.fontys.s3.individual.news.domain.dto.comment.CreateCommentResponse;
import nl.fontys.s3.individual.news.domain.dto.comment.UpdateCommentRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/comments")
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;
    @PostMapping()
    public ResponseEntity<Object> createNewComment(@RequestBody @Valid CreateCommentRequest request){
        try{
            CreateCommentResponse response = commentService.createItem(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteComment(@PathVariable int id){
        commentService.deleteItem(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("{id}")
    public ResponseEntity<Object> updateTopic(@PathVariable("id") long id,
                                              @RequestBody @Valid UpdateCommentRequest request){
        request.setId(id);
        try{
            commentService.updateItem(request);
            return ResponseEntity.noContent().build();
        }
        catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}
