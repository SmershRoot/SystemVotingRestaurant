package ru.smv.system.restaurant.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import ru.smv.system.restaurant.models.db.UserEntity;

public interface UserRepository extends JpaRepository<UserEntity, Long> {

}
