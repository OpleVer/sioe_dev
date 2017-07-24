package com.mycompany.myapp.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.mycompany.myapp.domain.Peticion;

import com.mycompany.myapp.repository.PeticionRepository;
import com.mycompany.myapp.web.rest.util.HeaderUtil;
import com.mycompany.myapp.web.rest.util.PaginationUtil;
import io.swagger.annotations.ApiParam;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Peticion.
 */
@RestController
@RequestMapping("/api")
public class PeticionResource {

    private final Logger log = LoggerFactory.getLogger(PeticionResource.class);

    private static final String ENTITY_NAME = "peticion";

    private final PeticionRepository peticionRepository;

    public PeticionResource(PeticionRepository peticionRepository) {
        this.peticionRepository = peticionRepository;
    }

    /**
     * POST  /peticions : Create a new peticion.
     *
     * @param peticion the peticion to create
     * @return the ResponseEntity with status 201 (Created) and with body the new peticion, or with status 400 (Bad Request) if the peticion has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/peticions")
    @Timed
    public ResponseEntity<Peticion> createPeticion(@Valid @RequestBody Peticion peticion) throws URISyntaxException {
        log.debug("REST request to save Peticion : {}", peticion);
        if (peticion.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new peticion cannot already have an ID")).body(null);
        }
        Peticion result = peticionRepository.save(peticion);
        return ResponseEntity.created(new URI("/api/peticions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /peticions : Updates an existing peticion.
     *
     * @param peticion the peticion to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated peticion,
     * or with status 400 (Bad Request) if the peticion is not valid,
     * or with status 500 (Internal Server Error) if the peticion couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/peticions")
    @Timed
    public ResponseEntity<Peticion> updatePeticion(@Valid @RequestBody Peticion peticion) throws URISyntaxException {
        log.debug("REST request to update Peticion : {}", peticion);
        if (peticion.getId() == null) {
            return createPeticion(peticion);
        }
        Peticion result = peticionRepository.save(peticion);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, peticion.getId().toString()))
            .body(result);
    }

    /**
     * GET  /peticions : get all the peticions.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of peticions in body
     */
    @GetMapping("/peticions")
    @Timed
    public ResponseEntity<List<Peticion>> getAllPeticions(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of Peticions");
        Page<Peticion> page = peticionRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/peticions");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /peticions/:id : get the "id" peticion.
     *
     * @param id the id of the peticion to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the peticion, or with status 404 (Not Found)
     */
    @GetMapping("/peticions/{id}")
    @Timed
    public ResponseEntity<Peticion> getPeticion(@PathVariable Long id) {
        log.debug("REST request to get Peticion : {}", id);
        Peticion peticion = peticionRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(peticion));
    }

    /**
     * DELETE  /peticions/:id : delete the "id" peticion.
     *
     * @param id the id of the peticion to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/peticions/{id}")
    @Timed
    public ResponseEntity<Void> deletePeticion(@PathVariable Long id) {
        log.debug("REST request to delete Peticion : {}", id);
        peticionRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
