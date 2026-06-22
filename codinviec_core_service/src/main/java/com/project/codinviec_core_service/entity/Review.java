package com.project.codinviec_core_service.entity;

import com.project.codinviec_core_service.entity.auth.Company;
import com.project.codinviec_core_service.entity.auth.User;
import jakarta.persistence.*;
import lombok.*;

@Entity(name = "review")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String title;
    private String description;
    private int rated;


    @ManyToOne
    @JoinColumn(name = "company_id")
    private Company company;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

}
