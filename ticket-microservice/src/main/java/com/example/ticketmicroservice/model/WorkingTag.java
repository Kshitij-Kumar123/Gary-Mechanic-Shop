package com.example.ticketmicroservice.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "WorkingTag")
public class WorkingTag {
    @Id
    @SequenceGenerator(
            name="working_tag_sequence", sequenceName = "working_tag_sequence", allocationSize = 1
    )
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "working_tag_sequence")
    private Long tagId;

    private String title;

    private String description;


    @OneToMany(mappedBy = "workingTag", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Ticket> tickets;

}
