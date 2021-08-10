package br.com.dev4u.faroldocs.service;

import br.com.dev4u.faroldocs.domain.Etiqueta;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link Etiqueta}.
 */
public interface EtiquetaService {
    /**
     * Save a etiqueta.
     *
     * @param etiqueta the entity to save.
     * @return the persisted entity.
     */
    Etiqueta save(Etiqueta etiqueta);

    /**
     * Partially updates a etiqueta.
     *
     * @param etiqueta the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Etiqueta> partialUpdate(Etiqueta etiqueta);

    /**
     * Get all the etiquetas.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Etiqueta> findAll(Pageable pageable);

    /**
     * Get the "id" etiqueta.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Etiqueta> findOne(Long id);

    /**
     * Delete the "id" etiqueta.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the etiqueta corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Etiqueta> search(String query, Pageable pageable);
}
