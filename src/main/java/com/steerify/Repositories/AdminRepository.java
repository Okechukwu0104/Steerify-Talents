package com.steerify.Repositories;


import com.steerify.Entities.Admin;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;
import java.util.UUID;

public interface AdminRepository extends MongoRepository<Admin, UUID> {
    Optional<Admin> findByAdminEmail(String adminEmail);
}
