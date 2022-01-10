package com.example.ticketmicroservice.VO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AppUser {
    private Long userId;

    private String name;

    private String username;

    private String description;
}
