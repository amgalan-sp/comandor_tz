package com.example.demo.repository;

import com.example.demo.Good;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GoodRepository extends JpaRepository<Good, Integer>{
    Good findByName(String name);
}
