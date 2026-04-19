package com.compagnieaerienne.repository;

import java.util.List;
import java.util.Optional;

public interface CrudRepository<T, ID> {
    void ajouter(T entity);
    void modifier(T entity);
    Optional<T> chercherParId(ID id);
    boolean supprimer(ID id);
    List<T> lister();
}
