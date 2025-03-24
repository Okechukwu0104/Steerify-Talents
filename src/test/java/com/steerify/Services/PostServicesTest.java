package com.steerify.Services;

import com.steerify.Dtos.Reusables.PostDto;
import com.steerify.Entities.Reusables.Post;
import com.steerify.Mappers.Reusables.PostMapper;
import com.steerify.Repositories.Reusables.PostRepository;
import com.steerify.Services.impl.PostServicesImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.Import;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DataMongoTest
@Import(PostServicesImpl.class)
public class PostServicesTest {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private PostServices postService;

    private Post post;

    @BeforeEach
    void setUp() {
        post = new Post(
                UUID.randomUUID(),
                "Their Fada",
                "Web Developer",
                "7 years experience in HTML",
                "#1M/hour",
                "897789787"
        );
    }

    @Test
    void createPost_ShouldReturnCreatedPost() {
        PostDto postDto = PostMapper.mapPostToDto(post);
        PostDto result = postService.createPost(postDto);

        assertNotNull(result);
        assertEquals(postDto.getTitle(), result.getTitle());

        Post savedPost = postRepository.findById(result.getPostId()).orElse(null);
        assertNotNull(savedPost);
        assertEquals(postDto.getTitle(), savedPost.getTitle());
    }

    @Test
    void getAllPosts_ShouldReturnListOfPosts() {
        List<PostDto> result = postService.getAllPosts();

        assertNotNull(result);
        System.out.println(result.size());
    }

    @Test
    void getPostById_ShouldReturnPost() {
        PostDto result = postService.getPostById(post.getPostId());

        assertNotNull(result);
        assertEquals(post.getTitle(), result.getTitle());
    }

    @Test
    void getPostById_ShouldThrowExceptionWhenPostNotFound() {
        UUID nonExistentId = UUID.randomUUID();
        assertThrows(RuntimeException.class, () -> postService.getPostById(nonExistentId));
    }

    @Test
    void updatePost_ShouldReturnUpdatedPost() {
        PostDto updatedPostDto = new PostDto(
                post.getPostId(),
                "Okechukwu Peter",
                "Senior Web Developer",
                "Experienced in Spring Boot, React, and AWS. " +
                        "I can fully develop your full stack applications ",
                "$70/hour",
                "13245678"
        );

        PostDto result = postService.updatePost(post.getPostId(), updatedPostDto);
        assertNotNull(result);
        assertEquals(updatedPostDto.getTitle(), result.getTitle());

        Post updatedPost = postRepository.findById(post.getPostId()).orElse(null);
        assertNotNull(updatedPost);
        assertEquals(updatedPostDto.getTitle(), updatedPost.getTitle());
    }

    @Test
    void deletePost_ShouldDeletePost() {
        postService.deletePost(post.getPostId());

        Post deletedPost = postRepository.findById(post.getPostId()).orElse(null);
        assertNull(deletedPost);
    }
}