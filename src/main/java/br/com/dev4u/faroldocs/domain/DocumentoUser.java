package br.com.dev4u.faroldocs.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.FieldType;

/**
 * A DocumentoUser.
 */
@Entity
@Table(name = "documento_user")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "documentouser")
public class DocumentoUser implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @ManyToOne
    private User user;

    @ManyToOne
    @JsonIgnoreProperties(value = { "projeto", "tipo", "orgaoEmissor", "tipoNorma" }, allowSetters = true)
    private Documento documento;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public DocumentoUser id(Long id) {
        this.id = id;
        return this;
    }

    public User getUser() {
        return this.user;
    }

    public DocumentoUser user(User user) {
        this.setUser(user);
        return this;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Documento getDocumento() {
        return this.documento;
    }

    public DocumentoUser documento(Documento documento) {
        this.setDocumento(documento);
        return this;
    }

    public void setDocumento(Documento documento) {
        this.documento = documento;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DocumentoUser)) {
            return false;
        }
        return id != null && id.equals(((DocumentoUser) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DocumentoUser{" +
            "id=" + getId() +
            "}";
    }
}
