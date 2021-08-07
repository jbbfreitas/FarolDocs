package br.com.dev4u.faroldocs.domain;

import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.FieldType;

/**
 * A Documento.
 */
@Entity
@Table(name = "documento")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "documento")
public class Documento implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "projeto")
    private String projeto;

    @Column(name = "assunto")
    private String assunto;

    @Column(name = "descricao")
    private String descricao;

    @Column(name = "etiqueta")
    private String etiqueta;

    @Column(name = "url")
    private String url;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Documento id(Long id) {
        this.id = id;
        return this;
    }

    public String getProjeto() {
        return this.projeto;
    }

    public Documento projeto(String projeto) {
        this.projeto = projeto;
        return this;
    }

    public void setProjeto(String projeto) {
        this.projeto = projeto;
    }

    public String getAssunto() {
        return this.assunto;
    }

    public Documento assunto(String assunto) {
        this.assunto = assunto;
        return this;
    }

    public void setAssunto(String assunto) {
        this.assunto = assunto;
    }

    public String getDescricao() {
        return this.descricao;
    }

    public Documento descricao(String descricao) {
        this.descricao = descricao;
        return this;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getEtiqueta() {
        return this.etiqueta;
    }

    public Documento etiqueta(String etiqueta) {
        this.etiqueta = etiqueta;
        return this;
    }

    public void setEtiqueta(String etiqueta) {
        this.etiqueta = etiqueta;
    }

    public String getUrl() {
        return this.url;
    }

    public Documento url(String url) {
        this.url = url;
        return this;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Documento)) {
            return false;
        }
        return id != null && id.equals(((Documento) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Documento{" +
            "id=" + getId() +
            ", projeto='" + getProjeto() + "'" +
            ", assunto='" + getAssunto() + "'" +
            ", descricao='" + getDescricao() + "'" +
            ", etiqueta='" + getEtiqueta() + "'" +
            ", url='" + getUrl() + "'" +
            "}";
    }
}
