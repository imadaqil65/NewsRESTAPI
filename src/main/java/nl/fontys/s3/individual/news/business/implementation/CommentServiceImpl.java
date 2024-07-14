package nl.fontys.s3.individual.news.business.implementation;

import lombok.AllArgsConstructor;
import nl.fontys.s3.individual.news.business.CommentService;
import nl.fontys.s3.individual.news.configuration.security.token.AccessToken;
import nl.fontys.s3.individual.news.domain.dto.comment.CreateCommentRequest;
import nl.fontys.s3.individual.news.domain.dto.comment.CreateCommentResponse;
import nl.fontys.s3.individual.news.domain.dto.comment.UpdateCommentRequest;
import nl.fontys.s3.individual.news.exception.UnauthorizedDataAccessException;
import nl.fontys.s3.individual.news.persistence.CommentRepository;
import nl.fontys.s3.individual.news.persistence.entities.CommentEntity;
import nl.fontys.s3.individual.news.persistence.entities.NewsEntity;
import nl.fontys.s3.individual.news.persistence.entities.UserEntity;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@AllArgsConstructor
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepo;
    private final AccessToken requestAccessToken;
    private static final String ERROR_1 = "EDIT_NOT_ALLOWED";

    private CommentEntity saveNewComment(CreateCommentRequest item){
        UserEntity user = UserEntity.builder().id(item.getUserId()).build();
        NewsEntity news = NewsEntity.builder().id(item.getNewsId()).build();

        return commentRepo.save(CommentEntity.builder()
                        .news(news)
                        .user(user)
                        .content(item.getContent())
                        .build());
    }
    @Override
    public CreateCommentResponse createItem(CreateCommentRequest item) {

        return CreateCommentResponse.builder().commentId(saveNewComment(item).getId()).build();
    }

    @Override
    public void updateItem(UpdateCommentRequest item) {
        if(!Objects.equals(requestAccessToken.getUserId(), item.getUserId())){
            throw new UnauthorizedDataAccessException(ERROR_1);
        }
        CommentEntity entity = commentRepo.getReferenceById(item.getId());
        entity.setContent(item.getContent());
        commentRepo.save(entity);
    }

    @Override
    public void deleteItem(long id) {
        commentRepo.deleteById(id);
    }

}