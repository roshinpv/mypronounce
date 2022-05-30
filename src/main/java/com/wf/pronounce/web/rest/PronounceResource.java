package com.wf.pronounce.web.rest;

import com.google.protobuf.ByteString;
import com.wf.pronounce.repository.PronounceRepository;
import com.wf.pronounce.service.PronounceService;
import com.wf.pronounce.service.dto.PronounceDTO;
import com.wf.pronounce.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.*;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.reactive.ResponseUtil;

/**
 * REST controller for managing {@link com.wf.pronounce.domain.Pronounce}.
 */
@RestController
@RequestMapping("/api")
public class PronounceResource {

    private final Logger log = LoggerFactory.getLogger(PronounceResource.class);

    private static final String ENTITY_NAME = "pronounce";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PronounceService pronounceService;

    private final PronounceRepository pronounceRepository;

    public PronounceResource(PronounceService pronounceService, PronounceRepository pronounceRepository) {
        this.pronounceService = pronounceService;
        this.pronounceRepository = pronounceRepository;
    }

    /**
     * {@code POST  /pronounces} : Create a new pronounce.
     *
     * @param pronounceDTO the pronounceDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new pronounceDTO, or with status {@code 400 (Bad Request)} if the pronounce has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/pronounces")
    public Mono<ResponseEntity<PronounceDTO>> createPronounce(@RequestBody PronounceDTO pronounceDTO) throws URISyntaxException {
        log.debug("REST request to save Pronounce : {}", pronounceDTO);
        if (pronounceDTO.getId() != null) {
            throw new BadRequestAlertException("A new pronounce cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return pronounceService
            .save(pronounceDTO)
            .map(result -> {
                try {
                    return ResponseEntity
                        .created(new URI("/api/pronounces/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /pronounces/:id} : Updates an existing pronounce.
     *
     * @param id the id of the pronounceDTO to save.
     * @param pronounceDTO the pronounceDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated pronounceDTO,
     * or with status {@code 400 (Bad Request)} if the pronounceDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the pronounceDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/pronounces/{id}")
    public Mono<ResponseEntity<PronounceDTO>> updatePronounce(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody PronounceDTO pronounceDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Pronounce : {}, {}", id, pronounceDTO);
        if (pronounceDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, pronounceDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return pronounceRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return pronounceService
                    .update(pronounceDTO)
                    .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                    .map(result ->
                        ResponseEntity
                            .ok()
                            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
                            .body(result)
                    );
            });
    }

    /**
     * {@code PATCH  /pronounces/:id} : Partial updates given fields of an existing pronounce, field will ignore if it is null
     *
     * @param id the id of the pronounceDTO to save.
     * @param pronounceDTO the pronounceDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated pronounceDTO,
     * or with status {@code 400 (Bad Request)} if the pronounceDTO is not valid,
     * or with status {@code 404 (Not Found)} if the pronounceDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the pronounceDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/pronounces/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<PronounceDTO>> partialUpdatePronounce(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody PronounceDTO pronounceDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Pronounce partially : {}, {}", id, pronounceDTO);
        if (pronounceDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, pronounceDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return pronounceRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<PronounceDTO> result = pronounceService.partialUpdate(pronounceDTO);

                return result
                    .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                    .map(res ->
                        ResponseEntity
                            .ok()
                            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, res.getId().toString()))
                            .body(res)
                    );
            });
    }

    /**
     * {@code GET  /pronounces} : get all the pronounces.
     *
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of pronounces in body.
     */
    @GetMapping("/pronounces")
    public Mono<ResponseEntity<List<PronounceDTO>>> getAllPronounces(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request
    ) {
        log.debug("REST request to get a page of Pronounces");
        return pronounceService
            .countAll()
            .zipWith(pronounceService.findAll(pageable).collectList())
            .map(countWithEntities ->
                ResponseEntity
                    .ok()
                    .headers(
                        PaginationUtil.generatePaginationHttpHeaders(
                            UriComponentsBuilder.fromHttpRequest(request),
                            new PageImpl<>(countWithEntities.getT2(), pageable, countWithEntities.getT1())
                        )
                    )
                    .body(countWithEntities.getT2())
            );
    }

    /**
     * {@code GET  /pronounces/:id} : get the "id" pronounce.
     *
     * @param id the id of the pronounceDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the pronounceDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/pronounces/{id}")
    public Mono<ResponseEntity<PronounceDTO>> getPronounce(@PathVariable Long id) {
        log.debug("REST request to get Pronounce : {}", id);
        Mono<PronounceDTO> pronounceDTO = pronounceService.findOne(id);
        return ResponseUtil.wrapOrNotFound(pronounceDTO);
    }

    /**
     * {@code DELETE  /pronounces/:id} : delete the "id" pronounce.
     *
     * @param id the id of the pronounceDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/pronounces/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public Mono<ResponseEntity<Void>> deletePronounce(@PathVariable Long id) {
        log.debug("REST request to delete Pronounce : {}", id);
        return pronounceService
            .delete(id)
            .map(result ->
                ResponseEntity
                    .noContent()
                    .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
                    .build()
            );
    }


    @GetMapping("/pronounces/user/{login}")
    public Mono<ResponseEntity<PronounceDTO>> getPronounce(@PathVariable String login) {
        log.debug("REST request to get Pronounce : {}", login);
        Mono<PronounceDTO> pronounceDTO = pronounceService.findOne(login);
        return ResponseUtil.wrapOrNotFound(pronounceDTO);
    }


    /*@GetMapping("/pronounces/audio/")
    public Mono<String> getAudio(ServerHttpResponse response , @RequestParam String name , @RequestParam String country ) {

        ZeroCopyHttpOutputMessage zeroCopyResponse = (ZeroCopyHttpOutputMessage) response;
        zeroCopyResponse.

        res.

    }*/


}
