package com.example.ticketmicroservice.repository;

import com.example.ticketmicroservice.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}