package br.com.dev4u.faroldocs.service.impl;

import static org.elasticsearch.index.query.QueryBuilders.*;

import br.com.dev4u.faroldocs.domain.OrgaoEmissor;
import br.com.dev4u.faroldocs.repository.OrgaoEmissorRepository;
import br.com.dev4u.faroldocs.repository.search.OrgaoEmissorSearchRepository;
import br.com.dev4u.faroldocs.service.OrgaoEmissorService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link OrgaoEmissor}.
 */
@Service
@Transactional
public class OrgaoEmissorServiceImpl implements OrgaoEmissorService {

    private final Logger log = LoggerFactory.getLogger(OrgaoEmissorServiceImpl.class);

    private final OrgaoEmissorRepository orgaoEmissorRepository;

    private final OrgaoEmissorSearchRepository orgaoEmissorSearchRepository;

    public OrgaoEmissorServiceImpl(
        OrgaoEmissorRepository orgaoEmissorRepository,
        OrgaoEmissorSearchRepository orgaoEmissorSearchRepository
    ) {
        this.orgaoEmissorRepository = orgaoEmissorRepository;
        this.orgaoEmissorSearchRepository = orgaoEmissorSearchRepository;
    }

    @Override
    public OrgaoEmissor save(OrgaoEmissor orgaoEmissor) {
        log.debug("Request to save OrgaoEmissor : {}", orgaoEmissor);
        OrgaoEmissor result = orgaoEmissorRepository.save(orgaoEmissor);
        orgaoEmissorSearchRepository.save(result);
        return result;
    }

    @Override
    public Optional<OrgaoEmissor> partialUpdate(OrgaoEmissor orgaoEmissor) {
        log.debug("Request to partially update OrgaoEmissor : {}", orgaoEmissor);

        return orgaoEmissorRepository
            .findById(orgaoEmissor.getId())
            .map(
                existingOrgaoEmissor -> {
                    if (orgaoEmissor.getSigla() != null) {
                        existingOrgaoEmissor.setSigla(orgaoEmissor.getSigla());
                    }
                    if (orgaoEmissor.getNome() != null) {
                        existingOrgaoEmissor.setNome(orgaoEmissor.getNome());
                    }

                    return existingOrgaoEmissor;
                }
            )
            .map(orgaoEmissorRepository::save)
            .map(
                savedOrgaoEmissor -> {
                    orgaoEmissorSearchRepository.save(savedOrgaoEmissor);

                    return savedOrgaoEmissor;
                }
            );
    }

    @Override
    @Transactional(readOnly = true)
    public Page<OrgaoEmissor> findAll(Pageable pageable) {
        log.debug("Request to get all OrgaoEmissors");
        return orgaoEmissorRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<OrgaoEmissor> findOne(Long id) {
        log.debug("Request to get OrgaoEmissor : {}", id);
        return orgaoEmissorRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete OrgaoEmissor : {}", id);
        orgaoEmissorRepository.deleteById(id);
        orgaoEmissorSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<OrgaoEmissor> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of OrgaoEmissors for query {}", query);
        return orgaoEmissorSearchRepository.search(queryStringQuery(query), pageable);
    }
}
