package com.example.demo.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;
import java.util.Set;

@Data
public class PostDTO {
    private Long id;
    @NotEmpty
    private String caption;
    private Integer likes;
    private String username;
    private Set<String> usersLiked;
    private LocalDateTime createdDate;
}
