package com.steerify.Mappers.Reusables;

import com.steerify.Entities.Reusables.Post;
import com.steerify.Dtos.Reusables.PostDto;

import java.util.UUID;

public class PostMapper {

    public static PostDto mapPostToDto(Post post) {
        return new PostDto(
                post.getPostId(),
                post.getUserId(),
                post.getName(),
                post.getTitle(),
                post.getDescription(),
                post.getPrice(),
                post.getPhoneNumber(),
                post.getRole()
        );
    }

    public static Post mapDtoToPost(PostDto postDto) {
        return new Post(
                postDto.getPostId() != null ? postDto.getPostId() : UUID.randomUUID(),
                postDto.getUserId(),
                postDto.getName(),
                postDto.getTitle(),
                postDto.getDescription(),
                postDto.getPrice(),
                postDto.getPhoneNumber(),
                postDto.getRole()
        );
    }
}
