package relacija.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import relacija.domain.Ponude;
import relacija.repository.PonudeRepository;
import relacija.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link relacija.domain.Ponude}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class PonudeResource {

    private final Logger log = LoggerFactory.getLogger(PonudeResource.class);

    private static final String ENTITY_NAME = "ponude";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PonudeRepository ponudeRepository;

    public PonudeResource(PonudeRepository ponudeRepository) {
        this.ponudeRepository = ponudeRepository;
    }

    /**
     * {@code POST  /ponudes} : Create a new ponude.
     *
     * @param ponude the ponude to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new ponude, or with status {@code 400 (Bad Request)} if the ponude has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/ponudes")
    public ResponseEntity<Ponude> createPonude(@RequestBody Ponude ponude) throws URISyntaxException {
        log.debug("REST request to save Ponude : {}", ponude);
        if (ponude.getId() != null) {
            throw new BadRequestAlertException("A new ponude cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Ponude result = ponudeRepository.save(ponude);
        return ResponseEntity
            .created(new URI("/api/ponudes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /ponudes/:id} : Updates an existing ponude.
     *
     * @param id the id of the ponude to save.
     * @param ponude the ponude to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated ponude,
     * or with status {@code 400 (Bad Request)} if the ponude is not valid,
     * or with status {@code 500 (Internal Server Error)} if the ponude couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/ponudes/{id}")
    public ResponseEntity<Ponude> updatePonude(@PathVariable(value = "id", required = false) final Long id, @RequestBody Ponude ponude)
        throws URISyntaxException {
        log.debug("REST request to update Ponude : {}, {}", id, ponude);
        if (ponude.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, ponude.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!ponudeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Ponude result = ponudeRepository.save(ponude);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, ponude.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /ponudes/:id} : Partial updates given fields of an existing ponude, field will ignore if it is null
     *
     * @param id the id of the ponude to save.
     * @param ponude the ponude to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated ponude,
     * or with status {@code 400 (Bad Request)} if the ponude is not valid,
     * or with status {@code 404 (Not Found)} if the ponude is not found,
     * or with status {@code 500 (Internal Server Error)} if the ponude couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/ponudes/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Ponude> partialUpdatePonude(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Ponude ponude
    ) throws URISyntaxException {
        log.debug("REST request to partial update Ponude partially : {}, {}", id, ponude);
        if (ponude.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, ponude.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!ponudeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Ponude> result = ponudeRepository
            .findById(ponude.getId())
            .map(existingPonude -> {
                if (ponude.getSifraPostupka() != null) {
                    existingPonude.setSifraPostupka(ponude.getSifraPostupka());
                }
                if (ponude.getNazivLijeka() != null) {
                    existingPonude.setNazivLijeka(ponude.getNazivLijeka());
                }
                if (ponude.getIznos() != null) {
                    existingPonude.setIznos(ponude.getIznos());
                }

                return existingPonude;
            })
            .map(ponudeRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, ponude.getId().toString())
        );
    }

    /**
     * {@code GET  /ponudes} : get all the ponudes.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of ponudes in body.
     */
    @GetMapping("/ponudes")
    public List<Ponude> getAllPonudes(@RequestParam(required = false, defaultValue = "false") boolean eagerload) {
        log.debug("REST request to get all Ponudes");
        return ponudeRepository.findAllWithEagerRelationships();
    }

    /**
     * {@code GET  /ponudes/:id} : get the "id" ponude.
     *
     * @param id the id of the ponude to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the ponude, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/ponudes/{id}")
    public ResponseEntity<Ponude> getPonude(@PathVariable Long id) {
        log.debug("REST request to get Ponude : {}", id);
        Optional<Ponude> ponude = ponudeRepository.findOneWithEagerRelationships(id);
        return ResponseUtil.wrapOrNotFound(ponude);
    }

    /**
     * {@code DELETE  /ponudes/:id} : delete the "id" ponude.
     *
     * @param id the id of the ponude to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/ponudes/{id}")
    public ResponseEntity<Void> deletePonude(@PathVariable Long id) {
        log.debug("REST request to delete Ponude : {}", id);
        ponudeRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
