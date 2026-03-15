package com.Travelrithm.repository;

import com.Travelrithm.domain.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
        UserEntity findByName(String name);
        Optional<UserEntity> findByEmail(String email);

        Long userId(Integer userId);
}
