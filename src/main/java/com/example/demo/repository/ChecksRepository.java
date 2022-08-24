package com.example.demo.repository;

import com.example.demo.model.Checks;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChecksRepository extends JpaRepository<Checks, Integer> {
}
