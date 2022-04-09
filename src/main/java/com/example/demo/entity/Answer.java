package com.example.demo.entity;


import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
public class Answer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String userId;

    @Column(columnDefinition = "text")
    private String message;

    @Column
    @ElementCollection(targetClass = String.class)
    private Set<String> rateUsers = new HashSet<>();

    @ManyToOne(fetch = FetchType.EAGER)
    private Forum forum;

    private LocalDateTime createdDate;

    @PrePersist
    protected void onCreate(){
        this.createdDate = LocalDateTime.now();
    }
}
