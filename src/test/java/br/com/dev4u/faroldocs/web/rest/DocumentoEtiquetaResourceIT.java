package br.com.dev4u.faroldocs.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import br.com.dev4u.faroldocs.IntegrationTest;
import br.com.dev4u.faroldocs.domain.DocumentoEtiqueta;
import br.com.dev4u.faroldocs.repository.DocumentoEtiquetaRepository;
import br.com.dev4u.faroldocs.repository.search.DocumentoEtiquetaSearchRepository;
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

/**
 * Integration tests for the {@link DocumentoEtiquetaResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class DocumentoEtiquetaResourceIT {

    private static final String ENTITY_API_URL = "/api/documento-etiquetas";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/_search/documento-etiquetas";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private DocumentoEtiquetaRepository documentoEtiquetaRepository;

    /**
     * This repository is mocked in the br.com.dev4u.faroldocs.repository.search test package.
     *
     * @see br.com.dev4u.faroldocs.repository.search.DocumentoEtiquetaSearchRepositoryMockConfiguration
     */
    @Autowired
    private DocumentoEtiquetaSearchRepository mockDocumentoEtiquetaSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDocumentoEtiquetaMockMvc;

    private DocumentoEtiqueta documentoEtiqueta;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DocumentoEtiqueta createEntity(EntityManager em) {
        DocumentoEtiqueta documentoEtiqueta = new DocumentoEtiqueta();
        return documentoEtiqueta;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DocumentoEtiqueta createUpdatedEntity(EntityManager em) {
        DocumentoEtiqueta documentoEtiqueta = new DocumentoEtiqueta();
        return documentoEtiqueta;
    }

    @BeforeEach
    public void initTest() {
        documentoEtiqueta = createEntity(em);
    }

    @Test
    @Transactional
    void createDocumentoEtiqueta() throws Exception {
        int databaseSizeBeforeCreate = documentoEtiquetaRepository.findAll().size();
        // Create the DocumentoEtiqueta
        restDocumentoEtiquetaMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(documentoEtiqueta))
            )
            .andExpect(status().isCreated());

        // Validate the DocumentoEtiqueta in the database
        List<DocumentoEtiqueta> documentoEtiquetaList = documentoEtiquetaRepository.findAll();
        assertThat(documentoEtiquetaList).hasSize(databaseSizeBeforeCreate + 1);
        DocumentoEtiqueta testDocumentoEtiqueta = documentoEtiquetaList.get(documentoEtiquetaList.size() - 1);

        // Validate the DocumentoEtiqueta in Elasticsearch
        verify(mockDocumentoEtiquetaSearchRepository, times(1)).save(testDocumentoEtiqueta);
    }

    @Test
    @Transactional
    void createDocumentoEtiquetaWithExistingId() throws Exception {
        // Create the DocumentoEtiqueta with an existing ID
        documentoEtiqueta.setId(1L);

        int databaseSizeBeforeCreate = documentoEtiquetaRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restDocumentoEtiquetaMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(documentoEtiqueta))
            )
            .andExpect(status().isBadRequest());

        // Validate the DocumentoEtiqueta in the database
        List<DocumentoEtiqueta> documentoEtiquetaList = documentoEtiquetaRepository.findAll();
        assertThat(documentoEtiquetaList).hasSize(databaseSizeBeforeCreate);

        // Validate the DocumentoEtiqueta in Elasticsearch
        verify(mockDocumentoEtiquetaSearchRepository, times(0)).save(documentoEtiqueta);
    }

    @Test
    @Transactional
    void getAllDocumentoEtiquetas() throws Exception {
        // Initialize the database
        documentoEtiquetaRepository.saveAndFlush(documentoEtiqueta);

        // Get all the documentoEtiquetaList
        restDocumentoEtiquetaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(documentoEtiqueta.getId().intValue())));
    }

    @Test
    @Transactional
    void getDocumentoEtiqueta() throws Exception {
        // Initialize the database
        documentoEtiquetaRepository.saveAndFlush(documentoEtiqueta);

        // Get the documentoEtiqueta
        restDocumentoEtiquetaMockMvc
            .perform(get(ENTITY_API_URL_ID, documentoEtiqueta.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(documentoEtiqueta.getId().intValue()));
    }

    @Test
    @Transactional
    void getNonExistingDocumentoEtiqueta() throws Exception {
        // Get the documentoEtiqueta
        restDocumentoEtiquetaMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewDocumentoEtiqueta() throws Exception {
        // Initialize the database
        documentoEtiquetaRepository.saveAndFlush(documentoEtiqueta);

        int databaseSizeBeforeUpdate = documentoEtiquetaRepository.findAll().size();

        // Update the documentoEtiqueta
        DocumentoEtiqueta updatedDocumentoEtiqueta = documentoEtiquetaRepository.findById(documentoEtiqueta.getId()).get();
        // Disconnect from session so that the updates on updatedDocumentoEtiqueta are not directly saved in db
        em.detach(updatedDocumentoEtiqueta);

        restDocumentoEtiquetaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedDocumentoEtiqueta.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedDocumentoEtiqueta))
            )
            .andExpect(status().isOk());

        // Validate the DocumentoEtiqueta in the database
        List<DocumentoEtiqueta> documentoEtiquetaList = documentoEtiquetaRepository.findAll();
        assertThat(documentoEtiquetaList).hasSize(databaseSizeBeforeUpdate);
        DocumentoEtiqueta testDocumentoEtiqueta = documentoEtiquetaList.get(documentoEtiquetaList.size() - 1);

        // Validate the DocumentoEtiqueta in Elasticsearch
        verify(mockDocumentoEtiquetaSearchRepository).save(testDocumentoEtiqueta);
    }

    @Test
    @Transactional
    void putNonExistingDocumentoEtiqueta() throws Exception {
        int databaseSizeBeforeUpdate = documentoEtiquetaRepository.findAll().size();
        documentoEtiqueta.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDocumentoEtiquetaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, documentoEtiqueta.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(documentoEtiqueta))
            )
            .andExpect(status().isBadRequest());

        // Validate the DocumentoEtiqueta in the database
        List<DocumentoEtiqueta> documentoEtiquetaList = documentoEtiquetaRepository.findAll();
        assertThat(documentoEtiquetaList).hasSize(databaseSizeBeforeUpdate);

        // Validate the DocumentoEtiqueta in Elasticsearch
        verify(mockDocumentoEtiquetaSearchRepository, times(0)).save(documentoEtiqueta);
    }

    @Test
    @Transactional
    void putWithIdMismatchDocumentoEtiqueta() throws Exception {
        int databaseSizeBeforeUpdate = documentoEtiquetaRepository.findAll().size();
        documentoEtiqueta.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDocumentoEtiquetaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(documentoEtiqueta))
            )
            .andExpect(status().isBadRequest());

        // Validate the DocumentoEtiqueta in the database
        List<DocumentoEtiqueta> documentoEtiquetaList = documentoEtiquetaRepository.findAll();
        assertThat(documentoEtiquetaList).hasSize(databaseSizeBeforeUpdate);

        // Validate the DocumentoEtiqueta in Elasticsearch
        verify(mockDocumentoEtiquetaSearchRepository, times(0)).save(documentoEtiqueta);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamDocumentoEtiqueta() throws Exception {
        int databaseSizeBeforeUpdate = documentoEtiquetaRepository.findAll().size();
        documentoEtiqueta.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDocumentoEtiquetaMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(documentoEtiqueta))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the DocumentoEtiqueta in the database
        List<DocumentoEtiqueta> documentoEtiquetaList = documentoEtiquetaRepository.findAll();
        assertThat(documentoEtiquetaList).hasSize(databaseSizeBeforeUpdate);

        // Validate the DocumentoEtiqueta in Elasticsearch
        verify(mockDocumentoEtiquetaSearchRepository, times(0)).save(documentoEtiqueta);
    }

    @Test
    @Transactional
    void partialUpdateDocumentoEtiquetaWithPatch() throws Exception {
        // Initialize the database
        documentoEtiquetaRepository.saveAndFlush(documentoEtiqueta);

        int databaseSizeBeforeUpdate = documentoEtiquetaRepository.findAll().size();

        // Update the documentoEtiqueta using partial update
        DocumentoEtiqueta partialUpdatedDocumentoEtiqueta = new DocumentoEtiqueta();
        partialUpdatedDocumentoEtiqueta.setId(documentoEtiqueta.getId());

        restDocumentoEtiquetaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDocumentoEtiqueta.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDocumentoEtiqueta))
            )
            .andExpect(status().isOk());

        // Validate the DocumentoEtiqueta in the database
        List<DocumentoEtiqueta> documentoEtiquetaList = documentoEtiquetaRepository.findAll();
        assertThat(documentoEtiquetaList).hasSize(databaseSizeBeforeUpdate);
        DocumentoEtiqueta testDocumentoEtiqueta = documentoEtiquetaList.get(documentoEtiquetaList.size() - 1);
    }

    @Test
    @Transactional
    void fullUpdateDocumentoEtiquetaWithPatch() throws Exception {
        // Initialize the database
        documentoEtiquetaRepository.saveAndFlush(documentoEtiqueta);

        int databaseSizeBeforeUpdate = documentoEtiquetaRepository.findAll().size();

        // Update the documentoEtiqueta using partial update
        DocumentoEtiqueta partialUpdatedDocumentoEtiqueta = new DocumentoEtiqueta();
        partialUpdatedDocumentoEtiqueta.setId(documentoEtiqueta.getId());

        restDocumentoEtiquetaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDocumentoEtiqueta.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDocumentoEtiqueta))
            )
            .andExpect(status().isOk());

        // Validate the DocumentoEtiqueta in the database
        List<DocumentoEtiqueta> documentoEtiquetaList = documentoEtiquetaRepository.findAll();
        assertThat(documentoEtiquetaList).hasSize(databaseSizeBeforeUpdate);
        DocumentoEtiqueta testDocumentoEtiqueta = documentoEtiquetaList.get(documentoEtiquetaList.size() - 1);
    }

    @Test
    @Transactional
    void patchNonExistingDocumentoEtiqueta() throws Exception {
        int databaseSizeBeforeUpdate = documentoEtiquetaRepository.findAll().size();
        documentoEtiqueta.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDocumentoEtiquetaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, documentoEtiqueta.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(documentoEtiqueta))
            )
            .andExpect(status().isBadRequest());

        // Validate the DocumentoEtiqueta in the database
        List<DocumentoEtiqueta> documentoEtiquetaList = documentoEtiquetaRepository.findAll();
        assertThat(documentoEtiquetaList).hasSize(databaseSizeBeforeUpdate);

        // Validate the DocumentoEtiqueta in Elasticsearch
        verify(mockDocumentoEtiquetaSearchRepository, times(0)).save(documentoEtiqueta);
    }

    @Test
    @Transactional
    void patchWithIdMismatchDocumentoEtiqueta() throws Exception {
        int databaseSizeBeforeUpdate = documentoEtiquetaRepository.findAll().size();
        documentoEtiqueta.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDocumentoEtiquetaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(documentoEtiqueta))
            )
            .andExpect(status().isBadRequest());

        // Validate the DocumentoEtiqueta in the database
        List<DocumentoEtiqueta> documentoEtiquetaList = documentoEtiquetaRepository.findAll();
        assertThat(documentoEtiquetaList).hasSize(databaseSizeBeforeUpdate);

        // Validate the DocumentoEtiqueta in Elasticsearch
        verify(mockDocumentoEtiquetaSearchRepository, times(0)).save(documentoEtiqueta);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamDocumentoEtiqueta() throws Exception {
        int databaseSizeBeforeUpdate = documentoEtiquetaRepository.findAll().size();
        documentoEtiqueta.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDocumentoEtiquetaMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(documentoEtiqueta))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the DocumentoEtiqueta in the database
        List<DocumentoEtiqueta> documentoEtiquetaList = documentoEtiquetaRepository.findAll();
        assertThat(documentoEtiquetaList).hasSize(databaseSizeBeforeUpdate);

        // Validate the DocumentoEtiqueta in Elasticsearch
        verify(mockDocumentoEtiquetaSearchRepository, times(0)).save(documentoEtiqueta);
    }

    @Test
    @Transactional
    void deleteDocumentoEtiqueta() throws Exception {
        // Initialize the database
        documentoEtiquetaRepository.saveAndFlush(documentoEtiqueta);

        int databaseSizeBeforeDelete = documentoEtiquetaRepository.findAll().size();

        // Delete the documentoEtiqueta
        restDocumentoEtiquetaMockMvc
            .perform(delete(ENTITY_API_URL_ID, documentoEtiqueta.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<DocumentoEtiqueta> documentoEtiquetaList = documentoEtiquetaRepository.findAll();
        assertThat(documentoEtiquetaList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the DocumentoEtiqueta in Elasticsearch
        verify(mockDocumentoEtiquetaSearchRepository, times(1)).deleteById(documentoEtiqueta.getId());
    }

    @Test
    @Transactional
    void searchDocumentoEtiqueta() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        documentoEtiquetaRepository.saveAndFlush(documentoEtiqueta);
        when(mockDocumentoEtiquetaSearchRepository.search(queryStringQuery("id:" + documentoEtiqueta.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(documentoEtiqueta), PageRequest.of(0, 1), 1));

        // Search the documentoEtiqueta
        restDocumentoEtiquetaMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + documentoEtiqueta.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(documentoEtiqueta.getId().intValue())));
    }
}
