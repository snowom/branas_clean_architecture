package com.curso.cleancode.branas.service;

import com.curso.cleancode.branas.constants.RideStatusEnum;
import com.curso.cleancode.branas.dto.ride.AcceptRideDTO;
import com.curso.cleancode.branas.dto.ride.RequestRideDTO;
import com.curso.cleancode.branas.exceptions.RideException;
import com.curso.cleancode.branas.exceptions.RideNotFoundException;
import com.curso.cleancode.branas.exceptions.UserException;
import com.curso.cleancode.branas.model.Ride;
import com.curso.cleancode.branas.model.User;
import com.curso.cleancode.branas.repository.RideRepository;
import com.curso.cleancode.branas.utils.RideUtils;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.Optional;

@Service
public class RideService {

    private final RideRepository rideRepository;
    private final UserService userService;

    RideService(RideRepository rideRepository, UserService userService) {
        this.rideRepository = rideRepository;
        this.userService = userService;
    }

    public String requestRide(RequestRideDTO requestRideDTO) {
        this.checkIsPassenger(requestRideDTO.getPassengerId());
        this.checkExistPendingRide(requestRideDTO.getPassengerId());
        Ride ride = RequestRideDTO.parseToEntity(requestRideDTO);
        rideRepository.save(ride);
        return ride.getRideId();
    }

    public void acceptRide(AcceptRideDTO acceptRideDTO) {
        this.checkIsDriver(acceptRideDTO.getAccountId());
        this.checkDriverIsFree(acceptRideDTO.getAccountId());
        this.checkValidRideStatus(acceptRideDTO.getRideId());
        Ride ride = this.getRide(acceptRideDTO.getRideId());
        ride.setRideStatus(RideStatusEnum.STATUS_ACCEPTED.getRideStatus());
        ride.setDriverAccountId(acceptRideDTO.getAccountId());
        rideRepository.save(ride);
    }

    public void startRide(String rideId) {
        Ride ride = this.getRide(rideId);
        this.isAcceptedRide(ride);
        ride.setRideStatus(RideStatusEnum.STATUS_IN_PROGRESS.getRideStatus());
        rideRepository.save(ride);
    }

    public Ride getRide(String rideId) {
        Optional<Ride> ride = rideRepository.findByRideId(rideId);
        ride.orElseThrow(() -> new RideNotFoundException("A corrida informada não foi localizada"));
        return ride.get();
    }

    public Ride finishRide(String rideId) {
        Ride ride = this.getRide(rideId);
        this.checkIsInProgressRide(ride);
        Float distanceRide = RideUtils.calculateKilometerDistance(ride.getFromLat(), ride.getFromLong(), ride.getToLat(), ride.getToLong());
        BigDecimal fare = this.calculateTotalRideFare(distanceRide);
        ride.setRideStatus(RideStatusEnum.STATUS_COMPLETED.getRideStatus());
        ride.setFare(fare);
        rideRepository.save(ride);
        return ride;
    }

    private void checkIsInProgressRide(Ride ride) {
        if(!ride.getRideStatus().equals(RideStatusEnum.STATUS_IN_PROGRESS.getRideStatus())) {
            throw new RideException("A corrida deve estar em progresso para ser finalizada.");
        }
    }

    private BigDecimal calculateTotalRideFare(Float totalDistanceRide) {
        return BigDecimal.valueOf(totalDistanceRide * 2.1);
    }

    private void checkIsPassenger(String accountId) {
        User user = userService.getUser(accountId);
        if(!user.getIsPassenger()) {
            throw new UserException("O usuário requisitante não tem perfil de passageiro");
        }
    }

    private void checkExistPendingRide(String accountId) {
        Integer rides = rideRepository.countPassengerRidesWithStatusNotEquals(accountId, RideStatusEnum.STATUS_COMPLETED.getRideStatus());
        if(rides>0) {
            throw new RideException("O passageiro possui corridas pendentes ou em execução");
        }
    }

    private void checkIsDriver(String accountId) {
        User user = userService.getUser(accountId);
        if(user.getIsPassenger()) {
            throw new UserException("O usuário do tipo passageiro não pode aceitar corridas");
        }
    }

    private void checkDriverIsFree(String accountId) {
        Integer rides = rideRepository.countDriverPendingRide(accountId);
        if(rides > 0) {
            throw new RideException("Não é possível aceitar corridas simultâneas");
        }
    }

    private void checkValidRideStatus(String rideId) {
        Ride ride = this.getRide(rideId);
        if(!(ride.getRideStatus().equals(RideStatusEnum.STATUS_REQUESTED.getRideStatus()))) {
            throw new RideException("Não é possível aceitar uma corrida em progresso ou encerrada");
        }
    }

    public void isAcceptedRide(Ride ride) {
        if(!ride.getRideStatus().equals(RideStatusEnum.STATUS_ACCEPTED.getRideStatus())) {
            throw new RideException("Não é possível começar uma corrida que não está aceita");
        }
    }
}
