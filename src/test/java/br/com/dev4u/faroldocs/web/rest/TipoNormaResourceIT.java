package br.com.dev4u.faroldocs.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import br.com.dev4u.faroldocs.IntegrationTest;
import br.com.dev4u.faroldocs.domain.TipoNorma;
import br.com.dev4u.faroldocs.repository.TipoNormaRepository;
import br.com.dev4u.faroldocs.repository.search.TipoNormaSearchRepository;
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
 * Integration tests for the {@link TipoNormaResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class TipoNormaResourceIT {

    private static final String DEFAULT_TIPO = "AAAAAAAAAA";
    private static final String UPDATED_TIPO = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRICAO = "AAAAAAAAAA";
    private static final String UPDATED_DESCRICAO = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/tipo-normas";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/_search/tipo-normas";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private TipoNormaRepository tipoNormaRepository;

    /**
     * This repository is mocked in the br.com.dev4u.faroldocs.repository.search test package.
     *
     * @see br.com.dev4u.faroldocs.repository.search.TipoNormaSearchRepositoryMockConfiguration
     */
    @Autowired
    private TipoNormaSearchRepository mockTipoNormaSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTipoNormaMockMvc;

    private TipoNorma tipoNorma;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TipoNorma createEntity(EntityManager em) {
        TipoNorma tipoNorma = new TipoNorma().tipo(DEFAULT_TIPO).descricao(DEFAULT_DESCRICAO);
        return tipoNorma;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TipoNorma createUpdatedEntity(EntityManager em) {
        TipoNorma tipoNorma = new TipoNorma().tipo(UPDATED_TIPO).descricao(UPDATED_DESCRICAO);
        return tipoNorma;
    }

    @BeforeEach
    public void initTest() {
        tipoNorma = createEntity(em);
    }

    @Test
    @Transactional
    void createTipoNorma() throws Exception {
        int databaseSizeBeforeCreate = tipoNormaRepository.findAll().size();
        // Create the TipoNorma
        restTipoNormaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(tipoNorma)))
            .andExpect(status().isCreated());

        // Validate the TipoNorma in the database
        List<TipoNorma> tipoNormaList = tipoNormaRepository.findAll();
        assertThat(tipoNormaList).hasSize(databaseSizeBeforeCreate + 1);
        TipoNorma testTipoNorma = tipoNormaList.get(tipoNormaList.size() - 1);
        assertThat(testTipoNorma.getTipo()).isEqualTo(DEFAULT_TIPO);
        assertThat(testTipoNorma.getDescricao()).isEqualTo(DEFAULT_DESCRICAO);

        // Validate the TipoNorma in Elasticsearch
        verify(mockTipoNormaSearchRepository, times(1)).save(testTipoNorma);
    }

    @Test
    @Transactional
    void createTipoNormaWithExistingId() throws Exception {
        // Create the TipoNorma with an existing ID
        tipoNorma.setId(1L);

        int databaseSizeBeforeCreate = tipoNormaRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTipoNormaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(tipoNorma)))
            .andExpect(status().isBadRequest());

        // Validate the TipoNorma in the database
        List<TipoNorma> tipoNormaList = tipoNormaRepository.findAll();
        assertThat(tipoNormaList).hasSize(databaseSizeBeforeCreate);

        // Validate the TipoNorma in Elasticsearch
        verify(mockTipoNormaSearchRepository, times(0)).save(tipoNorma);
    }

    @Test
    @Transactional
    void checkTipoIsRequired() throws Exception {
        int databaseSizeBeforeTest = tipoNormaRepository.findAll().size();
        // set the field null
        tipoNorma.setTipo(null);

        // Create the TipoNorma, which fails.

        restTipoNormaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(tipoNorma)))
            .andExpect(status().isBadRequest());

        List<TipoNorma> tipoNormaList = tipoNormaRepository.findAll();
        assertThat(tipoNormaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllTipoNormas() throws Exception {
        // Initialize the database
        tipoNormaRepository.saveAndFlush(tipoNorma);

        // Get all the tipoNormaList
        restTipoNormaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tipoNorma.getId().intValue())))
            .andExpect(jsonPath("$.[*].tipo").value(hasItem(DEFAULT_TIPO)))
            .andExpect(jsonPath("$.[*].descricao").value(hasItem(DEFAULT_DESCRICAO)));
    }

    @Test
    @Transactional
    void getTipoNorma() throws Exception {
        // Initialize the database
        tipoNormaRepository.saveAndFlush(tipoNorma);

        // Get the tipoNorma
        restTipoNormaMockMvc
            .perform(get(ENTITY_API_URL_ID, tipoNorma.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(tipoNorma.getId().intValue()))
            .andExpect(jsonPath("$.tipo").value(DEFAULT_TIPO))
            .andExpect(jsonPath("$.descricao").value(DEFAULT_DESCRICAO));
    }

    @Test
    @Transactional
    void getNonExistingTipoNorma() throws Exception {
        // Get the tipoNorma
        restTipoNormaMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewTipoNorma() throws Exception {
        // Initialize the database
        tipoNormaRepository.saveAndFlush(tipoNorma);

        int databaseSizeBeforeUpdate = tipoNormaRepository.findAll().size();

        // Update the tipoNorma
        TipoNorma updatedTipoNorma = tipoNormaRepository.findById(tipoNorma.getId()).get();
        // Disconnect from session so that the updates on updatedTipoNorma are not directly saved in db
        em.detach(updatedTipoNorma);
        updatedTipoNorma.tipo(UPDATED_TIPO).descricao(UPDATED_DESCRICAO);

        restTipoNormaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedTipoNorma.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedTipoNorma))
            )
            .andExpect(status().isOk());

        // Validate the TipoNorma in the database
        List<TipoNorma> tipoNormaList = tipoNormaRepository.findAll();
        assertThat(tipoNormaList).hasSize(databaseSizeBeforeUpdate);
        TipoNorma testTipoNorma = tipoNormaList.get(tipoNormaList.size() - 1);
        assertThat(testTipoNorma.getTipo()).isEqualTo(UPDATED_TIPO);
        assertThat(testTipoNorma.getDescricao()).isEqualTo(UPDATED_DESCRICAO);

        // Validate the TipoNorma in Elasticsearch
        verify(mockTipoNormaSearchRepository).save(testTipoNorma);
    }

    @Test
    @Transactional
    void putNonExistingTipoNorma() throws Exception {
        int databaseSizeBeforeUpdate = tipoNormaRepository.findAll().size();
        tipoNorma.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTipoNormaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, tipoNorma.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tipoNorma))
            )
            .andExpect(status().isBadRequest());

        // Validate the TipoNorma in the database
        List<TipoNorma> tipoNormaList = tipoNormaRepository.findAll();
        assertThat(tipoNormaList).hasSize(databaseSizeBeforeUpdate);

        // Validate the TipoNorma in Elasticsearch
        verify(mockTipoNormaSearchRepository, times(0)).save(tipoNorma);
    }

    @Test
    @Transactional
    void putWithIdMismatchTipoNorma() throws Exception {
        int databaseSizeBeforeUpdate = tipoNormaRepository.findAll().size();
        tipoNorma.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTipoNormaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tipoNorma))
            )
            .andExpect(status().isBadRequest());

        // Validate the TipoNorma in the database
        List<TipoNorma> tipoNormaList = tipoNormaRepository.findAll();
        assertThat(tipoNormaList).hasSize(databaseSizeBeforeUpdate);

        // Validate the TipoNorma in Elasticsearch
        verify(mockTipoNormaSearchRepository, times(0)).save(tipoNorma);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTipoNorma() throws Exception {
        int databaseSizeBeforeUpdate = tipoNormaRepository.findAll().size();
        tipoNorma.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTipoNormaMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(tipoNorma)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the TipoNorma in the database
        List<TipoNorma> tipoNormaList = tipoNormaRepository.findAll();
        assertThat(tipoNormaList).hasSize(databaseSizeBeforeUpdate);

        // Validate the TipoNorma in Elasticsearch
        verify(mockTipoNormaSearchRepository, times(0)).save(tipoNorma);
    }

    @Test
    @Transactional
    void partialUpdateTipoNormaWithPatch() throws Exception {
        // Initialize the database
        tipoNormaRepository.saveAndFlush(tipoNorma);

        int databaseSizeBeforeUpdate = tipoNormaRepository.findAll().size();

        // Update the tipoNorma using partial update
        TipoNorma partialUpdatedTipoNorma = new TipoNorma();
        partialUpdatedTipoNorma.setId(tipoNorma.getId());

        partialUpdatedTipoNorma.tipo(UPDATED_TIPO).descricao(UPDATED_DESCRICAO);

        restTipoNormaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTipoNorma.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTipoNorma))
            )
            .andExpect(status().isOk());

        // Validate the TipoNorma in the database
        List<TipoNorma> tipoNormaList = tipoNormaRepository.findAll();
        assertThat(tipoNormaList).hasSize(databaseSizeBeforeUpdate);
        TipoNorma testTipoNorma = tipoNormaList.get(tipoNormaList.size() - 1);
        assertThat(testTipoNorma.getTipo()).isEqualTo(UPDATED_TIPO);
        assertThat(testTipoNorma.getDescricao()).isEqualTo(UPDATED_DESCRICAO);
    }

    @Test
    @Transactional
    void fullUpdateTipoNormaWithPatch() throws Exception {
        // Initialize the database
        tipoNormaRepository.saveAndFlush(tipoNorma);

        int databaseSizeBeforeUpdate = tipoNormaRepository.findAll().size();

        // Update the tipoNorma using partial update
        TipoNorma partialUpdatedTipoNorma = new TipoNorma();
        partialUpdatedTipoNorma.setId(tipoNorma.getId());

        partialUpdatedTipoNorma.tipo(UPDATED_TIPO).descricao(UPDATED_DESCRICAO);

        restTipoNormaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTipoNorma.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTipoNorma))
            )
            .andExpect(status().isOk());

        // Validate the TipoNorma in the database
        List<TipoNorma> tipoNormaList = tipoNormaRepository.findAll();
        assertThat(tipoNormaList).hasSize(databaseSizeBeforeUpdate);
        TipoNorma testTipoNorma = tipoNormaList.get(tipoNormaList.size() - 1);
        assertThat(testTipoNorma.getTipo()).isEqualTo(UPDATED_TIPO);
        assertThat(testTipoNorma.getDescricao()).isEqualTo(UPDATED_DESCRICAO);
    }

    @Test
    @Transactional
    void patchNonExistingTipoNorma() throws Exception {
        int databaseSizeBeforeUpdate = tipoNormaRepository.findAll().size();
        tipoNorma.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTipoNormaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, tipoNorma.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(tipoNorma))
            )
            .andExpect(status().isBadRequest());

        // Validate the TipoNorma in the database
        List<TipoNorma> tipoNormaList = tipoNormaRepository.findAll();
        assertThat(tipoNormaList).hasSize(databaseSizeBeforeUpdate);

        // Validate the TipoNorma in Elasticsearch
        verify(mockTipoNormaSearchRepository, times(0)).save(tipoNorma);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTipoNorma() throws Exception {
        int databaseSizeBeforeUpdate = tipoNormaRepository.findAll().size();
        tipoNorma.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTipoNormaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(tipoNorma))
            )
            .andExpect(status().isBadRequest());

        // Validate the TipoNorma in the database
        List<TipoNorma> tipoNormaList = tipoNormaRepository.findAll();
        assertThat(tipoNormaList).hasSize(databaseSizeBeforeUpdate);

        // Validate the TipoNorma in Elasticsearch
        verify(mockTipoNormaSearchRepository, times(0)).save(tipoNorma);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTipoNorma() throws Exception {
        int databaseSizeBeforeUpdate = tipoNormaRepository.findAll().size();
        tipoNorma.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTipoNormaMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(tipoNorma))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the TipoNorma in the database
        List<TipoNorma> tipoNormaList = tipoNormaRepository.findAll();
        assertThat(tipoNormaList).hasSize(databaseSizeBeforeUpdate);

        // Validate the TipoNorma in Elasticsearch
        verify(mockTipoNormaSearchRepository, times(0)).save(tipoNorma);
    }

    @Test
    @Transactional
    void deleteTipoNorma() throws Exception {
        // Initialize the database
        tipoNormaRepository.saveAndFlush(tipoNorma);

        int databaseSizeBeforeDelete = tipoNormaRepository.findAll().size();

        // Delete the tipoNorma
        restTipoNormaMockMvc
            .perform(delete(ENTITY_API_URL_ID, tipoNorma.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<TipoNorma> tipoNormaList = tipoNormaRepository.findAll();
        assertThat(tipoNormaList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the TipoNorma in Elasticsearch
        verify(mockTipoNormaSearchRepository, times(1)).deleteById(tipoNorma.getId());
    }

    @Test
    @Transactional
    void searchTipoNorma() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        tipoNormaRepository.saveAndFlush(tipoNorma);
        when(mockTipoNormaSearchRepository.search(queryStringQuery("id:" + tipoNorma.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(tipoNorma), PageRequest.of(0, 1), 1));

        // Search the tipoNorma
        restTipoNormaMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + tipoNorma.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tipoNorma.getId().intValue())))
            .andExpect(jsonPath("$.[*].tipo").value(hasItem(DEFAULT_TIPO)))
            .andExpect(jsonPath("$.[*].descricao").value(hasItem(DEFAULT_DESCRICAO)));
    }
}
