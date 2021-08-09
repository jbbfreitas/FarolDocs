package br.com.dev4u.faroldocs.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import br.com.dev4u.faroldocs.IntegrationTest;
import br.com.dev4u.faroldocs.domain.Tipo;
import br.com.dev4u.faroldocs.repository.TipoRepository;
import br.com.dev4u.faroldocs.repository.search.TipoSearchRepository;
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
 * Integration tests for the {@link TipoResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class TipoResourceIT {

    private static final String DEFAULT_NOME = "AAAAAAAAAA";
    private static final String UPDATED_NOME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRICAO = "AAAAAAAAAA";
    private static final String UPDATED_DESCRICAO = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/tipos";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/_search/tipos";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private TipoRepository tipoRepository;

    /**
     * This repository is mocked in the br.com.dev4u.faroldocs.repository.search test package.
     *
     * @see br.com.dev4u.faroldocs.repository.search.TipoSearchRepositoryMockConfiguration
     */
    @Autowired
    private TipoSearchRepository mockTipoSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTipoMockMvc;

    private Tipo tipo;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Tipo createEntity(EntityManager em) {
        Tipo tipo = new Tipo().nome(DEFAULT_NOME).descricao(DEFAULT_DESCRICAO);
        return tipo;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Tipo createUpdatedEntity(EntityManager em) {
        Tipo tipo = new Tipo().nome(UPDATED_NOME).descricao(UPDATED_DESCRICAO);
        return tipo;
    }

    @BeforeEach
    public void initTest() {
        tipo = createEntity(em);
    }

    @Test
    @Transactional
    void createTipo() throws Exception {
        int databaseSizeBeforeCreate = tipoRepository.findAll().size();
        // Create the Tipo
        restTipoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(tipo)))
            .andExpect(status().isCreated());

        // Validate the Tipo in the database
        List<Tipo> tipoList = tipoRepository.findAll();
        assertThat(tipoList).hasSize(databaseSizeBeforeCreate + 1);
        Tipo testTipo = tipoList.get(tipoList.size() - 1);
        assertThat(testTipo.getNome()).isEqualTo(DEFAULT_NOME);
        assertThat(testTipo.getDescricao()).isEqualTo(DEFAULT_DESCRICAO);

        // Validate the Tipo in Elasticsearch
        verify(mockTipoSearchRepository, times(1)).save(testTipo);
    }

    @Test
    @Transactional
    void createTipoWithExistingId() throws Exception {
        // Create the Tipo with an existing ID
        tipo.setId(1L);

        int databaseSizeBeforeCreate = tipoRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTipoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(tipo)))
            .andExpect(status().isBadRequest());

        // Validate the Tipo in the database
        List<Tipo> tipoList = tipoRepository.findAll();
        assertThat(tipoList).hasSize(databaseSizeBeforeCreate);

        // Validate the Tipo in Elasticsearch
        verify(mockTipoSearchRepository, times(0)).save(tipo);
    }

    @Test
    @Transactional
    void checkNomeIsRequired() throws Exception {
        int databaseSizeBeforeTest = tipoRepository.findAll().size();
        // set the field null
        tipo.setNome(null);

        // Create the Tipo, which fails.

        restTipoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(tipo)))
            .andExpect(status().isBadRequest());

        List<Tipo> tipoList = tipoRepository.findAll();
        assertThat(tipoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllTipos() throws Exception {
        // Initialize the database
        tipoRepository.saveAndFlush(tipo);

        // Get all the tipoList
        restTipoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tipo.getId().intValue())))
            .andExpect(jsonPath("$.[*].nome").value(hasItem(DEFAULT_NOME)))
            .andExpect(jsonPath("$.[*].descricao").value(hasItem(DEFAULT_DESCRICAO)));
    }

    @Test
    @Transactional
    void getTipo() throws Exception {
        // Initialize the database
        tipoRepository.saveAndFlush(tipo);

        // Get the tipo
        restTipoMockMvc
            .perform(get(ENTITY_API_URL_ID, tipo.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(tipo.getId().intValue()))
            .andExpect(jsonPath("$.nome").value(DEFAULT_NOME))
            .andExpect(jsonPath("$.descricao").value(DEFAULT_DESCRICAO));
    }

    @Test
    @Transactional
    void getNonExistingTipo() throws Exception {
        // Get the tipo
        restTipoMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewTipo() throws Exception {
        // Initialize the database
        tipoRepository.saveAndFlush(tipo);

        int databaseSizeBeforeUpdate = tipoRepository.findAll().size();

        // Update the tipo
        Tipo updatedTipo = tipoRepository.findById(tipo.getId()).get();
        // Disconnect from session so that the updates on updatedTipo are not directly saved in db
        em.detach(updatedTipo);
        updatedTipo.nome(UPDATED_NOME).descricao(UPDATED_DESCRICAO);

        restTipoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedTipo.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedTipo))
            )
            .andExpect(status().isOk());

        // Validate the Tipo in the database
        List<Tipo> tipoList = tipoRepository.findAll();
        assertThat(tipoList).hasSize(databaseSizeBeforeUpdate);
        Tipo testTipo = tipoList.get(tipoList.size() - 1);
        assertThat(testTipo.getNome()).isEqualTo(UPDATED_NOME);
        assertThat(testTipo.getDescricao()).isEqualTo(UPDATED_DESCRICAO);

        // Validate the Tipo in Elasticsearch
        verify(mockTipoSearchRepository).save(testTipo);
    }

    @Test
    @Transactional
    void putNonExistingTipo() throws Exception {
        int databaseSizeBeforeUpdate = tipoRepository.findAll().size();
        tipo.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTipoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, tipo.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tipo))
            )
            .andExpect(status().isBadRequest());

        // Validate the Tipo in the database
        List<Tipo> tipoList = tipoRepository.findAll();
        assertThat(tipoList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Tipo in Elasticsearch
        verify(mockTipoSearchRepository, times(0)).save(tipo);
    }

    @Test
    @Transactional
    void putWithIdMismatchTipo() throws Exception {
        int databaseSizeBeforeUpdate = tipoRepository.findAll().size();
        tipo.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTipoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tipo))
            )
            .andExpect(status().isBadRequest());

        // Validate the Tipo in the database
        List<Tipo> tipoList = tipoRepository.findAll();
        assertThat(tipoList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Tipo in Elasticsearch
        verify(mockTipoSearchRepository, times(0)).save(tipo);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTipo() throws Exception {
        int databaseSizeBeforeUpdate = tipoRepository.findAll().size();
        tipo.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTipoMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(tipo)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Tipo in the database
        List<Tipo> tipoList = tipoRepository.findAll();
        assertThat(tipoList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Tipo in Elasticsearch
        verify(mockTipoSearchRepository, times(0)).save(tipo);
    }

    @Test
    @Transactional
    void partialUpdateTipoWithPatch() throws Exception {
        // Initialize the database
        tipoRepository.saveAndFlush(tipo);

        int databaseSizeBeforeUpdate = tipoRepository.findAll().size();

        // Update the tipo using partial update
        Tipo partialUpdatedTipo = new Tipo();
        partialUpdatedTipo.setId(tipo.getId());

        partialUpdatedTipo.descricao(UPDATED_DESCRICAO);

        restTipoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTipo.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTipo))
            )
            .andExpect(status().isOk());

        // Validate the Tipo in the database
        List<Tipo> tipoList = tipoRepository.findAll();
        assertThat(tipoList).hasSize(databaseSizeBeforeUpdate);
        Tipo testTipo = tipoList.get(tipoList.size() - 1);
        assertThat(testTipo.getNome()).isEqualTo(DEFAULT_NOME);
        assertThat(testTipo.getDescricao()).isEqualTo(UPDATED_DESCRICAO);
    }

    @Test
    @Transactional
    void fullUpdateTipoWithPatch() throws Exception {
        // Initialize the database
        tipoRepository.saveAndFlush(tipo);

        int databaseSizeBeforeUpdate = tipoRepository.findAll().size();

        // Update the tipo using partial update
        Tipo partialUpdatedTipo = new Tipo();
        partialUpdatedTipo.setId(tipo.getId());

        partialUpdatedTipo.nome(UPDATED_NOME).descricao(UPDATED_DESCRICAO);

        restTipoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTipo.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTipo))
            )
            .andExpect(status().isOk());

        // Validate the Tipo in the database
        List<Tipo> tipoList = tipoRepository.findAll();
        assertThat(tipoList).hasSize(databaseSizeBeforeUpdate);
        Tipo testTipo = tipoList.get(tipoList.size() - 1);
        assertThat(testTipo.getNome()).isEqualTo(UPDATED_NOME);
        assertThat(testTipo.getDescricao()).isEqualTo(UPDATED_DESCRICAO);
    }

    @Test
    @Transactional
    void patchNonExistingTipo() throws Exception {
        int databaseSizeBeforeUpdate = tipoRepository.findAll().size();
        tipo.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTipoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, tipo.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(tipo))
            )
            .andExpect(status().isBadRequest());

        // Validate the Tipo in the database
        List<Tipo> tipoList = tipoRepository.findAll();
        assertThat(tipoList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Tipo in Elasticsearch
        verify(mockTipoSearchRepository, times(0)).save(tipo);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTipo() throws Exception {
        int databaseSizeBeforeUpdate = tipoRepository.findAll().size();
        tipo.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTipoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(tipo))
            )
            .andExpect(status().isBadRequest());

        // Validate the Tipo in the database
        List<Tipo> tipoList = tipoRepository.findAll();
        assertThat(tipoList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Tipo in Elasticsearch
        verify(mockTipoSearchRepository, times(0)).save(tipo);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTipo() throws Exception {
        int databaseSizeBeforeUpdate = tipoRepository.findAll().size();
        tipo.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTipoMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(tipo)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Tipo in the database
        List<Tipo> tipoList = tipoRepository.findAll();
        assertThat(tipoList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Tipo in Elasticsearch
        verify(mockTipoSearchRepository, times(0)).save(tipo);
    }

    @Test
    @Transactional
    void deleteTipo() throws Exception {
        // Initialize the database
        tipoRepository.saveAndFlush(tipo);

        int databaseSizeBeforeDelete = tipoRepository.findAll().size();

        // Delete the tipo
        restTipoMockMvc
            .perform(delete(ENTITY_API_URL_ID, tipo.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Tipo> tipoList = tipoRepository.findAll();
        assertThat(tipoList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Tipo in Elasticsearch
        verify(mockTipoSearchRepository, times(1)).deleteById(tipo.getId());
    }

    @Test
    @Transactional
    void searchTipo() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        tipoRepository.saveAndFlush(tipo);
        when(mockTipoSearchRepository.search(queryStringQuery("id:" + tipo.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(tipo), PageRequest.of(0, 1), 1));

        // Search the tipo
        restTipoMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + tipo.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tipo.getId().intValue())))
            .andExpect(jsonPath("$.[*].nome").value(hasItem(DEFAULT_NOME)))
            .andExpect(jsonPath("$.[*].descricao").value(hasItem(DEFAULT_DESCRICAO)));
    }
}
