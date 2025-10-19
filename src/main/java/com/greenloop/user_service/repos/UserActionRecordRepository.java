package com.greenloop.user_service.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import com.greenloop.user_service.models.UserActionRecord;
import java.util.*;


public interface UserActionRecordRepository extends JpaRepository<UserActionRecord, UUID>{
    List<UserActionRecord> findByUserId(UUID userId);
}
