package nl.fontys.s3.individual.news.business;

import nl.fontys.s3.individual.news.business.implementation.NewsServiceImpl;
import nl.fontys.s3.individual.news.configuration.security.token.AccessToken;
import nl.fontys.s3.individual.news.domain.NewsArticle;
import nl.fontys.s3.individual.news.domain.Usertype;
import nl.fontys.s3.individual.news.domain.dto.news.CreateNewsArticleRequest;
import nl.fontys.s3.individual.news.domain.dto.news.UpdateNewsArticleRequest;
import nl.fontys.s3.individual.news.exception.UnauthorizedAccessException;
import nl.fontys.s3.individual.news.persistence.CommentRepository;
import nl.fontys.s3.individual.news.persistence.NewsRepository;
import nl.fontys.s3.individual.news.persistence.TopicRepository;
import nl.fontys.s3.individual.news.persistence.UserRepository;
import nl.fontys.s3.individual.news.persistence.entities.NewsEntity;
import nl.fontys.s3.individual.news.persistence.entities.TopicEntity;
import nl.fontys.s3.individual.news.persistence.entities.UserEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NewsServiceImplTest {
    @Mock
    private AccessToken requestAccessToken;
    @Mock
    private UserRepository userRepository;
    @Mock
    private TopicRepository topicRepository;
    @Mock
    private NewsRepository newsRepository;
    @Mock
    private CommentRepository commentRepository;
    @InjectMocks
    private NewsServiceImpl newsService;

    private UserEntity notJournalist(){
        return UserEntity.builder()
                .id(2L)
                .email("j.doe@email.com")
                .name("John Doe")
                .password("password")
                .bio("person biography")
                .usertype(Usertype.READER)
                .build();
    }

    private UserEntity journalistEntity(){
        return UserEntity.builder()
                .id(1L)
                .email("j.doe@email.com")
                .name("John Doe")
                .password("password")
                .bio("person biography")
                .usertype(Usertype.JOURNALIST)
                .build();
    }

    private CreateNewsArticleRequest mockNews(){
        return CreateNewsArticleRequest.builder()
                .title("Title")
                .topic(1L)
                .journalistId(1L)
                .content("Lorem Ipsum Dolor Sit Amet")
                .build();
    }

    private CreateNewsArticleRequest mockNotJournalistNews(){
        return CreateNewsArticleRequest.builder()
                .title("Title")
                .topic(1L)
                .journalistId(2L)
                .content("Lorem Ipsum Dolor Sit Amet")
                .build();
    }
    private UpdateNewsArticleRequest mockUpdateNews(){
        return UpdateNewsArticleRequest.builder()
                .title("Title2")
                .topic(1L)
                .journalistId(1L)
                .content("Lorem Ipsum Dolor Sit Amet ABCDE")
                .build();
    }

    private NewsEntity mockNewsEntity(Long id){
        return NewsEntity.builder()
                .id(id)
                .title("Title" + id)
                .topic(TopicEntity.builder()
                        .id(1L).topicName("sports")
                        .build())
                .journalist(journalistEntity())
                .content("Lorem Ipsum Dolor Sit Amet")
                .dateTime(LocalDate.parse("2022-02-02"))
                .build();
    }

    private NewsEntity mockFalseNewsEntity(Long id){
        return NewsEntity.builder()
                .id(id)
                .title("Title" + id)
                .topic(TopicEntity.builder()
                        .id(1L).topicName("sports")
                        .build())
                .journalist(notJournalist())
                .content("Lorem Ipsum Dolor Sit Amet")
                .dateTime(LocalDate.parse("2022-02-02"))
                .build();
    }

    private NewsEntity mockNewsEntity(Long id, Long topicId, String topic){
        return NewsEntity.builder()
                .id(id)
                .title("Title")
                .topic(TopicEntity.builder()
                        .id(topicId).topicName(topic)
                        .build())
                .journalist(journalistEntity())
                .content("Lorem Ipsum Dolor Sit Amet")
                .dateTime(LocalDate.parse("2022-02-02"))
                .build();
    }
    
    private void setUpMockRepositories() {
        // Mock behavior for userRepository
        when(userRepository.findById(1L)).thenReturn(Optional.of(journalistEntity()));

        // Mock behavior for newsRepository
        when(topicRepository.findById(1L)).thenReturn(Optional.of(TopicEntity.builder()
                .id(1L)
                .topicName("sports")
                .build()));
    }

    @Test
    void createNewItem_successful() {
        setUpMockRepositories();

        NewsEntity savedEntity = mockNewsEntity(1L);
        
        when(newsRepository.save(any(NewsEntity.class))).thenReturn(savedEntity);

        Long newsId = newsService.createItem(mockNews()).getArticleId();
        assertNotNull(newsId);
        assertEquals(1L, newsId);

        verify(newsRepository, times(1)).save(any(NewsEntity.class));
    }

    @Test
    void createNewItem_unauthorized() {
        when(userRepository.findById(2L)).thenReturn(Optional.of(notJournalist()));

        assertThrows(UnauthorizedAccessException.class, this::attemptToCreateItemAsNotJournalist);
    }

    private void attemptToCreateItemAsNotJournalist() {
        newsService.createItem(mockNotJournalistNews());
    }

    @Test
    void updateItem_success() {
        setUpMockRepositories();
        UpdateNewsArticleRequest news = mockUpdateNews();
        NewsEntity existingEntity = mockNewsEntity(1L);

        when(newsRepository.getReferenceById(news.getId())).thenReturn(existingEntity);
        when(requestAccessToken.getUserId()).thenReturn(1L);

        newsService.updateItem(news);
        verify(newsRepository, times(1)).getReferenceById(news.getId());
        verify(newsRepository, times(1)).save(any(NewsEntity.class));
    }

    @Test
    void deleteItem_success() {
        long newsId = 1L;
        doNothing().when(newsRepository).deleteById(newsId);

        newsService.deleteItem(newsId);
        verify(newsRepository, times(1)).deleteById(newsId);
    }

    @Test
    void getItem_success() {

        long newsId = 1L;
        NewsEntity returnedEntity = mockNewsEntity(newsId);
        when(newsRepository.getReferenceById(newsId)).thenReturn(returnedEntity);
        when(commentRepository.findAllByNewsId_IdOrderByNewsIdDesc(newsId)).thenReturn(new ArrayList<>());
        when(commentRepository.countByNewsId(newsId)).thenReturn(0L);

        Optional<NewsArticle> result = newsService.getItem(newsId);
        assertTrue(result.isPresent());
        assertEquals("Title1", result.get().getTitle());
        verify(newsRepository, times(1)).getReferenceById(newsId);
    }

    @Test
    void getAllItems_success(){

        List<NewsEntity> entityList = List.of(mockNewsEntity(1L), mockNewsEntity(2L));
        when(newsRepository.findAll()).thenReturn(entityList);

        List<NewsArticle> result = newsService.getAllItems().getNewsArticleList();
        assertFalse(result.isEmpty());
        assertEquals(2, result.size());
        verify(newsRepository, times(1)).findAll();
    }

    @Test
    void getAllNewsByJournalist_success(){

        List<NewsEntity> entityList = List.of(mockNewsEntity(1L), mockNewsEntity(2L));
        when(newsRepository.findAllByJournalist_Id(1L)).thenReturn(entityList.stream().filter(x->x.getJournalist().getId().equals(1L)).toList());

        List<NewsArticle> result = newsService.getAllNewsByJournalist(1L).getNewsArticleList();
        assertFalse(result.isEmpty());
        assertEquals(2, result.size());
        verify(newsRepository, times(1)).findAllByJournalist_Id(1L);
    }

    @Test
    void getNewest5_success(){

        List<NewsEntity> entityList = List.of(  mockNewsEntity(1L),
                                                mockNewsEntity(2L),
                                                mockNewsEntity(3L),
                                                mockNewsEntity(4L),
                                                mockNewsEntity(5L),
                                                mockNewsEntity(6L)
        );
        when(newsRepository.findFirst5ByOrderByDateTimeDesc()).thenReturn(entityList.stream()
                .sorted((e1, e2) -> e2.getDateTime().compareTo(e1.getDateTime()))
                .limit(5)
                .collect(Collectors.toList()));

        List<NewsArticle> result = newsService.get5Newest().getNewsArticleList();
        assertFalse(result.isEmpty());
        assertEquals(5, result.size());
        verify(newsRepository, times(1)).findFirst5ByOrderByDateTimeDesc();
    }

    @Test
    void getAllItems_getByName_withTopic_NoTitle_success(){

        String param = "sports";
        List<NewsEntity> entityList = List.of(mockNewsEntity(1L), mockNewsEntity(2L, 2L, "Travels"));
        when(newsRepository.findAllByTopic_TopicNameOrderByDateTimeDesc(param)).thenReturn(entityList.stream().filter(x->x.getTopic().getTopicName().equals(param)).toList());

        List<NewsArticle> result = newsService.getByName(param, null).getNewsArticleList();
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        verify(newsRepository, times(1)).findAllByTopic_TopicNameOrderByDateTimeDesc(param);
    }

    @Test
    void getAllItems_getByName_noTopic_success(){

        List<NewsEntity> entityList = List.of(mockNewsEntity(1L), mockNewsEntity(2L));
        when(newsRepository.findAllByTitleContaining("1")).thenReturn(entityList.stream()
                .filter(x -> x.getTitle().contains("1"))
                .collect(Collectors.toList()));

        // Call the service method
        List<NewsArticle> result = newsService.getByName(null,"1").getNewsArticleList();

        // Assertions
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());  // Only one entity has a title containing "1"
        verify(newsRepository, times(1)).findAllByTitleContaining("1");
    }

    @Test
    void getAllItems_getByName_withTopic_success() {
        List<NewsEntity> entityList = List.of(mockNewsEntity(1L), mockNewsEntity(2L));
        when(newsRepository.findAllByTopicTopicNameAndTitleContaining("sports", "1")).thenReturn(
                entityList.stream()
                        .filter(x -> x.getTitle().contains("1") && x.getTopic().getTopicName().equals("sports"))
                        .collect(Collectors.toList())
        );

        // Call the service method
        List<NewsArticle> result = newsService.getByName("sports", "1").getNewsArticleList();

        // Assertions
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());  // Only one entity has a title containing "1"
        verify(newsRepository, times(1)).findAllByTopicTopicNameAndTitleContaining("sports", "1");
    }

}
