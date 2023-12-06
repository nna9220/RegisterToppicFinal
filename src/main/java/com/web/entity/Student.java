package com.web.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import javax.persistence.*;
import lombok.*;
import org.w3c.dom.stylesheets.LinkStyle;

import java.io.Serializable;
import java.util.List;

@Setter
@Getter
@Entity
@Table(name = "student")
@NoArgsConstructor
@AllArgsConstructor
public class Student implements Serializable {
    @Id
    @Column(name = "student_id", columnDefinition = "VARCHAR(255)")
    private String studentId;

    @OneToOne
    @JoinColumn(name = "student_id")
    private Person person;

    @Column(name = "major", length = 50)
    @Enumerated(EnumType.STRING)
    private Major major;

    @ManyToOne
    @JoinColumn(name="class_id")
    private StudentClass studentClass;

    @ManyToOne
    @JoinColumn(name="year_id")
    private SchoolYear schoolYear;

    @ManyToOne
    @JoinColumn(name="subject_id")
    private Subject subjectId;

    @OneToMany(mappedBy = "assignTo", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Task> tasks;
}
