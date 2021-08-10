package br.com.dev4u.faroldocs.web.rest;

import static org.elasticsearch.index.query.QueryBuilders.*;

import br.com.dev4u.faroldocs.domain.OrgaoEmissor;
import br.com.dev4u.faroldocs.repository.OrgaoEmissorRepository;
import br.com.dev4u.faroldocs.service.OrgaoEmissorService;
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
 * REST controller for managing {@link br.com.dev4u.faroldocs.domain.OrgaoEmissor}.
 */
@RestController
@RequestMapping("/api")
public class OrgaoEmissorResource {

    private final Logger log = LoggerFactory.getLogger(OrgaoEmissorResource.class);

    private static final String ENTITY_NAME = "orgaoEmissor";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final OrgaoEmissorService orgaoEmissorService;

    private final OrgaoEmissorRepository orgaoEmissorRepository;

    public OrgaoEmissorResource(OrgaoEmissorService orgaoEmissorService, OrgaoEmissorRepository orgaoEmissorRepository) {
        this.orgaoEmissorService = orgaoEmissorService;
        this.orgaoEmissorRepository = orgaoEmissorRepository;
    }

    /**
     * {@code POST  /orgao-emissors} : Create a new orgaoEmissor.
     *
     * @param orgaoEmissor the orgaoEmissor to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new orgaoEmissor, or with status {@code 400 (Bad Request)} if the orgaoEmissor has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/orgao-emissors")
    public ResponseEntity<OrgaoEmissor> createOrgaoEmissor(@Valid @RequestBody OrgaoEmissor orgaoEmissor) throws URISyntaxException {
        log.debug("REST request to save OrgaoEmissor : {}", orgaoEmissor);
        if (orgaoEmissor.getId() != null) {
            throw new BadRequestAlertException("A new orgaoEmissor cannot already have an ID", ENTITY_NAME, "idexists");
        }
        OrgaoEmissor result = orgaoEmissorService.save(orgaoEmissor);
        return ResponseEntity
            .created(new URI("/api/orgao-emissors/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /orgao-emissors/:id} : Updates an existing orgaoEmissor.
     *
     * @param id the id of the orgaoEmissor to save.
     * @param orgaoEmissor the orgaoEmissor to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated orgaoEmissor,
     * or with status {@code 400 (Bad Request)} if the orgaoEmissor is not valid,
     * or with status {@code 500 (Internal Server Error)} if the orgaoEmissor couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/orgao-emissors/{id}")
    public ResponseEntity<OrgaoEmissor> updateOrgaoEmissor(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody OrgaoEmissor orgaoEmissor
    ) throws URISyntaxException {
        log.debug("REST request to update OrgaoEmissor : {}, {}", id, orgaoEmissor);
        if (orgaoEmissor.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, orgaoEmissor.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!orgaoEmissorRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        OrgaoEmissor result = orgaoEmissorService.save(orgaoEmissor);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, orgaoEmissor.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /orgao-emissors/:id} : Partial updates given fields of an existing orgaoEmissor, field will ignore if it is null
     *
     * @param id the id of the orgaoEmissor to save.
     * @param orgaoEmissor the orgaoEmissor to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated orgaoEmissor,
     * or with status {@code 400 (Bad Request)} if the orgaoEmissor is not valid,
     * or with status {@code 404 (Not Found)} if the orgaoEmissor is not found,
     * or with status {@code 500 (Internal Server Error)} if the orgaoEmissor couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/orgao-emissors/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<OrgaoEmissor> partialUpdateOrgaoEmissor(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody OrgaoEmissor orgaoEmissor
    ) throws URISyntaxException {
        log.debug("REST request to partial update OrgaoEmissor partially : {}, {}", id, orgaoEmissor);
        if (orgaoEmissor.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, orgaoEmissor.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!orgaoEmissorRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<OrgaoEmissor> result = orgaoEmissorService.partialUpdate(orgaoEmissor);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, orgaoEmissor.getId().toString())
        );
    }

    /**
     * {@code GET  /orgao-emissors} : get all the orgaoEmissors.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of orgaoEmissors in body.
     */
    @GetMapping("/orgao-emissors")
    public ResponseEntity<List<OrgaoEmissor>> getAllOrgaoEmissors(Pageable pageable) {
        log.debug("REST request to get a page of OrgaoEmissors");
        Page<OrgaoEmissor> page = orgaoEmissorService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /orgao-emissors/:id} : get the "id" orgaoEmissor.
     *
     * @param id the id of the orgaoEmissor to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the orgaoEmissor, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/orgao-emissors/{id}")
    public ResponseEntity<OrgaoEmissor> getOrgaoEmissor(@PathVariable Long id) {
        log.debug("REST request to get OrgaoEmissor : {}", id);
        Optional<OrgaoEmissor> orgaoEmissor = orgaoEmissorService.findOne(id);
        return ResponseUtil.wrapOrNotFound(orgaoEmissor);
    }

    /**
     * {@code DELETE  /orgao-emissors/:id} : delete the "id" orgaoEmissor.
     *
     * @param id the id of the orgaoEmissor to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/orgao-emissors/{id}")
    public ResponseEntity<Void> deleteOrgaoEmissor(@PathVariable Long id) {
        log.debug("REST request to delete OrgaoEmissor : {}", id);
        orgaoEmissorService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/orgao-emissors?query=:query} : search for the orgaoEmissor corresponding
     * to the query.
     *
     * @param query the query of the orgaoEmissor search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/orgao-emissors")
    public ResponseEntity<List<OrgaoEmissor>> searchOrgaoEmissors(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of OrgaoEmissors for query {}", query);
        Page<OrgaoEmissor> page = orgaoEmissorService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
