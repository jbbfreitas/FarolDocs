package br.com.dev4u.faroldocs.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import br.com.dev4u.faroldocs.IntegrationTest;
import br.com.dev4u.faroldocs.domain.Projeto;
import br.com.dev4u.faroldocs.domain.enumeration.SituacaoProjeto;
import br.com.dev4u.faroldocs.repository.ProjetoRepository;
import br.com.dev4u.faroldocs.repository.search.ProjetoSearchRepository;
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

/**
 * Integration tests for the {@link ProjetoResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class ProjetoResourceIT {

    private static final String DEFAULT_NOME = "AAAAAAAAAA";
    private static final String UPDATED_NOME = "BBBBBBBBBB";

    private static final Instant DEFAULT_INICIO = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_INICIO = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_FIM = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_FIM = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final SituacaoProjeto DEFAULT_SITUACAO = SituacaoProjeto.INICIADO;
    private static final SituacaoProjeto UPDATED_SITUACAO = SituacaoProjeto.PAUSADO;

    private static final String ENTITY_API_URL = "/api/projetos";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/_search/projetos";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ProjetoRepository projetoRepository;

    /**
     * This repository is mocked in the br.com.dev4u.faroldocs.repository.search test package.
     *
     * @see br.com.dev4u.faroldocs.repository.search.ProjetoSearchRepositoryMockConfiguration
     */
    @Autowired
    private ProjetoSearchRepository mockProjetoSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restProjetoMockMvc;

    private Projeto projeto;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Projeto createEntity(EntityManager em) {
        Projeto projeto = new Projeto().nome(DEFAULT_NOME).inicio(DEFAULT_INICIO).fim(DEFAULT_FIM).situacao(DEFAULT_SITUACAO);
        return projeto;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Projeto createUpdatedEntity(EntityManager em) {
        Projeto projeto = new Projeto().nome(UPDATED_NOME).inicio(UPDATED_INICIO).fim(UPDATED_FIM).situacao(UPDATED_SITUACAO);
        return projeto;
    }

    @BeforeEach
    public void initTest() {
        projeto = createEntity(em);
    }

    @Test
    @Transactional
    void createProjeto() throws Exception {
        int databaseSizeBeforeCreate = projetoRepository.findAll().size();
        // Create the Projeto
        restProjetoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(projeto)))
            .andExpect(status().isCreated());

        // Validate the Projeto in the database
        List<Projeto> projetoList = projetoRepository.findAll();
        assertThat(projetoList).hasSize(databaseSizeBeforeCreate + 1);
        Projeto testProjeto = projetoList.get(projetoList.size() - 1);
        assertThat(testProjeto.getNome()).isEqualTo(DEFAULT_NOME);
        assertThat(testProjeto.getInicio()).isEqualTo(DEFAULT_INICIO);
        assertThat(testProjeto.getFim()).isEqualTo(DEFAULT_FIM);
        assertThat(testProjeto.getSituacao()).isEqualTo(DEFAULT_SITUACAO);

        // Validate the Projeto in Elasticsearch
        verify(mockProjetoSearchRepository, times(1)).save(testProjeto);
    }

    @Test
    @Transactional
    void createProjetoWithExistingId() throws Exception {
        // Create the Projeto with an existing ID
        projeto.setId(1L);

        int databaseSizeBeforeCreate = projetoRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restProjetoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(projeto)))
            .andExpect(status().isBadRequest());

        // Validate the Projeto in the database
        List<Projeto> projetoList = projetoRepository.findAll();
        assertThat(projetoList).hasSize(databaseSizeBeforeCreate);

        // Validate the Projeto in Elasticsearch
        verify(mockProjetoSearchRepository, times(0)).save(projeto);
    }

    @Test
    @Transactional
    void checkNomeIsRequired() throws Exception {
        int databaseSizeBeforeTest = projetoRepository.findAll().size();
        // set the field null
        projeto.setNome(null);

        // Create the Projeto, which fails.

        restProjetoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(projeto)))
            .andExpect(status().isBadRequest());

        List<Projeto> projetoList = projetoRepository.findAll();
        assertThat(projetoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkInicioIsRequired() throws Exception {
        int databaseSizeBeforeTest = projetoRepository.findAll().size();
        // set the field null
        projeto.setInicio(null);

        // Create the Projeto, which fails.

        restProjetoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(projeto)))
            .andExpect(status().isBadRequest());

        List<Projeto> projetoList = projetoRepository.findAll();
        assertThat(projetoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllProjetos() throws Exception {
        // Initialize the database
        projetoRepository.saveAndFlush(projeto);

        // Get all the projetoList
        restProjetoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(projeto.getId().intValue())))
            .andExpect(jsonPath("$.[*].nome").value(hasItem(DEFAULT_NOME)))
            .andExpect(jsonPath("$.[*].inicio").value(hasItem(DEFAULT_INICIO.toString())))
            .andExpect(jsonPath("$.[*].fim").value(hasItem(DEFAULT_FIM.toString())))
            .andExpect(jsonPath("$.[*].situacao").value(hasItem(DEFAULT_SITUACAO.toString())));
    }

    @Test
    @Transactional
    void getProjeto() throws Exception {
        // Initialize the database
        projetoRepository.saveAndFlush(projeto);

        // Get the projeto
        restProjetoMockMvc
            .perform(get(ENTITY_API_URL_ID, projeto.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(projeto.getId().intValue()))
            .andExpect(jsonPath("$.nome").value(DEFAULT_NOME))
            .andExpect(jsonPath("$.inicio").value(DEFAULT_INICIO.toString()))
            .andExpect(jsonPath("$.fim").value(DEFAULT_FIM.toString()))
            .andExpect(jsonPath("$.situacao").value(DEFAULT_SITUACAO.toString()));
    }

    @Test
    @Transactional
    void getNonExistingProjeto() throws Exception {
        // Get the projeto
        restProjetoMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewProjeto() throws Exception {
        // Initialize the database
        projetoRepository.saveAndFlush(projeto);

        int databaseSizeBeforeUpdate = projetoRepository.findAll().size();

        // Update the projeto
        Projeto updatedProjeto = projetoRepository.findById(projeto.getId()).get();
        // Disconnect from session so that the updates on updatedProjeto are not directly saved in db
        em.detach(updatedProjeto);
        updatedProjeto.nome(UPDATED_NOME).inicio(UPDATED_INICIO).fim(UPDATED_FIM).situacao(UPDATED_SITUACAO);

        restProjetoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedProjeto.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedProjeto))
            )
            .andExpect(status().isOk());

        // Validate the Projeto in the database
        List<Projeto> projetoList = projetoRepository.findAll();
        assertThat(projetoList).hasSize(databaseSizeBeforeUpdate);
        Projeto testProjeto = projetoList.get(projetoList.size() - 1);
        assertThat(testProjeto.getNome()).isEqualTo(UPDATED_NOME);
        assertThat(testProjeto.getInicio()).isEqualTo(UPDATED_INICIO);
        assertThat(testProjeto.getFim()).isEqualTo(UPDATED_FIM);
        assertThat(testProjeto.getSituacao()).isEqualTo(UPDATED_SITUACAO);

        // Validate the Projeto in Elasticsearch
        verify(mockProjetoSearchRepository).save(testProjeto);
    }

    @Test
    @Transactional
    void putNonExistingProjeto() throws Exception {
        int databaseSizeBeforeUpdate = projetoRepository.findAll().size();
        projeto.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProjetoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, projeto.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(projeto))
            )
            .andExpect(status().isBadRequest());

        // Validate the Projeto in the database
        List<Projeto> projetoList = projetoRepository.findAll();
        assertThat(projetoList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Projeto in Elasticsearch
        verify(mockProjetoSearchRepository, times(0)).save(projeto);
    }

    @Test
    @Transactional
    void putWithIdMismatchProjeto() throws Exception {
        int databaseSizeBeforeUpdate = projetoRepository.findAll().size();
        projeto.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProjetoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(projeto))
            )
            .andExpect(status().isBadRequest());

        // Validate the Projeto in the database
        List<Projeto> projetoList = projetoRepository.findAll();
        assertThat(projetoList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Projeto in Elasticsearch
        verify(mockProjetoSearchRepository, times(0)).save(projeto);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamProjeto() throws Exception {
        int databaseSizeBeforeUpdate = projetoRepository.findAll().size();
        projeto.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProjetoMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(projeto)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Projeto in the database
        List<Projeto> projetoList = projetoRepository.findAll();
        assertThat(projetoList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Projeto in Elasticsearch
        verify(mockProjetoSearchRepository, times(0)).save(projeto);
    }

    @Test
    @Transactional
    void partialUpdateProjetoWithPatch() throws Exception {
        // Initialize the database
        projetoRepository.saveAndFlush(projeto);

        int databaseSizeBeforeUpdate = projetoRepository.findAll().size();

        // Update the projeto using partial update
        Projeto partialUpdatedProjeto = new Projeto();
        partialUpdatedProjeto.setId(projeto.getId());

        partialUpdatedProjeto.nome(UPDATED_NOME);

        restProjetoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProjeto.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedProjeto))
            )
            .andExpect(status().isOk());

        // Validate the Projeto in the database
        List<Projeto> projetoList = projetoRepository.findAll();
        assertThat(projetoList).hasSize(databaseSizeBeforeUpdate);
        Projeto testProjeto = projetoList.get(projetoList.size() - 1);
        assertThat(testProjeto.getNome()).isEqualTo(UPDATED_NOME);
        assertThat(testProjeto.getInicio()).isEqualTo(DEFAULT_INICIO);
        assertThat(testProjeto.getFim()).isEqualTo(DEFAULT_FIM);
        assertThat(testProjeto.getSituacao()).isEqualTo(DEFAULT_SITUACAO);
    }

    @Test
    @Transactional
    void fullUpdateProjetoWithPatch() throws Exception {
        // Initialize the database
        projetoRepository.saveAndFlush(projeto);

        int databaseSizeBeforeUpdate = projetoRepository.findAll().size();

        // Update the projeto using partial update
        Projeto partialUpdatedProjeto = new Projeto();
        partialUpdatedProjeto.setId(projeto.getId());

        partialUpdatedProjeto.nome(UPDATED_NOME).inicio(UPDATED_INICIO).fim(UPDATED_FIM).situacao(UPDATED_SITUACAO);

        restProjetoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProjeto.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedProjeto))
            )
            .andExpect(status().isOk());

        // Validate the Projeto in the database
        List<Projeto> projetoList = projetoRepository.findAll();
        assertThat(projetoList).hasSize(databaseSizeBeforeUpdate);
        Projeto testProjeto = projetoList.get(projetoList.size() - 1);
        assertThat(testProjeto.getNome()).isEqualTo(UPDATED_NOME);
        assertThat(testProjeto.getInicio()).isEqualTo(UPDATED_INICIO);
        assertThat(testProjeto.getFim()).isEqualTo(UPDATED_FIM);
        assertThat(testProjeto.getSituacao()).isEqualTo(UPDATED_SITUACAO);
    }

    @Test
    @Transactional
    void patchNonExistingProjeto() throws Exception {
        int databaseSizeBeforeUpdate = projetoRepository.findAll().size();
        projeto.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProjetoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, projeto.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(projeto))
            )
            .andExpect(status().isBadRequest());

        // Validate the Projeto in the database
        List<Projeto> projetoList = projetoRepository.findAll();
        assertThat(projetoList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Projeto in Elasticsearch
        verify(mockProjetoSearchRepository, times(0)).save(projeto);
    }

    @Test
    @Transactional
    void patchWithIdMismatchProjeto() throws Exception {
        int databaseSizeBeforeUpdate = projetoRepository.findAll().size();
        projeto.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProjetoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(projeto))
            )
            .andExpect(status().isBadRequest());

        // Validate the Projeto in the database
        List<Projeto> projetoList = projetoRepository.findAll();
        assertThat(projetoList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Projeto in Elasticsearch
        verify(mockProjetoSearchRepository, times(0)).save(projeto);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamProjeto() throws Exception {
        int databaseSizeBeforeUpdate = projetoRepository.findAll().size();
        projeto.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProjetoMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(projeto)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Projeto in the database
        List<Projeto> projetoList = projetoRepository.findAll();
        assertThat(projetoList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Projeto in Elasticsearch
        verify(mockProjetoSearchRepository, times(0)).save(projeto);
    }

    @Test
    @Transactional
    void deleteProjeto() throws Exception {
        // Initialize the database
        projetoRepository.saveAndFlush(projeto);

        int databaseSizeBeforeDelete = projetoRepository.findAll().size();

        // Delete the projeto
        restProjetoMockMvc
            .perform(delete(ENTITY_API_URL_ID, projeto.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Projeto> projetoList = projetoRepository.findAll();
        assertThat(projetoList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Projeto in Elasticsearch
        verify(mockProjetoSearchRepository, times(1)).deleteById(projeto.getId());
    }

    @Test
    @Transactional
    void searchProjeto() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        projetoRepository.saveAndFlush(projeto);
        when(mockProjetoSearchRepository.search(queryStringQuery("id:" + projeto.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(projeto), PageRequest.of(0, 1), 1));

        // Search the projeto
        restProjetoMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + projeto.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(projeto.getId().intValue())))
            .andExpect(jsonPath("$.[*].nome").value(hasItem(DEFAULT_NOME)))
            .andExpect(jsonPath("$.[*].inicio").value(hasItem(DEFAULT_INICIO.toString())))
            .andExpect(jsonPath("$.[*].fim").value(hasItem(DEFAULT_FIM.toString())))
            .andExpect(jsonPath("$.[*].situacao").value(hasItem(DEFAULT_SITUACAO.toString())));
    }
}
