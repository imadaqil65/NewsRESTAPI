package nl.fontys.s3.individual.news.controller;

import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import nl.fontys.s3.individual.news.domain.dto.news.CreateNewsArticleRequest;
import nl.fontys.s3.individual.news.domain.dto.news.GetAllNewsResponse;
import nl.fontys.s3.individual.news.business.NewsService;
import nl.fontys.s3.individual.news.domain.NewsArticle;
import nl.fontys.s3.individual.news.domain.dto.news.CreateNewsArticleResponse;
import nl.fontys.s3.individual.news.domain.dto.news.UpdateNewsArticleRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/news")
@RequiredArgsConstructor
//@CrossOrigin(origins = "*", allowedHeaders = "*")
public class NewsController {
    private final NewsService newsService;

    @GetMapping("{id}")
    public ResponseEntity<NewsArticle> getArticle(@PathVariable(value = "id") final long id){
        final Optional<NewsArticle> newsOptional = newsService.getItem(id);

        return newsOptional.map(newsArticle -> ResponseEntity.ok().body(newsArticle))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("5newest")
    public ResponseEntity<GetAllNewsResponse> get5NewestArticles(){
        GetAllNewsResponse newsResponse = newsService.get5Newest();
        return ResponseEntity.ok(newsResponse);
    }

    @GetMapping("journalist/{id}")
    @RolesAllowed("JOURNALIST")
    public ResponseEntity<GetAllNewsResponse> getAllNewsArticles(@PathVariable("id") long id){
        GetAllNewsResponse newsResponse = newsService.getAllNewsByJournalist(id);
        return ResponseEntity.ok(newsResponse);
    }

    @GetMapping
    public ResponseEntity<GetAllNewsResponse> getAllNewsArticlesByJournalist(Long id){
        GetAllNewsResponse newsResponse = newsService.getAllItems();
        return ResponseEntity.ok(newsResponse);
    }

    @GetMapping("search")
    public ResponseEntity<GetAllNewsResponse> getAllNewsArticlesBySearchQuery(@RequestParam(value = "topic", required = false) String topic,
                                                                                @RequestParam(value = "title", required = false) String query){
        GetAllNewsResponse newsResponse = newsService.getByName(topic, query);

        return ResponseEntity.ok(newsResponse);
    }

    @PostMapping
    @RolesAllowed("JOURNALIST")
    public ResponseEntity<Object> createNewsArticle(@RequestBody @Valid CreateNewsArticleRequest request){
        try{
            CreateNewsArticleResponse response = newsService.createItem(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        }
        catch(Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @DeleteMapping("{id}")
    @RolesAllowed("JOURNALIST")
    public ResponseEntity<Void> deleteArticle(@PathVariable long id){
        newsService.deleteItem(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("{id}")
    @RolesAllowed("JOURNALIST")
    public ResponseEntity<Object> updateArticle(@PathVariable("id") long id,
                                                @RequestBody @Valid UpdateNewsArticleRequest request){
        request.setId(id);
        try{
            newsService.updateItem(request);
            return ResponseEntity.noContent().build();
        }
        catch(Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

}
