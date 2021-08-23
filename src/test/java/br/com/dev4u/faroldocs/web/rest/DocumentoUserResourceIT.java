package br.com.dev4u.faroldocs.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import br.com.dev4u.faroldocs.IntegrationTest;
import br.com.dev4u.faroldocs.domain.DocumentoUser;
import br.com.dev4u.faroldocs.repository.DocumentoUserRepository;
import br.com.dev4u.faroldocs.repository.search.DocumentoUserSearchRepository;
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
 * Integration tests for the {@link DocumentoUserResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class DocumentoUserResourceIT {

    private static final String ENTITY_API_URL = "/api/documento-users";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/_search/documento-users";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private DocumentoUserRepository documentoUserRepository;

    /**
     * This repository is mocked in the br.com.dev4u.faroldocs.repository.search test package.
     *
     * @see br.com.dev4u.faroldocs.repository.search.DocumentoUserSearchRepositoryMockConfiguration
     */
    @Autowired
    private DocumentoUserSearchRepository mockDocumentoUserSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDocumentoUserMockMvc;

    private DocumentoUser documentoUser;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DocumentoUser createEntity(EntityManager em) {
        DocumentoUser documentoUser = new DocumentoUser();
        return documentoUser;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DocumentoUser createUpdatedEntity(EntityManager em) {
        DocumentoUser documentoUser = new DocumentoUser();
        return documentoUser;
    }

    @BeforeEach
    public void initTest() {
        documentoUser = createEntity(em);
    }

    @Test
    @Transactional
    void createDocumentoUser() throws Exception {
        int databaseSizeBeforeCreate = documentoUserRepository.findAll().size();
        // Create the DocumentoUser
        restDocumentoUserMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(documentoUser)))
            .andExpect(status().isCreated());

        // Validate the DocumentoUser in the database
        List<DocumentoUser> documentoUserList = documentoUserRepository.findAll();
        assertThat(documentoUserList).hasSize(databaseSizeBeforeCreate + 1);
        DocumentoUser testDocumentoUser = documentoUserList.get(documentoUserList.size() - 1);

        // Validate the DocumentoUser in Elasticsearch
        verify(mockDocumentoUserSearchRepository, times(1)).save(testDocumentoUser);
    }

    @Test
    @Transactional
    void createDocumentoUserWithExistingId() throws Exception {
        // Create the DocumentoUser with an existing ID
        documentoUser.setId(1L);

        int databaseSizeBeforeCreate = documentoUserRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restDocumentoUserMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(documentoUser)))
            .andExpect(status().isBadRequest());

        // Validate the DocumentoUser in the database
        List<DocumentoUser> documentoUserList = documentoUserRepository.findAll();
        assertThat(documentoUserList).hasSize(databaseSizeBeforeCreate);

        // Validate the DocumentoUser in Elasticsearch
        verify(mockDocumentoUserSearchRepository, times(0)).save(documentoUser);
    }

    @Test
    @Transactional
    void getAllDocumentoUsers() throws Exception {
        // Initialize the database
        documentoUserRepository.saveAndFlush(documentoUser);

        // Get all the documentoUserList
        restDocumentoUserMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(documentoUser.getId().intValue())));
    }

    @Test
    @Transactional
    void getDocumentoUser() throws Exception {
        // Initialize the database
        documentoUserRepository.saveAndFlush(documentoUser);

        // Get the documentoUser
        restDocumentoUserMockMvc
            .perform(get(ENTITY_API_URL_ID, documentoUser.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(documentoUser.getId().intValue()));
    }

    @Test
    @Transactional
    void getNonExistingDocumentoUser() throws Exception {
        // Get the documentoUser
        restDocumentoUserMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewDocumentoUser() throws Exception {
        // Initialize the database
        documentoUserRepository.saveAndFlush(documentoUser);

        int databaseSizeBeforeUpdate = documentoUserRepository.findAll().size();

        // Update the documentoUser
        DocumentoUser updatedDocumentoUser = documentoUserRepository.findById(documentoUser.getId()).get();
        // Disconnect from session so that the updates on updatedDocumentoUser are not directly saved in db
        em.detach(updatedDocumentoUser);

        restDocumentoUserMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedDocumentoUser.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedDocumentoUser))
            )
            .andExpect(status().isOk());

        // Validate the DocumentoUser in the database
        List<DocumentoUser> documentoUserList = documentoUserRepository.findAll();
        assertThat(documentoUserList).hasSize(databaseSizeBeforeUpdate);
        DocumentoUser testDocumentoUser = documentoUserList.get(documentoUserList.size() - 1);

        // Validate the DocumentoUser in Elasticsearch
        verify(mockDocumentoUserSearchRepository).save(testDocumentoUser);
    }

    @Test
    @Transactional
    void putNonExistingDocumentoUser() throws Exception {
        int databaseSizeBeforeUpdate = documentoUserRepository.findAll().size();
        documentoUser.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDocumentoUserMockMvc
            .perform(
                put(ENTITY_API_URL_ID, documentoUser.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(documentoUser))
            )
            .andExpect(status().isBadRequest());

        // Validate the DocumentoUser in the database
        List<DocumentoUser> documentoUserList = documentoUserRepository.findAll();
        assertThat(documentoUserList).hasSize(databaseSizeBeforeUpdate);

        // Validate the DocumentoUser in Elasticsearch
        verify(mockDocumentoUserSearchRepository, times(0)).save(documentoUser);
    }

    @Test
    @Transactional
    void putWithIdMismatchDocumentoUser() throws Exception {
        int databaseSizeBeforeUpdate = documentoUserRepository.findAll().size();
        documentoUser.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDocumentoUserMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(documentoUser))
            )
            .andExpect(status().isBadRequest());

        // Validate the DocumentoUser in the database
        List<DocumentoUser> documentoUserList = documentoUserRepository.findAll();
        assertThat(documentoUserList).hasSize(databaseSizeBeforeUpdate);

        // Validate the DocumentoUser in Elasticsearch
        verify(mockDocumentoUserSearchRepository, times(0)).save(documentoUser);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamDocumentoUser() throws Exception {
        int databaseSizeBeforeUpdate = documentoUserRepository.findAll().size();
        documentoUser.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDocumentoUserMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(documentoUser)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the DocumentoUser in the database
        List<DocumentoUser> documentoUserList = documentoUserRepository.findAll();
        assertThat(documentoUserList).hasSize(databaseSizeBeforeUpdate);

        // Validate the DocumentoUser in Elasticsearch
        verify(mockDocumentoUserSearchRepository, times(0)).save(documentoUser);
    }

    @Test
    @Transactional
    void partialUpdateDocumentoUserWithPatch() throws Exception {
        // Initialize the database
        documentoUserRepository.saveAndFlush(documentoUser);

        int databaseSizeBeforeUpdate = documentoUserRepository.findAll().size();

        // Update the documentoUser using partial update
        DocumentoUser partialUpdatedDocumentoUser = new DocumentoUser();
        partialUpdatedDocumentoUser.setId(documentoUser.getId());

        restDocumentoUserMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDocumentoUser.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDocumentoUser))
            )
            .andExpect(status().isOk());

        // Validate the DocumentoUser in the database
        List<DocumentoUser> documentoUserList = documentoUserRepository.findAll();
        assertThat(documentoUserList).hasSize(databaseSizeBeforeUpdate);
        DocumentoUser testDocumentoUser = documentoUserList.get(documentoUserList.size() - 1);
    }

    @Test
    @Transactional
    void fullUpdateDocumentoUserWithPatch() throws Exception {
        // Initialize the database
        documentoUserRepository.saveAndFlush(documentoUser);

        int databaseSizeBeforeUpdate = documentoUserRepository.findAll().size();

        // Update the documentoUser using partial update
        DocumentoUser partialUpdatedDocumentoUser = new DocumentoUser();
        partialUpdatedDocumentoUser.setId(documentoUser.getId());

        restDocumentoUserMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDocumentoUser.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDocumentoUser))
            )
            .andExpect(status().isOk());

        // Validate the DocumentoUser in the database
        List<DocumentoUser> documentoUserList = documentoUserRepository.findAll();
        assertThat(documentoUserList).hasSize(databaseSizeBeforeUpdate);
        DocumentoUser testDocumentoUser = documentoUserList.get(documentoUserList.size() - 1);
    }

    @Test
    @Transactional
    void patchNonExistingDocumentoUser() throws Exception {
        int databaseSizeBeforeUpdate = documentoUserRepository.findAll().size();
        documentoUser.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDocumentoUserMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, documentoUser.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(documentoUser))
            )
            .andExpect(status().isBadRequest());

        // Validate the DocumentoUser in the database
        List<DocumentoUser> documentoUserList = documentoUserRepository.findAll();
        assertThat(documentoUserList).hasSize(databaseSizeBeforeUpdate);

        // Validate the DocumentoUser in Elasticsearch
        verify(mockDocumentoUserSearchRepository, times(0)).save(documentoUser);
    }

    @Test
    @Transactional
    void patchWithIdMismatchDocumentoUser() throws Exception {
        int databaseSizeBeforeUpdate = documentoUserRepository.findAll().size();
        documentoUser.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDocumentoUserMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(documentoUser))
            )
            .andExpect(status().isBadRequest());

        // Validate the DocumentoUser in the database
        List<DocumentoUser> documentoUserList = documentoUserRepository.findAll();
        assertThat(documentoUserList).hasSize(databaseSizeBeforeUpdate);

        // Validate the DocumentoUser in Elasticsearch
        verify(mockDocumentoUserSearchRepository, times(0)).save(documentoUser);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamDocumentoUser() throws Exception {
        int databaseSizeBeforeUpdate = documentoUserRepository.findAll().size();
        documentoUser.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDocumentoUserMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(documentoUser))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the DocumentoUser in the database
        List<DocumentoUser> documentoUserList = documentoUserRepository.findAll();
        assertThat(documentoUserList).hasSize(databaseSizeBeforeUpdate);

        // Validate the DocumentoUser in Elasticsearch
        verify(mockDocumentoUserSearchRepository, times(0)).save(documentoUser);
    }

    @Test
    @Transactional
    void deleteDocumentoUser() throws Exception {
        // Initialize the database
        documentoUserRepository.saveAndFlush(documentoUser);

        int databaseSizeBeforeDelete = documentoUserRepository.findAll().size();

        // Delete the documentoUser
        restDocumentoUserMockMvc
            .perform(delete(ENTITY_API_URL_ID, documentoUser.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<DocumentoUser> documentoUserList = documentoUserRepository.findAll();
        assertThat(documentoUserList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the DocumentoUser in Elasticsearch
        verify(mockDocumentoUserSearchRepository, times(1)).deleteById(documentoUser.getId());
    }

    @Test
    @Transactional
    void searchDocumentoUser() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        documentoUserRepository.saveAndFlush(documentoUser);
        when(mockDocumentoUserSearchRepository.search(queryStringQuery("id:" + documentoUser.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(documentoUser), PageRequest.of(0, 1), 1));

        // Search the documentoUser
        restDocumentoUserMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + documentoUser.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(documentoUser.getId().intValue())));
    }
}
