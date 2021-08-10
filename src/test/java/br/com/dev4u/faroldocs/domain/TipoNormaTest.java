package br.com.dev4u.faroldocs.domain;

import static org.assertj.core.api.Assertions.assertThat;

import br.com.dev4u.faroldocs.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TipoNormaTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TipoNorma.class);
        TipoNorma tipoNorma1 = new TipoNorma();
        tipoNorma1.setId(1L);
        TipoNorma tipoNorma2 = new TipoNorma();
        tipoNorma2.setId(tipoNorma1.getId());
        assertThat(tipoNorma1).isEqualTo(tipoNorma2);
        tipoNorma2.setId(2L);
        assertThat(tipoNorma1).isNotEqualTo(tipoNorma2);
        tipoNorma1.setId(null);
        assertThat(tipoNorma1).isNotEqualTo(tipoNorma2);
    }
}
