package br.com.dev4u.faroldocs.service.impl;

import static org.elasticsearch.index.query.QueryBuilders.*;

import br.com.dev4u.faroldocs.domain.Projeto;
import br.com.dev4u.faroldocs.repository.ProjetoRepository;
import br.com.dev4u.faroldocs.repository.search.ProjetoSearchRepository;
import br.com.dev4u.faroldocs.service.ProjetoService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Projeto}.
 */
@Service
@Transactional
public class ProjetoServiceImpl implements ProjetoService {

    private final Logger log = LoggerFactory.getLogger(ProjetoServiceImpl.class);

    private final ProjetoRepository projetoRepository;

    private final ProjetoSearchRepository projetoSearchRepository;

    public ProjetoServiceImpl(ProjetoRepository projetoRepository, ProjetoSearchRepository projetoSearchRepository) {
        this.projetoRepository = projetoRepository;
        this.projetoSearchRepository = projetoSearchRepository;
    }

    @Override
    public Projeto save(Projeto projeto) {
        log.debug("Request to save Projeto : {}", projeto);
        Projeto result = projetoRepository.save(projeto);
        projetoSearchRepository.save(result);
        return result;
    }

    @Override
    public Optional<Projeto> partialUpdate(Projeto projeto) {
        log.debug("Request to partially update Projeto : {}", projeto);

        return projetoRepository
            .findById(projeto.getId())
            .map(
                existingProjeto -> {
                    if (projeto.getNome() != null) {
                        existingProjeto.setNome(projeto.getNome());
                    }
                    if (projeto.getInicio() != null) {
                        existingProjeto.setInicio(projeto.getInicio());
                    }
                    if (projeto.getFim() != null) {
                        existingProjeto.setFim(projeto.getFim());
                    }
                    if (projeto.getSituacao() != null) {
                        existingProjeto.setSituacao(projeto.getSituacao());
                    }

                    return existingProjeto;
                }
            )
            .map(projetoRepository::save)
            .map(
                savedProjeto -> {
                    projetoSearchRepository.save(savedProjeto);

                    return savedProjeto;
                }
            );
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Projeto> findAll(Pageable pageable) {
        log.debug("Request to get all Projetos");
        return projetoRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Projeto> findOne(Long id) {
        log.debug("Request to get Projeto : {}", id);
        return projetoRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Projeto : {}", id);
        projetoRepository.deleteById(id);
        projetoSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Projeto> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Projetos for query {}", query);
        return projetoSearchRepository.search(queryStringQuery(query), pageable);
    }
}
