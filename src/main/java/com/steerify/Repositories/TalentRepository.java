package com.steerify.Repositories;


import com.steerify.Entities.Talent;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;
import java.util.UUID;

public interface TalentRepository extends MongoRepository<Talent, UUID> {
    Optional<Talent> findByEmailAddress(String email);
}
