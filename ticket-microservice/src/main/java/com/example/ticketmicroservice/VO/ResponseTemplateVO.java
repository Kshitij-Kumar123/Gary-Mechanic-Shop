package com.example.ticketmicroservice.VO;

import com.example.ticketmicroservice.model.Ticket;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseTemplateVO {
    private CarProduct carProduct;
    private Ticket ticket;
    private AppUser customer;

    private List<AppUser> appUser;

}
