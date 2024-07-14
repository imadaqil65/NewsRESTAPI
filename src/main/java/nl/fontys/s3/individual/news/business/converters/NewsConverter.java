package nl.fontys.s3.individual.news.business.converters;

import nl.fontys.s3.individual.news.domain.Comment;
import nl.fontys.s3.individual.news.domain.NewsArticle;
import nl.fontys.s3.individual.news.persistence.entities.NewsEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class NewsConverter {
    private NewsConverter(){}
    public static NewsArticle convert(NewsEntity entity) {
        return NewsArticle.builder()
                .id(entity.getId())
                .title(entity.getTitle())
                .topic(TopicConverter.convert(entity.getTopic()))
                .journalist(UserConverter.convert(entity.getJournalist()))
                .dateTime(entity.getDateTime())
                .content(entity.getContent())
                .build();
    }

    public static Optional<NewsArticle> convert(NewsEntity entity, Long numComments,List<Comment> comments) {
        return Optional.ofNullable(NewsArticle.builder()
                .id(entity.getId())
                .title(entity.getTitle())
                .topic(TopicConverter.convert(entity.getTopic()))
                .journalist(UserConverter.convert(entity.getJournalist()))
                .dateTime(entity.getDateTime())
                .content(entity.getContent())
                .numOfComments(numComments)
                .commentList(comments)
                .build());
    }

    public static List<NewsArticle> convert(List<NewsEntity> entities) {
        List<NewsArticle> news = new ArrayList<>();

        entities.forEach(n-> news.add(convert(n)));

        return news;
    }

}
