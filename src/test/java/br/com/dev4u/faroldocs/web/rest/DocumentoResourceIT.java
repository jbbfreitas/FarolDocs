package br.com.dev4u.faroldocs.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import br.com.dev4u.faroldocs.IntegrationTest;
import br.com.dev4u.faroldocs.domain.Documento;
import br.com.dev4u.faroldocs.domain.enumeration.SituacaoDocumento;
import br.com.dev4u.faroldocs.repository.DocumentoRepository;
import br.com.dev4u.faroldocs.repository.search.DocumentoSearchRepository;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Base64Utils;

/**
 * Integration tests for the {@link DocumentoResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class DocumentoResourceIT {

    private static final String DEFAULT_ASSUNTO = "AAAAAAAAAA";
    private static final String UPDATED_ASSUNTO = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRICAO = "AAAAAAAAAA";
    private static final String UPDATED_DESCRICAO = "BBBBBBBBBB";

    private static final String DEFAULT_EMENTA = "AAAAAAAAAA";
    private static final String UPDATED_EMENTA = "BBBBBBBBBB";

    private static final String DEFAULT_URL = "https://-.l";
    private static final String UPDATED_URL = "ftp://8CK4";

    private static final String DEFAULT_NUMERO = "AAAAAAAAAA";
    private static final String UPDATED_NUMERO = "BBBBBBBBBB";

    private static final Integer DEFAULT_ANO = 1;
    private static final Integer UPDATED_ANO = 2;

    private static final SituacaoDocumento DEFAULT_SITUACAO = SituacaoDocumento.VIGENTE;
    private static final SituacaoDocumento UPDATED_SITUACAO = SituacaoDocumento.SUBSTITUIDO;

    private static final Instant DEFAULT_CRIACAO = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CRIACAO = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/documentos";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/_search/documentos";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private DocumentoRepository documentoRepository;

    /**
     * This repository is mocked in the br.com.dev4u.faroldocs.repository.search test package.
     *
     * @see br.com.dev4u.faroldocs.repository.search.DocumentoSearchRepositoryMockConfiguration
     */
    @Autowired
    private DocumentoSearchRepository mockDocumentoSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDocumentoMockMvc;

    private Documento documento;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Documento createEntity(EntityManager em) {
        Documento documento = new Documento()
            .assunto(DEFAULT_ASSUNTO)
            .descricao(DEFAULT_DESCRICAO)
            .ementa(DEFAULT_EMENTA)
            .url(DEFAULT_URL)
            .numero(DEFAULT_NUMERO)
            .ano(DEFAULT_ANO)
            .situacao(DEFAULT_SITUACAO)
            .criacao(DEFAULT_CRIACAO);
        return documento;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Documento createUpdatedEntity(EntityManager em) {
        Documento documento = new Documento()
            .assunto(UPDATED_ASSUNTO)
            .descricao(UPDATED_DESCRICAO)
            .ementa(UPDATED_EMENTA)
            .url(UPDATED_URL)
            .numero(UPDATED_NUMERO)
            .ano(UPDATED_ANO)
            .situacao(UPDATED_SITUACAO)
            .criacao(UPDATED_CRIACAO);
        return documento;
    }

    @BeforeEach
    public void initTest() {
        documento = createEntity(em);
    }

    @Test
    @Transactional
    void createDocumento() throws Exception {
        int databaseSizeBeforeCreate = documentoRepository.findAll().size();
        // Create the Documento
        restDocumentoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(documento)))
            .andExpect(status().isCreated());

        // Validate the Documento in the database
        List<Documento> documentoList = documentoRepository.findAll();
        assertThat(documentoList).hasSize(databaseSizeBeforeCreate + 1);
        Documento testDocumento = documentoList.get(documentoList.size() - 1);
        assertThat(testDocumento.getAssunto()).isEqualTo(DEFAULT_ASSUNTO);
        assertThat(testDocumento.getDescricao()).isEqualTo(DEFAULT_DESCRICAO);
        assertThat(testDocumento.getEmenta()).isEqualTo(DEFAULT_EMENTA);
        assertThat(testDocumento.getUrl()).isEqualTo(DEFAULT_URL);
        assertThat(testDocumento.getNumero()).isEqualTo(DEFAULT_NUMERO);
        assertThat(testDocumento.getAno()).isEqualTo(DEFAULT_ANO);
        assertThat(testDocumento.getSituacao()).isEqualTo(DEFAULT_SITUACAO);
        assertThat(testDocumento.getCriacao()).isEqualTo(DEFAULT_CRIACAO);

        // Validate the Documento in Elasticsearch
        verify(mockDocumentoSearchRepository, times(1)).save(testDocumento);
    }

    @Test
    @Transactional
    void createDocumentoWithExistingId() throws Exception {
        // Create the Documento with an existing ID
        documento.setId(1L);

        int databaseSizeBeforeCreate = documentoRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restDocumentoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(documento)))
            .andExpect(status().isBadRequest());

        // Validate the Documento in the database
        List<Documento> documentoList = documentoRepository.findAll();
        assertThat(documentoList).hasSize(databaseSizeBeforeCreate);

        // Validate the Documento in Elasticsearch
        verify(mockDocumentoSearchRepository, times(0)).save(documento);
    }

    @Test
    @Transactional
    void getAllDocumentos() throws Exception {
        // Initialize the database
        documentoRepository.saveAndFlush(documento);

        // Get all the documentoList
        restDocumentoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(documento.getId().intValue())))
            .andExpect(jsonPath("$.[*].assunto").value(hasItem(DEFAULT_ASSUNTO)))
            .andExpect(jsonPath("$.[*].descricao").value(hasItem(DEFAULT_DESCRICAO)))
            .andExpect(jsonPath("$.[*].ementa").value(hasItem(DEFAULT_EMENTA.toString())))
            .andExpect(jsonPath("$.[*].url").value(hasItem(DEFAULT_URL)))
            .andExpect(jsonPath("$.[*].numero").value(hasItem(DEFAULT_NUMERO)))
            .andExpect(jsonPath("$.[*].ano").value(hasItem(DEFAULT_ANO)))
            .andExpect(jsonPath("$.[*].situacao").value(hasItem(DEFAULT_SITUACAO.toString())))
            .andExpect(jsonPath("$.[*].criacao").value(hasItem(DEFAULT_CRIACAO.toString())));
    }

    @Test
    @Transactional
    void getDocumento() throws Exception {
        // Initialize the database
        documentoRepository.saveAndFlush(documento);

        // Get the documento
        restDocumentoMockMvc
            .perform(get(ENTITY_API_URL_ID, documento.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(documento.getId().intValue()))
            .andExpect(jsonPath("$.assunto").value(DEFAULT_ASSUNTO))
            .andExpect(jsonPath("$.descricao").value(DEFAULT_DESCRICAO))
            .andExpect(jsonPath("$.ementa").value(DEFAULT_EMENTA.toString()))
            .andExpect(jsonPath("$.url").value(DEFAULT_URL))
            .andExpect(jsonPath("$.numero").value(DEFAULT_NUMERO))
            .andExpect(jsonPath("$.ano").value(DEFAULT_ANO))
            .andExpect(jsonPath("$.situacao").value(DEFAULT_SITUACAO.toString()))
            .andExpect(jsonPath("$.criacao").value(DEFAULT_CRIACAO.toString()));
    }

    @Test
    @Transactional
    void getNonExistingDocumento() throws Exception {
        // Get the documento
        restDocumentoMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewDocumento() throws Exception {
        // Initialize the database
        documentoRepository.saveAndFlush(documento);

        int databaseSizeBeforeUpdate = documentoRepository.findAll().size();

        // Update the documento
        Documento updatedDocumento = documentoRepository.findById(documento.getId()).get();
        // Disconnect from session so that the updates on updatedDocumento are not directly saved in db
        em.detach(updatedDocumento);
        updatedDocumento
            .assunto(UPDATED_ASSUNTO)
            .descricao(UPDATED_DESCRICAO)
            .ementa(UPDATED_EMENTA)
            .url(UPDATED_URL)
            .numero(UPDATED_NUMERO)
            .ano(UPDATED_ANO)
            .situacao(UPDATED_SITUACAO)
            .criacao(UPDATED_CRIACAO);

        restDocumentoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedDocumento.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedDocumento))
            )
            .andExpect(status().isOk());

        // Validate the Documento in the database
        List<Documento> documentoList = documentoRepository.findAll();
        assertThat(documentoList).hasSize(databaseSizeBeforeUpdate);
        Documento testDocumento = documentoList.get(documentoList.size() - 1);
        assertThat(testDocumento.getAssunto()).isEqualTo(UPDATED_ASSUNTO);
        assertThat(testDocumento.getDescricao()).isEqualTo(UPDATED_DESCRICAO);
        assertThat(testDocumento.getEmenta()).isEqualTo(UPDATED_EMENTA);
        assertThat(testDocumento.getUrl()).isEqualTo(UPDATED_URL);
        assertThat(testDocumento.getNumero()).isEqualTo(UPDATED_NUMERO);
        assertThat(testDocumento.getAno()).isEqualTo(UPDATED_ANO);
        assertThat(testDocumento.getSituacao()).isEqualTo(UPDATED_SITUACAO);
        assertThat(testDocumento.getCriacao()).isEqualTo(UPDATED_CRIACAO);

        // Validate the Documento in Elasticsearch
        verify(mockDocumentoSearchRepository).save(testDocumento);
    }

    @Test
    @Transactional
    void putNonExistingDocumento() throws Exception {
        int databaseSizeBeforeUpdate = documentoRepository.findAll().size();
        documento.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDocumentoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, documento.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(documento))
            )
            .andExpect(status().isBadRequest());

        // Validate the Documento in the database
        List<Documento> documentoList = documentoRepository.findAll();
        assertThat(documentoList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Documento in Elasticsearch
        verify(mockDocumentoSearchRepository, times(0)).save(documento);
    }

    @Test
    @Transactional
    void putWithIdMismatchDocumento() throws Exception {
        int databaseSizeBeforeUpdate = documentoRepository.findAll().size();
        documento.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDocumentoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(documento))
            )
            .andExpect(status().isBadRequest());

        // Validate the Documento in the database
        List<Documento> documentoList = documentoRepository.findAll();
        assertThat(documentoList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Documento in Elasticsearch
        verify(mockDocumentoSearchRepository, times(0)).save(documento);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamDocumento() throws Exception {
        int databaseSizeBeforeUpdate = documentoRepository.findAll().size();
        documento.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDocumentoMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(documento)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Documento in the database
        List<Documento> documentoList = documentoRepository.findAll();
        assertThat(documentoList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Documento in Elasticsearch
        verify(mockDocumentoSearchRepository, times(0)).save(documento);
    }

    @Test
    @Transactional
    void partialUpdateDocumentoWithPatch() throws Exception {
        // Initialize the database
        documentoRepository.saveAndFlush(documento);

        int databaseSizeBeforeUpdate = documentoRepository.findAll().size();

        // Update the documento using partial update
        Documento partialUpdatedDocumento = new Documento();
        partialUpdatedDocumento.setId(documento.getId());

        partialUpdatedDocumento.ementa(UPDATED_EMENTA).url(UPDATED_URL).numero(UPDATED_NUMERO).ano(UPDATED_ANO).criacao(UPDATED_CRIACAO);

        restDocumentoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDocumento.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDocumento))
            )
            .andExpect(status().isOk());

        // Validate the Documento in the database
        List<Documento> documentoList = documentoRepository.findAll();
        assertThat(documentoList).hasSize(databaseSizeBeforeUpdate);
        Documento testDocumento = documentoList.get(documentoList.size() - 1);
        assertThat(testDocumento.getAssunto()).isEqualTo(DEFAULT_ASSUNTO);
        assertThat(testDocumento.getDescricao()).isEqualTo(DEFAULT_DESCRICAO);
        assertThat(testDocumento.getEmenta()).isEqualTo(UPDATED_EMENTA);
        assertThat(testDocumento.getUrl()).isEqualTo(UPDATED_URL);
        assertThat(testDocumento.getNumero()).isEqualTo(UPDATED_NUMERO);
        assertThat(testDocumento.getAno()).isEqualTo(UPDATED_ANO);
        assertThat(testDocumento.getSituacao()).isEqualTo(DEFAULT_SITUACAO);
        assertThat(testDocumento.getCriacao()).isEqualTo(UPDATED_CRIACAO);
    }

    @Test
    @Transactional
    void fullUpdateDocumentoWithPatch() throws Exception {
        // Initialize the database
        documentoRepository.saveAndFlush(documento);

        int databaseSizeBeforeUpdate = documentoRepository.findAll().size();

        // Update the documento using partial update
        Documento partialUpdatedDocumento = new Documento();
        partialUpdatedDocumento.setId(documento.getId());

        partialUpdatedDocumento
            .assunto(UPDATED_ASSUNTO)
            .descricao(UPDATED_DESCRICAO)
            .ementa(UPDATED_EMENTA)
            .url(UPDATED_URL)
            .numero(UPDATED_NUMERO)
            .ano(UPDATED_ANO)
            .situacao(UPDATED_SITUACAO)
            .criacao(UPDATED_CRIACAO);

        restDocumentoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDocumento.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDocumento))
            )
            .andExpect(status().isOk());

        // Validate the Documento in the database
        List<Documento> documentoList = documentoRepository.findAll();
        assertThat(documentoList).hasSize(databaseSizeBeforeUpdate);
        Documento testDocumento = documentoList.get(documentoList.size() - 1);
        assertThat(testDocumento.getAssunto()).isEqualTo(UPDATED_ASSUNTO);
        assertThat(testDocumento.getDescricao()).isEqualTo(UPDATED_DESCRICAO);
        assertThat(testDocumento.getEmenta()).isEqualTo(UPDATED_EMENTA);
        assertThat(testDocumento.getUrl()).isEqualTo(UPDATED_URL);
        assertThat(testDocumento.getNumero()).isEqualTo(UPDATED_NUMERO);
        assertThat(testDocumento.getAno()).isEqualTo(UPDATED_ANO);
        assertThat(testDocumento.getSituacao()).isEqualTo(UPDATED_SITUACAO);
        assertThat(testDocumento.getCriacao()).isEqualTo(UPDATED_CRIACAO);
    }

    @Test
    @Transactional
    void patchNonExistingDocumento() throws Exception {
        int databaseSizeBeforeUpdate = documentoRepository.findAll().size();
        documento.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDocumentoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, documento.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(documento))
            )
            .andExpect(status().isBadRequest());

        // Validate the Documento in the database
        List<Documento> documentoList = documentoRepository.findAll();
        assertThat(documentoList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Documento in Elasticsearch
        verify(mockDocumentoSearchRepository, times(0)).save(documento);
    }

    @Test
    @Transactional
    void patchWithIdMismatchDocumento() throws Exception {
        int databaseSizeBeforeUpdate = documentoRepository.findAll().size();
        documento.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDocumentoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(documento))
            )
            .andExpect(status().isBadRequest());

        // Validate the Documento in the database
        List<Documento> documentoList = documentoRepository.findAll();
        assertThat(documentoList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Documento in Elasticsearch
        verify(mockDocumentoSearchRepository, times(0)).save(documento);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamDocumento() throws Exception {
        int databaseSizeBeforeUpdate = documentoRepository.findAll().size();
        documento.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDocumentoMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(documento))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Documento in the database
        List<Documento> documentoList = documentoRepository.findAll();
        assertThat(documentoList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Documento in Elasticsearch
        verify(mockDocumentoSearchRepository, times(0)).save(documento);
    }

    @Test
    @Transactional
    void deleteDocumento() throws Exception {
        // Initialize the database
        documentoRepository.saveAndFlush(documento);

        int databaseSizeBeforeDelete = documentoRepository.findAll().size();

        // Delete the documento
        restDocumentoMockMvc
            .perform(delete(ENTITY_API_URL_ID, documento.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Documento> documentoList = documentoRepository.findAll();
        assertThat(documentoList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Documento in Elasticsearch
        verify(mockDocumentoSearchRepository, times(1)).deleteById(documento.getId());
    }

    @Test
    @Transactional
    void searchDocumento() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        documentoRepository.saveAndFlush(documento);
        when(mockDocumentoSearchRepository.search(queryStringQuery("id:" + documento.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(documento), PageRequest.of(0, 1), 1));

        // Search the documento
        restDocumentoMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + documento.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(documento.getId().intValue())))
            .andExpect(jsonPath("$.[*].assunto").value(hasItem(DEFAULT_ASSUNTO)))
            .andExpect(jsonPath("$.[*].descricao").value(hasItem(DEFAULT_DESCRICAO)))
            .andExpect(jsonPath("$.[*].ementa").value(hasItem(DEFAULT_EMENTA.toString())))
            .andExpect(jsonPath("$.[*].url").value(hasItem(DEFAULT_URL)))
            .andExpect(jsonPath("$.[*].numero").value(hasItem(DEFAULT_NUMERO)))
            .andExpect(jsonPath("$.[*].ano").value(hasItem(DEFAULT_ANO)))
            .andExpect(jsonPath("$.[*].situacao").value(hasItem(DEFAULT_SITUACAO.toString())))
            .andExpect(jsonPath("$.[*].criacao").value(hasItem(DEFAULT_CRIACAO.toString())));
    }
}
