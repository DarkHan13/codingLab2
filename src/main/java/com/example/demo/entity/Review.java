package com.example.demo.entity;


import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(columnDefinition = "text")
    private String text;
    @ManyToOne(fetch = FetchType.EAGER)
    private Feedback feedback;
}
