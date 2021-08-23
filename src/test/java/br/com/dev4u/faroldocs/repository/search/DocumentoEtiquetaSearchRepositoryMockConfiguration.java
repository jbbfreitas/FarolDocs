package br.com.dev4u.faroldocs.repository.search;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;

/**
 * Configure a Mock version of {@link DocumentoEtiquetaSearchRepository} to test the
 * application without starting Elasticsearch.
 */
@Configuration
public class DocumentoEtiquetaSearchRepositoryMockConfiguration {

    @MockBean
    private DocumentoEtiquetaSearchRepository mockDocumentoEtiquetaSearchRepository;
}
