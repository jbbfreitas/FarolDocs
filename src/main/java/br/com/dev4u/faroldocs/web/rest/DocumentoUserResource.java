package br.com.dev4u.faroldocs.web.rest;

import static org.elasticsearch.index.query.QueryBuilders.*;

import br.com.dev4u.faroldocs.domain.DocumentoUser;
import br.com.dev4u.faroldocs.repository.DocumentoUserRepository;
import br.com.dev4u.faroldocs.service.DocumentoUserService;
import br.com.dev4u.faroldocs.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.StreamSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link br.com.dev4u.faroldocs.domain.DocumentoUser}.
 */
@RestController
@RequestMapping("/api")
public class DocumentoUserResource {

    private final Logger log = LoggerFactory.getLogger(DocumentoUserResource.class);

    private static final String ENTITY_NAME = "documentoUser";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DocumentoUserService documentoUserService;

    private final DocumentoUserRepository documentoUserRepository;

    public DocumentoUserResource(DocumentoUserService documentoUserService, DocumentoUserRepository documentoUserRepository) {
        this.documentoUserService = documentoUserService;
        this.documentoUserRepository = documentoUserRepository;
    }

    /**
     * {@code POST  /documento-users} : Create a new documentoUser.
     *
     * @param documentoUser the documentoUser to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new documentoUser, or with status {@code 400 (Bad Request)} if the documentoUser has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/documento-users")
    public ResponseEntity<DocumentoUser> createDocumentoUser(@RequestBody DocumentoUser documentoUser) throws URISyntaxException {
        log.debug("REST request to save DocumentoUser : {}", documentoUser);
        if (documentoUser.getId() != null) {
            throw new BadRequestAlertException("A new documentoUser cannot already have an ID", ENTITY_NAME, "idexists");
        }
        DocumentoUser result = documentoUserService.save(documentoUser);
        return ResponseEntity
            .created(new URI("/api/documento-users/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /documento-users/:id} : Updates an existing documentoUser.
     *
     * @param id the id of the documentoUser to save.
     * @param documentoUser the documentoUser to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated documentoUser,
     * or with status {@code 400 (Bad Request)} if the documentoUser is not valid,
     * or with status {@code 500 (Internal Server Error)} if the documentoUser couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/documento-users/{id}")
    public ResponseEntity<DocumentoUser> updateDocumentoUser(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody DocumentoUser documentoUser
    ) throws URISyntaxException {
        log.debug("REST request to update DocumentoUser : {}, {}", id, documentoUser);
        if (documentoUser.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, documentoUser.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!documentoUserRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        DocumentoUser result = documentoUserService.save(documentoUser);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, documentoUser.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /documento-users/:id} : Partial updates given fields of an existing documentoUser, field will ignore if it is null
     *
     * @param id the id of the documentoUser to save.
     * @param documentoUser the documentoUser to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated documentoUser,
     * or with status {@code 400 (Bad Request)} if the documentoUser is not valid,
     * or with status {@code 404 (Not Found)} if the documentoUser is not found,
     * or with status {@code 500 (Internal Server Error)} if the documentoUser couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/documento-users/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<DocumentoUser> partialUpdateDocumentoUser(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody DocumentoUser documentoUser
    ) throws URISyntaxException {
        log.debug("REST request to partial update DocumentoUser partially : {}, {}", id, documentoUser);
        if (documentoUser.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, documentoUser.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!documentoUserRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<DocumentoUser> result = documentoUserService.partialUpdate(documentoUser);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, documentoUser.getId().toString())
        );
    }

    /**
     * {@code GET  /documento-users} : get all the documentoUsers.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of documentoUsers in body.
     */
    @GetMapping("/documento-users")
    public ResponseEntity<List<DocumentoUser>> getAllDocumentoUsers(Pageable pageable) {
        log.debug("REST request to get a page of DocumentoUsers");
        Page<DocumentoUser> page = documentoUserService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /documento-users/:id} : get the "id" documentoUser.
     *
     * @param id the id of the documentoUser to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the documentoUser, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/documento-users/{id}")
    public ResponseEntity<DocumentoUser> getDocumentoUser(@PathVariable Long id) {
        log.debug("REST request to get DocumentoUser : {}", id);
        Optional<DocumentoUser> documentoUser = documentoUserService.findOne(id);
        return ResponseUtil.wrapOrNotFound(documentoUser);
    }

    /**
     * {@code DELETE  /documento-users/:id} : delete the "id" documentoUser.
     *
     * @param id the id of the documentoUser to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/documento-users/{id}")
    public ResponseEntity<Void> deleteDocumentoUser(@PathVariable Long id) {
        log.debug("REST request to delete DocumentoUser : {}", id);
        documentoUserService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/documento-users?query=:query} : search for the documentoUser corresponding
     * to the query.
     *
     * @param query the query of the documentoUser search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/documento-users")
    public ResponseEntity<List<DocumentoUser>> searchDocumentoUsers(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of DocumentoUsers for query {}", query);
        Page<DocumentoUser> page = documentoUserService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
