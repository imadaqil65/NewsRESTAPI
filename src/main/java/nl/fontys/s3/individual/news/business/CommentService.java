package nl.fontys.s3.individual.news.business;

import nl.fontys.s3.individual.news.business.interfaces.DataCreationInterface;
import nl.fontys.s3.individual.news.domain.dto.comment.CreateCommentRequest;
import nl.fontys.s3.individual.news.domain.dto.comment.CreateCommentResponse;
import nl.fontys.s3.individual.news.domain.dto.comment.UpdateCommentRequest;

public interface CommentService extends DataCreationInterface<CreateCommentResponse, CreateCommentRequest, UpdateCommentRequest> {

}
