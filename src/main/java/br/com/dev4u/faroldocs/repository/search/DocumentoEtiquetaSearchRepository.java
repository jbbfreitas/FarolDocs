package br.com.dev4u.faroldocs.repository.search;

import br.com.dev4u.faroldocs.domain.DocumentoEtiqueta;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link DocumentoEtiqueta} entity.
 */
public interface DocumentoEtiquetaSearchRepository extends ElasticsearchRepository<DocumentoEtiqueta, Long> {}
