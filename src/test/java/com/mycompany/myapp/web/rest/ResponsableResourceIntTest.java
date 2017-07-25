package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.SioeDevApp;

import com.mycompany.myapp.domain.Responsable;
import com.mycompany.myapp.repository.ResponsableRepository;
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
 * Test class for the ResponsableResource REST controller.
 *
 * @see ResponsableResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = SioeDevApp.class)
public class ResponsableResourceIntTest {

    private static final String DEFAULT_TIPO = "AAAAAAAAAA";
    private static final String UPDATED_TIPO = "BBBBBBBBBB";

    @Autowired
    private ResponsableRepository responsableRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restResponsableMockMvc;

    private Responsable responsable;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ResponsableResource responsableResource = new ResponsableResource(responsableRepository);
        this.restResponsableMockMvc = MockMvcBuilders.standaloneSetup(responsableResource)
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
    public static Responsable createEntity(EntityManager em) {
        Responsable responsable = new Responsable()
            .tipo(DEFAULT_TIPO);
        return responsable;
    }

    @Before
    public void initTest() {
        responsable = createEntity(em);
    }

    @Test
    @Transactional
    public void createResponsable() throws Exception {
        int databaseSizeBeforeCreate = responsableRepository.findAll().size();

        // Create the Responsable
        restResponsableMockMvc.perform(post("/api/responsables")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(responsable)))
            .andExpect(status().isCreated());

        // Validate the Responsable in the database
        List<Responsable> responsableList = responsableRepository.findAll();
        assertThat(responsableList).hasSize(databaseSizeBeforeCreate + 1);
        Responsable testResponsable = responsableList.get(responsableList.size() - 1);
        assertThat(testResponsable.getTipo()).isEqualTo(DEFAULT_TIPO);
    }

    @Test
    @Transactional
    public void createResponsableWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = responsableRepository.findAll().size();

        // Create the Responsable with an existing ID
        responsable.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restResponsableMockMvc.perform(post("/api/responsables")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(responsable)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<Responsable> responsableList = responsableRepository.findAll();
        assertThat(responsableList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkTipoIsRequired() throws Exception {
        int databaseSizeBeforeTest = responsableRepository.findAll().size();
        // set the field null
        responsable.setTipo(null);

        // Create the Responsable, which fails.

        restResponsableMockMvc.perform(post("/api/responsables")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(responsable)))
            .andExpect(status().isBadRequest());

        List<Responsable> responsableList = responsableRepository.findAll();
        assertThat(responsableList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllResponsables() throws Exception {
        // Initialize the database
        responsableRepository.saveAndFlush(responsable);

        // Get all the responsableList
        restResponsableMockMvc.perform(get("/api/responsables?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(responsable.getId().intValue())))
            .andExpect(jsonPath("$.[*].tipo").value(hasItem(DEFAULT_TIPO.toString())));
    }

    @Test
    @Transactional
    public void getResponsable() throws Exception {
        // Initialize the database
        responsableRepository.saveAndFlush(responsable);

        // Get the responsable
        restResponsableMockMvc.perform(get("/api/responsables/{id}", responsable.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(responsable.getId().intValue()))
            .andExpect(jsonPath("$.tipo").value(DEFAULT_TIPO.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingResponsable() throws Exception {
        // Get the responsable
        restResponsableMockMvc.perform(get("/api/responsables/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateResponsable() throws Exception {
        // Initialize the database
        responsableRepository.saveAndFlush(responsable);
        int databaseSizeBeforeUpdate = responsableRepository.findAll().size();

        // Update the responsable
        Responsable updatedResponsable = responsableRepository.findOne(responsable.getId());
        updatedResponsable
            .tipo(UPDATED_TIPO);

        restResponsableMockMvc.perform(put("/api/responsables")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedResponsable)))
            .andExpect(status().isOk());

        // Validate the Responsable in the database
        List<Responsable> responsableList = responsableRepository.findAll();
        assertThat(responsableList).hasSize(databaseSizeBeforeUpdate);
        Responsable testResponsable = responsableList.get(responsableList.size() - 1);
        assertThat(testResponsable.getTipo()).isEqualTo(UPDATED_TIPO);
    }

    @Test
    @Transactional
    public void updateNonExistingResponsable() throws Exception {
        int databaseSizeBeforeUpdate = responsableRepository.findAll().size();

        // Create the Responsable

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restResponsableMockMvc.perform(put("/api/responsables")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(responsable)))
            .andExpect(status().isCreated());

        // Validate the Responsable in the database
        List<Responsable> responsableList = responsableRepository.findAll();
        assertThat(responsableList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteResponsable() throws Exception {
        // Initialize the database
        responsableRepository.saveAndFlush(responsable);
        int databaseSizeBeforeDelete = responsableRepository.findAll().size();

        // Get the responsable
        restResponsableMockMvc.perform(delete("/api/responsables/{id}", responsable.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Responsable> responsableList = responsableRepository.findAll();
        assertThat(responsableList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Responsable.class);
        Responsable responsable1 = new Responsable();
        responsable1.setId(1L);
        Responsable responsable2 = new Responsable();
        responsable2.setId(responsable1.getId());
        assertThat(responsable1).isEqualTo(responsable2);
        responsable2.setId(2L);
        assertThat(responsable1).isNotEqualTo(responsable2);
        responsable1.setId(null);
        assertThat(responsable1).isNotEqualTo(responsable2);
    }
}
