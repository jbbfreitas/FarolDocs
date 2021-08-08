package br.com.dev4u.faroldocs.domain;

import br.com.dev4u.faroldocs.domain.enumeration.SituacaoProjeto;
import java.io.Serializable;
import java.time.Instant;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.FieldType;

/**
 * A Projeto.
 */
@Entity
@Table(name = "projeto")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "projeto")
public class Projeto implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "nome", nullable = false, unique = true)
    private String nome;

    @NotNull
    @Column(name = "inicio", nullable = false)
    private Instant inicio;

    @Column(name = "fim")
    private Instant fim;

    @Enumerated(EnumType.STRING)
    @Column(name = "situacao")
    private SituacaoProjeto situacao;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Projeto id(Long id) {
        this.id = id;
        return this;
    }

    public String getNome() {
        return this.nome;
    }

    public Projeto nome(String nome) {
        this.nome = nome;
        return this;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Instant getInicio() {
        return this.inicio;
    }

    public Projeto inicio(Instant inicio) {
        this.inicio = inicio;
        return this;
    }

    public void setInicio(Instant inicio) {
        this.inicio = inicio;
    }

    public Instant getFim() {
        return this.fim;
    }

    public Projeto fim(Instant fim) {
        this.fim = fim;
        return this;
    }

    public void setFim(Instant fim) {
        this.fim = fim;
    }

    public SituacaoProjeto getSituacao() {
        return this.situacao;
    }

    public Projeto situacao(SituacaoProjeto situacao) {
        this.situacao = situacao;
        return this;
    }

    public void setSituacao(SituacaoProjeto situacao) {
        this.situacao = situacao;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Projeto)) {
            return false;
        }
        return id != null && id.equals(((Projeto) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Projeto{" +
            "id=" + getId() +
            ", nome='" + getNome() + "'" +
            ", inicio='" + getInicio() + "'" +
            ", fim='" + getFim() + "'" +
            ", situacao='" + getSituacao() + "'" +
            "}";
    }
}
