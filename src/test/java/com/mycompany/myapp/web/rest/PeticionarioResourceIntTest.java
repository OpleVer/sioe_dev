package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.SioeDevApp;

import com.mycompany.myapp.domain.Peticionario;
import com.mycompany.myapp.repository.PeticionarioRepository;
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
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the PeticionarioResource REST controller.
 *
 * @see PeticionarioResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = SioeDevApp.class)
public class PeticionarioResourceIntTest {

    private static final String DEFAULT_NOMBRE = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRE = "BBBBBBBBBB";

    @Autowired
    private PeticionarioRepository peticionarioRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restPeticionarioMockMvc;

    private Peticionario peticionario;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        PeticionarioResource peticionarioResource = new PeticionarioResource(peticionarioRepository);
        this.restPeticionarioMockMvc = MockMvcBuilders.standaloneSetup(peticionarioResource)
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
    public static Peticionario createEntity(EntityManager em) {
        Peticionario peticionario = new Peticionario()
            .nombre(DEFAULT_NOMBRE);
        return peticionario;
    }

    @Before
    public void initTest() {
        peticionario = createEntity(em);
    }

    @Test
    @Transactional
    public void createPeticionario() throws Exception {
        int databaseSizeBeforeCreate = peticionarioRepository.findAll().size();

        // Create the Peticionario
        restPeticionarioMockMvc.perform(post("/api/peticionarios")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(peticionario)))
            .andExpect(status().isCreated());

        // Validate the Peticionario in the database
        List<Peticionario> peticionarioList = peticionarioRepository.findAll();
        assertThat(peticionarioList).hasSize(databaseSizeBeforeCreate + 1);
        Peticionario testPeticionario = peticionarioList.get(peticionarioList.size() - 1);
        assertThat(testPeticionario.getNombre()).isEqualTo(DEFAULT_NOMBRE);
    }

    @Test
    @Transactional
    public void createPeticionarioWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = peticionarioRepository.findAll().size();

        // Create the Peticionario with an existing ID
        peticionario.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restPeticionarioMockMvc.perform(post("/api/peticionarios")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(peticionario)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<Peticionario> peticionarioList = peticionarioRepository.findAll();
        assertThat(peticionarioList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkNombreIsRequired() throws Exception {
        int databaseSizeBeforeTest = peticionarioRepository.findAll().size();
        // set the field null
        peticionario.setNombre(null);

        // Create the Peticionario, which fails.

        restPeticionarioMockMvc.perform(post("/api/peticionarios")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(peticionario)))
            .andExpect(status().isBadRequest());

        List<Peticionario> peticionarioList = peticionarioRepository.findAll();
        assertThat(peticionarioList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllPeticionarios() throws Exception {
        // Initialize the database
        peticionarioRepository.saveAndFlush(peticionario);

        // Get all the peticionarioList
        restPeticionarioMockMvc.perform(get("/api/peticionarios?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(peticionario.getId().intValue())))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE.toString())));
    }

    @Test
    @Transactional
    public void getPeticionario() throws Exception {
        // Initialize the database
        peticionarioRepository.saveAndFlush(peticionario);

        // Get the peticionario
        restPeticionarioMockMvc.perform(get("/api/peticionarios/{id}", peticionario.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(peticionario.getId().intValue()))
            .andExpect(jsonPath("$.nombre").value(DEFAULT_NOMBRE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingPeticionario() throws Exception {
        // Get the peticionario
        restPeticionarioMockMvc.perform(get("/api/peticionarios/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePeticionario() throws Exception {
        // Initialize the database
        peticionarioRepository.saveAndFlush(peticionario);
        int databaseSizeBeforeUpdate = peticionarioRepository.findAll().size();

        // Update the peticionario
        Peticionario updatedPeticionario = peticionarioRepository.findOne(peticionario.getId());
        updatedPeticionario
            .nombre(UPDATED_NOMBRE);

        restPeticionarioMockMvc.perform(put("/api/peticionarios")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedPeticionario)))
            .andExpect(status().isOk());

        // Validate the Peticionario in the database
        List<Peticionario> peticionarioList = peticionarioRepository.findAll();
        assertThat(peticionarioList).hasSize(databaseSizeBeforeUpdate);
        Peticionario testPeticionario = peticionarioList.get(peticionarioList.size() - 1);
        assertThat(testPeticionario.getNombre()).isEqualTo(UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    public void updateNonExistingPeticionario() throws Exception {
        int databaseSizeBeforeUpdate = peticionarioRepository.findAll().size();

        // Create the Peticionario

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restPeticionarioMockMvc.perform(put("/api/peticionarios")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(peticionario)))
            .andExpect(status().isCreated());

        // Validate the Peticionario in the database
        List<Peticionario> peticionarioList = peticionarioRepository.findAll();
        assertThat(peticionarioList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deletePeticionario() throws Exception {
        // Initialize the database
        peticionarioRepository.saveAndFlush(peticionario);
        int databaseSizeBeforeDelete = peticionarioRepository.findAll().size();

        // Get the peticionario
        restPeticionarioMockMvc.perform(delete("/api/peticionarios/{id}", peticionario.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Peticionario> peticionarioList = peticionarioRepository.findAll();
        assertThat(peticionarioList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Peticionario.class);
        Peticionario peticionario1 = new Peticionario();
        peticionario1.setId(1L);
        Peticionario peticionario2 = new Peticionario();
        peticionario2.setId(peticionario1.getId());
        assertThat(peticionario1).isEqualTo(peticionario2);
        peticionario2.setId(2L);
        assertThat(peticionario1).isNotEqualTo(peticionario2);
        peticionario1.setId(null);
        assertThat(peticionario1).isNotEqualTo(peticionario2);
    }
}
