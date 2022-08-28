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
import java.util.Optional;
import java.util.OptionalDouble;

@Entity(name="checks")
@Getter
@Setter
@AllArgsConstructor
public class Checks {

    public Checks(BigDecimal sum) {
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
    private BigDecimal sum;

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

//    public void setDoubleSum(Optional<Double> aDouble) {
//        this.sum  = OptionalDouble.empty().orElse(0);
//    }
}
