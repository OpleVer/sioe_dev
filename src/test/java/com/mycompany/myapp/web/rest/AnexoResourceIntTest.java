package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.SioeDevApp;

import com.mycompany.myapp.domain.Anexo;
import com.mycompany.myapp.repository.AnexoRepository;
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
 * Test class for the AnexoResource REST controller.
 *
 * @see AnexoResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = SioeDevApp.class)
public class AnexoResourceIntTest {

    private static final String DEFAULT_DESCRIPCION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPCION = "BBBBBBBBBB";

    private static final String DEFAULT_LINK = "AAAAAAAAAA";
    private static final String UPDATED_LINK = "BBBBBBBBBB";

    @Autowired
    private AnexoRepository anexoRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restAnexoMockMvc;

    private Anexo anexo;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        AnexoResource anexoResource = new AnexoResource(anexoRepository);
        this.restAnexoMockMvc = MockMvcBuilders.standaloneSetup(anexoResource)
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
    public static Anexo createEntity(EntityManager em) {
        Anexo anexo = new Anexo()
            .descripcion(DEFAULT_DESCRIPCION)
            .link(DEFAULT_LINK);
        return anexo;
    }

    @Before
    public void initTest() {
        anexo = createEntity(em);
    }

    @Test
    @Transactional
    public void createAnexo() throws Exception {
        int databaseSizeBeforeCreate = anexoRepository.findAll().size();

        // Create the Anexo
        restAnexoMockMvc.perform(post("/api/anexos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(anexo)))
            .andExpect(status().isCreated());

        // Validate the Anexo in the database
        List<Anexo> anexoList = anexoRepository.findAll();
        assertThat(anexoList).hasSize(databaseSizeBeforeCreate + 1);
        Anexo testAnexo = anexoList.get(anexoList.size() - 1);
        assertThat(testAnexo.getDescripcion()).isEqualTo(DEFAULT_DESCRIPCION);
        assertThat(testAnexo.getLink()).isEqualTo(DEFAULT_LINK);
    }

    @Test
    @Transactional
    public void createAnexoWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = anexoRepository.findAll().size();

        // Create the Anexo with an existing ID
        anexo.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restAnexoMockMvc.perform(post("/api/anexos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(anexo)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<Anexo> anexoList = anexoRepository.findAll();
        assertThat(anexoList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkDescripcionIsRequired() throws Exception {
        int databaseSizeBeforeTest = anexoRepository.findAll().size();
        // set the field null
        anexo.setDescripcion(null);

        // Create the Anexo, which fails.

        restAnexoMockMvc.perform(post("/api/anexos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(anexo)))
            .andExpect(status().isBadRequest());

        List<Anexo> anexoList = anexoRepository.findAll();
        assertThat(anexoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkLinkIsRequired() throws Exception {
        int databaseSizeBeforeTest = anexoRepository.findAll().size();
        // set the field null
        anexo.setLink(null);

        // Create the Anexo, which fails.

        restAnexoMockMvc.perform(post("/api/anexos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(anexo)))
            .andExpect(status().isBadRequest());

        List<Anexo> anexoList = anexoRepository.findAll();
        assertThat(anexoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllAnexos() throws Exception {
        // Initialize the database
        anexoRepository.saveAndFlush(anexo);

        // Get all the anexoList
        restAnexoMockMvc.perform(get("/api/anexos?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(anexo.getId().intValue())))
            .andExpect(jsonPath("$.[*].descripcion").value(hasItem(DEFAULT_DESCRIPCION.toString())))
            .andExpect(jsonPath("$.[*].link").value(hasItem(DEFAULT_LINK.toString())));
    }

    @Test
    @Transactional
    public void getAnexo() throws Exception {
        // Initialize the database
        anexoRepository.saveAndFlush(anexo);

        // Get the anexo
        restAnexoMockMvc.perform(get("/api/anexos/{id}", anexo.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(anexo.getId().intValue()))
            .andExpect(jsonPath("$.descripcion").value(DEFAULT_DESCRIPCION.toString()))
            .andExpect(jsonPath("$.link").value(DEFAULT_LINK.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingAnexo() throws Exception {
        // Get the anexo
        restAnexoMockMvc.perform(get("/api/anexos/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateAnexo() throws Exception {
        // Initialize the database
        anexoRepository.saveAndFlush(anexo);
        int databaseSizeBeforeUpdate = anexoRepository.findAll().size();

        // Update the anexo
        Anexo updatedAnexo = anexoRepository.findOne(anexo.getId());
        updatedAnexo
            .descripcion(UPDATED_DESCRIPCION)
            .link(UPDATED_LINK);

        restAnexoMockMvc.perform(put("/api/anexos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedAnexo)))
            .andExpect(status().isOk());

        // Validate the Anexo in the database
        List<Anexo> anexoList = anexoRepository.findAll();
        assertThat(anexoList).hasSize(databaseSizeBeforeUpdate);
        Anexo testAnexo = anexoList.get(anexoList.size() - 1);
        assertThat(testAnexo.getDescripcion()).isEqualTo(UPDATED_DESCRIPCION);
        assertThat(testAnexo.getLink()).isEqualTo(UPDATED_LINK);
    }

    @Test
    @Transactional
    public void updateNonExistingAnexo() throws Exception {
        int databaseSizeBeforeUpdate = anexoRepository.findAll().size();

        // Create the Anexo

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restAnexoMockMvc.perform(put("/api/anexos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(anexo)))
            .andExpect(status().isCreated());

        // Validate the Anexo in the database
        List<Anexo> anexoList = anexoRepository.findAll();
        assertThat(anexoList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteAnexo() throws Exception {
        // Initialize the database
        anexoRepository.saveAndFlush(anexo);
        int databaseSizeBeforeDelete = anexoRepository.findAll().size();

        // Get the anexo
        restAnexoMockMvc.perform(delete("/api/anexos/{id}", anexo.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Anexo> anexoList = anexoRepository.findAll();
        assertThat(anexoList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Anexo.class);
        Anexo anexo1 = new Anexo();
        anexo1.setId(1L);
        Anexo anexo2 = new Anexo();
        anexo2.setId(anexo1.getId());
        assertThat(anexo1).isEqualTo(anexo2);
        anexo2.setId(2L);
        assertThat(anexo1).isNotEqualTo(anexo2);
        anexo1.setId(null);
        assertThat(anexo1).isNotEqualTo(anexo2);
    }
}
