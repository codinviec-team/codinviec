package com.project.codinviec.entity.auth;

import com.project.codinviec.entity.Review;
import com.project.codinviec.entity.WishlistCandidate;
import com.project.codinviec.entity.WishlistJob;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.domain.Persistable;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "user")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User implements Persistable<String> {
    @Id
    private String id;
    private String email;
    private String password;
    private String firstName;
    private String lastName;
    private String avatar;
    private String phone;
    private String gender;
    private String education;
    private String address;
    private String websiteLink;
    private LocalDateTime birthDate;
    private Boolean  isFindJob;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;
    private String updatedPerson;
    private String groupSoftSkill;
    private Boolean  isBlock;
    private String cv;

    @ManyToOne
    @JoinColumn(name = "company_id")
    private Company company;

    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role role;

    @OneToMany(mappedBy = "user")
    private List<Review> listReview;

    // ds wishlist của bản thân
    @OneToMany(mappedBy = "userHr", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<WishlistCandidate> wishlistCandidates = new ArrayList<>();

    // bị wistlist
    @OneToMany(mappedBy = "userCandidate", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<WishlistCandidate> wishedByHRs = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<WishlistJob> listWishlistJob = new ArrayList<>();


//    xác định không phải user cũ để tránh update
    @Transient
    private boolean isNew = true;

    @Override
    public boolean isNew() {
        return isNew;
    }

    @PostLoad
    @PostPersist
    void markNotNew() {
        this.isNew = false;
    }
}
