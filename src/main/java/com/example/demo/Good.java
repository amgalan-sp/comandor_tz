package com.example.demo;

import lombok.*;

import javax.persistence.*;

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
    private Double price;

    @Override
    public String toString() {
        return name;
    }
}
