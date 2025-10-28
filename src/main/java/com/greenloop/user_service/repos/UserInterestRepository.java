package com.greenloop.user_service.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.greenloop.user_service.enums.Interest;
import com.greenloop.user_service.models.UserInterest;
import java.util.*;

@Repository
public interface UserInterestRepository extends JpaRepository<UserInterest, UUID> {
    List<UserInterest> findByUserId(UUID userId);

    UserInterest findByUserIdAndInterest(UUID userId, String interest);

    boolean existsByUserIdAndInterest(UUID userId, Interest interest);
}
