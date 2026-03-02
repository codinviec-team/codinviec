package com.project.codinviec.entity;

import com.project.codinviec.entity.auth.Company;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "company_size")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CompanySize {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private int minEmployees;
    private int maxEmployees;

    @OneToMany(mappedBy = "companySize")
    private List<Company> companies = new ArrayList<>();
}
