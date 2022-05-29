package com.wf.pronounce.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import com.wf.pronounce.IntegrationTest;
import com.wf.pronounce.domain.Pronounce;
import com.wf.pronounce.repository.EntityManager;
import com.wf.pronounce.repository.PronounceRepository;
import com.wf.pronounce.service.dto.PronounceDTO;
import com.wf.pronounce.service.mapper.PronounceMapper;
import java.time.Duration;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.util.Base64Utils;

/**
 * Integration tests for the {@link PronounceResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class PronounceResourceIT {

    private static final String DEFAULT_LOGIN = "AAAAAAAAAA";
    private static final String UPDATED_LOGIN = "BBBBBBBBBB";

    private static final String DEFAULT_FIRST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FIRST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_LAST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_LAST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_PREFERRED_NAME = "AAAAAAAAAA";
    private static final String UPDATED_PREFERRED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_COUNTRY = "AAAAAAAAAA";
    private static final String UPDATED_COUNTRY = "BBBBBBBBBB";

    private static final String DEFAULT_LANGUAGE = "AAAAAAAAAA";
    private static final String UPDATED_LANGUAGE = "BBBBBBBBBB";

    private static final byte[] DEFAULT_PRONUNCIATION = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_PRONUNCIATION = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_PRONUNCIATION_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_PRONUNCIATION_CONTENT_TYPE = "image/png";

    private static final String ENTITY_API_URL = "/api/pronounces";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PronounceRepository pronounceRepository;

    @Autowired
    private PronounceMapper pronounceMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private Pronounce pronounce;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Pronounce createEntity(EntityManager em) {
        Pronounce pronounce = new Pronounce()
            .login(DEFAULT_LOGIN)
            .firstName(DEFAULT_FIRST_NAME)
            .lastName(DEFAULT_LAST_NAME)
            .preferredName(DEFAULT_PREFERRED_NAME)
            .country(DEFAULT_COUNTRY)
            .language(DEFAULT_LANGUAGE)
            .pronunciation(DEFAULT_PRONUNCIATION)
            .pronunciationContentType(DEFAULT_PRONUNCIATION_CONTENT_TYPE);
        return pronounce;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Pronounce createUpdatedEntity(EntityManager em) {
        Pronounce pronounce = new Pronounce()
            .login(UPDATED_LOGIN)
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .preferredName(UPDATED_PREFERRED_NAME)
            .country(UPDATED_COUNTRY)
            .language(UPDATED_LANGUAGE)
            .pronunciation(UPDATED_PRONUNCIATION)
            .pronunciationContentType(UPDATED_PRONUNCIATION_CONTENT_TYPE);
        return pronounce;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(Pronounce.class).block();
        } catch (Exception e) {
            // It can fail, if other entities are still referring this - it will be removed later.
        }
    }

    @AfterEach
    public void cleanup() {
        deleteEntities(em);
    }

    @BeforeEach
    public void initTest() {
        deleteEntities(em);
        pronounce = createEntity(em);
    }

    @Test
    void createPronounce() throws Exception {
        int databaseSizeBeforeCreate = pronounceRepository.findAll().collectList().block().size();
        // Create the Pronounce
        PronounceDTO pronounceDTO = pronounceMapper.toDto(pronounce);
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(pronounceDTO))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the Pronounce in the database
        List<Pronounce> pronounceList = pronounceRepository.findAll().collectList().block();
        assertThat(pronounceList).hasSize(databaseSizeBeforeCreate + 1);
        Pronounce testPronounce = pronounceList.get(pronounceList.size() - 1);
        assertThat(testPronounce.getLogin()).isEqualTo(DEFAULT_LOGIN);
        assertThat(testPronounce.getFirstName()).isEqualTo(DEFAULT_FIRST_NAME);
        assertThat(testPronounce.getLastName()).isEqualTo(DEFAULT_LAST_NAME);
        assertThat(testPronounce.getPreferredName()).isEqualTo(DEFAULT_PREFERRED_NAME);
        assertThat(testPronounce.getCountry()).isEqualTo(DEFAULT_COUNTRY);
        assertThat(testPronounce.getLanguage()).isEqualTo(DEFAULT_LANGUAGE);
        assertThat(testPronounce.getPronunciation()).isEqualTo(DEFAULT_PRONUNCIATION);
        assertThat(testPronounce.getPronunciationContentType()).isEqualTo(DEFAULT_PRONUNCIATION_CONTENT_TYPE);
    }

    @Test
    void createPronounceWithExistingId() throws Exception {
        // Create the Pronounce with an existing ID
        pronounce.setId(1L);
        PronounceDTO pronounceDTO = pronounceMapper.toDto(pronounce);

        int databaseSizeBeforeCreate = pronounceRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(pronounceDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Pronounce in the database
        List<Pronounce> pronounceList = pronounceRepository.findAll().collectList().block();
        assertThat(pronounceList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void getAllPronounces() {
        // Initialize the database
        pronounceRepository.save(pronounce).block();

        // Get all the pronounceList
        webTestClient
            .get()
            .uri(ENTITY_API_URL + "?sort=id,desc")
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.[*].id")
            .value(hasItem(pronounce.getId().intValue()))
            .jsonPath("$.[*].login")
            .value(hasItem(DEFAULT_LOGIN))
            .jsonPath("$.[*].firstName")
            .value(hasItem(DEFAULT_FIRST_NAME))
            .jsonPath("$.[*].lastName")
            .value(hasItem(DEFAULT_LAST_NAME))
            .jsonPath("$.[*].preferredName")
            .value(hasItem(DEFAULT_PREFERRED_NAME))
            .jsonPath("$.[*].country")
            .value(hasItem(DEFAULT_COUNTRY))
            .jsonPath("$.[*].language")
            .value(hasItem(DEFAULT_LANGUAGE))
            .jsonPath("$.[*].pronunciationContentType")
            .value(hasItem(DEFAULT_PRONUNCIATION_CONTENT_TYPE))
            .jsonPath("$.[*].pronunciation")
            .value(hasItem(Base64Utils.encodeToString(DEFAULT_PRONUNCIATION)));
    }

    @Test
    void getPronounce() {
        // Initialize the database
        pronounceRepository.save(pronounce).block();

        // Get the pronounce
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, pronounce.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(pronounce.getId().intValue()))
            .jsonPath("$.login")
            .value(is(DEFAULT_LOGIN))
            .jsonPath("$.firstName")
            .value(is(DEFAULT_FIRST_NAME))
            .jsonPath("$.lastName")
            .value(is(DEFAULT_LAST_NAME))
            .jsonPath("$.preferredName")
            .value(is(DEFAULT_PREFERRED_NAME))
            .jsonPath("$.country")
            .value(is(DEFAULT_COUNTRY))
            .jsonPath("$.language")
            .value(is(DEFAULT_LANGUAGE))
            .jsonPath("$.pronunciationContentType")
            .value(is(DEFAULT_PRONUNCIATION_CONTENT_TYPE))
            .jsonPath("$.pronunciation")
            .value(is(Base64Utils.encodeToString(DEFAULT_PRONUNCIATION)));
    }

    @Test
    void getNonExistingPronounce() {
        // Get the pronounce
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putNewPronounce() throws Exception {
        // Initialize the database
        pronounceRepository.save(pronounce).block();

        int databaseSizeBeforeUpdate = pronounceRepository.findAll().collectList().block().size();

        // Update the pronounce
        Pronounce updatedPronounce = pronounceRepository.findById(pronounce.getId()).block();
        updatedPronounce
            .login(UPDATED_LOGIN)
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .preferredName(UPDATED_PREFERRED_NAME)
            .country(UPDATED_COUNTRY)
            .language(UPDATED_LANGUAGE)
            .pronunciation(UPDATED_PRONUNCIATION)
            .pronunciationContentType(UPDATED_PRONUNCIATION_CONTENT_TYPE);
        PronounceDTO pronounceDTO = pronounceMapper.toDto(updatedPronounce);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, pronounceDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(pronounceDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Pronounce in the database
        List<Pronounce> pronounceList = pronounceRepository.findAll().collectList().block();
        assertThat(pronounceList).hasSize(databaseSizeBeforeUpdate);
        Pronounce testPronounce = pronounceList.get(pronounceList.size() - 1);
        assertThat(testPronounce.getLogin()).isEqualTo(UPDATED_LOGIN);
        assertThat(testPronounce.getFirstName()).isEqualTo(UPDATED_FIRST_NAME);
        assertThat(testPronounce.getLastName()).isEqualTo(UPDATED_LAST_NAME);
        assertThat(testPronounce.getPreferredName()).isEqualTo(UPDATED_PREFERRED_NAME);
        assertThat(testPronounce.getCountry()).isEqualTo(UPDATED_COUNTRY);
        assertThat(testPronounce.getLanguage()).isEqualTo(UPDATED_LANGUAGE);
        assertThat(testPronounce.getPronunciation()).isEqualTo(UPDATED_PRONUNCIATION);
        assertThat(testPronounce.getPronunciationContentType()).isEqualTo(UPDATED_PRONUNCIATION_CONTENT_TYPE);
    }

    @Test
    void putNonExistingPronounce() throws Exception {
        int databaseSizeBeforeUpdate = pronounceRepository.findAll().collectList().block().size();
        pronounce.setId(count.incrementAndGet());

        // Create the Pronounce
        PronounceDTO pronounceDTO = pronounceMapper.toDto(pronounce);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, pronounceDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(pronounceDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Pronounce in the database
        List<Pronounce> pronounceList = pronounceRepository.findAll().collectList().block();
        assertThat(pronounceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchPronounce() throws Exception {
        int databaseSizeBeforeUpdate = pronounceRepository.findAll().collectList().block().size();
        pronounce.setId(count.incrementAndGet());

        // Create the Pronounce
        PronounceDTO pronounceDTO = pronounceMapper.toDto(pronounce);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(pronounceDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Pronounce in the database
        List<Pronounce> pronounceList = pronounceRepository.findAll().collectList().block();
        assertThat(pronounceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamPronounce() throws Exception {
        int databaseSizeBeforeUpdate = pronounceRepository.findAll().collectList().block().size();
        pronounce.setId(count.incrementAndGet());

        // Create the Pronounce
        PronounceDTO pronounceDTO = pronounceMapper.toDto(pronounce);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(pronounceDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Pronounce in the database
        List<Pronounce> pronounceList = pronounceRepository.findAll().collectList().block();
        assertThat(pronounceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdatePronounceWithPatch() throws Exception {
        // Initialize the database
        pronounceRepository.save(pronounce).block();

        int databaseSizeBeforeUpdate = pronounceRepository.findAll().collectList().block().size();

        // Update the pronounce using partial update
        Pronounce partialUpdatedPronounce = new Pronounce();
        partialUpdatedPronounce.setId(pronounce.getId());

        partialUpdatedPronounce.login(UPDATED_LOGIN).firstName(UPDATED_FIRST_NAME).country(UPDATED_COUNTRY).language(UPDATED_LANGUAGE);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedPronounce.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedPronounce))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Pronounce in the database
        List<Pronounce> pronounceList = pronounceRepository.findAll().collectList().block();
        assertThat(pronounceList).hasSize(databaseSizeBeforeUpdate);
        Pronounce testPronounce = pronounceList.get(pronounceList.size() - 1);
        assertThat(testPronounce.getLogin()).isEqualTo(UPDATED_LOGIN);
        assertThat(testPronounce.getFirstName()).isEqualTo(UPDATED_FIRST_NAME);
        assertThat(testPronounce.getLastName()).isEqualTo(DEFAULT_LAST_NAME);
        assertThat(testPronounce.getPreferredName()).isEqualTo(DEFAULT_PREFERRED_NAME);
        assertThat(testPronounce.getCountry()).isEqualTo(UPDATED_COUNTRY);
        assertThat(testPronounce.getLanguage()).isEqualTo(UPDATED_LANGUAGE);
        assertThat(testPronounce.getPronunciation()).isEqualTo(DEFAULT_PRONUNCIATION);
        assertThat(testPronounce.getPronunciationContentType()).isEqualTo(DEFAULT_PRONUNCIATION_CONTENT_TYPE);
    }

    @Test
    void fullUpdatePronounceWithPatch() throws Exception {
        // Initialize the database
        pronounceRepository.save(pronounce).block();

        int databaseSizeBeforeUpdate = pronounceRepository.findAll().collectList().block().size();

        // Update the pronounce using partial update
        Pronounce partialUpdatedPronounce = new Pronounce();
        partialUpdatedPronounce.setId(pronounce.getId());

        partialUpdatedPronounce
            .login(UPDATED_LOGIN)
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .preferredName(UPDATED_PREFERRED_NAME)
            .country(UPDATED_COUNTRY)
            .language(UPDATED_LANGUAGE)
            .pronunciation(UPDATED_PRONUNCIATION)
            .pronunciationContentType(UPDATED_PRONUNCIATION_CONTENT_TYPE);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedPronounce.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedPronounce))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Pronounce in the database
        List<Pronounce> pronounceList = pronounceRepository.findAll().collectList().block();
        assertThat(pronounceList).hasSize(databaseSizeBeforeUpdate);
        Pronounce testPronounce = pronounceList.get(pronounceList.size() - 1);
        assertThat(testPronounce.getLogin()).isEqualTo(UPDATED_LOGIN);
        assertThat(testPronounce.getFirstName()).isEqualTo(UPDATED_FIRST_NAME);
        assertThat(testPronounce.getLastName()).isEqualTo(UPDATED_LAST_NAME);
        assertThat(testPronounce.getPreferredName()).isEqualTo(UPDATED_PREFERRED_NAME);
        assertThat(testPronounce.getCountry()).isEqualTo(UPDATED_COUNTRY);
        assertThat(testPronounce.getLanguage()).isEqualTo(UPDATED_LANGUAGE);
        assertThat(testPronounce.getPronunciation()).isEqualTo(UPDATED_PRONUNCIATION);
        assertThat(testPronounce.getPronunciationContentType()).isEqualTo(UPDATED_PRONUNCIATION_CONTENT_TYPE);
    }

    @Test
    void patchNonExistingPronounce() throws Exception {
        int databaseSizeBeforeUpdate = pronounceRepository.findAll().collectList().block().size();
        pronounce.setId(count.incrementAndGet());

        // Create the Pronounce
        PronounceDTO pronounceDTO = pronounceMapper.toDto(pronounce);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, pronounceDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(pronounceDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Pronounce in the database
        List<Pronounce> pronounceList = pronounceRepository.findAll().collectList().block();
        assertThat(pronounceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchPronounce() throws Exception {
        int databaseSizeBeforeUpdate = pronounceRepository.findAll().collectList().block().size();
        pronounce.setId(count.incrementAndGet());

        // Create the Pronounce
        PronounceDTO pronounceDTO = pronounceMapper.toDto(pronounce);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(pronounceDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Pronounce in the database
        List<Pronounce> pronounceList = pronounceRepository.findAll().collectList().block();
        assertThat(pronounceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamPronounce() throws Exception {
        int databaseSizeBeforeUpdate = pronounceRepository.findAll().collectList().block().size();
        pronounce.setId(count.incrementAndGet());

        // Create the Pronounce
        PronounceDTO pronounceDTO = pronounceMapper.toDto(pronounce);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(pronounceDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Pronounce in the database
        List<Pronounce> pronounceList = pronounceRepository.findAll().collectList().block();
        assertThat(pronounceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deletePronounce() {
        // Initialize the database
        pronounceRepository.save(pronounce).block();

        int databaseSizeBeforeDelete = pronounceRepository.findAll().collectList().block().size();

        // Delete the pronounce
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, pronounce.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<Pronounce> pronounceList = pronounceRepository.findAll().collectList().block();
        assertThat(pronounceList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
