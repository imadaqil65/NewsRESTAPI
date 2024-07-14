package nl.fontys.s3.individual.news.business.converters;

import nl.fontys.s3.individual.news.domain.Comment;
import nl.fontys.s3.individual.news.persistence.entities.CommentEntity;

import java.util.ArrayList;
import java.util.List;

public class CommentConverter {
    private CommentConverter(){}

    public static Comment convert(CommentEntity entity){
        return Comment.builder()
                .id(entity.getId())
                .userId(entity.getUser().getId())
                .user(entity.getUser().getName())
                .news(entity.getNews().getId())
                .content(entity.getContent())
                .build();
    }

    public static List<Comment> convert(List<CommentEntity> entities){
        List<Comment> comments = new ArrayList<>();

        entities.forEach(c -> comments.add(convert(c)));

        return comments;
    }
}
