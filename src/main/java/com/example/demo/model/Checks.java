package com.example.demo.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Entity(name="checks")
@Getter
@Setter
@AllArgsConstructor
public class Checks {

    public Checks() {
        this.date = LocalDate.now();
        this.time = LocalTime.now();
    }
    @Id
    @Column(name="id", nullable = false, unique = true)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name ="time")
    private LocalTime time;
    @Column(name = "date")
    private LocalDate date;
    @OneToMany
    List<Checklines> checklinesList;


}
