package com.ssafy.realrealfinal.userms.db.repository;

import com.ssafy.realrealfinal.userms.db.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {

    User findByProviderId(String providerId);
}
