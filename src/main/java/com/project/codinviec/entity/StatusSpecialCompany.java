package com.project.codinviec.entity;

import com.project.codinviec.entity.auth.Company;
import com.project.codinviec.entity.key.StatusSpecialCompanyId;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name = "status_special_company", schema = "codinviec_db")
public class StatusSpecialCompany {
    @EmbeddedId
    private StatusSpecialCompanyId id;

    @MapsId("idCompany")
    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "id_company", nullable = false)
    private Company idCompany;

    @MapsId("idStatusSpecial")
    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "id_status_special", nullable = false)
    private StatusSpecial idStatusSpecial;

}