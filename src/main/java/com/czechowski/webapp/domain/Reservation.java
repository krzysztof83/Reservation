package com.czechowski.webapp.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Entity
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    private Date start;
    @NotNull
    private Long time=null;

    public Reservation() {
    }

    public Reservation(Date start, Long time) {
        this.start =start;
        this.time = time;
    }

    public Long getId() {
        return id;
    }

    public Reservation setId(Long id) {
        this.id = id;
        return this;
    }

    public Date getStart() {
        return start;
    }

    public Reservation setStart(Date start) {
        this.start =start;
        return this;
    }

    public Long getTime() {
        return time;
    }

    public Reservation setTime(Long time) {
        this.time = time;
        return this;
    }

    public String toString(){
        String s="id= "+this.getId()+" date= "+this.getStart()+" time= "+this.getTime();
        return s;
    }
}
