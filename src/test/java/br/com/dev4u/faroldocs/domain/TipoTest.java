package br.com.dev4u.faroldocs.domain;

import static org.assertj.core.api.Assertions.assertThat;

import br.com.dev4u.faroldocs.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TipoTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Tipo.class);
        Tipo tipo1 = new Tipo();
        tipo1.setId(1L);
        Tipo tipo2 = new Tipo();
        tipo2.setId(tipo1.getId());
        assertThat(tipo1).isEqualTo(tipo2);
        tipo2.setId(2L);
        assertThat(tipo1).isNotEqualTo(tipo2);
        tipo1.setId(null);
        assertThat(tipo1).isNotEqualTo(tipo2);
    }
}
