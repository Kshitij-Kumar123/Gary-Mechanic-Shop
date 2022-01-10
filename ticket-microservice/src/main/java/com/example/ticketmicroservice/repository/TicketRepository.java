package com.example.ticketmicroservice.repository;

import com.example.ticketmicroservice.model.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

public interface TicketRepository extends JpaRepository<Ticket, Long> {
    Ticket findByTicketId(long id);

    List<Ticket> findAllByCarProductId(long carId);

    List<Ticket> findByNameContaining(String keyword);

    List<Ticket> findByDescriptionContaining(String keyword);

    List<Ticket> findAllByWorkingTag(String workingTag);
}