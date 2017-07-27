package com.mycompany.myapp.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.mycompany.myapp.domain.Anexo;

import com.mycompany.myapp.repository.AnexoRepository;
import com.mycompany.myapp.web.rest.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Anexo.
 */
@RestController
@RequestMapping("/api")
public class AnexoResource {

    private final Logger log = LoggerFactory.getLogger(AnexoResource.class);

    private static final String ENTITY_NAME = "anexo";

    private final AnexoRepository anexoRepository;

    public AnexoResource(AnexoRepository anexoRepository) {
        this.anexoRepository = anexoRepository;
    }

    /**
     * POST  /anexos : Create a new anexo.
     *
     * @param anexo the anexo to create
     * @return the ResponseEntity with status 201 (Created) and with body the new anexo, or with status 400 (Bad Request) if the anexo has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/anexos")
    @Timed
    public ResponseEntity<Anexo> createAnexo(@RequestBody Anexo anexo) throws URISyntaxException {
        log.debug("REST request to save Anexo : {}", anexo);
        if (anexo.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new anexo cannot already have an ID")).body(null);
        }
        Anexo result = anexoRepository.save(anexo);
        return ResponseEntity.created(new URI("/api/anexos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /anexos : Updates an existing anexo.
     *
     * @param anexo the anexo to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated anexo,
     * or with status 400 (Bad Request) if the anexo is not valid,
     * or with status 500 (Internal Server Error) if the anexo couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/anexos")
    @Timed
    public ResponseEntity<Anexo> updateAnexo(@RequestBody Anexo anexo) throws URISyntaxException {
        log.debug("REST request to update Anexo : {}", anexo);
        if (anexo.getId() == null) {
            return createAnexo(anexo);
        }
        Anexo result = anexoRepository.save(anexo);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, anexo.getId().toString()))
            .body(result);
    }

    /**
     * GET  /anexos : get all the anexos.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of anexos in body
     */
    @GetMapping("/anexos")
    @Timed
    public List<Anexo> getAllAnexos() {
        log.debug("REST request to get all Anexos");
        return anexoRepository.findAll();
    }

    /**
     * GET  /anexos/:id : get the "id" anexo.
     *
     * @param id the id of the anexo to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the anexo, or with status 404 (Not Found)
     */
    @GetMapping("/anexos/{id}")
    @Timed
    public ResponseEntity<Anexo> getAnexo(@PathVariable Long id) {
        log.debug("REST request to get Anexo : {}", id);
        Anexo anexo = anexoRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(anexo));
    }

    /**
     * DELETE  /anexos/:id : delete the "id" anexo.
     *
     * @param id the id of the anexo to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/anexos/{id}")
    @Timed
    public ResponseEntity<Void> deleteAnexo(@PathVariable Long id) {
        log.debug("REST request to delete Anexo : {}", id);
        anexoRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
