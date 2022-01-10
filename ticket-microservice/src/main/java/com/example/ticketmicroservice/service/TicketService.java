package com.example.ticketmicroservice.service;

import com.example.ticketmicroservice.VO.AppUser;
import com.example.ticketmicroservice.VO.CarProduct;
import com.example.ticketmicroservice.VO.ResponseTemplateVO;
import com.example.ticketmicroservice.model.Comment;
import com.example.ticketmicroservice.model.Ticket;
import com.example.ticketmicroservice.model.WorkingTag;
import com.example.ticketmicroservice.repository.CommentRepository;
import com.example.ticketmicroservice.repository.TicketRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@Slf4j
public class TicketService {

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private HttpHeaders headers;

    public Ticket save(Ticket ticket) {
        return ticketRepository.save(ticket);
    }

    public ResponseTemplateVO findTicketWithCar(Long ticketId, String token) {

        ResponseTemplateVO responseTemplateVO = new ResponseTemplateVO();

        Ticket ticket = ticketRepository.findByTicketId(ticketId);
        if (Objects.nonNull(ticket)) {
            responseTemplateVO.setTicket(ticket);
        }
        if (Objects.nonNull(ticket) && ticket.getCarProductId() != null) {
            try {
                  CarProduct carProduct =
              restTemplate.getForObject("http://CAR-PRODUCT-SERVICE/api/customer_car/" + ticket.getCarProductId().toString(),
                                CarProduct.class);

                if (Objects.nonNull(carProduct)) {
                    responseTemplateVO.setCarProduct(carProduct);
                }
            } catch (HttpClientErrorException | HttpServerErrorException httpClientOrServerExc) {
                System.out.println("httpClientOrServerExc.getStatusCode() = " + httpClientOrServerExc.getStatusCode());
            }
        }

        if (Objects.nonNull(ticket) && ticket.getCustomerId() != null) {
            try {

                HttpHeaders headers = new HttpHeaders();
                headers.set("accessToken", token);

                HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

                ResponseEntity<AppUser> customer = restTemplate.exchange("http://APP-USER-SERVICE/api/user/find?userId=" + ticket.getCustomerId().toString(), HttpMethod.GET, requestEntity, AppUser.class);

                if (Objects.nonNull(customer)) {
                    responseTemplateVO.setCustomer(customer.getBody());
                }
            } catch (HttpClientErrorException | HttpServerErrorException httpClientOrServerExc) {
                System.out.println("httpClientOrServerExc.getStatusCode() = " + httpClientOrServerExc.getStatusCode());
            }
        }

        if (Objects.nonNull(ticket) && ticket.getTechnicianIds() != null) {
            try {
                HttpHeaders headers = new HttpHeaders();
                headers.set("accessToken", token);

                System.out.println("token = " + token);
                HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

                List <AppUser> technicians = new ArrayList<>();
                ticket.getTechnicianIds().forEach(id -> {
                    ResponseEntity<AppUser> technician = restTemplate.exchange("http://APP-USER-SERVICE/api/user/find?userId=" + id.toString(), HttpMethod.GET, requestEntity, AppUser.class);
                    if (Objects.nonNull(technician)) technicians.add(technician.getBody());
                    System.out.println("technician = " + technician.getBody());
                });

                if (Objects.nonNull(technicians)) {
                    responseTemplateVO.setAppUser(technicians);
                }
            } catch (HttpClientErrorException | HttpServerErrorException httpClientOrServerExc) {
                System.out.println("httpClientOrServerExc.getStatusCode() = " + httpClientOrServerExc.getStatusCode());
            }
        }

        return responseTemplateVO;
    }

    public ResponseTemplateVO saveTicketWithCar(Long ticketId, Long carProductId) {
        CarProduct carProduct =
                restTemplate.getForObject("http://CAR-PRODUCT-SERVICE/api/customer_car/" + carProductId.toString(),
                        CarProduct.class);

        Ticket ticket = ticketRepository.findByTicketId(ticketId);

        ResponseTemplateVO responseTemplateVO = new ResponseTemplateVO();

        if (Objects.nonNull(carProduct) && Objects.nonNull(ticket)){
            responseTemplateVO.setTicket(ticket);
            responseTemplateVO.setCarProduct(carProduct);

            ticket.setCarProductId(carProduct.getCarId());
            ticketRepository.save(ticket);
        }

        return responseTemplateVO;

    }


    public String delete(Long deleteTicketId) {
        boolean ticketIsFound = ticketRepository.existsById(deleteTicketId);
        if (ticketIsFound) {
            ticketRepository.deleteById(deleteTicketId);
            return "Ticket id " + deleteTicketId + " deleted";
        }

        return "No ticket found to be deleted";
    }

    public List<Ticket> findAll() {
        return ticketRepository.findAll();
    }

    public List<Ticket> findSortedTickets(String sort, String order, Long carId, String keyword, String workingTag) {
        if (carId != null) {
            return ticketRepository.findAllByCarProductId(carId);
        }

        if (workingTag != null) {
            return ticketRepository.findAllByWorkingTag(workingTag);
        }
        if (keyword != null) {
            List<Ticket> filteredTickets = new ArrayList<>();
            filteredTickets.addAll(ticketRepository.findByNameContaining(keyword));
            filteredTickets.addAll(ticketRepository.findByDescriptionContaining(keyword));

            return ticketRepository.findByNameContaining(keyword);
        }
        return ticketRepository.findAll(Sort.by(sort).descending());
    }

    public Ticket saveComment(Comment comment, Long ticketId, Long userId) {
        Ticket ticketToFind = ticketRepository.findByTicketId(ticketId);

        if (Objects.nonNull(ticketToFind) && Objects.nonNull(comment)) {
            comment.setTicket(ticketToFind);
            comment.setUserId(userId);
            commentRepository.save(comment);
        }
        return ticketToFind;
    }


    public Ticket saveTag(WorkingTag workingTag, Long ticketId) {
        Ticket ticketToFind = ticketRepository.findByTicketId(ticketId);

        if (Objects.nonNull(ticketToFind) && Objects.nonNull(workingTag)) {
            ticketToFind.setWorkingTag(workingTag);
            ticketRepository.save(ticketToFind);
        }
        return ticketToFind;
    }

    @Transactional
    public Ticket editTicket(Ticket ticket, Long userId) {
        Ticket ticketToEdit = ticketRepository.findByTicketId(ticket.getTicketId());

        if (Objects.nonNull(ticketToEdit) && !ticketToEdit.equals(ticket)) {
            ticketToEdit.setCustomerId(ticket.getCustomerId());
            ticketToEdit.setWorkingTag(ticket.getWorkingTag());
            ticketToEdit.setCarProductId(ticket.getCarProductId());
            ticketToEdit.setDescription(ticket.getDescription());
            ticketToEdit.setDueDate(ticket.getDueDate());
            ticketToEdit.setFinalPriceEstimate(ticket.getFinalPriceEstimate());
            ticketToEdit.setOriginalPriceEstimate(ticket.getOriginalPriceEstimate());
            ticketToEdit.setName(ticket.getName());
            ticketToEdit.setTechnicianIds(ticket.getTechnicianIds());
            ticketToEdit.setUpdatedOn(LocalDateTime.now());

            Comment commentToSave = Comment.builder()
                    .ticket(ticketToEdit).description("Auto created history log")
                    .updatedOn(LocalDateTime.now())
                    .build();

            commentToSave.setUserId(userId);

            commentRepository.save(commentToSave);

            ticketRepository.save(ticketToEdit);
        }

        return ticketToEdit;
    }
}
