package nl.fontys.s3.individual.news.business;

import nl.fontys.s3.individual.news.business.interfaces.DataCreationInterface;
import nl.fontys.s3.individual.news.domain.NewsArticle;
import nl.fontys.s3.individual.news.domain.dto.news.CreateNewsArticleRequest;
import nl.fontys.s3.individual.news.domain.dto.news.CreateNewsArticleResponse;
import nl.fontys.s3.individual.news.domain.dto.news.GetAllNewsResponse;
import nl.fontys.s3.individual.news.domain.dto.news.UpdateNewsArticleRequest;

import java.util.Optional;

public interface NewsService extends DataCreationInterface<CreateNewsArticleResponse, CreateNewsArticleRequest, UpdateNewsArticleRequest> {

    GetAllNewsResponse get5Newest();
    GetAllNewsResponse getByName(String topic, String search);
    GetAllNewsResponse getAllItems();
    GetAllNewsResponse getAllNewsByJournalist(Long id);
    Optional<NewsArticle> getItem(long id);
}
