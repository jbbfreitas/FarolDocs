package br.com.dev4u.faroldocs.domain;

import static org.assertj.core.api.Assertions.assertThat;

import br.com.dev4u.faroldocs.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class DocumentoUserTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(DocumentoUser.class);
        DocumentoUser documentoUser1 = new DocumentoUser();
        documentoUser1.setId(1L);
        DocumentoUser documentoUser2 = new DocumentoUser();
        documentoUser2.setId(documentoUser1.getId());
        assertThat(documentoUser1).isEqualTo(documentoUser2);
        documentoUser2.setId(2L);
        assertThat(documentoUser1).isNotEqualTo(documentoUser2);
        documentoUser1.setId(null);
        assertThat(documentoUser1).isNotEqualTo(documentoUser2);
    }
}
