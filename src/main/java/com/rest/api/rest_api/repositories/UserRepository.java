package com.rest.api.rest_api.repositories;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import com.rest.api.rest_api.entities.UserEntity;

import reactor.core.publisher.Mono;



public interface UserRepository extends ReactiveCrudRepository<UserEntity, Long> {
    public Mono<UserEntity> findByEmail(String email);
}
