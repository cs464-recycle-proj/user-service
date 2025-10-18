package com.greenloop.user_service.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import com.greenloop.user_service.models.User;
import java.util.*;


public interface UserRepository extends JpaRepository<User, UUID> {
}
