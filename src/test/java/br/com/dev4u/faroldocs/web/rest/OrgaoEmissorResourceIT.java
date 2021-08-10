package br.com.dev4u.faroldocs.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import br.com.dev4u.faroldocs.IntegrationTest;
import br.com.dev4u.faroldocs.domain.OrgaoEmissor;
import br.com.dev4u.faroldocs.repository.OrgaoEmissorRepository;
import br.com.dev4u.faroldocs.repository.search.OrgaoEmissorSearchRepository;
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
 * Integration tests for the {@link OrgaoEmissorResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class OrgaoEmissorResourceIT {

    private static final String DEFAULT_SIGLA = "AAAAAAAAAA";
    private static final String UPDATED_SIGLA = "BBBBBBBBBB";

    private static final String DEFAULT_NOME = "AAAAAAAAAA";
    private static final String UPDATED_NOME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/orgao-emissors";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/_search/orgao-emissors";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private OrgaoEmissorRepository orgaoEmissorRepository;

    /**
     * This repository is mocked in the br.com.dev4u.faroldocs.repository.search test package.
     *
     * @see br.com.dev4u.faroldocs.repository.search.OrgaoEmissorSearchRepositoryMockConfiguration
     */
    @Autowired
    private OrgaoEmissorSearchRepository mockOrgaoEmissorSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restOrgaoEmissorMockMvc;

    private OrgaoEmissor orgaoEmissor;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static OrgaoEmissor createEntity(EntityManager em) {
        OrgaoEmissor orgaoEmissor = new OrgaoEmissor().sigla(DEFAULT_SIGLA).nome(DEFAULT_NOME);
        return orgaoEmissor;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static OrgaoEmissor createUpdatedEntity(EntityManager em) {
        OrgaoEmissor orgaoEmissor = new OrgaoEmissor().sigla(UPDATED_SIGLA).nome(UPDATED_NOME);
        return orgaoEmissor;
    }

    @BeforeEach
    public void initTest() {
        orgaoEmissor = createEntity(em);
    }

    @Test
    @Transactional
    void createOrgaoEmissor() throws Exception {
        int databaseSizeBeforeCreate = orgaoEmissorRepository.findAll().size();
        // Create the OrgaoEmissor
        restOrgaoEmissorMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(orgaoEmissor)))
            .andExpect(status().isCreated());

        // Validate the OrgaoEmissor in the database
        List<OrgaoEmissor> orgaoEmissorList = orgaoEmissorRepository.findAll();
        assertThat(orgaoEmissorList).hasSize(databaseSizeBeforeCreate + 1);
        OrgaoEmissor testOrgaoEmissor = orgaoEmissorList.get(orgaoEmissorList.size() - 1);
        assertThat(testOrgaoEmissor.getSigla()).isEqualTo(DEFAULT_SIGLA);
        assertThat(testOrgaoEmissor.getNome()).isEqualTo(DEFAULT_NOME);

        // Validate the OrgaoEmissor in Elasticsearch
        verify(mockOrgaoEmissorSearchRepository, times(1)).save(testOrgaoEmissor);
    }

    @Test
    @Transactional
    void createOrgaoEmissorWithExistingId() throws Exception {
        // Create the OrgaoEmissor with an existing ID
        orgaoEmissor.setId(1L);

        int databaseSizeBeforeCreate = orgaoEmissorRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restOrgaoEmissorMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(orgaoEmissor)))
            .andExpect(status().isBadRequest());

        // Validate the OrgaoEmissor in the database
        List<OrgaoEmissor> orgaoEmissorList = orgaoEmissorRepository.findAll();
        assertThat(orgaoEmissorList).hasSize(databaseSizeBeforeCreate);

        // Validate the OrgaoEmissor in Elasticsearch
        verify(mockOrgaoEmissorSearchRepository, times(0)).save(orgaoEmissor);
    }

    @Test
    @Transactional
    void checkSiglaIsRequired() throws Exception {
        int databaseSizeBeforeTest = orgaoEmissorRepository.findAll().size();
        // set the field null
        orgaoEmissor.setSigla(null);

        // Create the OrgaoEmissor, which fails.

        restOrgaoEmissorMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(orgaoEmissor)))
            .andExpect(status().isBadRequest());

        List<OrgaoEmissor> orgaoEmissorList = orgaoEmissorRepository.findAll();
        assertThat(orgaoEmissorList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkNomeIsRequired() throws Exception {
        int databaseSizeBeforeTest = orgaoEmissorRepository.findAll().size();
        // set the field null
        orgaoEmissor.setNome(null);

        // Create the OrgaoEmissor, which fails.

        restOrgaoEmissorMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(orgaoEmissor)))
            .andExpect(status().isBadRequest());

        List<OrgaoEmissor> orgaoEmissorList = orgaoEmissorRepository.findAll();
        assertThat(orgaoEmissorList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllOrgaoEmissors() throws Exception {
        // Initialize the database
        orgaoEmissorRepository.saveAndFlush(orgaoEmissor);

        // Get all the orgaoEmissorList
        restOrgaoEmissorMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(orgaoEmissor.getId().intValue())))
            .andExpect(jsonPath("$.[*].sigla").value(hasItem(DEFAULT_SIGLA)))
            .andExpect(jsonPath("$.[*].nome").value(hasItem(DEFAULT_NOME)));
    }

    @Test
    @Transactional
    void getOrgaoEmissor() throws Exception {
        // Initialize the database
        orgaoEmissorRepository.saveAndFlush(orgaoEmissor);

        // Get the orgaoEmissor
        restOrgaoEmissorMockMvc
            .perform(get(ENTITY_API_URL_ID, orgaoEmissor.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(orgaoEmissor.getId().intValue()))
            .andExpect(jsonPath("$.sigla").value(DEFAULT_SIGLA))
            .andExpect(jsonPath("$.nome").value(DEFAULT_NOME));
    }

    @Test
    @Transactional
    void getNonExistingOrgaoEmissor() throws Exception {
        // Get the orgaoEmissor
        restOrgaoEmissorMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewOrgaoEmissor() throws Exception {
        // Initialize the database
        orgaoEmissorRepository.saveAndFlush(orgaoEmissor);

        int databaseSizeBeforeUpdate = orgaoEmissorRepository.findAll().size();

        // Update the orgaoEmissor
        OrgaoEmissor updatedOrgaoEmissor = orgaoEmissorRepository.findById(orgaoEmissor.getId()).get();
        // Disconnect from session so that the updates on updatedOrgaoEmissor are not directly saved in db
        em.detach(updatedOrgaoEmissor);
        updatedOrgaoEmissor.sigla(UPDATED_SIGLA).nome(UPDATED_NOME);

        restOrgaoEmissorMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedOrgaoEmissor.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedOrgaoEmissor))
            )
            .andExpect(status().isOk());

        // Validate the OrgaoEmissor in the database
        List<OrgaoEmissor> orgaoEmissorList = orgaoEmissorRepository.findAll();
        assertThat(orgaoEmissorList).hasSize(databaseSizeBeforeUpdate);
        OrgaoEmissor testOrgaoEmissor = orgaoEmissorList.get(orgaoEmissorList.size() - 1);
        assertThat(testOrgaoEmissor.getSigla()).isEqualTo(UPDATED_SIGLA);
        assertThat(testOrgaoEmissor.getNome()).isEqualTo(UPDATED_NOME);

        // Validate the OrgaoEmissor in Elasticsearch
        verify(mockOrgaoEmissorSearchRepository).save(testOrgaoEmissor);
    }

    @Test
    @Transactional
    void putNonExistingOrgaoEmissor() throws Exception {
        int databaseSizeBeforeUpdate = orgaoEmissorRepository.findAll().size();
        orgaoEmissor.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOrgaoEmissorMockMvc
            .perform(
                put(ENTITY_API_URL_ID, orgaoEmissor.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(orgaoEmissor))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrgaoEmissor in the database
        List<OrgaoEmissor> orgaoEmissorList = orgaoEmissorRepository.findAll();
        assertThat(orgaoEmissorList).hasSize(databaseSizeBeforeUpdate);

        // Validate the OrgaoEmissor in Elasticsearch
        verify(mockOrgaoEmissorSearchRepository, times(0)).save(orgaoEmissor);
    }

    @Test
    @Transactional
    void putWithIdMismatchOrgaoEmissor() throws Exception {
        int databaseSizeBeforeUpdate = orgaoEmissorRepository.findAll().size();
        orgaoEmissor.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrgaoEmissorMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(orgaoEmissor))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrgaoEmissor in the database
        List<OrgaoEmissor> orgaoEmissorList = orgaoEmissorRepository.findAll();
        assertThat(orgaoEmissorList).hasSize(databaseSizeBeforeUpdate);

        // Validate the OrgaoEmissor in Elasticsearch
        verify(mockOrgaoEmissorSearchRepository, times(0)).save(orgaoEmissor);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamOrgaoEmissor() throws Exception {
        int databaseSizeBeforeUpdate = orgaoEmissorRepository.findAll().size();
        orgaoEmissor.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrgaoEmissorMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(orgaoEmissor)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the OrgaoEmissor in the database
        List<OrgaoEmissor> orgaoEmissorList = orgaoEmissorRepository.findAll();
        assertThat(orgaoEmissorList).hasSize(databaseSizeBeforeUpdate);

        // Validate the OrgaoEmissor in Elasticsearch
        verify(mockOrgaoEmissorSearchRepository, times(0)).save(orgaoEmissor);
    }

    @Test
    @Transactional
    void partialUpdateOrgaoEmissorWithPatch() throws Exception {
        // Initialize the database
        orgaoEmissorRepository.saveAndFlush(orgaoEmissor);

        int databaseSizeBeforeUpdate = orgaoEmissorRepository.findAll().size();

        // Update the orgaoEmissor using partial update
        OrgaoEmissor partialUpdatedOrgaoEmissor = new OrgaoEmissor();
        partialUpdatedOrgaoEmissor.setId(orgaoEmissor.getId());

        partialUpdatedOrgaoEmissor.sigla(UPDATED_SIGLA).nome(UPDATED_NOME);

        restOrgaoEmissorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOrgaoEmissor.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedOrgaoEmissor))
            )
            .andExpect(status().isOk());

        // Validate the OrgaoEmissor in the database
        List<OrgaoEmissor> orgaoEmissorList = orgaoEmissorRepository.findAll();
        assertThat(orgaoEmissorList).hasSize(databaseSizeBeforeUpdate);
        OrgaoEmissor testOrgaoEmissor = orgaoEmissorList.get(orgaoEmissorList.size() - 1);
        assertThat(testOrgaoEmissor.getSigla()).isEqualTo(UPDATED_SIGLA);
        assertThat(testOrgaoEmissor.getNome()).isEqualTo(UPDATED_NOME);
    }

    @Test
    @Transactional
    void fullUpdateOrgaoEmissorWithPatch() throws Exception {
        // Initialize the database
        orgaoEmissorRepository.saveAndFlush(orgaoEmissor);

        int databaseSizeBeforeUpdate = orgaoEmissorRepository.findAll().size();

        // Update the orgaoEmissor using partial update
        OrgaoEmissor partialUpdatedOrgaoEmissor = new OrgaoEmissor();
        partialUpdatedOrgaoEmissor.setId(orgaoEmissor.getId());

        partialUpdatedOrgaoEmissor.sigla(UPDATED_SIGLA).nome(UPDATED_NOME);

        restOrgaoEmissorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOrgaoEmissor.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedOrgaoEmissor))
            )
            .andExpect(status().isOk());

        // Validate the OrgaoEmissor in the database
        List<OrgaoEmissor> orgaoEmissorList = orgaoEmissorRepository.findAll();
        assertThat(orgaoEmissorList).hasSize(databaseSizeBeforeUpdate);
        OrgaoEmissor testOrgaoEmissor = orgaoEmissorList.get(orgaoEmissorList.size() - 1);
        assertThat(testOrgaoEmissor.getSigla()).isEqualTo(UPDATED_SIGLA);
        assertThat(testOrgaoEmissor.getNome()).isEqualTo(UPDATED_NOME);
    }

    @Test
    @Transactional
    void patchNonExistingOrgaoEmissor() throws Exception {
        int databaseSizeBeforeUpdate = orgaoEmissorRepository.findAll().size();
        orgaoEmissor.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOrgaoEmissorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, orgaoEmissor.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(orgaoEmissor))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrgaoEmissor in the database
        List<OrgaoEmissor> orgaoEmissorList = orgaoEmissorRepository.findAll();
        assertThat(orgaoEmissorList).hasSize(databaseSizeBeforeUpdate);

        // Validate the OrgaoEmissor in Elasticsearch
        verify(mockOrgaoEmissorSearchRepository, times(0)).save(orgaoEmissor);
    }

    @Test
    @Transactional
    void patchWithIdMismatchOrgaoEmissor() throws Exception {
        int databaseSizeBeforeUpdate = orgaoEmissorRepository.findAll().size();
        orgaoEmissor.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrgaoEmissorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(orgaoEmissor))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrgaoEmissor in the database
        List<OrgaoEmissor> orgaoEmissorList = orgaoEmissorRepository.findAll();
        assertThat(orgaoEmissorList).hasSize(databaseSizeBeforeUpdate);

        // Validate the OrgaoEmissor in Elasticsearch
        verify(mockOrgaoEmissorSearchRepository, times(0)).save(orgaoEmissor);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamOrgaoEmissor() throws Exception {
        int databaseSizeBeforeUpdate = orgaoEmissorRepository.findAll().size();
        orgaoEmissor.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrgaoEmissorMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(orgaoEmissor))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the OrgaoEmissor in the database
        List<OrgaoEmissor> orgaoEmissorList = orgaoEmissorRepository.findAll();
        assertThat(orgaoEmissorList).hasSize(databaseSizeBeforeUpdate);

        // Validate the OrgaoEmissor in Elasticsearch
        verify(mockOrgaoEmissorSearchRepository, times(0)).save(orgaoEmissor);
    }

    @Test
    @Transactional
    void deleteOrgaoEmissor() throws Exception {
        // Initialize the database
        orgaoEmissorRepository.saveAndFlush(orgaoEmissor);

        int databaseSizeBeforeDelete = orgaoEmissorRepository.findAll().size();

        // Delete the orgaoEmissor
        restOrgaoEmissorMockMvc
            .perform(delete(ENTITY_API_URL_ID, orgaoEmissor.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<OrgaoEmissor> orgaoEmissorList = orgaoEmissorRepository.findAll();
        assertThat(orgaoEmissorList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the OrgaoEmissor in Elasticsearch
        verify(mockOrgaoEmissorSearchRepository, times(1)).deleteById(orgaoEmissor.getId());
    }

    @Test
    @Transactional
    void searchOrgaoEmissor() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        orgaoEmissorRepository.saveAndFlush(orgaoEmissor);
        when(mockOrgaoEmissorSearchRepository.search(queryStringQuery("id:" + orgaoEmissor.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(orgaoEmissor), PageRequest.of(0, 1), 1));

        // Search the orgaoEmissor
        restOrgaoEmissorMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + orgaoEmissor.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(orgaoEmissor.getId().intValue())))
            .andExpect(jsonPath("$.[*].sigla").value(hasItem(DEFAULT_SIGLA)))
            .andExpect(jsonPath("$.[*].nome").value(hasItem(DEFAULT_NOME)));
    }
}
