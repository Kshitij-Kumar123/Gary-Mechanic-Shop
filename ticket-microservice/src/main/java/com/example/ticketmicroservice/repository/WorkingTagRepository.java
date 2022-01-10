package com.example.ticketmicroservice.repository;

import com.example.ticketmicroservice.model.WorkingTag;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WorkingTagRepository extends JpaRepository<WorkingTag, Long> {
}