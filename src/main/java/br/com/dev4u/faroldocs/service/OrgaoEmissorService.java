package br.com.dev4u.faroldocs.service;

import br.com.dev4u.faroldocs.domain.OrgaoEmissor;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link OrgaoEmissor}.
 */
public interface OrgaoEmissorService {
    /**
     * Save a orgaoEmissor.
     *
     * @param orgaoEmissor the entity to save.
     * @return the persisted entity.
     */
    OrgaoEmissor save(OrgaoEmissor orgaoEmissor);

    /**
     * Partially updates a orgaoEmissor.
     *
     * @param orgaoEmissor the entity to update partially.
     * @return the persisted entity.
     */
    Optional<OrgaoEmissor> partialUpdate(OrgaoEmissor orgaoEmissor);

    /**
     * Get all the orgaoEmissors.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<OrgaoEmissor> findAll(Pageable pageable);

    /**
     * Get the "id" orgaoEmissor.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<OrgaoEmissor> findOne(Long id);

    /**
     * Delete the "id" orgaoEmissor.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the orgaoEmissor corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<OrgaoEmissor> search(String query, Pageable pageable);
}
