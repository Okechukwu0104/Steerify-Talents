package com.steerify.Mappers.Reusables;

import com.steerify.Entities.Reusables.Post;
import com.steerify.Dtos.Reusables.PostDto;

public class PostMapper {

    public static PostDto mapPostToDto(Post post) {
        return new PostDto(
                post.getPostId(),
                post.getName(),
                post.getTitle(),
                post.getDescription(),
                post.getPrice(),
                post.getPhoneNumber()
        );
    }

    public static Post mapDtoToPost(PostDto postDto) {
        return new Post(
                postDto.getPostId(),
                postDto.getName(),
                postDto.getTitle(),
                postDto.getDescription(),
                postDto.getPrice(),
                postDto.getPhoneNumber()
        );
    }
}
