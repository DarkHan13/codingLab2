package com.example.demo.facade;

import com.example.demo.dto.PostDTO;
import com.example.demo.entity.Post;
import org.springframework.stereotype.Component;

@Component
public class PostFacade {

    public PostDTO postToPostDTO(Post post) {
        PostDTO postDTO = new PostDTO();
        postDTO.setId(post.getId());
        postDTO.setCaption(post.getCaption());
        postDTO.setLikes(post.getLikes());
        postDTO.setUsername(post.getUser().getUsername());
        postDTO.setUsersLiked(post.getLikedUsers());
        postDTO.setCreatedDate(post.getCreatedDate());

        return postDTO;
    }
}
