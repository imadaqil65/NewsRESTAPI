package nl.fontys.s3.individual.news.business.interfaces;

import java.util.Optional;

public interface DataReadInterface<S, T> {
    S getAllItems();
    Optional<T> getItem(long id);
}
