package com.steerify.Repositories.Reusables;
import com.steerify.Entities.Reusables.Application;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ApplicationRepository extends MongoRepository<Application, UUID> {
    List<Application> findByTalentId(UUID talentId);
    List<Application> findByJobId(UUID jobId);
    @Query("{ 'coverLetter' : { $regex: ?0, $options: 'i' } }")
    List<Application> findByCoverLetterContaining(String searchTerm);
}
