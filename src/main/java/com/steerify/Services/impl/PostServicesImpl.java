package com.steerify.Services.impl;


import com.steerify.Dtos.Reusables.PostDto;
import com.steerify.Entities.Reusables.Post;
import com.steerify.Entities.Talent;
import com.steerify.Enums.Role;
import com.steerify.Mappers.Reusables.PostMapper;
import com.steerify.Repositories.Reusables.PostRepository;
import com.steerify.Repositories.TalentRepository;
import com.steerify.Services.PostServices;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostServicesImpl implements PostServices {

    private final PostRepository postRepository;
    private final TalentRepository talentRepository;

    public PostDto createPost(Talent talent, PostDto postDto) {
        Post post = PostMapper.mapDtoToPost(postDto);
        post.setName(talent.getFirstName() + " " + talent.getLastName());
        post.setUserId(talent.getId());
        post.setPhoneNumber(talent.getPhoneNumber());

        Post savedPost = postRepository.save(post);
        return PostMapper.mapPostToDto(savedPost);
    }

    @Override
    public PostDto createPost(PostDto postDto, String firstName, String lastName, UUID userId, String phoneNumber, Role role) {
        Post post = PostMapper.mapDtoToPost(postDto);
        post.setName(firstName + " " + lastName);
        post.setUserId(userId);
        post.setPhoneNumber(phoneNumber);
        post.setRole(role.name());
        Post savedPost = postRepository.save(post);
        return PostMapper.mapPostToDto(savedPost);
    }



    public List<PostDto> getAllPosts() {
        return postRepository.findAll()
                .stream()
                .map(PostMapper::mapPostToDto)
                .collect(Collectors.toList());
    }

    public PostDto getPostById(UUID postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));
        return PostMapper.mapPostToDto(post);
    }

    public PostDto updatePost(UUID postId, PostDto postDto) {
        Post existingPost = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));

        if (postDto.getName() != null) {
            existingPost.setName(postDto.getName());
        }
        if (postDto.getTitle() != null) {
            existingPost.setTitle(postDto.getTitle());
        }
        if (postDto.getDescription() != null) {
            existingPost.setDescription(postDto.getDescription());
        }
        if (postDto.getPrice() != null) {
            existingPost.setPrice(postDto.getPrice());
        }

        Post updatedPost = postRepository.save(existingPost);
        return PostMapper.mapPostToDto(updatedPost);
    }

    public PostDto findPostByTalentName(String name){
        Post foundPost = postRepository.findPostsByNameEqualsIgnoreCase(name)
                .orElseThrow(()-> new RuntimeException("cant find post with Name: "+name));
        return PostMapper.mapPostToDto(foundPost);
    }
    public void deletePost(UUID postId) {
        postRepository.deleteById(postId);
    }

}
