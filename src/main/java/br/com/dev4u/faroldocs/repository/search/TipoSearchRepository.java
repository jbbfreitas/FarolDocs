package br.com.dev4u.faroldocs.repository.search;

import br.com.dev4u.faroldocs.domain.Tipo;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link Tipo} entity.
 */
public interface TipoSearchRepository extends ElasticsearchRepository<Tipo, Long> {}
