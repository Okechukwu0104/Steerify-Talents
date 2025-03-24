package com.steerify.Repositories.Reusables;

import com.steerify.Dtos.Reusables.PostDto;
import com.steerify.Entities.Reusables.Post;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;
import java.util.UUID;

public interface PostRepository extends MongoRepository<Post, UUID> {
    Optional<Post> findPostsByNameEqualsIgnoreCase(String name);
}
