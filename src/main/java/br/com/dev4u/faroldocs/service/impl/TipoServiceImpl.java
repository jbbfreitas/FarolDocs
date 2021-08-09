package br.com.dev4u.faroldocs.service.impl;

import static org.elasticsearch.index.query.QueryBuilders.*;

import br.com.dev4u.faroldocs.domain.Tipo;
import br.com.dev4u.faroldocs.repository.TipoRepository;
import br.com.dev4u.faroldocs.repository.search.TipoSearchRepository;
import br.com.dev4u.faroldocs.service.TipoService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Tipo}.
 */
@Service
@Transactional
public class TipoServiceImpl implements TipoService {

    private final Logger log = LoggerFactory.getLogger(TipoServiceImpl.class);

    private final TipoRepository tipoRepository;

    private final TipoSearchRepository tipoSearchRepository;

    public TipoServiceImpl(TipoRepository tipoRepository, TipoSearchRepository tipoSearchRepository) {
        this.tipoRepository = tipoRepository;
        this.tipoSearchRepository = tipoSearchRepository;
    }

    @Override
    public Tipo save(Tipo tipo) {
        log.debug("Request to save Tipo : {}", tipo);
        Tipo result = tipoRepository.save(tipo);
        tipoSearchRepository.save(result);
        return result;
    }

    @Override
    public Optional<Tipo> partialUpdate(Tipo tipo) {
        log.debug("Request to partially update Tipo : {}", tipo);

        return tipoRepository
            .findById(tipo.getId())
            .map(
                existingTipo -> {
                    if (tipo.getNome() != null) {
                        existingTipo.setNome(tipo.getNome());
                    }
                    if (tipo.getDescricao() != null) {
                        existingTipo.setDescricao(tipo.getDescricao());
                    }

                    return existingTipo;
                }
            )
            .map(tipoRepository::save)
            .map(
                savedTipo -> {
                    tipoSearchRepository.save(savedTipo);

                    return savedTipo;
                }
            );
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Tipo> findAll(Pageable pageable) {
        log.debug("Request to get all Tipos");
        return tipoRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Tipo> findOne(Long id) {
        log.debug("Request to get Tipo : {}", id);
        return tipoRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Tipo : {}", id);
        tipoRepository.deleteById(id);
        tipoSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Tipo> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Tipos for query {}", query);
        return tipoSearchRepository.search(queryStringQuery(query), pageable);
    }
}
