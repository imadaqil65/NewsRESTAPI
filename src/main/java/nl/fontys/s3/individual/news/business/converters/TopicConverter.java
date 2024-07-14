package nl.fontys.s3.individual.news.business.converters;

import nl.fontys.s3.individual.news.domain.Topic;
import nl.fontys.s3.individual.news.persistence.entities.TopicEntity;
import java.util.ArrayList;
import java.util.List;

public class TopicConverter {
    private TopicConverter(){}
    public static Topic convert(TopicEntity entity) {
        return Topic.builder()
                .id(entity.getId())
                .topicName(entity.getTopicName())
                .build();
    }

    public static List<Topic> convert(List<TopicEntity> entities) {
        List<Topic> topics = new ArrayList<>();

        entities.forEach(t-> topics.add(convert(t)));

        return topics;
    }
}
