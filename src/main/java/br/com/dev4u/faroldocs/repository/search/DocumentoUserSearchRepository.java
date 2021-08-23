package br.com.dev4u.faroldocs.repository.search;

import br.com.dev4u.faroldocs.domain.DocumentoUser;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link DocumentoUser} entity.
 */
public interface DocumentoUserSearchRepository extends ElasticsearchRepository<DocumentoUser, Long> {}
