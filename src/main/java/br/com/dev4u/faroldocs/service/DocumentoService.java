package br.com.dev4u.faroldocs.service;

import br.com.dev4u.faroldocs.domain.Documento;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link Documento}.
 */
public interface DocumentoService {
    /**
     * Save a documento.
     *
     * @param documento the entity to save.
     * @return the persisted entity.
     */
    Documento save(Documento documento);

    /**
     * Partially updates a documento.
     *
     * @param documento the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Documento> partialUpdate(Documento documento);

    /**
     * Get all the documentos.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Documento> findAll(Pageable pageable);

    /**
     * Get all the documentos with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Documento> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" documento.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Documento> findOne(Long id);

    /**
     * Delete the "id" documento.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the documento corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Documento> search(String query, Pageable pageable);
}
