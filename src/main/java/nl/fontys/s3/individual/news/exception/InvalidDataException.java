package nl.fontys.s3.individual.news.exception;

import org.springframework.http.HttpStatusCode;
import org.springframework.web.server.ResponseStatusException;

public class InvalidDataException extends ResponseStatusException {
    public InvalidDataException(HttpStatusCode status, String error) {
        super(status, error);
    }
}
