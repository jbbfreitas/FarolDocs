package br.com.dev4u.faroldocs.repository.search;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;

/**
 * Configure a Mock version of {@link DocumentoUserSearchRepository} to test the
 * application without starting Elasticsearch.
 */
@Configuration
public class DocumentoUserSearchRepositoryMockConfiguration {

    @MockBean
    private DocumentoUserSearchRepository mockDocumentoUserSearchRepository;
}
