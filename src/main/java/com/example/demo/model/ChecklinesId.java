package com.example.demo.model;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;

import java.io.Serializable;

@EqualsAndHashCode
@AllArgsConstructor
@RequiredArgsConstructor
public class ChecklinesId implements Serializable {

    private Checks check;
    private Good good;
    private int lineNumber;

}
