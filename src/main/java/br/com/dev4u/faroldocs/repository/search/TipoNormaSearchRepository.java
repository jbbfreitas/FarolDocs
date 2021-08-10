package br.com.dev4u.faroldocs.repository.search;

import br.com.dev4u.faroldocs.domain.TipoNorma;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link TipoNorma} entity.
 */
public interface TipoNormaSearchRepository extends ElasticsearchRepository<TipoNorma, Long> {}
