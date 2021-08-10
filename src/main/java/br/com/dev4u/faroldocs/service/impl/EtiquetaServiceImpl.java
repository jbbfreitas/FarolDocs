package br.com.dev4u.faroldocs.service.impl;

import static org.elasticsearch.index.query.QueryBuilders.*;

import br.com.dev4u.faroldocs.domain.Etiqueta;
import br.com.dev4u.faroldocs.repository.EtiquetaRepository;
import br.com.dev4u.faroldocs.repository.search.EtiquetaSearchRepository;
import br.com.dev4u.faroldocs.service.EtiquetaService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Etiqueta}.
 */
@Service
@Transactional
public class EtiquetaServiceImpl implements EtiquetaService {

    private final Logger log = LoggerFactory.getLogger(EtiquetaServiceImpl.class);

    private final EtiquetaRepository etiquetaRepository;

    private final EtiquetaSearchRepository etiquetaSearchRepository;

    public EtiquetaServiceImpl(EtiquetaRepository etiquetaRepository, EtiquetaSearchRepository etiquetaSearchRepository) {
        this.etiquetaRepository = etiquetaRepository;
        this.etiquetaSearchRepository = etiquetaSearchRepository;
    }

    @Override
    public Etiqueta save(Etiqueta etiqueta) {
        log.debug("Request to save Etiqueta : {}", etiqueta);
        Etiqueta result = etiquetaRepository.save(etiqueta);
        etiquetaSearchRepository.save(result);
        return result;
    }

    @Override
    public Optional<Etiqueta> partialUpdate(Etiqueta etiqueta) {
        log.debug("Request to partially update Etiqueta : {}", etiqueta);

        return etiquetaRepository
            .findById(etiqueta.getId())
            .map(
                existingEtiqueta -> {
                    if (etiqueta.getNome() != null) {
                        existingEtiqueta.setNome(etiqueta.getNome());
                    }

                    return existingEtiqueta;
                }
            )
            .map(etiquetaRepository::save)
            .map(
                savedEtiqueta -> {
                    etiquetaSearchRepository.save(savedEtiqueta);

                    return savedEtiqueta;
                }
            );
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Etiqueta> findAll(Pageable pageable) {
        log.debug("Request to get all Etiquetas");
        return etiquetaRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Etiqueta> findOne(Long id) {
        log.debug("Request to get Etiqueta : {}", id);
        return etiquetaRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Etiqueta : {}", id);
        etiquetaRepository.deleteById(id);
        etiquetaSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Etiqueta> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Etiquetas for query {}", query);
        return etiquetaSearchRepository.search(queryStringQuery(query), pageable);
    }
}
