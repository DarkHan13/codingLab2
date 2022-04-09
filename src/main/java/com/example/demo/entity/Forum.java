package com.example.demo.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
public class Forum {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    @Column(columnDefinition = "text")
    private String caption;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @JsonFormat(pattern = "yyyy-mm-dd HH:mm-ss")
    @Column(updatable = false)
    private LocalDateTime createdDate;

    @OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY, mappedBy = "forum", orphanRemoval = true)
    private List<Answer> answers = new ArrayList<>();

    @PrePersist
    protected void onCreate(){
        this.createdDate = LocalDateTime.now();
    }
}
