package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.SioeDevApp;

import com.mycompany.myapp.domain.Peticion;
import com.mycompany.myapp.domain.Peticionario;
import com.mycompany.myapp.repository.PeticionRepository;
import com.mycompany.myapp.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the PeticionResource REST controller.
 *
 * @see PeticionResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = SioeDevApp.class)
public class PeticionResourceIntTest {

    private static final String DEFAULT_NUMERO_PETICION = "AAAAAAAAAA";
    private static final String UPDATED_NUMERO_PETICION = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_FECHA = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_FECHA = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_ACTO_CERTIFICAR = "AAAAAAAAAA";
    private static final String UPDATED_ACTO_CERTIFICAR = "BBBBBBBBBB";

    private static final String DEFAULT_RESPONSABLE = "AAAAAAAAAA";
    private static final String UPDATED_RESPONSABLE = "BBBBBBBBBB";

    @Autowired
    private PeticionRepository peticionRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restPeticionMockMvc;

    private Peticion peticion;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        PeticionResource peticionResource = new PeticionResource(peticionRepository);
        this.restPeticionMockMvc = MockMvcBuilders.standaloneSetup(peticionResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Peticion createEntity(EntityManager em) {
        Peticion peticion = new Peticion()
            .numero_peticion(DEFAULT_NUMERO_PETICION)
            .fecha(DEFAULT_FECHA)
            .acto_certificar(DEFAULT_ACTO_CERTIFICAR)
            .responsable(DEFAULT_RESPONSABLE);
        // Add required entity
        Peticionario peticionarios = PeticionarioResourceIntTest.createEntity(em);
        em.persist(peticionarios);
        em.flush();
        peticion.setPeticionarios(peticionarios);
        return peticion;
    }

    @Before
    public void initTest() {
        peticion = createEntity(em);
    }

    @Test
    @Transactional
    public void createPeticion() throws Exception {
        int databaseSizeBeforeCreate = peticionRepository.findAll().size();

        // Create the Peticion
        restPeticionMockMvc.perform(post("/api/peticions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(peticion)))
            .andExpect(status().isCreated());

        // Validate the Peticion in the database
        List<Peticion> peticionList = peticionRepository.findAll();
        assertThat(peticionList).hasSize(databaseSizeBeforeCreate + 1);
        Peticion testPeticion = peticionList.get(peticionList.size() - 1);
        assertThat(testPeticion.getNumero_peticion()).isEqualTo(DEFAULT_NUMERO_PETICION);
        assertThat(testPeticion.getFecha()).isEqualTo(DEFAULT_FECHA);
        assertThat(testPeticion.getActo_certificar()).isEqualTo(DEFAULT_ACTO_CERTIFICAR);
        assertThat(testPeticion.getResponsable()).isEqualTo(DEFAULT_RESPONSABLE);
    }

    @Test
    @Transactional
    public void createPeticionWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = peticionRepository.findAll().size();

        // Create the Peticion with an existing ID
        peticion.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restPeticionMockMvc.perform(post("/api/peticions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(peticion)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<Peticion> peticionList = peticionRepository.findAll();
        assertThat(peticionList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkNumero_peticionIsRequired() throws Exception {
        int databaseSizeBeforeTest = peticionRepository.findAll().size();
        // set the field null
        peticion.setNumero_peticion(null);

        // Create the Peticion, which fails.

        restPeticionMockMvc.perform(post("/api/peticions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(peticion)))
            .andExpect(status().isBadRequest());

        List<Peticion> peticionList = peticionRepository.findAll();
        assertThat(peticionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkFechaIsRequired() throws Exception {
        int databaseSizeBeforeTest = peticionRepository.findAll().size();
        // set the field null
        peticion.setFecha(null);

        // Create the Peticion, which fails.

        restPeticionMockMvc.perform(post("/api/peticions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(peticion)))
            .andExpect(status().isBadRequest());

        List<Peticion> peticionList = peticionRepository.findAll();
        assertThat(peticionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkActo_certificarIsRequired() throws Exception {
        int databaseSizeBeforeTest = peticionRepository.findAll().size();
        // set the field null
        peticion.setActo_certificar(null);

        // Create the Peticion, which fails.

        restPeticionMockMvc.perform(post("/api/peticions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(peticion)))
            .andExpect(status().isBadRequest());

        List<Peticion> peticionList = peticionRepository.findAll();
        assertThat(peticionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkResponsableIsRequired() throws Exception {
        int databaseSizeBeforeTest = peticionRepository.findAll().size();
        // set the field null
        peticion.setResponsable(null);

        // Create the Peticion, which fails.

        restPeticionMockMvc.perform(post("/api/peticions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(peticion)))
            .andExpect(status().isBadRequest());

        List<Peticion> peticionList = peticionRepository.findAll();
        assertThat(peticionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllPeticions() throws Exception {
        // Initialize the database
        peticionRepository.saveAndFlush(peticion);

        // Get all the peticionList
        restPeticionMockMvc.perform(get("/api/peticions?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(peticion.getId().intValue())))
            .andExpect(jsonPath("$.[*].numero_peticion").value(hasItem(DEFAULT_NUMERO_PETICION.toString())))
            .andExpect(jsonPath("$.[*].fecha").value(hasItem(DEFAULT_FECHA.toString())))
            .andExpect(jsonPath("$.[*].acto_certificar").value(hasItem(DEFAULT_ACTO_CERTIFICAR.toString())))
            .andExpect(jsonPath("$.[*].responsable").value(hasItem(DEFAULT_RESPONSABLE.toString())));
    }

    @Test
    @Transactional
    public void getPeticion() throws Exception {
        // Initialize the database
        peticionRepository.saveAndFlush(peticion);

        // Get the peticion
        restPeticionMockMvc.perform(get("/api/peticions/{id}", peticion.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(peticion.getId().intValue()))
            .andExpect(jsonPath("$.numero_peticion").value(DEFAULT_NUMERO_PETICION.toString()))
            .andExpect(jsonPath("$.fecha").value(DEFAULT_FECHA.toString()))
            .andExpect(jsonPath("$.acto_certificar").value(DEFAULT_ACTO_CERTIFICAR.toString()))
            .andExpect(jsonPath("$.responsable").value(DEFAULT_RESPONSABLE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingPeticion() throws Exception {
        // Get the peticion
        restPeticionMockMvc.perform(get("/api/peticions/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePeticion() throws Exception {
        // Initialize the database
        peticionRepository.saveAndFlush(peticion);
        int databaseSizeBeforeUpdate = peticionRepository.findAll().size();

        // Update the peticion
        Peticion updatedPeticion = peticionRepository.findOne(peticion.getId());
        updatedPeticion
            .numero_peticion(UPDATED_NUMERO_PETICION)
            .fecha(UPDATED_FECHA)
            .acto_certificar(UPDATED_ACTO_CERTIFICAR)
            .responsable(UPDATED_RESPONSABLE);

        restPeticionMockMvc.perform(put("/api/peticions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedPeticion)))
            .andExpect(status().isOk());

        // Validate the Peticion in the database
        List<Peticion> peticionList = peticionRepository.findAll();
        assertThat(peticionList).hasSize(databaseSizeBeforeUpdate);
        Peticion testPeticion = peticionList.get(peticionList.size() - 1);
        assertThat(testPeticion.getNumero_peticion()).isEqualTo(UPDATED_NUMERO_PETICION);
        assertThat(testPeticion.getFecha()).isEqualTo(UPDATED_FECHA);
        assertThat(testPeticion.getActo_certificar()).isEqualTo(UPDATED_ACTO_CERTIFICAR);
        assertThat(testPeticion.getResponsable()).isEqualTo(UPDATED_RESPONSABLE);
    }

    @Test
    @Transactional
    public void updateNonExistingPeticion() throws Exception {
        int databaseSizeBeforeUpdate = peticionRepository.findAll().size();

        // Create the Peticion

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restPeticionMockMvc.perform(put("/api/peticions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(peticion)))
            .andExpect(status().isCreated());

        // Validate the Peticion in the database
        List<Peticion> peticionList = peticionRepository.findAll();
        assertThat(peticionList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deletePeticion() throws Exception {
        // Initialize the database
        peticionRepository.saveAndFlush(peticion);
        int databaseSizeBeforeDelete = peticionRepository.findAll().size();

        // Get the peticion
        restPeticionMockMvc.perform(delete("/api/peticions/{id}", peticion.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Peticion> peticionList = peticionRepository.findAll();
        assertThat(peticionList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Peticion.class);
        Peticion peticion1 = new Peticion();
        peticion1.setId(1L);
        Peticion peticion2 = new Peticion();
        peticion2.setId(peticion1.getId());
        assertThat(peticion1).isEqualTo(peticion2);
        peticion2.setId(2L);
        assertThat(peticion1).isNotEqualTo(peticion2);
        peticion1.setId(null);
        assertThat(peticion1).isNotEqualTo(peticion2);
    }
}
