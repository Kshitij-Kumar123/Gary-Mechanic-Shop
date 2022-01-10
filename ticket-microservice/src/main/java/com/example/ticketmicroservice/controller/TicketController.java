package com.example.ticketmicroservice.controller;

import com.example.ticketmicroservice.VO.ResponseTemplateVO;
import com.example.ticketmicroservice.model.Comment;
import com.example.ticketmicroservice.model.Ticket;
import com.example.ticketmicroservice.model.WorkingTag;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.ticketmicroservice.service.TicketService;
import java.util.List;

@RestController
@RequestMapping("/api/ticket")
@Slf4j
public class TicketController {

    @Autowired
    private TicketService ticketService;

    @GetMapping("/all")
    public ResponseEntity<List<Ticket>> getAllTickets() {
        log.info("Find all tickets");
        return new ResponseEntity<List<Ticket>>(ticketService.findAll(), HttpStatus.OK);
    }

    @PostMapping("/save")
    public ResponseEntity<Ticket> save(@RequestBody Ticket ticket) {
        log.info("Save ticket with data");
        return new ResponseEntity<Ticket>(ticketService.save(ticket), HttpStatus.CREATED);
    }

    @GetMapping("/findBy")
    public ResponseEntity<List<Ticket>> findSortedTickets(
            @RequestParam(name = "sort", defaultValue = "ticketId") String sort,
            @RequestParam(name = "order", defaultValue = "asc") String order,
            @RequestParam(name = "carId", required = false) Long carId,
            @RequestParam(name = "keyword", required = false) String keyword,
            @RequestParam(name = "workingTag", required = false) String workingTag

    ) {
        return new ResponseEntity<List<Ticket>>(ticketService.findSortedTickets(sort, order, carId, keyword, workingTag), HttpStatus.OK);
    }

    @PutMapping("/edit")
    public ResponseEntity<Ticket> editTicket(@RequestBody Ticket ticket, @RequestParam("userId") Long userId) {
        return new ResponseEntity<Ticket>(ticketService.editTicket(ticket, userId), HttpStatus.OK);
    }

    @PostMapping("/save/comment")
    public ResponseEntity<Ticket> addCommentToTicket(
            @RequestBody Comment comment,
            @RequestParam(name = "ticketId") Long ticketId,
            @RequestParam(name="userId") Long userId
    ) {
        return new ResponseEntity<Ticket>(ticketService.saveComment(comment, ticketId, userId), HttpStatus.OK);
    }

    @PostMapping("/save/tag")
    public ResponseEntity<Ticket> addTagToTicket(
            @RequestBody WorkingTag workingTag,
            @RequestParam(name = "ticketId") Long ticketId
    ) {
        return new ResponseEntity<Ticket>(ticketService.saveTag(workingTag, ticketId), HttpStatus.OK);
    }

    @GetMapping("{id}")
    public ResponseEntity<ResponseTemplateVO> getTicketWithCar(@PathVariable("id") Long ticketId, @RequestHeader (name="Authorization") String token) {
        log.info("Get ticket with ticket id: " + ticketId);
        System.out.println("token = " + token);
        return new ResponseEntity<ResponseTemplateVO>(ticketService.findTicketWithCar(ticketId, token), HttpStatus.OK);
    }

    @CircuitBreaker(name="ticket_service", fallbackMethod = "ticketFallback")
    @PostMapping("/save/car")
    public ResponseEntity<ResponseTemplateVO> saveCarWithTicket(  @RequestParam(name = "carProductId") Long carProductId,
                                                                  @RequestParam(name = "ticketId") Long ticketId) {
        log.info("Get ticket with ticket id: " + ticketId);

        return new ResponseEntity<ResponseTemplateVO>(ticketService.saveTicketWithCar(ticketId, carProductId), HttpStatus.OK);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<String> deleteTicket(@PathVariable("id") Long ticketId) {
        return new ResponseEntity<String>(ticketService.delete(ticketId), HttpStatus.OK);
    }

    public ResponseEntity<String> ticketFallback(Exception error) {
        return new ResponseEntity<String>("car product service is down ", HttpStatus.EXPECTATION_FAILED);
    }

}
