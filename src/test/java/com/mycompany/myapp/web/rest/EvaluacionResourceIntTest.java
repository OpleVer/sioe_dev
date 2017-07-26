package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.SioeDevApp;

import com.mycompany.myapp.domain.Evaluacion;
import com.mycompany.myapp.repository.EvaluacionRepository;
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
 * Test class for the EvaluacionResource REST controller.
 *
 * @see EvaluacionResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = SioeDevApp.class)
public class EvaluacionResourceIntTest {

    private static final Integer DEFAULT_TIPO = 1;
    private static final Integer UPDATED_TIPO = 2;

    private static final String DEFAULT_NUMERO_ACTA = "AAAAAAAAAA";
    private static final String UPDATED_NUMERO_ACTA = "BBBBBBBBBB";

    private static final String DEFAULT_ACTA = "AAAAAAAAAA";
    private static final String UPDATED_ACTA = "BBBBBBBBBB";

    private static final String DEFAULT_ACUERDO = "AAAAAAAAAA";
    private static final String UPDATED_ACUERDO = "BBBBBBBBBB";

    private static final String DEFAULT_CEDULA = "AAAAAAAAAA";
    private static final String UPDATED_CEDULA = "BBBBBBBBBB";

    @Autowired
    private EvaluacionRepository evaluacionRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restEvaluacionMockMvc;

    private Evaluacion evaluacion;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        EvaluacionResource evaluacionResource = new EvaluacionResource(evaluacionRepository);
        this.restEvaluacionMockMvc = MockMvcBuilders.standaloneSetup(evaluacionResource)
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
    public static Evaluacion createEntity(EntityManager em) {
        Evaluacion evaluacion = new Evaluacion()
            .tipo(DEFAULT_TIPO)
            .numero_acta(DEFAULT_NUMERO_ACTA)
            .acta(DEFAULT_ACTA)
            .acuerdo(DEFAULT_ACUERDO)
            .cedula(DEFAULT_CEDULA);
        return evaluacion;
    }

    @Before
    public void initTest() {
        evaluacion = createEntity(em);
    }

