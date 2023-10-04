package com.example.user_validator.repository;

import com.example.user_validator.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface UserRepository extends JpaRepository<User, Integer> {
    List<User>findUserByBirthDayBetween(LocalDate fromDate, LocalDate toDate);
}
