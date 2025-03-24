package com.steerify.Repositories;


import com.steerify.Entities.JwtUser;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;
import java.util.UUID;

public interface JwtUserRepository extends MongoRepository<JwtUser, UUID> {
    Optional<JwtUser> findByEmail(String email);
}
