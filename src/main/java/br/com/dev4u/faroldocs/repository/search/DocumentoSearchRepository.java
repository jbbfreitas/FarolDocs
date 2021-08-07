package br.com.dev4u.faroldocs.repository.search;

import br.com.dev4u.faroldocs.domain.Documento;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link Documento} entity.
 */
public interface DocumentoSearchRepository extends ElasticsearchRepository<Documento, Long> {}
