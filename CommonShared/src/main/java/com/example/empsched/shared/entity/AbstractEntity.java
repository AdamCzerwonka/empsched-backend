package com.example.empsched.shared.entity;

import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Version;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.proxy.HibernateProxy;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

@Getter
@Setter
@ToString
@AllArgsConstructor
@MappedSuperclass
@NoArgsConstructor
public abstract class AbstractEntity {
    @Id
    private UUID id;
    @Version
    private Long version;
    @CreationTimestamp
    private LocalDateTime createdAt;
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    protected AbstractEntity(UUID id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || this.getClass() == o.getClass()) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy proxy ?
                proxy.getHibernateLazyInitializer()
                        .getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy proxy ?
                proxy.getHibernateLazyInitializer()
                        .getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        AbstractEntity that = (AbstractEntity) o;
        return getId() != null && Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return this instanceof HibernateProxy proxy ? proxy.getHibernateLazyInitializer()
                .getPersistentClass()
                .hashCode() : getClass().hashCode();
    }
}
