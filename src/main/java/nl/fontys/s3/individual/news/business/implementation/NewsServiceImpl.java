package nl.fontys.s3.individual.news.business.implementation;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import nl.fontys.s3.individual.news.business.converters.CommentConverter;
import nl.fontys.s3.individual.news.configuration.security.token.AccessToken;
import nl.fontys.s3.individual.news.domain.Comment;
import nl.fontys.s3.individual.news.domain.Usertype;
import nl.fontys.s3.individual.news.domain.dto.news.CreateNewsArticleRequest;
import nl.fontys.s3.individual.news.domain.dto.news.CreateNewsArticleResponse;
import nl.fontys.s3.individual.news.domain.dto.news.GetAllNewsResponse;
import nl.fontys.s3.individual.news.domain.dto.news.UpdateNewsArticleRequest;
import nl.fontys.s3.individual.news.exception.EmptyEntityException;
import nl.fontys.s3.individual.news.exception.UnauthorizedAccessException;
import nl.fontys.s3.individual.news.exception.UnauthorizedDataAccessException;
import nl.fontys.s3.individual.news.persistence.CommentRepository;
import nl.fontys.s3.individual.news.persistence.entities.UserEntity;
import nl.fontys.s3.individual.news.persistence.TopicRepository;
import nl.fontys.s3.individual.news.persistence.UserRepository;
import nl.fontys.s3.individual.news.business.NewsService;
import nl.fontys.s3.individual.news.domain.NewsArticle;
import nl.fontys.s3.individual.news.business.converters.NewsConverter;
import nl.fontys.s3.individual.news.persistence.entities.NewsEntity;
import nl.fontys.s3.individual.news.persistence.entities.TopicEntity;
import nl.fontys.s3.individual.news.persistence.NewsRepository;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class NewsServiceImpl implements NewsService {

    private final NewsRepository newsRepo;
    private final UserRepository userRepo;
    private final TopicRepository topicRepo;
    private final CommentRepository commentRepo;
    private final AccessToken requestAccessToken;
    private static final String ERROR_1 = "EDIT_NOT_ALLOWED";

    @SneakyThrows
    private NewsEntity saveNewArticle(CreateNewsArticleRequest newsArticle) {
        UserEntity userEntity = userRepo.findById(newsArticle.getJournalistId())
                .orElseThrow(EmptyEntityException::new);
        if (!userEntity.getUsertype().equals(Usertype.JOURNALIST)){
            throw new UnauthorizedAccessException();
        }
        TopicEntity topicEntity = topicRepo.findById(newsArticle.getTopic())
                .orElseThrow(EmptyEntityException::new);



        NewsEntity newArticle = NewsEntity.builder()
                .title(newsArticle.getTitle())
                .dateTime(LocalDate.now())
                .journalist(userEntity)
                .topic(topicEntity)
                .content(newsArticle.getContent())
                .build();
        return newsRepo.save(newArticle);
    }

    @Override
    public CreateNewsArticleResponse createItem(CreateNewsArticleRequest item) {
        return CreateNewsArticleResponse.builder()
                .articleId(saveNewArticle(item).getId())
                .build();
    }

    @SneakyThrows
    private void updateFields(UpdateNewsArticleRequest news, NewsEntity entity) {
        TopicEntity topic = topicRepo.findById(news.getTopic())
                .orElseThrow(EmptyEntityException::new);
        UserEntity user = userRepo.findById(news.getJournalistId())
                .orElseThrow(EmptyEntityException::new);
        String editedContent = String.format("(Edited - %s) %s",
                LocalDate.now().format(DateTimeFormatter.ofPattern("dd MMMM yyyy")),
                news.getContent());

        entity.setTitle(news.getTitle());
        entity.setContent(editedContent);
        entity.setTopic(topic);
        entity.setJournalist(user);

        newsRepo.save(entity);
    }

    @Override
    public void updateItem(UpdateNewsArticleRequest item) {
        if(!Objects.equals(requestAccessToken.getUserId(), item.getJournalistId())){
            throw new UnauthorizedDataAccessException(ERROR_1);
        }
        NewsEntity newsEntity = newsRepo.getReferenceById(item.getId());
        updateFields(item, newsEntity);
    }

    @Override
    public void deleteItem(long id) {
        newsRepo.deleteById(id);
    }

    @Override
    public GetAllNewsResponse get5Newest() {
        return GetAllNewsResponse
                .builder()
                .newsArticleList(NewsConverter.convert(newsRepo.findFirst5ByOrderByDateTimeDesc()))
                .build();
    }

    @Override
    public GetAllNewsResponse getByName(String topic, String search) {
        List<NewsEntity> entities;
        if(StringUtils.hasText(topic) && StringUtils.hasText(search)) {
            entities = newsRepo.findAllByTopicTopicNameAndTitleContaining(topic, search);
        }
        else if(StringUtils.hasText(topic) && !StringUtils.hasText(search)){
            entities = newsRepo.findAllByTopic_TopicNameOrderByDateTimeDesc(topic);
        }
        else {
            entities = newsRepo.findAllByTitleContaining(search);
        }
        return GetAllNewsResponse
                .builder()
                .newsArticleList(NewsConverter
                        .convert(entities))
                .build();
    }

    @Override
    public GetAllNewsResponse getAllItems() {
        List<NewsEntity> entities = newsRepo.findAll();
        return GetAllNewsResponse
                .builder()
                .newsArticleList(NewsConverter
                        .convert(entities))
                .build();
    }

    @Override
    public GetAllNewsResponse getAllNewsByJournalist(Long id) {
        return GetAllNewsResponse
                .builder()
                .newsArticleList(NewsConverter.convert(newsRepo.findAllByJournalist_Id(id)))
                .build();
    }

    @Override
    public Optional<NewsArticle> getItem(long id) {
        List<Comment> comments = CommentConverter.convert(commentRepo.findAllByNewsId_IdOrderByNewsIdDesc(id));
        Long num = commentRepo.countByNewsId(id);
        return NewsConverter.convert(newsRepo.getReferenceById(id), num, comments);
    }
}
