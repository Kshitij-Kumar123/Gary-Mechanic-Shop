package com.example.ticketmicroservice.model;

import com.example.ticketmicroservice.VO.CarProduct;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "Ticket")
public class Ticket {

    @Id
    @SequenceGenerator(
            name="ticket_sequence", sequenceName = "ticket_sequence", allocationSize = 1
    )
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ticket_sequence")
    private Long ticketId;

    private String name;

    private String description;

    @Column(name = "created_on")
    @CreationTimestamp
    private LocalDateTime createdOn;

    @Column(name = "updated_on")
    @UpdateTimestamp
    private LocalDateTime updatedOn;

    @Column(name = "original_price_estimate")
    private String originalPriceEstimate;

    @Column(name = "final_price_estimate")
    private String finalPriceEstimate;

    @Column(name ="due_date")
    private LocalDateTime dueDate;

    @OneToMany(mappedBy = "ticket", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Comment> comments;

    private Long carProductId;

    private Long customerId;

    @Column
    @ElementCollection
    private Collection<Long> technicianIds;

    @ManyToOne
    @JoinColumn(name = "workingTagId")
    private WorkingTag workingTag;

}
