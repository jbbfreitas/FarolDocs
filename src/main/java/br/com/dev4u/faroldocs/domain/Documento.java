package br.com.dev4u.faroldocs.domain;

import br.com.dev4u.faroldocs.domain.enumeration.SituacaoDocumento;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Type;
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

    @Column(name = "assunto")
    private String assunto;

    @Column(name = "descricao")
    private String descricao;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "ementa")
    private String ementa;

    @Pattern(regexp = "^(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]")
    @Column(name = "url")
    private String url;

    @Column(name = "numero")
    private String numero;

    @Column(name = "ano")
    private Integer ano;

    @Enumerated(EnumType.STRING)
    @Column(name = "situacao")
    private SituacaoDocumento situacao;

    @Column(name = "criacao")
    private Instant criacao;

    @ManyToOne
    private Projeto projeto;

    @ManyToOne
    private Tipo tipo;

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JoinTable(
        name = "rel_documento__etiqueta",
        joinColumns = @JoinColumn(name = "documento_id"),
        inverseJoinColumns = @JoinColumn(name = "etiqueta_id")
    )
    private Set<Etiqueta> etiquetas = new HashSet<>();

    @ManyToOne
    private OrgaoEmissor orgaoEmissor;

    @ManyToOne
    private TipoNorma tipoNorma;

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

    public String getEmenta() {
        return this.ementa;
    }

    public Documento ementa(String ementa) {
        this.ementa = ementa;
        return this;
    }

    public void setEmenta(String ementa) {
        this.ementa = ementa;
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

    public String getNumero() {
        return this.numero;
    }

    public Documento numero(String numero) {
        this.numero = numero;
        return this;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public Integer getAno() {
        return this.ano;
    }

    public Documento ano(Integer ano) {
        this.ano = ano;
        return this;
    }

    public void setAno(Integer ano) {
        this.ano = ano;
    }

    public SituacaoDocumento getSituacao() {
        return this.situacao;
    }

    public Documento situacao(SituacaoDocumento situacao) {
        this.situacao = situacao;
        return this;
    }

    public void setSituacao(SituacaoDocumento situacao) {
        this.situacao = situacao;
    }

    public Instant getCriacao() {
        return this.criacao;
    }

    public Documento criacao(Instant criacao) {
        this.criacao = criacao;
        return this;
    }

    public void setCriacao(Instant criacao) {
        this.criacao = criacao;
    }

    public Projeto getProjeto() {
        return this.projeto;
    }

    public Documento projeto(Projeto projeto) {
        this.setProjeto(projeto);
        return this;
    }

    public void setProjeto(Projeto projeto) {
        this.projeto = projeto;
    }

    public Tipo getTipo() {
        return this.tipo;
    }

    public Documento tipo(Tipo tipo) {
        this.setTipo(tipo);
        return this;
    }

    public void setTipo(Tipo tipo) {
        this.tipo = tipo;
    }

    public Set<Etiqueta> getEtiquetas() {
        return this.etiquetas;
    }

    public Documento etiquetas(Set<Etiqueta> etiquetas) {
        this.setEtiquetas(etiquetas);
        return this;
    }

    public Documento addEtiqueta(Etiqueta etiqueta) {
        this.etiquetas.add(etiqueta);
        return this;
    }

    public Documento removeEtiqueta(Etiqueta etiqueta) {
        this.etiquetas.remove(etiqueta);
        return this;
    }

    public void setEtiquetas(Set<Etiqueta> etiquetas) {
        this.etiquetas = etiquetas;
    }

    public OrgaoEmissor getOrgaoEmissor() {
        return this.orgaoEmissor;
    }

    public Documento orgaoEmissor(OrgaoEmissor orgaoEmissor) {
        this.setOrgaoEmissor(orgaoEmissor);
        return this;
    }

    public void setOrgaoEmissor(OrgaoEmissor orgaoEmissor) {
        this.orgaoEmissor = orgaoEmissor;
    }

    public TipoNorma getTipoNorma() {
        return this.tipoNorma;
    }

    public Documento tipoNorma(TipoNorma tipoNorma) {
        this.setTipoNorma(tipoNorma);
        return this;
    }

    public void setTipoNorma(TipoNorma tipoNorma) {
        this.tipoNorma = tipoNorma;
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
            ", assunto='" + getAssunto() + "'" +
            ", descricao='" + getDescricao() + "'" +
            ", ementa='" + getEmenta() + "'" +
            ", url='" + getUrl() + "'" +
            ", numero='" + getNumero() + "'" +
            ", ano=" + getAno() +
            ", situacao='" + getSituacao() + "'" +
            ", criacao='" + getCriacao() + "'" +
            "}";
    }
}
