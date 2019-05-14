package ru.smv.system.restaurant.repository.specification;

import org.springframework.data.jpa.domain.Specification;
import ru.smv.system.restaurant.models.db.MenuEntity;
import ru.smv.system.restaurant.models.db.RestaurantEntity;

import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

//https://www.logicbig.com/tutorials/spring-framework/spring-data/specifications.html
public class AllSpecification {

    public static Specification<RestaurantEntity> getRestaurantWithMenuByDate(LocalDate dateReporting){
        return (root, criteriaQuery, criteriaBuilder) -> {
            Join<RestaurantEntity,MenuEntity> menuJoin = root.join("menus");
            Predicate predicateMenu = criteriaBuilder.equal(menuJoin.get("dateMenu"), dateReporting);
            Predicate predicateRestaurant = criteriaBuilder.lessThanOrEqualTo(root.get("createDate"), LocalDateTime.of(dateReporting,LocalTime.MAX));

            return criteriaBuilder.and(predicateMenu,predicateRestaurant);
        };
    }

}
