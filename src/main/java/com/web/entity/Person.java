package com.web.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.BatchSize;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.sql.Date;
import java.sql.Time;
import java.util.*;

@Entity
@Table(name = "person")
@Getter
@Setter
public class Person {

//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Column(name = "id")
//    private Long id;

    @Id
    @Column(name = "person_id", columnDefinition = "VARCHAR(255)")
    private String personId;

    private String username;

    private String password;

    private String lastName;

    private String firstName;

    private Boolean actived;

    private String phone;

    private String email;

    private String image;

    private byte gender;

    private String providerId;

    @Column(name = "birth_Day")
    @DateTimeFormat(pattern = "dd/MM/yyyy HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private String birthDay;

    @Column(name = "status")
    private boolean status;

    @OneToMany(mappedBy = "poster", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JsonIgnore
    private List<Comment> comments;

    public Person() {
        super();
    }

    @ManyToOne
    @JoinColumn(name = "authority_name")
    private Authority authorities;
}

