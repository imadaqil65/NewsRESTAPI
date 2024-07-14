package nl.fontys.s3.individual.news.exception;

public class EmptyEntityException extends Exception{
    public EmptyEntityException() {
        super("No entity found");
    }
    public EmptyEntityException(String message) {
        super(message);
    }
}
