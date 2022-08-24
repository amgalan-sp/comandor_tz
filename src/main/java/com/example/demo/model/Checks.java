package com.example.demo.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Entity(name="checks")
@Getter
@Setter
@AllArgsConstructor
public class Checks {

    public Checks(LocalDate date, LocalTime  time, double sum) {
        this.date = LocalDate.now();
        this.time = LocalTime.now();
        this.sum = sum;

    }
    @Id
    @Column(name="id", nullable = false, unique = true)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name ="time")
    private LocalTime time;
    @Column(name = "date")
    private LocalDate date;
    @Column(name="total_sum")
    private double sum;

    public Checks() {

    }

    @Override
    public String toString() {
        return "Checks: " +
                "time=" + time +
                ", date=" + date +
                ", sum=" + sum +
                '}';
    }
}
