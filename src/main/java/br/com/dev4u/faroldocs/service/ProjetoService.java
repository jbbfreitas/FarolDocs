package br.com.dev4u.faroldocs.service;

import br.com.dev4u.faroldocs.domain.Projeto;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link Projeto}.
 */
public interface ProjetoService {
    /**
     * Save a projeto.
     *
     * @param projeto the entity to save.
     * @return the persisted entity.
     */
    Projeto save(Projeto projeto);

    /**
     * Partially updates a projeto.
     *
     * @param projeto the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Projeto> partialUpdate(Projeto projeto);

    /**
     * Get all the projetos.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Projeto> findAll(Pageable pageable);

    /**
     * Get the "id" projeto.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Projeto> findOne(Long id);

    /**
     * Delete the "id" projeto.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the projeto corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Projeto> search(String query, Pageable pageable);
}
