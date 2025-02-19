package com.elitekaycy.test.entity;

import com.elitekaycy.annotations.Column;
import com.elitekaycy.annotations.Entity;
import com.elitekaycy.annotations.GeneratedValue;
import com.elitekaycy.annotations.Id;
import com.elitekaycy.annotations.Table;

@Entity
@Table(name = "person")
public class Person {

    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "age", nullable = false)
    private Integer age;

    @Column(name = "height", nullable = true)
    private String height;

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
