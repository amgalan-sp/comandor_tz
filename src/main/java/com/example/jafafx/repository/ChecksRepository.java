package com.example.jafafx.repository;

import com.example.jafafx.model.Checks;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChecksRepository extends JpaRepository<Checks, Integer> {
}
