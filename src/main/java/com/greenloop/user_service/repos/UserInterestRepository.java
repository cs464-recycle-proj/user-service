package com.greenloop.user_service.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import com.greenloop.user_service.models.UserInterest;
import java.util.*;

public interface UserInterestRepository extends JpaRepository<UserInterest, UUID>{
    List<UserInterest> findByUserId(UUID userId);
}
