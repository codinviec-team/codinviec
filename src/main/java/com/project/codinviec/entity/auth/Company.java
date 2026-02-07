package com.project.codinviec.entity.auth;

import com.project.codinviec.entity.*;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "company")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Company {
    @Id
    private String id;
    private String name;
    private String description;
    private String website;
    private String logo;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;

    @ManyToOne
    @JoinColumn(name = "company_size_id")
    private CompanySize companySize;

    @OneToMany(mappedBy = "company", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)

    private Set<Review> listReview = new HashSet<>();

    @OneToMany(mappedBy = "company", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Job> jobs = new HashSet<>();

    @OneToMany(mappedBy = "company", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    Set<CompanyAddress> companyAddresses = new HashSet<>();

    @OneToMany(mappedBy = "idCompany", fetch = FetchType.LAZY,cascade = CascadeType.ALL, orphanRemoval = true)
    Set<StatusSpecialCompany> statusSpecialCompanies = new HashSet<>();
}
