package br.com.dev4u.faroldocs.service;

import br.com.dev4u.faroldocs.domain.DocumentoUser;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link DocumentoUser}.
 */
public interface DocumentoUserService {
    /**
     * Save a documentoUser.
     *
     * @param documentoUser the entity to save.
     * @return the persisted entity.
     */
    DocumentoUser save(DocumentoUser documentoUser);

    /**
     * Partially updates a documentoUser.
     *
     * @param documentoUser the entity to update partially.
     * @return the persisted entity.
     */
    Optional<DocumentoUser> partialUpdate(DocumentoUser documentoUser);

    /**
     * Get all the documentoUsers.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<DocumentoUser> findAll(Pageable pageable);

    /**
     * Get the "id" documentoUser.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<DocumentoUser> findOne(Long id);

    /**
     * Delete the "id" documentoUser.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the documentoUser corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<DocumentoUser> search(String query, Pageable pageable);
}
