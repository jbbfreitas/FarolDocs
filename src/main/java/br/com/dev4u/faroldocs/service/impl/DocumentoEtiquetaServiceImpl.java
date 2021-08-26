package br.com.dev4u.faroldocs.service.impl;

import static org.elasticsearch.index.query.QueryBuilders.*;

import br.com.dev4u.faroldocs.domain.DocumentoEtiqueta;
import br.com.dev4u.faroldocs.repository.DocumentoEtiquetaRepository;
import br.com.dev4u.faroldocs.repository.search.DocumentoEtiquetaSearchRepository;
import br.com.dev4u.faroldocs.service.DocumentoEtiquetaService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link DocumentoEtiqueta}.
 */
@Service
@Transactional
public class DocumentoEtiquetaServiceImpl implements DocumentoEtiquetaService {

    private final Logger log = LoggerFactory.getLogger(DocumentoEtiquetaServiceImpl.class);

    private final DocumentoEtiquetaRepository documentoEtiquetaRepository;

    private final DocumentoEtiquetaSearchRepository documentoEtiquetaSearchRepository;

    public DocumentoEtiquetaServiceImpl(
        DocumentoEtiquetaRepository documentoEtiquetaRepository,
        DocumentoEtiquetaSearchRepository documentoEtiquetaSearchRepository
    ) {
        this.documentoEtiquetaRepository = documentoEtiquetaRepository;
        this.documentoEtiquetaSearchRepository = documentoEtiquetaSearchRepository;
    }

    @Override
    public DocumentoEtiqueta save(DocumentoEtiqueta documentoEtiqueta) {
        log.debug("Request to save DocumentoEtiqueta : {}", documentoEtiqueta);
        DocumentoEtiqueta result = documentoEtiquetaRepository.save(documentoEtiqueta);
        documentoEtiquetaSearchRepository.save(result);
        return result;
    }

    @Override
    public Optional<DocumentoEtiqueta> partialUpdate(DocumentoEtiqueta documentoEtiqueta) {
        log.debug("Request to partially update DocumentoEtiqueta : {}", documentoEtiqueta);

        return documentoEtiquetaRepository
            .findById(documentoEtiqueta.getId())
            .map(
                existingDocumentoEtiqueta -> {
                    return existingDocumentoEtiqueta;
                }
            )
            .map(documentoEtiquetaRepository::save)
            .map(
                savedDocumentoEtiqueta -> {
                    documentoEtiquetaSearchRepository.save(savedDocumentoEtiqueta);

                    return savedDocumentoEtiqueta;
                }
            );
    }

    @Override
    @Transactional(readOnly = true)
    public Page<DocumentoEtiqueta> findAll(Pageable pageable) {
        log.debug("Request to get all DocumentoEtiquetas");
        return documentoEtiquetaRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<DocumentoEtiqueta> findOne(Long id) {
        log.debug("Request to get DocumentoEtiqueta : {}", id);
        return documentoEtiquetaRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete DocumentoEtiqueta : {}", id);
        documentoEtiquetaRepository.deleteById(id);
        documentoEtiquetaSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<DocumentoEtiqueta> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of DocumentoEtiquetas for query {}", query);
        return documentoEtiquetaSearchRepository.search(queryStringQuery(query), pageable);
    }

    

    @Override
    @Transactional(readOnly = true)
    public Page<DocumentoEtiqueta> findAllEtiquetasDocumento(Long id,Pageable pageable) {
        
         return documentoEtiquetaRepository.findAllEtiquetasDocumento(id, pageable);
    }

}
