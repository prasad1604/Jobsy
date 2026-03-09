package com.Prasad.Jobsy2.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.Prasad.Jobsy2.entity.ProfileEntity;

@Repository
public interface ProfileRepository extends JpaRepository<ProfileEntity,Long>{

    Optional<ProfileEntity> findByEmail(String email);

    Optional<ProfileEntity> findByActivationToken(String activationToken);
}
