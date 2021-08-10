package br.com.dev4u.faroldocs.service;

import br.com.dev4u.faroldocs.domain.TipoNorma;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link TipoNorma}.
 */
public interface TipoNormaService {
    /**
     * Save a tipoNorma.
     *
     * @param tipoNorma the entity to save.
     * @return the persisted entity.
     */
    TipoNorma save(TipoNorma tipoNorma);

    /**
     * Partially updates a tipoNorma.
     *
     * @param tipoNorma the entity to update partially.
     * @return the persisted entity.
     */
    Optional<TipoNorma> partialUpdate(TipoNorma tipoNorma);

    /**
     * Get all the tipoNormas.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<TipoNorma> findAll(Pageable pageable);

    /**
     * Get the "id" tipoNorma.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<TipoNorma> findOne(Long id);

    /**
     * Delete the "id" tipoNorma.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the tipoNorma corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<TipoNorma> search(String query, Pageable pageable);
}
