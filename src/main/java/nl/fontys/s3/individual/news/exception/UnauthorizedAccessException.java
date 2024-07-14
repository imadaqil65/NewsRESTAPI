package nl.fontys.s3.individual.news.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class UnauthorizedAccessException extends ResponseStatusException {
    public UnauthorizedAccessException() {
        super(HttpStatus.FORBIDDEN, "ROLE_UNAUTHORIZED");
    }
}