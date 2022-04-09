package com.example.demo.entity;


import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class News {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    @Column(columnDefinition = "text")
    private String text;
}
