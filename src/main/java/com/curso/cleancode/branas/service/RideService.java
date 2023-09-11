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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RideService {

    @Autowired
    RideRepository rideRepository;
    @Autowired
    UserService userService;


    public String requestRide(RequestRideDTO requestRideDTO) {
        this.checkIsPassenger(requestRideDTO.getPassengerId());
        this.checkExistPendingRide(requestRideDTO.getPassengerId());
        Ride ride = RequestRideDTO.parseToEntity(requestRideDTO);
        rideRepository.save(ride);
        return ride.getRideId();
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

    public void acceptRide(AcceptRideDTO acceptRideDTO) {
        this.checkIsDriver(acceptRideDTO.getAccountId());
        this.checkDriverIsFree(acceptRideDTO.getAccountId());
        this.checkValidRideStatus(acceptRideDTO.getRideId());
        Ride ride = this.getRide(acceptRideDTO.getRideId());
        ride.setRideStatus(RideStatusEnum.STATUS_IN_PROGRESS.getRideStatus());
        ride.setDriverAccountId(acceptRideDTO.getAccountId());
        rideRepository.save(ride);
    }

    public Ride getRide(String rideId) {
        Optional<Ride> ride = rideRepository.findByRideId(rideId);
        ride.orElseThrow(() -> new RideNotFoundException("A corrida informada não foi localizada"));
        return ride.get();
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
        Optional<Ride> ride = rideRepository.findByRideId(rideId);
        ride.orElseThrow(() -> new RideNotFoundException("A corrida escolhida não existe"));
        if(!(ride.get().getRideStatus().equals(RideStatusEnum.STATUS_REQUESTED.getRideStatus()))) {
            throw new RideException("Não é possível aceitar uma corrida em progresso ou encerrada");
        }
    }
}
