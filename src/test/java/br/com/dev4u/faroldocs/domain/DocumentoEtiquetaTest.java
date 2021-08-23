package br.com.dev4u.faroldocs.domain;

import static org.assertj.core.api.Assertions.assertThat;

import br.com.dev4u.faroldocs.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class DocumentoEtiquetaTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(DocumentoEtiqueta.class);
        DocumentoEtiqueta documentoEtiqueta1 = new DocumentoEtiqueta();
        documentoEtiqueta1.setId(1L);
        DocumentoEtiqueta documentoEtiqueta2 = new DocumentoEtiqueta();
        documentoEtiqueta2.setId(documentoEtiqueta1.getId());
        assertThat(documentoEtiqueta1).isEqualTo(documentoEtiqueta2);
        documentoEtiqueta2.setId(2L);
        assertThat(documentoEtiqueta1).isNotEqualTo(documentoEtiqueta2);
        documentoEtiqueta1.setId(null);
        assertThat(documentoEtiqueta1).isNotEqualTo(documentoEtiqueta2);
    }
}
