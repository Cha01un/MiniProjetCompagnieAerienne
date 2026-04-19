package com.compagnieaerienne.repository;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

public class RepositoryEnMemoire<T, ID> implements CrudRepository<T, ID> {
    private final Map<ID, T> stockage = new LinkedHashMap<>();
    private final Function<T, ID> idExtractor;

    public RepositoryEnMemoire(Function<T, ID> idExtractor) {
        this.idExtractor = idExtractor;
    }

    @Override
    public void ajouter(T entity) {
        stockage.put(idExtractor.apply(entity), entity);
    }

    @Override
    public void modifier(T entity) {
        stockage.put(idExtractor.apply(entity), entity);
    }

    @Override
    public Optional<T> chercherParId(ID id) {
        return Optional.ofNullable(stockage.get(id));
    }

    @Override
    public boolean supprimer(ID id) {
        return stockage.remove(id) != null;
    }

    @Override
    public List<T> lister() {
        return new ArrayList<>(stockage.values());
    }
}
