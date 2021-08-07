package br.com.dev4u.faroldocs.service.impl;

import static org.elasticsearch.index.query.QueryBuilders.*;

import br.com.dev4u.faroldocs.domain.Documento;
import br.com.dev4u.faroldocs.repository.DocumentoRepository;
import br.com.dev4u.faroldocs.repository.search.DocumentoSearchRepository;
import br.com.dev4u.faroldocs.service.DocumentoService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Documento}.
 */
@Service
@Transactional
public class DocumentoServiceImpl implements DocumentoService {

    private final Logger log = LoggerFactory.getLogger(DocumentoServiceImpl.class);

    private final DocumentoRepository documentoRepository;

    private final DocumentoSearchRepository documentoSearchRepository;

    public DocumentoServiceImpl(DocumentoRepository documentoRepository, DocumentoSearchRepository documentoSearchRepository) {
        this.documentoRepository = documentoRepository;
        this.documentoSearchRepository = documentoSearchRepository;
    }

    @Override
    public Documento save(Documento documento) {
        log.debug("Request to save Documento : {}", documento);
        Documento result = documentoRepository.save(documento);
        documentoSearchRepository.save(result);
        return result;
    }

    @Override
    public Optional<Documento> partialUpdate(Documento documento) {
        log.debug("Request to partially update Documento : {}", documento);

        return documentoRepository
            .findById(documento.getId())
            .map(
                existingDocumento -> {
                    if (documento.getProjeto() != null) {
                        existingDocumento.setProjeto(documento.getProjeto());
                    }
                    if (documento.getAssunto() != null) {
                        existingDocumento.setAssunto(documento.getAssunto());
                    }
                    if (documento.getDescricao() != null) {
                        existingDocumento.setDescricao(documento.getDescricao());
                    }
                    if (documento.getEtiqueta() != null) {
                        existingDocumento.setEtiqueta(documento.getEtiqueta());
                    }
                    if (documento.getUrl() != null) {
                        existingDocumento.setUrl(documento.getUrl());
                    }

                    return existingDocumento;
                }
            )
            .map(documentoRepository::save)
            .map(
                savedDocumento -> {
                    documentoSearchRepository.save(savedDocumento);

                    return savedDocumento;
                }
            );
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Documento> findAll(Pageable pageable) {
        log.debug("Request to get all Documentos");
        return documentoRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Documento> findOne(Long id) {
        log.debug("Request to get Documento : {}", id);
        return documentoRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Documento : {}", id);
        documentoRepository.deleteById(id);
        documentoSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Documento> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Documentos for query {}", query);
        return documentoSearchRepository.search(queryStringQuery(query), pageable);
    }
}
