package nl.fontys.s3.individual.news.business;

import nl.fontys.s3.individual.news.business.implementation.CommentServiceImpl;
import nl.fontys.s3.individual.news.configuration.security.token.AccessToken;
import nl.fontys.s3.individual.news.domain.dto.comment.CreateCommentRequest;
import nl.fontys.s3.individual.news.domain.dto.comment.CreateCommentResponse;
import nl.fontys.s3.individual.news.domain.dto.comment.UpdateCommentRequest;
import nl.fontys.s3.individual.news.persistence.CommentRepository;
import nl.fontys.s3.individual.news.persistence.entities.CommentEntity;
import nl.fontys.s3.individual.news.persistence.entities.NewsEntity;
import nl.fontys.s3.individual.news.persistence.entities.UserEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CommentServiceImplTest {
    @Mock
    private CommentRepository commentRepository;
    @Mock
    private AccessToken requestAccessToken;
    @InjectMocks
    private CommentServiceImpl commentService;

    private CreateCommentRequest mockComment() {
        return CreateCommentRequest.builder()
                .userId(1L)
                .newsId(1L)
                .content("ABCDEFGHIJKLMNOPQRSTUVWXYZ")
                .build();
    }

    private UpdateCommentRequest mockUpdateComment(Long id) {
        return UpdateCommentRequest.builder()
                .id(id)
                .userId(1L)
                .newsId(1L)
                .content("ABCDEFGHIJKLMNOPQRSTUVWXYZ")
                .build();
    }

    private CommentEntity mockCommentEntity(Long id) {
        return CommentEntity.builder()
                .id(id)
                .user(UserEntity.builder().id(1L).build())
                .news(NewsEntity.builder().id(1L).build())
                .content("ABCDEFGHIJKLMNOPQRSTUVWXYZ")
                .build();
    }

    @Test
    void createNewItem_success() {
        CommentEntity savedEntity = mockCommentEntity(1L);
        when(commentRepository.save(any(CommentEntity.class))).thenReturn(savedEntity);

        CreateCommentResponse commentId = commentService.createItem(mockComment());
        assertNotNull(commentId);
        assertEquals(1L, commentId.getCommentId());
        verify(commentRepository, times(1)).save(any(CommentEntity.class));
    }

    @Test
    void updateItem_success() {
        UpdateCommentRequest comment = mockUpdateComment(1L);
        CommentEntity existingEntity = mockCommentEntity(1L);


        when(commentRepository.getReferenceById(comment.getId())).thenReturn(existingEntity);
        when(requestAccessToken.getUserId()).thenReturn(1L);

        commentService.updateItem(comment);
        verify(commentRepository, times(1)).getReferenceById(comment.getId());
        verify(commentRepository, times(1)).save(any(CommentEntity.class));
    }

    @Test
    void deleteItem_success() {
        long CommentId = 1L;
        doNothing().when(commentRepository).deleteById(CommentId);

        commentService.deleteItem(CommentId);
        verify(commentRepository, times(1)).deleteById(CommentId);
    }
}
