package com.example.demo.services;

import com.example.demo.entity.User;
import com.example.demo.entity.enums.ERole;
import com.example.demo.exceptions.UserExistException;
import com.example.demo.payload.request.SignupRequest;
import com.example.demo.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.UUID;

@Service
public class UserService {

    public static final Logger LOG = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private MailSenderService mailSender;

    @Autowired
    public UserService(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    public User createUser(SignupRequest userIn) throws Exception {



        if (userRepository.existsUserByEmail(userIn.getEmail())) {
            User userDelete = userRepository.findUserByEmail(userIn.getEmail())
                    .orElseThrow(() -> new Exception());
            if (userDelete.getIsActivate()) {
                throw new Exception();
            }
            userRepository.delete(userDelete);

        }

        User user = new User();
        user.setName(userIn.getFirstname());
        user.setEmail(userIn.getEmail());
        user.setLastname(userIn.getLastname());
        user.setUsername(userIn.getUsername());
        user.setPassword(bCryptPasswordEncoder.encode(userIn.getPassword()));
        user.getRole().add(ERole.ROLE_USER);

        user.setActivationCode(UUID.randomUUID().toString());
        String message = String.format(
                "<h1>Hello</h1>, " + user.getUsername()
                + ". Welcome to my test site. Please visit next link: http://localhost:8080/api/auth/activate/"
                        + user.getActivationCode()
        );

        mailSender.send(user.getEmail(), "Activation Code", message);

        try {
            LOG.info("Saving user {}", userIn.getEmail());
            return userRepository.save(user);
        }
        catch (Exception e){
            LOG.error("Error during registration. {}", e.getMessage());
            throw new UserExistException("The user " + user.getUsername() + " already exist. Please check credentials");
        }
    }

    public User getCurrentUser(Principal principal){
        return getUserByPrincipal(principal);
    }


    public void deleteUser(Principal principal) {
        User user = getUserByPrincipal(principal);
        userRepository.delete(user);
    }

    public void activateUser(String code) throws Exception {
        if (code == "") {
            throw new Exception();
        }
        User user = userRepository.findUserByActivationCode(code)
                .orElseThrow(() -> new Exception());

        user.setActivationCode(null);

        user.setIsActivate(true);

        userRepository.save(user);

    }

    public User getAdminRole(Principal principal) {
        User user = getUserByPrincipal(principal);
        user.getRole().add(ERole.ROLE_ADMIN);
        return userRepository.save(user);
    }

    public boolean isActivated(String email) {
        User user = userRepository.findUserByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Username not found with username " + email));
        return user.getIsActivate();
    }

    private User getUserByPrincipal(Principal principal) {
        String username = principal.getName();
        User user = userRepository.findUserByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("Username not found with username " + username));

        return userRepository.findUserByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("Username not found with username " + username));
    }

}
