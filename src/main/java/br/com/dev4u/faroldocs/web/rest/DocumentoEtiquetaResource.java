package br.com.dev4u.faroldocs.web.rest;

import static org.elasticsearch.index.query.QueryBuilders.*;

import br.com.dev4u.faroldocs.domain.DocumentoEtiqueta;
import br.com.dev4u.faroldocs.repository.DocumentoEtiquetaRepository;
import br.com.dev4u.faroldocs.service.DocumentoEtiquetaService;
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
 * REST controller for managing {@link br.com.dev4u.faroldocs.domain.DocumentoEtiqueta}.
 */
@RestController
@RequestMapping("/api")
public class DocumentoEtiquetaResource {

    private final Logger log = LoggerFactory.getLogger(DocumentoEtiquetaResource.class);

    private static final String ENTITY_NAME = "documentoEtiqueta";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DocumentoEtiquetaService documentoEtiquetaService;

    private final DocumentoEtiquetaRepository documentoEtiquetaRepository;

    public DocumentoEtiquetaResource(
        DocumentoEtiquetaService documentoEtiquetaService,
        DocumentoEtiquetaRepository documentoEtiquetaRepository
    ) {
        this.documentoEtiquetaService = documentoEtiquetaService;
        this.documentoEtiquetaRepository = documentoEtiquetaRepository;
    }

    /**
     * {@code POST  /documento-etiquetas} : Create a new documentoEtiqueta.
     *
     * @param documentoEtiqueta the documentoEtiqueta to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new documentoEtiqueta, or with status {@code 400 (Bad Request)} if the documentoEtiqueta has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/documento-etiquetas")
    public ResponseEntity<DocumentoEtiqueta> createDocumentoEtiqueta(@RequestBody DocumentoEtiqueta documentoEtiqueta)
        throws URISyntaxException {
        log.debug("REST request to save DocumentoEtiqueta : {}", documentoEtiqueta);
        if (documentoEtiqueta.getId() != null) {
            throw new BadRequestAlertException("A new documentoEtiqueta cannot already have an ID", ENTITY_NAME, "idexists");
        }
        DocumentoEtiqueta result = documentoEtiquetaService.save(documentoEtiqueta);
        return ResponseEntity
            .created(new URI("/api/documento-etiquetas/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /documento-etiquetas/:id} : Updates an existing documentoEtiqueta.
     *
     * @param id the id of the documentoEtiqueta to save.
     * @param documentoEtiqueta the documentoEtiqueta to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated documentoEtiqueta,
     * or with status {@code 400 (Bad Request)} if the documentoEtiqueta is not valid,
     * or with status {@code 500 (Internal Server Error)} if the documentoEtiqueta couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/documento-etiquetas/{id}")
    public ResponseEntity<DocumentoEtiqueta> updateDocumentoEtiqueta(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody DocumentoEtiqueta documentoEtiqueta
    ) throws URISyntaxException {
        log.debug("REST request to update DocumentoEtiqueta : {}, {}", id, documentoEtiqueta);
        if (documentoEtiqueta.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, documentoEtiqueta.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!documentoEtiquetaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        DocumentoEtiqueta result = documentoEtiquetaService.save(documentoEtiqueta);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, documentoEtiqueta.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /documento-etiquetas/:id} : Partial updates given fields of an existing documentoEtiqueta, field will ignore if it is null
     *
     * @param id the id of the documentoEtiqueta to save.
     * @param documentoEtiqueta the documentoEtiqueta to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated documentoEtiqueta,
     * or with status {@code 400 (Bad Request)} if the documentoEtiqueta is not valid,
     * or with status {@code 404 (Not Found)} if the documentoEtiqueta is not found,
     * or with status {@code 500 (Internal Server Error)} if the documentoEtiqueta couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/documento-etiquetas/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<DocumentoEtiqueta> partialUpdateDocumentoEtiqueta(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody DocumentoEtiqueta documentoEtiqueta
    ) throws URISyntaxException {
        log.debug("REST request to partial update DocumentoEtiqueta partially : {}, {}", id, documentoEtiqueta);
        if (documentoEtiqueta.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, documentoEtiqueta.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!documentoEtiquetaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<DocumentoEtiqueta> result = documentoEtiquetaService.partialUpdate(documentoEtiqueta);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, documentoEtiqueta.getId().toString())
        );
    }

    /**
     * {@code GET  /documento-etiquetas} : get all the documentoEtiquetas.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of documentoEtiquetas in body.
     */
    @GetMapping("/documento-etiquetas")
    public ResponseEntity<List<DocumentoEtiqueta>> getAllDocumentoEtiquetas(Pageable pageable) {
        log.debug("REST request to get a page of DocumentoEtiquetas");
        Page<DocumentoEtiqueta> page = documentoEtiquetaService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /documento-etiquetas/:id} : get the "id" documentoEtiqueta.
     *
     * @param id the id of the documentoEtiqueta to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the documentoEtiqueta, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/documento-etiquetas/{id}")
    public ResponseEntity<DocumentoEtiqueta> getDocumentoEtiqueta(@PathVariable Long id) {
        log.debug("REST request to get DocumentoEtiqueta : {}", id);
        Optional<DocumentoEtiqueta> documentoEtiqueta = documentoEtiquetaService.findOne(id);
        return ResponseUtil.wrapOrNotFound(documentoEtiqueta);
    }

    /**
     * {@code DELETE  /documento-etiquetas/:id} : delete the "id" documentoEtiqueta.
     *
     * @param id the id of the documentoEtiqueta to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/documento-etiquetas/{id}")
    public ResponseEntity<Void> deleteDocumentoEtiqueta(@PathVariable Long id) {
        log.debug("REST request to delete DocumentoEtiqueta : {}", id);
        documentoEtiquetaService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/documento-etiquetas?query=:query} : search for the documentoEtiqueta corresponding
     * to the query.
     *
     * @param query the query of the documentoEtiqueta search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/documento-etiquetas")
    public ResponseEntity<List<DocumentoEtiqueta>> searchDocumentoEtiquetas(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of DocumentoEtiquetas for query {}", query);
        Page<DocumentoEtiqueta> page = documentoEtiquetaService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

/**
     * {@code GET  /documento-etiquetas/doc/{id}} : get all  Etiquetas of one Documento.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of documentoEtiquetas in body.
     */
    @GetMapping("/documento-etiquetas/doc/{id}")
    public ResponseEntity<List<DocumentoEtiqueta>> getAllEtiquetasDoc(Pageable pageable, @PathVariable Long id ) {
        
        log.debug("request to get a page of etiquetasDoc");
        
        Page<DocumentoEtiqueta> page = documentoEtiquetaService.findAllEtiquetasDocumento(id,pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }



}
