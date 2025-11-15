package com.rest.api.rest_api.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rest.api.rest_api.entities.UserEntity;
import com.rest.api.rest_api.repositories.UserRepository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserRepository repository;


    public UserController(UserRepository repository){
        this.repository = repository;
    }


    @PostMapping
    public Mono<ResponseEntity<UserEntity>> createUser( @RequestBody UserEntity user ){
        return repository.findByEmail(user.email())
            .flatMap(exist -> Mono.error(new RuntimeException("account already exists")))
            .then(repository.save(user)) // encadenar otro mono y ejecutarlo despues de que la operacion anterior se complete
            .map(ResponseEntity::ok) // retorna una nueva respuesta o envoler la respuesta anterior dentro de otra.
            .doOnNext(savedUser -> System.out.println("new user: " + savedUser)) // se ejecuta en segundo plano sin romper el flujo
            .onErrorResume(e -> { // maneja cualquier tipo de excepcion
                System.out.println(e.getMessage());
                return Mono.just(ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .build());
            });
    }

    @GetMapping
    public Flux<UserEntity> findAll(){
        return repository.findAll();
    }

    @GetMapping("/{id}")
    public Mono<UserEntity> findByID( @PathVariable Long id ){
        return repository.findById(id);
    }

    @DeleteMapping("/{id}")
    public Mono<Void> deleteByID( @PathVariable Long id ){
        return repository.deleteById(id);
    }

}
