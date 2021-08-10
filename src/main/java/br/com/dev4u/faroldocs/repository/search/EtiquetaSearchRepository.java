package br.com.dev4u.faroldocs.repository.search;

import br.com.dev4u.faroldocs.domain.Etiqueta;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link Etiqueta} entity.
 */
public interface EtiquetaSearchRepository extends ElasticsearchRepository<Etiqueta, Long> {}
