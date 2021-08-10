package br.com.dev4u.faroldocs.domain;

import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.FieldType;

/**
 * A Etiqueta.
 */
@Entity
@Table(name = "etiqueta")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "etiqueta")
public class Etiqueta implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "nome", nullable = false, unique = true)
    private String nome;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Etiqueta id(Long id) {
        this.id = id;
        return this;
    }

    public String getNome() {
        return this.nome;
    }

    public Etiqueta nome(String nome) {
        this.nome = nome;
        return this;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Etiqueta)) {
            return false;
        }
        return id != null && id.equals(((Etiqueta) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Etiqueta{" +
            "id=" + getId() +
            ", nome='" + getNome() + "'" +
            "}";
    }
}
