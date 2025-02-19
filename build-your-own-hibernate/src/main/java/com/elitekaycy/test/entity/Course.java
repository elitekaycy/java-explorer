package com.elitekaycy.test.entity;

import com.elitekaycy.annotations.Column;
import com.elitekaycy.annotations.Entity;
import com.elitekaycy.annotations.GeneratedValue;
import com.elitekaycy.annotations.Id;
import com.elitekaycy.annotations.Table;

@Entity
@Table(name = "course")
public class Course {

    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "description", nullable = true)
    private String description;

    public Course(String title, String description) {
        this.title = title;
        this.description = description;
    }

    public Long getId() {
        return id;
    }

}
