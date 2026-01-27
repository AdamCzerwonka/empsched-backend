package com.example.empsched.auth.repository;

import com.example.empsched.auth.entity.PushSubscription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@Transactional(propagation = Propagation.MANDATORY)
public interface PushSubscriptionRepository extends JpaRepository<PushSubscription, UUID> {

    @Query("SELECT ps FROM PushSubscription ps WHERE ps.user.email = :email AND ps.active = true")
    List<PushSubscription> findActiveByUserEmail(@Param("email") String email);

    Optional<PushSubscription> findByEndpoint(String endpoint);

    List<PushSubscription> findByUserId(UUID userId);

    void deleteByEndpoint(String endpoint);
}
