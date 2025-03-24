package com.steerify.Repositories.Reusables;


import com.steerify.Entities.Reusables.Job;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface JobRepository extends MongoRepository<Job, UUID> {
    List<Job> findByClientId(UUID clientId);
}
