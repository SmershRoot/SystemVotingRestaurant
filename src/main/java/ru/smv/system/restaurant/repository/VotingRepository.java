package ru.smv.system.restaurant.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.query.Param;
import ru.smv.system.restaurant.models.db.VotingEntity;

import java.time.LocalDate;
import java.util.Optional;

public interface VotingRepository extends JpaRepository<VotingEntity, Long>, JpaSpecificationExecutor<VotingEntity> {

    Long countByRestaurantIdAndReportDate(
            @Param("restaurant_id") Long restaurantId,
            @Param("report_date") LocalDate reportDate
    );

    Optional<VotingEntity> findByUserIdAndReportDate(
            @Param("user_id") Long userId,
            @Param("report_date") LocalDate reportDate);
}
