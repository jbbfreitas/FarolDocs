package br.com.dev4u.faroldocs.service;

import br.com.dev4u.faroldocs.domain.Tipo;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link Tipo}.
 */
public interface TipoService {
    /**
     * Save a tipo.
     *
     * @param tipo the entity to save.
     * @return the persisted entity.
     */
    Tipo save(Tipo tipo);

    /**
     * Partially updates a tipo.
     *
     * @param tipo the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Tipo> partialUpdate(Tipo tipo);

    /**
     * Get all the tipos.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Tipo> findAll(Pageable pageable);

    /**
     * Get the "id" tipo.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Tipo> findOne(Long id);

    /**
     * Delete the "id" tipo.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the tipo corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Tipo> search(String query, Pageable pageable);
}
