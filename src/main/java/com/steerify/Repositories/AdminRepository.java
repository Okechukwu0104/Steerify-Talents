package com.steerify.Repositories;

import com.steerify.Dtos.AdminDto;
import com.steerify.Entities.Admin;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AdminRepository extends MongoRepository<Admin, UUID> {
    Optional<Admin> findByEmail(String email);
    boolean existsByEmail(String email);
    Optional<Admin> findByFirstNameIgnoreCase(String adminName);
    List<Admin> findByLastNameContainingIgnoreCase(String adminName);
}