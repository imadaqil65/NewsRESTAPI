package nl.fontys.s3.individual.news.business.interfaces;

import nl.fontys.s3.individual.news.exception.EmptyEntityException;

public interface DataCreationInterface<S, T, U> {
    S createItem(T item);
    void updateItem(U item) throws EmptyEntityException;
    void deleteItem(long id);

}
