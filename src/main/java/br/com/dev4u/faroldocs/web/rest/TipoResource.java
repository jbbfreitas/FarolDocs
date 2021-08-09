package br.com.dev4u.faroldocs.web.rest;

import static org.elasticsearch.index.query.QueryBuilders.*;

import br.com.dev4u.faroldocs.domain.Tipo;
import br.com.dev4u.faroldocs.repository.TipoRepository;
import br.com.dev4u.faroldocs.service.TipoService;
import br.com.dev4u.faroldocs.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.StreamSupport;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
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
 * REST controller for managing {@link br.com.dev4u.faroldocs.domain.Tipo}.
 */
@RestController
@RequestMapping("/api")
public class TipoResource {

    private final Logger log = LoggerFactory.getLogger(TipoResource.class);

    private static final String ENTITY_NAME = "tipo";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TipoService tipoService;

    private final TipoRepository tipoRepository;

    public TipoResource(TipoService tipoService, TipoRepository tipoRepository) {
        this.tipoService = tipoService;
        this.tipoRepository = tipoRepository;
    }

    /**
     * {@code POST  /tipos} : Create a new tipo.
     *
     * @param tipo the tipo to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new tipo, or with status {@code 400 (Bad Request)} if the tipo has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/tipos")
    public ResponseEntity<Tipo> createTipo(@Valid @RequestBody Tipo tipo) throws URISyntaxException {
        log.debug("REST request to save Tipo : {}", tipo);
        if (tipo.getId() != null) {
            throw new BadRequestAlertException("A new tipo cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Tipo result = tipoService.save(tipo);
        return ResponseEntity
            .created(new URI("/api/tipos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /tipos/:id} : Updates an existing tipo.
     *
     * @param id the id of the tipo to save.
     * @param tipo the tipo to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated tipo,
     * or with status {@code 400 (Bad Request)} if the tipo is not valid,
     * or with status {@code 500 (Internal Server Error)} if the tipo couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/tipos/{id}")
    public ResponseEntity<Tipo> updateTipo(@PathVariable(value = "id", required = false) final Long id, @Valid @RequestBody Tipo tipo)
        throws URISyntaxException {
        log.debug("REST request to update Tipo : {}, {}", id, tipo);
        if (tipo.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, tipo.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!tipoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Tipo result = tipoService.save(tipo);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, tipo.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /tipos/:id} : Partial updates given fields of an existing tipo, field will ignore if it is null
     *
     * @param id the id of the tipo to save.
     * @param tipo the tipo to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated tipo,
     * or with status {@code 400 (Bad Request)} if the tipo is not valid,
     * or with status {@code 404 (Not Found)} if the tipo is not found,
     * or with status {@code 500 (Internal Server Error)} if the tipo couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/tipos/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<Tipo> partialUpdateTipo(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Tipo tipo
    ) throws URISyntaxException {
        log.debug("REST request to partial update Tipo partially : {}, {}", id, tipo);
        if (tipo.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, tipo.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!tipoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Tipo> result = tipoService.partialUpdate(tipo);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, tipo.getId().toString())
        );
    }

    /**
     * {@code GET  /tipos} : get all the tipos.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of tipos in body.
     */
    @GetMapping("/tipos")
    public ResponseEntity<List<Tipo>> getAllTipos(Pageable pageable) {
        log.debug("REST request to get a page of Tipos");
        Page<Tipo> page = tipoService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /tipos/:id} : get the "id" tipo.
     *
     * @param id the id of the tipo to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the tipo, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/tipos/{id}")
    public ResponseEntity<Tipo> getTipo(@PathVariable Long id) {
        log.debug("REST request to get Tipo : {}", id);
        Optional<Tipo> tipo = tipoService.findOne(id);
        return ResponseUtil.wrapOrNotFound(tipo);
    }

    /**
     * {@code DELETE  /tipos/:id} : delete the "id" tipo.
     *
     * @param id the id of the tipo to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/tipos/{id}")
    public ResponseEntity<Void> deleteTipo(@PathVariable Long id) {
        log.debug("REST request to delete Tipo : {}", id);
        tipoService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/tipos?query=:query} : search for the tipo corresponding
     * to the query.
     *
     * @param query the query of the tipo search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/tipos")
    public ResponseEntity<List<Tipo>> searchTipos(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of Tipos for query {}", query);
        Page<Tipo> page = tipoService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
