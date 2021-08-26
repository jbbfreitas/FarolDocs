package br.com.dev4u.faroldocs.service;

import br.com.dev4u.faroldocs.domain.DocumentoEtiqueta;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link DocumentoEtiqueta}.
 */
public interface DocumentoEtiquetaService {
    /**
     * Save a documentoEtiqueta.
     *
     * @param documentoEtiqueta the entity to save.
     * @return the persisted entity.
     */
    DocumentoEtiqueta save(DocumentoEtiqueta documentoEtiqueta);

    /**
     * Partially updates a documentoEtiqueta.
     *
     * @param documentoEtiqueta the entity to update partially.
     * @return the persisted entity.
     */
    Optional<DocumentoEtiqueta> partialUpdate(DocumentoEtiqueta documentoEtiqueta);

    /**
     * Get all the documentoEtiquetas.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<DocumentoEtiqueta> findAll(Pageable pageable);

    /**
     * Get the "id" documentoEtiqueta.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<DocumentoEtiqueta> findOne(Long id);

    /**
     * Delete the "id" documentoEtiqueta.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the documentoEtiqueta corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<DocumentoEtiqueta> search(String query, Pageable pageable);

    Page<DocumentoEtiqueta> findAllEtiquetasDocumento(Long id, Pageable pageable);
}
