package br.com.dev4u.faroldocs.web.rest;

import static org.elasticsearch.index.query.QueryBuilders.*;

import br.com.dev4u.faroldocs.domain.TipoNorma;
import br.com.dev4u.faroldocs.repository.TipoNormaRepository;
import br.com.dev4u.faroldocs.service.TipoNormaService;
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
 * REST controller for managing {@link br.com.dev4u.faroldocs.domain.TipoNorma}.
 */
@RestController
@RequestMapping("/api")
public class TipoNormaResource {

    private final Logger log = LoggerFactory.getLogger(TipoNormaResource.class);

    private static final String ENTITY_NAME = "tipoNorma";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TipoNormaService tipoNormaService;

    private final TipoNormaRepository tipoNormaRepository;

    public TipoNormaResource(TipoNormaService tipoNormaService, TipoNormaRepository tipoNormaRepository) {
        this.tipoNormaService = tipoNormaService;
        this.tipoNormaRepository = tipoNormaRepository;
    }

    /**
     * {@code POST  /tipo-normas} : Create a new tipoNorma.
     *
     * @param tipoNorma the tipoNorma to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new tipoNorma, or with status {@code 400 (Bad Request)} if the tipoNorma has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/tipo-normas")
    public ResponseEntity<TipoNorma> createTipoNorma(@Valid @RequestBody TipoNorma tipoNorma) throws URISyntaxException {
        log.debug("REST request to save TipoNorma : {}", tipoNorma);
        if (tipoNorma.getId() != null) {
            throw new BadRequestAlertException("A new tipoNorma cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TipoNorma result = tipoNormaService.save(tipoNorma);
        return ResponseEntity
            .created(new URI("/api/tipo-normas/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /tipo-normas/:id} : Updates an existing tipoNorma.
     *
     * @param id the id of the tipoNorma to save.
     * @param tipoNorma the tipoNorma to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated tipoNorma,
     * or with status {@code 400 (Bad Request)} if the tipoNorma is not valid,
     * or with status {@code 500 (Internal Server Error)} if the tipoNorma couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/tipo-normas/{id}")
    public ResponseEntity<TipoNorma> updateTipoNorma(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody TipoNorma tipoNorma
    ) throws URISyntaxException {
        log.debug("REST request to update TipoNorma : {}, {}", id, tipoNorma);
        if (tipoNorma.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, tipoNorma.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!tipoNormaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        TipoNorma result = tipoNormaService.save(tipoNorma);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, tipoNorma.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /tipo-normas/:id} : Partial updates given fields of an existing tipoNorma, field will ignore if it is null
     *
     * @param id the id of the tipoNorma to save.
     * @param tipoNorma the tipoNorma to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated tipoNorma,
     * or with status {@code 400 (Bad Request)} if the tipoNorma is not valid,
     * or with status {@code 404 (Not Found)} if the tipoNorma is not found,
     * or with status {@code 500 (Internal Server Error)} if the tipoNorma couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/tipo-normas/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<TipoNorma> partialUpdateTipoNorma(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody TipoNorma tipoNorma
    ) throws URISyntaxException {
        log.debug("REST request to partial update TipoNorma partially : {}, {}", id, tipoNorma);
        if (tipoNorma.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, tipoNorma.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!tipoNormaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<TipoNorma> result = tipoNormaService.partialUpdate(tipoNorma);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, tipoNorma.getId().toString())
        );
    }

    /**
     * {@code GET  /tipo-normas} : get all the tipoNormas.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of tipoNormas in body.
     */
    @GetMapping("/tipo-normas")
    public ResponseEntity<List<TipoNorma>> getAllTipoNormas(Pageable pageable) {
        log.debug("REST request to get a page of TipoNormas");
        Page<TipoNorma> page = tipoNormaService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /tipo-normas/:id} : get the "id" tipoNorma.
     *
     * @param id the id of the tipoNorma to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the tipoNorma, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/tipo-normas/{id}")
    public ResponseEntity<TipoNorma> getTipoNorma(@PathVariable Long id) {
        log.debug("REST request to get TipoNorma : {}", id);
        Optional<TipoNorma> tipoNorma = tipoNormaService.findOne(id);
        return ResponseUtil.wrapOrNotFound(tipoNorma);
    }

    /**
     * {@code DELETE  /tipo-normas/:id} : delete the "id" tipoNorma.
     *
     * @param id the id of the tipoNorma to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/tipo-normas/{id}")
    public ResponseEntity<Void> deleteTipoNorma(@PathVariable Long id) {
        log.debug("REST request to delete TipoNorma : {}", id);
        tipoNormaService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/tipo-normas?query=:query} : search for the tipoNorma corresponding
     * to the query.
     *
     * @param query the query of the tipoNorma search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/tipo-normas")
    public ResponseEntity<List<TipoNorma>> searchTipoNormas(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of TipoNormas for query {}", query);
        Page<TipoNorma> page = tipoNormaService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
