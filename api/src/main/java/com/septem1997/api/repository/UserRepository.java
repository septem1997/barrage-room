package com.septem1997.api.repository;

import com.septem1997.api.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    User findUserByUsernameAndIsDeleteFalse(String username);

}

