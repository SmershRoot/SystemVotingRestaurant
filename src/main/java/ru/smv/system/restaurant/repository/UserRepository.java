package ru.smv.system.restaurant.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.query.Param;
import ru.smv.system.restaurant.models.db.UserEntity;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long>, JpaSpecificationExecutor<UserEntity> {

    Optional<UserEntity> findByLogin(@Param("login") String login);

    Optional<UserEntity> findByEmail(@Param("email") String email);

}