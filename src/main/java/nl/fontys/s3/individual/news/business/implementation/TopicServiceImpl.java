package nl.fontys.s3.individual.news.business.implementation;

import lombok.RequiredArgsConstructor;
import nl.fontys.s3.individual.news.business.TopicService;
import nl.fontys.s3.individual.news.business.converters.TopicConverter;
import nl.fontys.s3.individual.news.domain.Topic;
import nl.fontys.s3.individual.news.domain.dto.topic.*;
import nl.fontys.s3.individual.news.persistence.entities.TopicEntity;
import nl.fontys.s3.individual.news.persistence.TopicRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TopicServiceImpl implements TopicService {
    private final TopicRepository topicRepo;

    @Override
    public CreateNewTopicResponse createItem(CreateTopicRequest item) {

        return CreateNewTopicResponse.builder()
                .id(topicRepo.save(
                        TopicEntity.builder()
                                .topicName(item.getTopicName())
                                .build())
                                .getId())
                .build();
    }

    @Override
    public void updateItem(UpdateTopicRequest item) {
        TopicEntity entity = topicRepo.getReferenceById(item.getId());
        entity.setTopicName(item.getTopicName());

        topicRepo.save(entity);
    }

    @Override
    public List<TopicDetails> getTopicDetails() {
        return topicRepo.findTopicDetails();
    }

    @Override
    public void deleteItem(long id) {

        topicRepo.deleteById(id);
    }

    @Override
    public GetAllTopicResponse getAllItems() {
        List<Topic> topics = TopicConverter.convert(topicRepo.findAll());

        return GetAllTopicResponse.builder().topicList(topics).build();
    }

    @Override
    public Optional<Topic> getItem(long id) {

        return topicRepo.findById(id).map(TopicConverter::convert);
    }
}
