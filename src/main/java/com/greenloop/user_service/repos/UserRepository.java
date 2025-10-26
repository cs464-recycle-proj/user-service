package com.greenloop.user_service.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.greenloop.user_service.models.User;
import java.util.*;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    
}
