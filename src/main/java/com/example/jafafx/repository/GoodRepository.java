package com.example.jafafx.repository;

import com.example.jafafx.model.Good;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GoodRepository extends JpaRepository<Good, Integer>{
    Good findByName(String name);
}