    @Test
    @Transactional
    public void createEvaluacion() throws Exception {
        int databaseSizeBeforeCreate = evaluacionRepository.findAll().size();

        // Create the Evaluacion
        restEvaluacionMockMvc.perform(post("/api/evaluacions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(evaluacion)))
            .andExpect(status().isCreated());

        // Validate the Evaluacion in the database
        List<Evaluacion> evaluacionList = evaluacionRepository.findAll();
        assertThat(evaluacionList).hasSize(databaseSizeBeforeCreate + 1);
        Evaluacion testEvaluacion = evaluacionList.get(evaluacionList.size() - 1);
        assertThat(testEvaluacion.getTipo()).isEqualTo(DEFAULT_TIPO);
        assertThat(testEvaluacion.getNumero_acta()).isEqualTo(DEFAULT_NUMERO_ACTA);
        assertThat(testEvaluacion.getActa()).isEqualTo(DEFAULT_ACTA);
        assertThat(testEvaluacion.getAcuerdo()).isEqualTo(DEFAULT_ACUERDO);
        assertThat(testEvaluacion.getCedula()).isEqualTo(DEFAULT_CEDULA);
    }

    @Test
    @Transactional
    public void createEvaluacionWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = evaluacionRepository.findAll().size();

        // Create the Evaluacion with an existing ID
        evaluacion.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restEvaluacionMockMvc.perform(post("/api/evaluacions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(evaluacion)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<Evaluacion> evaluacionList = evaluacionRepository.findAll();
        assertThat(evaluacionList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkTipoIsRequired() throws Exception {
        int databaseSizeBeforeTest = evaluacionRepository.findAll().size();
        // set the field null
        evaluacion.setTipo(null);

        // Create the Evaluacion, which fails.

        restEvaluacionMockMvc.perform(post("/api/evaluacions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(evaluacion)))
            .andExpect(status().isBadRequest());

        List<Evaluacion> evaluacionList = evaluacionRepository.findAll();
        assertThat(evaluacionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllEvaluacions() throws Exception {
        // Initialize the database
        evaluacionRepository.saveAndFlush(evaluacion);

        // Get all the evaluacionList
        restEvaluacionMockMvc.perform(get("/api/evaluacions?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(evaluacion.getId().intValue())))
            .andExpect(jsonPath("$.[*].tipo").value(hasItem(DEFAULT_TIPO)))
            .andExpect(jsonPath("$.[*].numero_acta").value(hasItem(DEFAULT_NUMERO_ACTA.toString())))
            .andExpect(jsonPath("$.[*].acta").value(hasItem(DEFAULT_ACTA.toString())))
            .andExpect(jsonPath("$.[*].acuerdo").value(hasItem(DEFAULT_ACUERDO.toString())))
            .andExpect(jsonPath("$.[*].cedula").value(hasItem(DEFAULT_CEDULA.toString())));
    }

    @Test
    @Transactional
    public void getEvaluacion() throws Exception {
        // Initialize the database
        evaluacionRepository.saveAndFlush(evaluacion);

        // Get the evaluacion
        restEvaluacionMockMvc.perform(get("/api/evaluacions/{id}", evaluacion.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(evaluacion.getId().intValue()))
            .andExpect(jsonPath("$.tipo").value(DEFAULT_TIPO))
            .andExpect(jsonPath("$.numero_acta").value(DEFAULT_NUMERO_ACTA.toString()))
            .andExpect(jsonPath("$.acta").value(DEFAULT_ACTA.toString()))
            .andExpect(jsonPath("$.acuerdo").value(DEFAULT_ACUERDO.toString()))
            .andExpect(jsonPath("$.cedula").value(DEFAULT_CEDULA.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingEvaluacion() throws Exception {
        // Get the evaluacion
        restEvaluacionMockMvc.perform(get("/api/evaluacions/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateEvaluacion() throws Exception {
        // Initialize the database
        evaluacionRepository.saveAndFlush(evaluacion);
        int databaseSizeBeforeUpdate = evaluacionRepository.findAll().size();

        // Update the evaluacion
        Evaluacion updatedEvaluacion = evaluacionRepository.findOne(evaluacion.getId());
        updatedEvaluacion
            .tipo(UPDATED_TIPO)
            .numero_acta(UPDATED_NUMERO_ACTA)
            .acta(UPDATED_ACTA)
            .acuerdo(UPDATED_ACUERDO)
            .cedula(UPDATED_CEDULA);

        restEvaluacionMockMvc.perform(put("/api/evaluacions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedEvaluacion)))
            .andExpect(status().isOk());

        // Validate the Evaluacion in the database
        List<Evaluacion> evaluacionList = evaluacionRepository.findAll();
        assertThat(evaluacionList).hasSize(databaseSizeBeforeUpdate);
        Evaluacion testEvaluacion = evaluacionList.get(evaluacionList.size() - 1);
        assertThat(testEvaluacion.getTipo()).isEqualTo(UPDATED_TIPO);
        assertThat(testEvaluacion.getNumero_acta()).isEqualTo(UPDATED_NUMERO_ACTA);
        assertThat(testEvaluacion.getActa()).isEqualTo(UPDATED_ACTA);
        assertThat(testEvaluacion.getAcuerdo()).isEqualTo(UPDATED_ACUERDO);
        assertThat(testEvaluacion.getCedula()).isEqualTo(UPDATED_CEDULA);
    }

    @Test
    @Transactional
    public void updateNonExistingEvaluacion() throws Exception {
        int databaseSizeBeforeUpdate = evaluacionRepository.findAll().size();

        // Create the Evaluacion

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restEvaluacionMockMvc.perform(put("/api/evaluacions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(evaluacion)))
            .andExpect(status().isCreated());

        // Validate the Evaluacion in the database
        List<Evaluacion> evaluacionList = evaluacionRepository.findAll();
        assertThat(evaluacionList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteEvaluacion() throws Exception {
        // Initialize the database
        evaluacionRepository.saveAndFlush(evaluacion);
        int databaseSizeBeforeDelete = evaluacionRepository.findAll().size();

        // Get the evaluacion
        restEvaluacionMockMvc.perform(delete("/api/evaluacions/{id}", evaluacion.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Evaluacion> evaluacionList = evaluacionRepository.findAll();
        assertThat(evaluacionList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Evaluacion.class);
        Evaluacion evaluacion1 = new Evaluacion();
        evaluacion1.setId(1L);
        Evaluacion evaluacion2 = new Evaluacion();
        evaluacion2.setId(evaluacion1.getId());
        assertThat(evaluacion1).isEqualTo(evaluacion2);
        evaluacion2.setId(2L);
        assertThat(evaluacion1).isNotEqualTo(evaluacion2);
        evaluacion1.setId(null);
        assertThat(evaluacion1).isNotEqualTo(evaluacion2);
    }
}
