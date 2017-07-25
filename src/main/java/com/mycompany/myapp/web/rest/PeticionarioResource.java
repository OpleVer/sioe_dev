package com.mycompany.myapp.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.mycompany.myapp.domain.Peticionario;

import com.mycompany.myapp.repository.PeticionarioRepository;
import com.mycompany.myapp.web.rest.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Peticionario.
 */
@RestController
@RequestMapping("/api")
public class PeticionarioResource {

    private final Logger log = LoggerFactory.getLogger(PeticionarioResource.class);

    private static final String ENTITY_NAME = "peticionario";

    private final PeticionarioRepository peticionarioRepository;

    public PeticionarioResource(PeticionarioRepository peticionarioRepository) {
        this.peticionarioRepository = peticionarioRepository;
    }

    /**
     * POST  /peticionarios : Create a new peticionario.
     *
     * @param peticionario the peticionario to create
     * @return the ResponseEntity with status 201 (Created) and with body the new peticionario, or with status 400 (Bad Request) if the peticionario has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/peticionarios")
    @Timed
    public ResponseEntity<Peticionario> createPeticionario(@Valid @RequestBody Peticionario peticionario) throws URISyntaxException {
        log.debug("REST request to save Peticionario : {}", peticionario);
        if (peticionario.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new peticionario cannot already have an ID")).body(null);
        }
        Peticionario result = peticionarioRepository.save(peticionario);
        return ResponseEntity.created(new URI("/api/peticionarios/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /peticionarios : Updates an existing peticionario.
     *
     * @param peticionario the peticionario to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated peticionario,
     * or with status 400 (Bad Request) if the peticionario is not valid,
     * or with status 500 (Internal Server Error) if the peticionario couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/peticionarios")
    @Timed
    public ResponseEntity<Peticionario> updatePeticionario(@Valid @RequestBody Peticionario peticionario) throws URISyntaxException {
        log.debug("REST request to update Peticionario : {}", peticionario);
        if (peticionario.getId() == null) {
            return createPeticionario(peticionario);
        }
        Peticionario result = peticionarioRepository.save(peticionario);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, peticionario.getId().toString()))
            .body(result);
    }

    /**
     * GET  /peticionarios : get all the peticionarios.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of peticionarios in body
     */
    @GetMapping("/peticionarios")
    @Timed
    public List<Peticionario> getAllPeticionarios() {
        log.debug("REST request to get all Peticionarios");
        return peticionarioRepository.findAll();
    }

    /**
     * GET  /peticionarios/:id : get the "id" peticionario.
     *
     * @param id the id of the peticionario to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the peticionario, or with status 404 (Not Found)
     */
    @GetMapping("/peticionarios/{id}")
    @Timed
    public ResponseEntity<Peticionario> getPeticionario(@PathVariable Long id) {
        log.debug("REST request to get Peticionario : {}", id);
        Peticionario peticionario = peticionarioRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(peticionario));
    }

    /**
     * DELETE  /peticionarios/:id : delete the "id" peticionario.
     *
     * @param id the id of the peticionario to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/peticionarios/{id}")
    @Timed
    public ResponseEntity<Void> deletePeticionario(@PathVariable Long id) {
        log.debug("REST request to delete Peticionario : {}", id);
        peticionarioRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
