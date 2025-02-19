package com.elitekaycy.test.entity.rocket;

import com.elitekaycy.annotations.Column;
import com.elitekaycy.annotations.Entity;
import com.elitekaycy.annotations.GeneratedValue;
import com.elitekaycy.annotations.Id;
import com.elitekaycy.annotations.Table;

@Entity
@Table(name = "rocket")
public class Rocket {

    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "height")
    private String height;

    @Column(name = "width")
    private String width;

    @Column(name = "velocity")
    private int velocity;

}
