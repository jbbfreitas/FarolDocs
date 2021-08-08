package br.com.dev4u.faroldocs.repository.search;

import br.com.dev4u.faroldocs.domain.Projeto;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link Projeto} entity.
 */
public interface ProjetoSearchRepository extends ElasticsearchRepository<Projeto, Long> {}
