package com.rest.api.rest_api.entities;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table("users")
public record UserEntity( @Id Long id, String email, String name ){};


