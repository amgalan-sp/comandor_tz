package com.example.jafafx.model;

import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity(name = "goods")
@Getter
@Setter
@RequiredArgsConstructor
@Builder
@AllArgsConstructor

public class Good {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id", nullable = false, unique = true)
    private int id;
    @Column(name = "name")
    private String name;
    @Column(name = "price")
    private BigDecimal price;

    public Good(String name, BigDecimal price) {
        this.name = name;
        this.price = price;
    }

    @Override
    public String toString() {
        return name;
    }
}
