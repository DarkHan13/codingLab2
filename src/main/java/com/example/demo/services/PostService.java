package com.example.demo.services;

import com.example.demo.dto.PostDTO;
import com.example.demo.entity.ImageModel;
import com.example.demo.entity.Post;
import com.example.demo.entity.User;
import com.example.demo.repository.ImageRepository;
import com.example.demo.repository.PostRepository;
import com.example.demo.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.Optional;

@Service
public class PostService {

    public static final Logger LOG = LoggerFactory.getLogger(PostService.class);

    private final PostRepository postRepository;
    private final ImageRepository imageRepository;
    private final UserRepository userRepository;

    @Autowired
    public PostService(PostRepository postRepository, ImageRepository imageRepository, UserRepository userRepository) {
        this.postRepository = postRepository;
        this.imageRepository = imageRepository;
        this.userRepository = userRepository;
    }

    public Post createPost(PostDTO postDTO, Principal principal) {

        User user = getUserByPrincipal(principal);
        Post post = new Post();
        post.setUser(user);
        post.setCaption(postDTO.getCaption());
        post.setLikes(0);

        LOG.info("Saving Post for user: {} ", user.getEmail());
        return postRepository.save(post);

    }

    private User getUserByPrincipal(Principal principal) {
        String username = principal.getName();
        System.out.println(username);
        return userRepository.findUserByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Username not found with username " + username));
    }

    public Post getPostById(Long postId, Principal principal) throws Exception {
        User user = getUserByPrincipal(principal);
        return postRepository.findPostByIdAndUser(postId, user)
                .orElseThrow(() -> new Exception("Post cannot be found for username " + user.getEmail()));
    }


    public void deletePost(Long postId, Principal principal) throws Exception {
        Post post = new Post();
        try {
            post = getPostById(postId, principal);
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
            throw new Exception();
        }
        Optional<ImageModel> imageModel = imageRepository.findByPostId(post.getId());
        postRepository.delete(post);
        imageModel.ifPresent(imageRepository::delete);
    }
}
