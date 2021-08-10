package br.com.dev4u.faroldocs.repository.search;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;

/**
 * Configure a Mock version of {@link TipoNormaSearchRepository} to test the
 * application without starting Elasticsearch.
 */
@Configuration
public class TipoNormaSearchRepositoryMockConfiguration {

    @MockBean
    private TipoNormaSearchRepository mockTipoNormaSearchRepository;
}
