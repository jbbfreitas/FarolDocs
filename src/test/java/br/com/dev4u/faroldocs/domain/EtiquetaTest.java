package br.com.dev4u.faroldocs.domain;

import static org.assertj.core.api.Assertions.assertThat;

import br.com.dev4u.faroldocs.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class EtiquetaTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Etiqueta.class);
        Etiqueta etiqueta1 = new Etiqueta();
        etiqueta1.setId(1L);
        Etiqueta etiqueta2 = new Etiqueta();
        etiqueta2.setId(etiqueta1.getId());
        assertThat(etiqueta1).isEqualTo(etiqueta2);
        etiqueta2.setId(2L);
        assertThat(etiqueta1).isNotEqualTo(etiqueta2);
        etiqueta1.setId(null);
        assertThat(etiqueta1).isNotEqualTo(etiqueta2);
    }
}
