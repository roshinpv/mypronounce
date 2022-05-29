package com.wf.pronounce.service;

import com.wf.pronounce.service.dto.PronounceDTO;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Interface for managing {@link com.wf.pronounce.domain.Pronounce}.
 */
public interface PronounceService {
    /**
     * Save a pronounce.
     *
     * @param pronounceDTO the entity to save.
     * @return the persisted entity.
     */
    Mono<PronounceDTO> save(PronounceDTO pronounceDTO);

    /**
     * Updates a pronounce.
     *
     * @param pronounceDTO the entity to update.
     * @return the persisted entity.
     */
    Mono<PronounceDTO> update(PronounceDTO pronounceDTO);

    /**
     * Partially updates a pronounce.
     *
     * @param pronounceDTO the entity to update partially.
     * @return the persisted entity.
     */
    Mono<PronounceDTO> partialUpdate(PronounceDTO pronounceDTO);

    /**
     * Get all the pronounces.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Flux<PronounceDTO> findAll(Pageable pageable);

    /**
     * Returns the number of pronounces available.
     * @return the number of entities in the database.
     *
     */
    Mono<Long> countAll();

    /**
     * Get the "id" pronounce.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Mono<PronounceDTO> findOne(Long id);

    /**
     * Delete the "id" pronounce.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    Mono<Void> delete(Long id);
}
