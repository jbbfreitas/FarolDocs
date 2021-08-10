package br.com.dev4u.faroldocs.service.impl;

import static org.elasticsearch.index.query.QueryBuilders.*;

import br.com.dev4u.faroldocs.domain.TipoNorma;
import br.com.dev4u.faroldocs.repository.TipoNormaRepository;
import br.com.dev4u.faroldocs.repository.search.TipoNormaSearchRepository;
import br.com.dev4u.faroldocs.service.TipoNormaService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link TipoNorma}.
 */
@Service
@Transactional
public class TipoNormaServiceImpl implements TipoNormaService {

    private final Logger log = LoggerFactory.getLogger(TipoNormaServiceImpl.class);

    private final TipoNormaRepository tipoNormaRepository;

    private final TipoNormaSearchRepository tipoNormaSearchRepository;

    public TipoNormaServiceImpl(TipoNormaRepository tipoNormaRepository, TipoNormaSearchRepository tipoNormaSearchRepository) {
        this.tipoNormaRepository = tipoNormaRepository;
        this.tipoNormaSearchRepository = tipoNormaSearchRepository;
    }

    @Override
    public TipoNorma save(TipoNorma tipoNorma) {
        log.debug("Request to save TipoNorma : {}", tipoNorma);
        TipoNorma result = tipoNormaRepository.save(tipoNorma);
        tipoNormaSearchRepository.save(result);
        return result;
    }

    @Override
    public Optional<TipoNorma> partialUpdate(TipoNorma tipoNorma) {
        log.debug("Request to partially update TipoNorma : {}", tipoNorma);

        return tipoNormaRepository
            .findById(tipoNorma.getId())
            .map(
                existingTipoNorma -> {
                    if (tipoNorma.getTipo() != null) {
                        existingTipoNorma.setTipo(tipoNorma.getTipo());
                    }
                    if (tipoNorma.getDescricao() != null) {
                        existingTipoNorma.setDescricao(tipoNorma.getDescricao());
                    }

                    return existingTipoNorma;
                }
            )
            .map(tipoNormaRepository::save)
            .map(
                savedTipoNorma -> {
                    tipoNormaSearchRepository.save(savedTipoNorma);

                    return savedTipoNorma;
                }
            );
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TipoNorma> findAll(Pageable pageable) {
        log.debug("Request to get all TipoNormas");
        return tipoNormaRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<TipoNorma> findOne(Long id) {
        log.debug("Request to get TipoNorma : {}", id);
        return tipoNormaRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete TipoNorma : {}", id);
        tipoNormaRepository.deleteById(id);
        tipoNormaSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TipoNorma> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of TipoNormas for query {}", query);
        return tipoNormaSearchRepository.search(queryStringQuery(query), pageable);
    }
}
