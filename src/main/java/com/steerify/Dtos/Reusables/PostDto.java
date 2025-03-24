package com.steerify.Dtos.Reusables;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PostDto {
    private UUID postId;
    @NotBlank(message = "Name is required")
    private String name;

    @NotBlank(message = "Title is required")
    private String title;

    @NotBlank(message = "Description is required")
    private String description;

    @NotBlank(message = "Price is required")
    private String price;

    private String phoneNumber;

}
