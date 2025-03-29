package com.steerify.Services;

import com.steerify.Dtos.Reusables.PostDto;
import com.steerify.Enums.Role;

import java.util.List;
import java.util.UUID;

public interface PostServices {

    PostDto createPost(PostDto postDto, String firstName, String lastName, UUID userId, String phoneNumber, Role role);
    List<PostDto> getAllPosts();
    PostDto getPostById(UUID postId);
    PostDto updatePost(UUID postId, PostDto postDto);
    PostDto findPostByTalentName(String name);
    void deletePost(UUID postId);
}
