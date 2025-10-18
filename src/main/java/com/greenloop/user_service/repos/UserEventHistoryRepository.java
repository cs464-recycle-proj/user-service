package com.greenloop.user_service.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import com.greenloop.user_service.models.UserEventHistory;
import java.util.*;


public interface UserEventHistoryRepository extends JpaRepository<UserEventHistory, UUID>{
    List<UserEventHistory> findByUserId(UUID userId);
}
