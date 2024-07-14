package nl.fontys.s3.individual.news.business;

import nl.fontys.s3.individual.news.business.interfaces.DataReadInterface;
import nl.fontys.s3.individual.news.domain.Topic;
import nl.fontys.s3.individual.news.business.interfaces.DataCreationInterface;
import nl.fontys.s3.individual.news.domain.dto.topic.*;

import java.util.List;

public interface TopicService extends DataReadInterface<GetAllTopicResponse, Topic>, DataCreationInterface<CreateNewTopicResponse, CreateTopicRequest, UpdateTopicRequest> {
    List<TopicDetails> getTopicDetails();
}
