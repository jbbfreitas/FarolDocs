package br.com.dev4u.faroldocs.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import br.com.dev4u.faroldocs.IntegrationTest;
import br.com.dev4u.faroldocs.domain.Etiqueta;
import br.com.dev4u.faroldocs.repository.EtiquetaRepository;
import br.com.dev4u.faroldocs.repository.search.EtiquetaSearchRepository;
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
 * Integration tests for the {@link EtiquetaResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class EtiquetaResourceIT {

    private static final String DEFAULT_NOME = "AAAAAAAAAA";
    private static final String UPDATED_NOME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/etiquetas";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/_search/etiquetas";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private EtiquetaRepository etiquetaRepository;

    /**
     * This repository is mocked in the br.com.dev4u.faroldocs.repository.search test package.
     *
     * @see br.com.dev4u.faroldocs.repository.search.EtiquetaSearchRepositoryMockConfiguration
     */
    @Autowired
    private EtiquetaSearchRepository mockEtiquetaSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restEtiquetaMockMvc;

    private Etiqueta etiqueta;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Etiqueta createEntity(EntityManager em) {
        Etiqueta etiqueta = new Etiqueta().nome(DEFAULT_NOME);
        return etiqueta;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Etiqueta createUpdatedEntity(EntityManager em) {
        Etiqueta etiqueta = new Etiqueta().nome(UPDATED_NOME);
        return etiqueta;
    }

    @BeforeEach
    public void initTest() {
        etiqueta = createEntity(em);
    }

    @Test
    @Transactional
    void createEtiqueta() throws Exception {
        int databaseSizeBeforeCreate = etiquetaRepository.findAll().size();
        // Create the Etiqueta
        restEtiquetaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(etiqueta)))
            .andExpect(status().isCreated());

        // Validate the Etiqueta in the database
        List<Etiqueta> etiquetaList = etiquetaRepository.findAll();
        assertThat(etiquetaList).hasSize(databaseSizeBeforeCreate + 1);
        Etiqueta testEtiqueta = etiquetaList.get(etiquetaList.size() - 1);
        assertThat(testEtiqueta.getNome()).isEqualTo(DEFAULT_NOME);

        // Validate the Etiqueta in Elasticsearch
        verify(mockEtiquetaSearchRepository, times(1)).save(testEtiqueta);
    }

    @Test
    @Transactional
    void createEtiquetaWithExistingId() throws Exception {
        // Create the Etiqueta with an existing ID
        etiqueta.setId(1L);

        int databaseSizeBeforeCreate = etiquetaRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restEtiquetaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(etiqueta)))
            .andExpect(status().isBadRequest());

        // Validate the Etiqueta in the database
        List<Etiqueta> etiquetaList = etiquetaRepository.findAll();
        assertThat(etiquetaList).hasSize(databaseSizeBeforeCreate);

        // Validate the Etiqueta in Elasticsearch
        verify(mockEtiquetaSearchRepository, times(0)).save(etiqueta);
    }

    @Test
    @Transactional
    void checkNomeIsRequired() throws Exception {
        int databaseSizeBeforeTest = etiquetaRepository.findAll().size();
        // set the field null
        etiqueta.setNome(null);

        // Create the Etiqueta, which fails.

        restEtiquetaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(etiqueta)))
            .andExpect(status().isBadRequest());

        List<Etiqueta> etiquetaList = etiquetaRepository.findAll();
        assertThat(etiquetaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllEtiquetas() throws Exception {
        // Initialize the database
        etiquetaRepository.saveAndFlush(etiqueta);

        // Get all the etiquetaList
        restEtiquetaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(etiqueta.getId().intValue())))
            .andExpect(jsonPath("$.[*].nome").value(hasItem(DEFAULT_NOME)));
    }

    @Test
    @Transactional
    void getEtiqueta() throws Exception {
        // Initialize the database
        etiquetaRepository.saveAndFlush(etiqueta);

        // Get the etiqueta
        restEtiquetaMockMvc
            .perform(get(ENTITY_API_URL_ID, etiqueta.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(etiqueta.getId().intValue()))
            .andExpect(jsonPath("$.nome").value(DEFAULT_NOME));
    }

    @Test
    @Transactional
    void getNonExistingEtiqueta() throws Exception {
        // Get the etiqueta
        restEtiquetaMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewEtiqueta() throws Exception {
        // Initialize the database
        etiquetaRepository.saveAndFlush(etiqueta);

        int databaseSizeBeforeUpdate = etiquetaRepository.findAll().size();

        // Update the etiqueta
        Etiqueta updatedEtiqueta = etiquetaRepository.findById(etiqueta.getId()).get();
        // Disconnect from session so that the updates on updatedEtiqueta are not directly saved in db
        em.detach(updatedEtiqueta);
        updatedEtiqueta.nome(UPDATED_NOME);

        restEtiquetaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedEtiqueta.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedEtiqueta))
            )
            .andExpect(status().isOk());

        // Validate the Etiqueta in the database
        List<Etiqueta> etiquetaList = etiquetaRepository.findAll();
        assertThat(etiquetaList).hasSize(databaseSizeBeforeUpdate);
        Etiqueta testEtiqueta = etiquetaList.get(etiquetaList.size() - 1);
        assertThat(testEtiqueta.getNome()).isEqualTo(UPDATED_NOME);

        // Validate the Etiqueta in Elasticsearch
        verify(mockEtiquetaSearchRepository).save(testEtiqueta);
    }

    @Test
    @Transactional
    void putNonExistingEtiqueta() throws Exception {
        int databaseSizeBeforeUpdate = etiquetaRepository.findAll().size();
        etiqueta.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEtiquetaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, etiqueta.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(etiqueta))
            )
            .andExpect(status().isBadRequest());

        // Validate the Etiqueta in the database
        List<Etiqueta> etiquetaList = etiquetaRepository.findAll();
        assertThat(etiquetaList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Etiqueta in Elasticsearch
        verify(mockEtiquetaSearchRepository, times(0)).save(etiqueta);
    }

    @Test
    @Transactional
    void putWithIdMismatchEtiqueta() throws Exception {
        int databaseSizeBeforeUpdate = etiquetaRepository.findAll().size();
        etiqueta.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEtiquetaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(etiqueta))
            )
            .andExpect(status().isBadRequest());

        // Validate the Etiqueta in the database
        List<Etiqueta> etiquetaList = etiquetaRepository.findAll();
        assertThat(etiquetaList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Etiqueta in Elasticsearch
        verify(mockEtiquetaSearchRepository, times(0)).save(etiqueta);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamEtiqueta() throws Exception {
        int databaseSizeBeforeUpdate = etiquetaRepository.findAll().size();
        etiqueta.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEtiquetaMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(etiqueta)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Etiqueta in the database
        List<Etiqueta> etiquetaList = etiquetaRepository.findAll();
        assertThat(etiquetaList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Etiqueta in Elasticsearch
        verify(mockEtiquetaSearchRepository, times(0)).save(etiqueta);
    }

    @Test
    @Transactional
    void partialUpdateEtiquetaWithPatch() throws Exception {
        // Initialize the database
        etiquetaRepository.saveAndFlush(etiqueta);

        int databaseSizeBeforeUpdate = etiquetaRepository.findAll().size();

        // Update the etiqueta using partial update
        Etiqueta partialUpdatedEtiqueta = new Etiqueta();
        partialUpdatedEtiqueta.setId(etiqueta.getId());

        restEtiquetaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEtiqueta.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedEtiqueta))
            )
            .andExpect(status().isOk());

        // Validate the Etiqueta in the database
        List<Etiqueta> etiquetaList = etiquetaRepository.findAll();
        assertThat(etiquetaList).hasSize(databaseSizeBeforeUpdate);
        Etiqueta testEtiqueta = etiquetaList.get(etiquetaList.size() - 1);
        assertThat(testEtiqueta.getNome()).isEqualTo(DEFAULT_NOME);
    }

    @Test
    @Transactional
    void fullUpdateEtiquetaWithPatch() throws Exception {
        // Initialize the database
        etiquetaRepository.saveAndFlush(etiqueta);

        int databaseSizeBeforeUpdate = etiquetaRepository.findAll().size();

        // Update the etiqueta using partial update
        Etiqueta partialUpdatedEtiqueta = new Etiqueta();
        partialUpdatedEtiqueta.setId(etiqueta.getId());

        partialUpdatedEtiqueta.nome(UPDATED_NOME);

        restEtiquetaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEtiqueta.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedEtiqueta))
            )
            .andExpect(status().isOk());

        // Validate the Etiqueta in the database
        List<Etiqueta> etiquetaList = etiquetaRepository.findAll();
        assertThat(etiquetaList).hasSize(databaseSizeBeforeUpdate);
        Etiqueta testEtiqueta = etiquetaList.get(etiquetaList.size() - 1);
        assertThat(testEtiqueta.getNome()).isEqualTo(UPDATED_NOME);
    }

    @Test
    @Transactional
    void patchNonExistingEtiqueta() throws Exception {
        int databaseSizeBeforeUpdate = etiquetaRepository.findAll().size();
        etiqueta.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEtiquetaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, etiqueta.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(etiqueta))
            )
            .andExpect(status().isBadRequest());

        // Validate the Etiqueta in the database
        List<Etiqueta> etiquetaList = etiquetaRepository.findAll();
        assertThat(etiquetaList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Etiqueta in Elasticsearch
        verify(mockEtiquetaSearchRepository, times(0)).save(etiqueta);
    }

    @Test
    @Transactional
    void patchWithIdMismatchEtiqueta() throws Exception {
        int databaseSizeBeforeUpdate = etiquetaRepository.findAll().size();
        etiqueta.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEtiquetaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(etiqueta))
            )
            .andExpect(status().isBadRequest());

        // Validate the Etiqueta in the database
        List<Etiqueta> etiquetaList = etiquetaRepository.findAll();
        assertThat(etiquetaList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Etiqueta in Elasticsearch
        verify(mockEtiquetaSearchRepository, times(0)).save(etiqueta);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamEtiqueta() throws Exception {
        int databaseSizeBeforeUpdate = etiquetaRepository.findAll().size();
        etiqueta.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEtiquetaMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(etiqueta)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Etiqueta in the database
        List<Etiqueta> etiquetaList = etiquetaRepository.findAll();
        assertThat(etiquetaList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Etiqueta in Elasticsearch
        verify(mockEtiquetaSearchRepository, times(0)).save(etiqueta);
    }

    @Test
    @Transactional
    void deleteEtiqueta() throws Exception {
        // Initialize the database
        etiquetaRepository.saveAndFlush(etiqueta);

        int databaseSizeBeforeDelete = etiquetaRepository.findAll().size();

        // Delete the etiqueta
        restEtiquetaMockMvc
            .perform(delete(ENTITY_API_URL_ID, etiqueta.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Etiqueta> etiquetaList = etiquetaRepository.findAll();
        assertThat(etiquetaList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Etiqueta in Elasticsearch
        verify(mockEtiquetaSearchRepository, times(1)).deleteById(etiqueta.getId());
    }

    @Test
    @Transactional
    void searchEtiqueta() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        etiquetaRepository.saveAndFlush(etiqueta);
        when(mockEtiquetaSearchRepository.search(queryStringQuery("id:" + etiqueta.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(etiqueta), PageRequest.of(0, 1), 1));

        // Search the etiqueta
        restEtiquetaMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + etiqueta.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(etiqueta.getId().intValue())))
            .andExpect(jsonPath("$.[*].nome").value(hasItem(DEFAULT_NOME)));
    }
}
