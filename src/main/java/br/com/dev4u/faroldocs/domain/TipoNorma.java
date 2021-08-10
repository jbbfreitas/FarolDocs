package br.com.dev4u.faroldocs.domain;

import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.FieldType;

/**
 * A TipoNorma.
 */
@Entity
@Table(name = "tipo_norma")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "tiponorma")
public class TipoNorma implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "tipo", nullable = false)
    private String tipo;

    @Column(name = "descricao")
    private String descricao;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TipoNorma id(Long id) {
        this.id = id;
        return this;
    }

    public String getTipo() {
        return this.tipo;
    }

    public TipoNorma tipo(String tipo) {
        this.tipo = tipo;
        return this;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getDescricao() {
        return this.descricao;
    }

    public TipoNorma descricao(String descricao) {
        this.descricao = descricao;
        return this;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TipoNorma)) {
            return false;
        }
        return id != null && id.equals(((TipoNorma) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TipoNorma{" +
            "id=" + getId() +
            ", tipo='" + getTipo() + "'" +
            ", descricao='" + getDescricao() + "'" +
            "}";
    }
}
