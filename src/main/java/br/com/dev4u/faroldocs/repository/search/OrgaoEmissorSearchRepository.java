package br.com.dev4u.faroldocs.repository.search;

import br.com.dev4u.faroldocs.domain.OrgaoEmissor;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link OrgaoEmissor} entity.
 */
public interface OrgaoEmissorSearchRepository extends ElasticsearchRepository<OrgaoEmissor, Long> {}
