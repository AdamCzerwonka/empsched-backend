package com.example.empsched.organisation.entity;

import com.example.empsched.shared.entity.AbstractEntity;
import com.example.empsched.shared.entity.OrganisationPlan;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.proxy.HibernateProxy;

import java.util.Objects;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(indexes = {
        @Index(columnList = "name", name = "idx_organisation_name")
})
public class Organisation extends AbstractEntity {
    @Column(name = "name", unique = true, nullable = false, length = 100)
    private String name;

    @Column(name = "max_employees", nullable = false)
    private int maxEmployees;

    @Column(name = "owner_id", nullable = false)
    private UUID ownerId;

    @Column(name = "plan", nullable = false)
    @Enumerated(EnumType.STRING)
    private OrganisationPlan plan;

    public Organisation(String name, int maxEmployees, UUID ownerId, OrganisationPlan plan) {
        super(UUID.randomUUID());
        this.name = name;
        this.maxEmployees = maxEmployees;
        this.ownerId = ownerId;
        this.plan = plan;
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || this.getClass() != o.getClass()) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy proxy ?
                proxy.getHibernateLazyInitializer()
                        .getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy proxy ?
                proxy.getHibernateLazyInitializer()
                        .getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        Organisation that = (Organisation) o;
        return getId() != null && Objects.equals(getId(), that.getId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy proxy ? proxy.getHibernateLazyInitializer()
                .getPersistentClass()
                .hashCode() : getClass().hashCode();
    }
}
