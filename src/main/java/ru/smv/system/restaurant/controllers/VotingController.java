package ru.smv.system.restaurant.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.util.Assert;
import org.springframework.web.server.ResponseStatusException;
import ru.smv.system.restaurant.constants.AccessPath;
import ru.smv.system.restaurant.exception.NotFoundException;
import ru.smv.system.restaurant.models.db.RestaurantEntity;
import ru.smv.system.restaurant.models.db.UserEntity;
import ru.smv.system.restaurant.models.db.VotingEntity;
import ru.smv.system.restaurant.models.dto.RestaurantDTO;
import ru.smv.system.restaurant.repository.RestaurantRepository;
import ru.smv.system.restaurant.repository.UserRepository;
import ru.smv.system.restaurant.repository.VotingRepository;
import ru.smv.system.restaurant.security.AuthorizedUser;
import ru.smv.system.restaurant.security.SecurityUtils;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

@RestController
public class VotingController {

    private final VotingRepository votingRepository;

    private final RestaurantRepository restaurantRepository;

    private final UserRepository userRepository;

    @Autowired
    public VotingController(VotingRepository votingRepository, RestaurantRepository restaurantRepository, UserRepository userRepository) {
        this.votingRepository = votingRepository;
        this.restaurantRepository = restaurantRepository;
        this.userRepository = userRepository;
    }

    @RequestMapping(path = AccessPath.API_RESTAURANTS_SUD_VOTING, method = RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void voting(
            @PathVariable Long restaurantId
    ) throws NotFoundException {
        AuthorizedUser currentUser = SecurityUtils.currentAuthentication();
        if(currentUser == null){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Пользователь не авторизован");
        }

        Assert.notNull(restaurantId, "Параметр строки обращения не корректен.");

        RestaurantEntity restaurantEntity = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new NotFoundException(
                        "Ресторан не найден. Возможно данный ресторан отсутствует в системе."));

        UserEntity userEntity = userRepository.findByLogin(currentUser.getUsername())
                .orElseThrow(() -> new NotFoundException(
                "Пользователь под которым осуществелн вход не найден. Возможно данный пользователь отсутствует в системе."));

        Optional<VotingEntity> optionalVotingEntity = votingRepository.findByUserIdAndReportDate(userEntity.getId(), LocalDate.now());
        boolean isNewVoting = !optionalVotingEntity.isPresent();

        if(LocalTime.now().isBefore(LocalTime.of(11,0,0)) && !isNewVoting){
            throw new ResponseStatusException(HttpStatus.LOCKED, "Изменить голос уже нельзя");
        }

        if(!isNewVoting){
            votingRepository.deleteById(optionalVotingEntity.get().getId());
        }

        VotingEntity newVotingEntity = new VotingEntity();
        newVotingEntity.setRestaurant(restaurantEntity);
        newVotingEntity.setReportDate(LocalDate.now());
        newVotingEntity.setUser(userEntity);

        votingRepository.save(newVotingEntity);
    }

    @RequestMapping(path = AccessPath.API_RESTAURANTS_VOTING, method = RequestMethod.GET)
    public Map<RestaurantDTO, Long> getResultVoting(
            @PathVariable(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate reportDate
    ){
        Map<RestaurantDTO, Long> result = new HashMap<>();

        List<RestaurantEntity> allRestaurant = restaurantRepository.findAll();
        allRestaurant.forEach((restaurantEntity) -> result.put(new RestaurantDTO(restaurantEntity),
                votingRepository.countByRestaurantIdAndReportDate(restaurantEntity.getId(),(reportDate == null?LocalDate.now():reportDate))));

        return result.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
    //            .limit()
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
    }

}
