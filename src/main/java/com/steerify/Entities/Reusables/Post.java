package com.steerify.Entities.Reusables;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.UUID;


@Getter
@Setter
@AllArgsConstructor
@Document(collection = "Talent_posts")
public class Post {
    @Id
    private UUID postId;

    private UUID userId;
    private String name;
    private String title;
    private String description;
    private String price;
    private String phoneNumber;
    private String role;

}
