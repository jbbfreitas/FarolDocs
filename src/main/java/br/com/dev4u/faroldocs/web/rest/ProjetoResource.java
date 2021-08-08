package br.com.dev4u.faroldocs.web.rest;

import static org.elasticsearch.index.query.QueryBuilders.*;

import br.com.dev4u.faroldocs.domain.Projeto;
import br.com.dev4u.faroldocs.repository.ProjetoRepository;
import br.com.dev4u.faroldocs.service.ProjetoService;
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
 * REST controller for managing {@link br.com.dev4u.faroldocs.domain.Projeto}.
 */
@RestController
@RequestMapping("/api")
public class ProjetoResource {

    private final Logger log = LoggerFactory.getLogger(ProjetoResource.class);

    private static final String ENTITY_NAME = "projeto";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ProjetoService projetoService;

    private final ProjetoRepository projetoRepository;

    public ProjetoResource(ProjetoService projetoService, ProjetoRepository projetoRepository) {
        this.projetoService = projetoService;
        this.projetoRepository = projetoRepository;
    }

    /**
     * {@code POST  /projetos} : Create a new projeto.
     *
     * @param projeto the projeto to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new projeto, or with status {@code 400 (Bad Request)} if the projeto has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/projetos")
    public ResponseEntity<Projeto> createProjeto(@Valid @RequestBody Projeto projeto) throws URISyntaxException {
        log.debug("REST request to save Projeto : {}", projeto);
        if (projeto.getId() != null) {
            throw new BadRequestAlertException("A new projeto cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Projeto result = projetoService.save(projeto);
        return ResponseEntity
            .created(new URI("/api/projetos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /projetos/:id} : Updates an existing projeto.
     *
     * @param id the id of the projeto to save.
     * @param projeto the projeto to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated projeto,
     * or with status {@code 400 (Bad Request)} if the projeto is not valid,
     * or with status {@code 500 (Internal Server Error)} if the projeto couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/projetos/{id}")
    public ResponseEntity<Projeto> updateProjeto(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Projeto projeto
    ) throws URISyntaxException {
        log.debug("REST request to update Projeto : {}, {}", id, projeto);
        if (projeto.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, projeto.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!projetoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Projeto result = projetoService.save(projeto);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, projeto.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /projetos/:id} : Partial updates given fields of an existing projeto, field will ignore if it is null
     *
     * @param id the id of the projeto to save.
     * @param projeto the projeto to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated projeto,
     * or with status {@code 400 (Bad Request)} if the projeto is not valid,
     * or with status {@code 404 (Not Found)} if the projeto is not found,
     * or with status {@code 500 (Internal Server Error)} if the projeto couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/projetos/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<Projeto> partialUpdateProjeto(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Projeto projeto
    ) throws URISyntaxException {
        log.debug("REST request to partial update Projeto partially : {}, {}", id, projeto);
        if (projeto.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, projeto.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!projetoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Projeto> result = projetoService.partialUpdate(projeto);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, projeto.getId().toString())
        );
    }

    /**
     * {@code GET  /projetos} : get all the projetos.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of projetos in body.
     */
    @GetMapping("/projetos")
    public ResponseEntity<List<Projeto>> getAllProjetos(Pageable pageable) {
        log.debug("REST request to get a page of Projetos");
        Page<Projeto> page = projetoService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /projetos/:id} : get the "id" projeto.
     *
     * @param id the id of the projeto to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the projeto, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/projetos/{id}")
    public ResponseEntity<Projeto> getProjeto(@PathVariable Long id) {
        log.debug("REST request to get Projeto : {}", id);
        Optional<Projeto> projeto = projetoService.findOne(id);
        return ResponseUtil.wrapOrNotFound(projeto);
    }

    /**
     * {@code DELETE  /projetos/:id} : delete the "id" projeto.
     *
     * @param id the id of the projeto to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/projetos/{id}")
    public ResponseEntity<Void> deleteProjeto(@PathVariable Long id) {
        log.debug("REST request to delete Projeto : {}", id);
        projetoService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/projetos?query=:query} : search for the projeto corresponding
     * to the query.
     *
     * @param query the query of the projeto search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/projetos")
    public ResponseEntity<List<Projeto>> searchProjetos(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of Projetos for query {}", query);
        Page<Projeto> page = projetoService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
