package com.wf.pronounce.repository;

import com.wf.pronounce.domain.Pronounce;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data SQL reactive repository for the Pronounce entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PronounceRepository extends ReactiveCrudRepository<Pronounce, Long>, PronounceRepositoryInternal {
    Flux<Pronounce> findAllBy(Pageable pageable);

    @Override
    <S extends Pronounce> Mono<S> save(S entity);

    @Override
    Flux<Pronounce> findAll();

    @Override
    Mono<Pronounce> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);
}

interface PronounceRepositoryInternal {
    <S extends Pronounce> Mono<S> save(S entity);

    Flux<Pronounce> findAllBy(Pageable pageable);

    Flux<Pronounce> findAll();

    Mono<Pronounce> findById(Long id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<Pronounce> findAllBy(Pageable pageable, Criteria criteria);

}
