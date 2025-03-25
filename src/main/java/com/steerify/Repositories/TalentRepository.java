package com.steerify.Repositories;


import com.steerify.Entities.Talent;
import jakarta.validation.constraints.Email;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;
import java.util.UUID;

public interface TalentRepository extends MongoRepository<Talent, UUID> {
    Optional<Talent> findByEmail(String email);
    boolean existsByEmail(@Email(message = "Email is required") String email);
}
