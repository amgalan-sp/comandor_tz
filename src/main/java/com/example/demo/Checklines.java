package com.example.demo;

import lombok.*;

import javax.persistence.*;

@Entity(name = "checklines")
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Builder
@AllArgsConstructor
@IdClass(ChecklinesId.class)
public class Checklines {
    @Id
    @ManyToOne
    @JoinColumn(name="receipt_id", nullable = false)
    private Checks check;
    @Id
    @OneToOne()
    @JoinColumn(name = "goods_id",nullable = false)
    private Good good;
    @Id
    @Column(name="line_number", nullable = false)
    private int lineNumber;
    @Column(name="count")
    private int count;
    @Column(name="sum")
    private double sum;
}
