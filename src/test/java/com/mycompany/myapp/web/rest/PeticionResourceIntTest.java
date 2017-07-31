package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.SioeDevApp;

import com.mycompany.myapp.domain.Peticion;
import com.mycompany.myapp.domain.Peticionario;
import com.mycompany.myapp.domain.Responsable;
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

    private static final String DEFAULT_SOLICITANTE = "AAAAAAAAAA";
    private static final String UPDATED_SOLICITANTE = "BBBBBBBBBB";

    private static final String DEFAULT_DIRECCION = "AAAAAAAAAA";
    private static final String UPDATED_DIRECCION = "BBBBBBBBBB";

    private static final String DEFAULT_OFICIO = "AAAAAAAAAA";
    private static final String UPDATED_OFICIO = "BBBBBBBBBB";

    private static final String DEFAULT_CARGO_SOLICITANTE = "AAAAAAAAAA";
    private static final String UPDATED_CARGO_SOLICITANTE = "BBBBBBBBBB";

    private static final String DEFAULT_NUMERO_ACTA = "AAAAAAAAAA";
    private static final String UPDATED_NUMERO_ACTA = "BBBBBBBBBB";

    private static final String DEFAULT_ACTA = "AAAAAAAAAA";
    private static final String UPDATED_ACTA = "BBBBBBBBBB";

    private static final String DEFAULT_ACUERDO = "AAAAAAAAAA";
    private static final String UPDATED_ACUERDO = "BBBBBBBBBB";

    private static final String DEFAULT_CEDULA = "AAAAAAAAAA";
    private static final String UPDATED_CEDULA = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPCION_ANEXO = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPCION_ANEXO = "BBBBBBBBBB";

    private static final String DEFAULT_LINK_ANEXO = "AAAAAAAAAA";
    private static final String UPDATED_LINK_ANEXO = "BBBBBBBBBB";

    private static final Boolean DEFAULT_COMPLETADA = false;
    private static final Boolean UPDATED_COMPLETADA = true;

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
            .responsable(DEFAULT_RESPONSABLE)
            .solicitante(DEFAULT_SOLICITANTE)
            .direccion(DEFAULT_DIRECCION)
            .oficio(DEFAULT_OFICIO)
            .cargo_solicitante(DEFAULT_CARGO_SOLICITANTE)
            .numero_acta(DEFAULT_NUMERO_ACTA)
            .acta(DEFAULT_ACTA)
            .acuerdo(DEFAULT_ACUERDO)
            .cedula(DEFAULT_CEDULA)
            .descripcion_anexo(DEFAULT_DESCRIPCION_ANEXO)
            .link_anexo(DEFAULT_LINK_ANEXO)
            .completada(DEFAULT_COMPLETADA);
        // Add required entity
        Peticionario peticionarios = PeticionarioResourceIntTest.createEntity(em);
        em.persist(peticionarios);
        em.flush();
        peticion.setPeticionarios(peticionarios);
        // Add required entity
        Responsable responsables = ResponsableResourceIntTest.createEntity(em);
        em.persist(responsables);
        em.flush();
        peticion.setResponsables(responsables);
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
        assertThat(testPeticion.getSolicitante()).isEqualTo(DEFAULT_SOLICITANTE);
        assertThat(testPeticion.getDireccion()).isEqualTo(DEFAULT_DIRECCION);
        assertThat(testPeticion.getOficio()).isEqualTo(DEFAULT_OFICIO);
        assertThat(testPeticion.getCargo_solicitante()).isEqualTo(DEFAULT_CARGO_SOLICITANTE);
        assertThat(testPeticion.getNumero_acta()).isEqualTo(DEFAULT_NUMERO_ACTA);
        assertThat(testPeticion.getActa()).isEqualTo(DEFAULT_ACTA);
        assertThat(testPeticion.getAcuerdo()).isEqualTo(DEFAULT_ACUERDO);
        assertThat(testPeticion.getCedula()).isEqualTo(DEFAULT_CEDULA);
        assertThat(testPeticion.getDescripcion_anexo()).isEqualTo(DEFAULT_DESCRIPCION_ANEXO);
        assertThat(testPeticion.getLink_anexo()).isEqualTo(DEFAULT_LINK_ANEXO);
        assertThat(testPeticion.isCompletada()).isEqualTo(DEFAULT_COMPLETADA);
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
    public void checkSolicitanteIsRequired() throws Exception {
        int databaseSizeBeforeTest = peticionRepository.findAll().size();
        // set the field null
        peticion.setSolicitante(null);

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
    public void checkDireccionIsRequired() throws Exception {
        int databaseSizeBeforeTest = peticionRepository.findAll().size();
        // set the field null
        peticion.setDireccion(null);

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
    public void checkCargo_solicitanteIsRequired() throws Exception {
        int databaseSizeBeforeTest = peticionRepository.findAll().size();
        // set the field null
        peticion.setCargo_solicitante(null);

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
            .andExpect(jsonPath("$.[*].responsable").value(hasItem(DEFAULT_RESPONSABLE.toString())))
            .andExpect(jsonPath("$.[*].solicitante").value(hasItem(DEFAULT_SOLICITANTE.toString())))
            .andExpect(jsonPath("$.[*].direccion").value(hasItem(DEFAULT_DIRECCION.toString())))
            .andExpect(jsonPath("$.[*].oficio").value(hasItem(DEFAULT_OFICIO.toString())))
            .andExpect(jsonPath("$.[*].cargo_solicitante").value(hasItem(DEFAULT_CARGO_SOLICITANTE.toString())))
            .andExpect(jsonPath("$.[*].numero_acta").value(hasItem(DEFAULT_NUMERO_ACTA.toString())))
            .andExpect(jsonPath("$.[*].acta").value(hasItem(DEFAULT_ACTA.toString())))
            .andExpect(jsonPath("$.[*].acuerdo").value(hasItem(DEFAULT_ACUERDO.toString())))
            .andExpect(jsonPath("$.[*].cedula").value(hasItem(DEFAULT_CEDULA.toString())))
            .andExpect(jsonPath("$.[*].descripcion_anexo").value(hasItem(DEFAULT_DESCRIPCION_ANEXO.toString())))
            .andExpect(jsonPath("$.[*].link_anexo").value(hasItem(DEFAULT_LINK_ANEXO.toString())))
            .andExpect(jsonPath("$.[*].completada").value(hasItem(DEFAULT_COMPLETADA.booleanValue())));
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
            .andExpect(jsonPath("$.responsable").value(DEFAULT_RESPONSABLE.toString()))
            .andExpect(jsonPath("$.solicitante").value(DEFAULT_SOLICITANTE.toString()))
            .andExpect(jsonPath("$.direccion").value(DEFAULT_DIRECCION.toString()))
            .andExpect(jsonPath("$.oficio").value(DEFAULT_OFICIO.toString()))
            .andExpect(jsonPath("$.cargo_solicitante").value(DEFAULT_CARGO_SOLICITANTE.toString()))
            .andExpect(jsonPath("$.numero_acta").value(DEFAULT_NUMERO_ACTA.toString()))
            .andExpect(jsonPath("$.acta").value(DEFAULT_ACTA.toString()))
            .andExpect(jsonPath("$.acuerdo").value(DEFAULT_ACUERDO.toString()))
            .andExpect(jsonPath("$.cedula").value(DEFAULT_CEDULA.toString()))
            .andExpect(jsonPath("$.descripcion_anexo").value(DEFAULT_DESCRIPCION_ANEXO.toString()))
            .andExpect(jsonPath("$.link_anexo").value(DEFAULT_LINK_ANEXO.toString()))
            .andExpect(jsonPath("$.completada").value(DEFAULT_COMPLETADA.booleanValue()));
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
            .responsable(UPDATED_RESPONSABLE)
            .solicitante(UPDATED_SOLICITANTE)
            .direccion(UPDATED_DIRECCION)
            .oficio(UPDATED_OFICIO)
            .cargo_solicitante(UPDATED_CARGO_SOLICITANTE)
            .numero_acta(UPDATED_NUMERO_ACTA)
            .acta(UPDATED_ACTA)
            .acuerdo(UPDATED_ACUERDO)
            .cedula(UPDATED_CEDULA)
            .descripcion_anexo(UPDATED_DESCRIPCION_ANEXO)
            .link_anexo(UPDATED_LINK_ANEXO)
            .completada(UPDATED_COMPLETADA);

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
        assertThat(testPeticion.getSolicitante()).isEqualTo(UPDATED_SOLICITANTE);
        assertThat(testPeticion.getDireccion()).isEqualTo(UPDATED_DIRECCION);
        assertThat(testPeticion.getOficio()).isEqualTo(UPDATED_OFICIO);
        assertThat(testPeticion.getCargo_solicitante()).isEqualTo(UPDATED_CARGO_SOLICITANTE);
        assertThat(testPeticion.getNumero_acta()).isEqualTo(UPDATED_NUMERO_ACTA);
        assertThat(testPeticion.getActa()).isEqualTo(UPDATED_ACTA);
        assertThat(testPeticion.getAcuerdo()).isEqualTo(UPDATED_ACUERDO);
        assertThat(testPeticion.getCedula()).isEqualTo(UPDATED_CEDULA);
        assertThat(testPeticion.getDescripcion_anexo()).isEqualTo(UPDATED_DESCRIPCION_ANEXO);
        assertThat(testPeticion.getLink_anexo()).isEqualTo(UPDATED_LINK_ANEXO);
        assertThat(testPeticion.isCompletada()).isEqualTo(UPDATED_COMPLETADA);
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
