package com.steerify.Repositories;

import com.steerify.Entities.Client;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ClientRepository extends MongoRepository<Client, UUID> {
    Optional<Client> findByEmail(String email);
    List<Client> findByCompanyNameContainingIgnoreCase(String nameOfCompany);
    boolean existsByEmail(String email);
}
