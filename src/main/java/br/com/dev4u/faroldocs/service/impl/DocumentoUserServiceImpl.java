package br.com.dev4u.faroldocs.service.impl;

import static org.elasticsearch.index.query.QueryBuilders.*;

import br.com.dev4u.faroldocs.domain.DocumentoUser;
import br.com.dev4u.faroldocs.repository.DocumentoUserRepository;
import br.com.dev4u.faroldocs.repository.search.DocumentoUserSearchRepository;
import br.com.dev4u.faroldocs.service.DocumentoUserService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link DocumentoUser}.
 */
@Service
@Transactional
public class DocumentoUserServiceImpl implements DocumentoUserService {

    private final Logger log = LoggerFactory.getLogger(DocumentoUserServiceImpl.class);

    private final DocumentoUserRepository documentoUserRepository;

    private final DocumentoUserSearchRepository documentoUserSearchRepository;

    public DocumentoUserServiceImpl(
        DocumentoUserRepository documentoUserRepository,
        DocumentoUserSearchRepository documentoUserSearchRepository
    ) {
        this.documentoUserRepository = documentoUserRepository;
        this.documentoUserSearchRepository = documentoUserSearchRepository;
    }

    @Override
    public DocumentoUser save(DocumentoUser documentoUser) {
        log.debug("Request to save DocumentoUser : {}", documentoUser);
        DocumentoUser result = documentoUserRepository.save(documentoUser);
        documentoUserSearchRepository.save(result);
        return result;
    }

    @Override
    public Optional<DocumentoUser> partialUpdate(DocumentoUser documentoUser) {
        log.debug("Request to partially update DocumentoUser : {}", documentoUser);

        return documentoUserRepository
            .findById(documentoUser.getId())
            .map(
                existingDocumentoUser -> {
                    return existingDocumentoUser;
                }
            )
            .map(documentoUserRepository::save)
            .map(
                savedDocumentoUser -> {
                    documentoUserSearchRepository.save(savedDocumentoUser);

                    return savedDocumentoUser;
                }
            );
    }

    @Override
    @Transactional(readOnly = true)
    public Page<DocumentoUser> findAll(Pageable pageable) {
        log.debug("Request to get all DocumentoUsers");
        return documentoUserRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<DocumentoUser> findOne(Long id) {
        log.debug("Request to get DocumentoUser : {}", id);
        return documentoUserRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete DocumentoUser : {}", id);
        documentoUserRepository.deleteById(id);
        documentoUserSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<DocumentoUser> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of DocumentoUsers for query {}", query);
        return documentoUserSearchRepository.search(queryStringQuery(query), pageable);
    }
}
