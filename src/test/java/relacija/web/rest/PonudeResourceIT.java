package relacija.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import relacija.IntegrationTest;
import relacija.domain.Ponude;
import relacija.repository.PonudeRepository;

/**
 * Integration tests for the {@link PonudeResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PonudeResourceIT {

    private static final Integer DEFAULT_SIFRA_POSTUPKA = 1;
    private static final Integer UPDATED_SIFRA_POSTUPKA = 2;

    private static final String DEFAULT_NAZIV_LIJEKA = "AAAAAAAAAA";
    private static final String UPDATED_NAZIV_LIJEKA = "BBBBBBBBBB";

    private static final String DEFAULT_IZNOS = "AAAAAAAAAA";
    private static final String UPDATED_IZNOS = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/ponudes";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PonudeRepository ponudeRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPonudeMockMvc;

    private Ponude ponude;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Ponude createEntity(EntityManager em) {
        Ponude ponude = new Ponude().sifraPostupka(DEFAULT_SIFRA_POSTUPKA).nazivLijeka(DEFAULT_NAZIV_LIJEKA).iznos(DEFAULT_IZNOS);
        return ponude;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Ponude createUpdatedEntity(EntityManager em) {
        Ponude ponude = new Ponude().sifraPostupka(UPDATED_SIFRA_POSTUPKA).nazivLijeka(UPDATED_NAZIV_LIJEKA).iznos(UPDATED_IZNOS);
        return ponude;
    }

    @BeforeEach
    public void initTest() {
        ponude = createEntity(em);
    }

    @Test
    @Transactional
    void createPonude() throws Exception {
        int databaseSizeBeforeCreate = ponudeRepository.findAll().size();
        // Create the Ponude
        restPonudeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(ponude)))
            .andExpect(status().isCreated());

        // Validate the Ponude in the database
        List<Ponude> ponudeList = ponudeRepository.findAll();
        assertThat(ponudeList).hasSize(databaseSizeBeforeCreate + 1);
        Ponude testPonude = ponudeList.get(ponudeList.size() - 1);
        assertThat(testPonude.getSifraPostupka()).isEqualTo(DEFAULT_SIFRA_POSTUPKA);
        assertThat(testPonude.getNazivLijeka()).isEqualTo(DEFAULT_NAZIV_LIJEKA);
        assertThat(testPonude.getIznos()).isEqualTo(DEFAULT_IZNOS);
    }

    @Test
    @Transactional
    void createPonudeWithExistingId() throws Exception {
        // Create the Ponude with an existing ID
        ponude.setId(1L);

        int databaseSizeBeforeCreate = ponudeRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPonudeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(ponude)))
            .andExpect(status().isBadRequest());

        // Validate the Ponude in the database
        List<Ponude> ponudeList = ponudeRepository.findAll();
        assertThat(ponudeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllPonudes() throws Exception {
        // Initialize the database
        ponudeRepository.saveAndFlush(ponude);

        // Get all the ponudeList
        restPonudeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(ponude.getId().intValue())))
            .andExpect(jsonPath("$.[*].sifraPostupka").value(hasItem(DEFAULT_SIFRA_POSTUPKA)))
            .andExpect(jsonPath("$.[*].nazivLijeka").value(hasItem(DEFAULT_NAZIV_LIJEKA)))
            .andExpect(jsonPath("$.[*].iznos").value(hasItem(DEFAULT_IZNOS)));
    }

    @Test
    @Transactional
    void getPonude() throws Exception {
        // Initialize the database
        ponudeRepository.saveAndFlush(ponude);

        // Get the ponude
        restPonudeMockMvc
            .perform(get(ENTITY_API_URL_ID, ponude.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(ponude.getId().intValue()))
            .andExpect(jsonPath("$.sifraPostupka").value(DEFAULT_SIFRA_POSTUPKA))
            .andExpect(jsonPath("$.nazivLijeka").value(DEFAULT_NAZIV_LIJEKA))
            .andExpect(jsonPath("$.iznos").value(DEFAULT_IZNOS));
    }

    @Test
    @Transactional
    void getNonExistingPonude() throws Exception {
        // Get the ponude
        restPonudeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewPonude() throws Exception {
        // Initialize the database
        ponudeRepository.saveAndFlush(ponude);

        int databaseSizeBeforeUpdate = ponudeRepository.findAll().size();

        // Update the ponude
        Ponude updatedPonude = ponudeRepository.findById(ponude.getId()).get();
        // Disconnect from session so that the updates on updatedPonude are not directly saved in db
        em.detach(updatedPonude);
        updatedPonude.sifraPostupka(UPDATED_SIFRA_POSTUPKA).nazivLijeka(UPDATED_NAZIV_LIJEKA).iznos(UPDATED_IZNOS);

        restPonudeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedPonude.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedPonude))
            )
            .andExpect(status().isOk());

        // Validate the Ponude in the database
        List<Ponude> ponudeList = ponudeRepository.findAll();
        assertThat(ponudeList).hasSize(databaseSizeBeforeUpdate);
        Ponude testPonude = ponudeList.get(ponudeList.size() - 1);
        assertThat(testPonude.getSifraPostupka()).isEqualTo(UPDATED_SIFRA_POSTUPKA);
        assertThat(testPonude.getNazivLijeka()).isEqualTo(UPDATED_NAZIV_LIJEKA);
        assertThat(testPonude.getIznos()).isEqualTo(UPDATED_IZNOS);
    }

    @Test
    @Transactional
    void putNonExistingPonude() throws Exception {
        int databaseSizeBeforeUpdate = ponudeRepository.findAll().size();
        ponude.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPonudeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, ponude.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(ponude))
            )
            .andExpect(status().isBadRequest());

        // Validate the Ponude in the database
        List<Ponude> ponudeList = ponudeRepository.findAll();
        assertThat(ponudeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPonude() throws Exception {
        int databaseSizeBeforeUpdate = ponudeRepository.findAll().size();
        ponude.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPonudeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(ponude))
            )
            .andExpect(status().isBadRequest());

        // Validate the Ponude in the database
        List<Ponude> ponudeList = ponudeRepository.findAll();
        assertThat(ponudeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPonude() throws Exception {
        int databaseSizeBeforeUpdate = ponudeRepository.findAll().size();
        ponude.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPonudeMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(ponude)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Ponude in the database
        List<Ponude> ponudeList = ponudeRepository.findAll();
        assertThat(ponudeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePonudeWithPatch() throws Exception {
        // Initialize the database
        ponudeRepository.saveAndFlush(ponude);

        int databaseSizeBeforeUpdate = ponudeRepository.findAll().size();

        // Update the ponude using partial update
        Ponude partialUpdatedPonude = new Ponude();
        partialUpdatedPonude.setId(ponude.getId());

        partialUpdatedPonude.sifraPostupka(UPDATED_SIFRA_POSTUPKA);

        restPonudeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPonude.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPonude))
            )
            .andExpect(status().isOk());

        // Validate the Ponude in the database
        List<Ponude> ponudeList = ponudeRepository.findAll();
        assertThat(ponudeList).hasSize(databaseSizeBeforeUpdate);
        Ponude testPonude = ponudeList.get(ponudeList.size() - 1);
        assertThat(testPonude.getSifraPostupka()).isEqualTo(UPDATED_SIFRA_POSTUPKA);
        assertThat(testPonude.getNazivLijeka()).isEqualTo(DEFAULT_NAZIV_LIJEKA);
        assertThat(testPonude.getIznos()).isEqualTo(DEFAULT_IZNOS);
    }

    @Test
    @Transactional
    void fullUpdatePonudeWithPatch() throws Exception {
        // Initialize the database
        ponudeRepository.saveAndFlush(ponude);

        int databaseSizeBeforeUpdate = ponudeRepository.findAll().size();

        // Update the ponude using partial update
        Ponude partialUpdatedPonude = new Ponude();
        partialUpdatedPonude.setId(ponude.getId());

        partialUpdatedPonude.sifraPostupka(UPDATED_SIFRA_POSTUPKA).nazivLijeka(UPDATED_NAZIV_LIJEKA).iznos(UPDATED_IZNOS);

        restPonudeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPonude.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPonude))
            )
            .andExpect(status().isOk());

        // Validate the Ponude in the database
        List<Ponude> ponudeList = ponudeRepository.findAll();
        assertThat(ponudeList).hasSize(databaseSizeBeforeUpdate);
        Ponude testPonude = ponudeList.get(ponudeList.size() - 1);
        assertThat(testPonude.getSifraPostupka()).isEqualTo(UPDATED_SIFRA_POSTUPKA);
        assertThat(testPonude.getNazivLijeka()).isEqualTo(UPDATED_NAZIV_LIJEKA);
        assertThat(testPonude.getIznos()).isEqualTo(UPDATED_IZNOS);
    }

    @Test
    @Transactional
    void patchNonExistingPonude() throws Exception {
        int databaseSizeBeforeUpdate = ponudeRepository.findAll().size();
        ponude.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPonudeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, ponude.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(ponude))
            )
            .andExpect(status().isBadRequest());

        // Validate the Ponude in the database
        List<Ponude> ponudeList = ponudeRepository.findAll();
        assertThat(ponudeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPonude() throws Exception {
        int databaseSizeBeforeUpdate = ponudeRepository.findAll().size();
        ponude.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPonudeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(ponude))
            )
            .andExpect(status().isBadRequest());

        // Validate the Ponude in the database
        List<Ponude> ponudeList = ponudeRepository.findAll();
        assertThat(ponudeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPonude() throws Exception {
        int databaseSizeBeforeUpdate = ponudeRepository.findAll().size();
        ponude.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPonudeMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(ponude)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Ponude in the database
        List<Ponude> ponudeList = ponudeRepository.findAll();
        assertThat(ponudeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePonude() throws Exception {
        // Initialize the database
        ponudeRepository.saveAndFlush(ponude);

        int databaseSizeBeforeDelete = ponudeRepository.findAll().size();

        // Delete the ponude
        restPonudeMockMvc
            .perform(delete(ENTITY_API_URL_ID, ponude.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Ponude> ponudeList = ponudeRepository.findAll();
        assertThat(ponudeList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
