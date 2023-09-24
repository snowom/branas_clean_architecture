package com.curso.cleancode.branas;

import com.curso.cleancode.branas.constants.RideStatusEnum;
import com.curso.cleancode.branas.dto.ride.AcceptRideDTO;
import com.curso.cleancode.branas.dto.ride.RequestRideDTO;
import com.curso.cleancode.branas.exceptions.RideException;
import com.curso.cleancode.branas.exceptions.RideNotFoundException;
import com.curso.cleancode.branas.exceptions.UserException;
import com.curso.cleancode.branas.model.Ride;
import com.curso.cleancode.branas.model.User;
import com.curso.cleancode.branas.repository.RideRepository;
import com.curso.cleancode.branas.service.RideService;
import com.curso.cleancode.branas.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

public class RiderServiceTests extends MockitoExtension {

    @Mock
    private RideRepository rideRepository;
    @Mock
    private UserService userService;
    @InjectMocks
    private RideService rideService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void test_get_ride_success() {
        Ride ride = Ride.builder().build();
        Mockito.when(rideRepository.findByRideId(Mockito.anyString())).thenReturn(Optional.of(ride));
        Ride result = rideService.getRide(Mockito.anyString());
        Assertions.assertNotNull(result);
    }

    @Test
    public void test_get_ride_fail() {
        RideNotFoundException rideNotFoundException = Assertions.assertThrows(RideNotFoundException.class, () -> {
            Mockito.when(rideRepository.findByRideId(Mockito.anyString())).thenReturn(Optional.empty());
            rideService.getRide(Mockito.anyString());
        });
        Assertions.assertEquals("A corrida informada não foi localizada", rideNotFoundException.getMessage());
    }

    @Test
    public void test_request_ride_success() {
        RequestRideDTO mockRideDTO = new RequestRideDTO("123456", (float) 12.5, -12.1, -13.1, 14.1, 15.1);
        User mockPassenger = User.builder().isPassenger(true).build();
        Mockito.when(userService.getUser(Mockito.anyString())).thenReturn(mockPassenger);
        Mockito.when(rideRepository.countPassengerRidesWithStatusNotEquals(Mockito.anyString(), Mockito.anyString())).thenReturn(0);
        String result = rideService.requestRide(mockRideDTO);
        Assertions.assertNotNull(result);
    }

    @Test
    public void test_request_ride_fail_is_passanger() {
        UserException userException = Assertions.assertThrows(UserException.class, () -> {
            RequestRideDTO mockRideDTO = new RequestRideDTO("123456", (float) 12.5, -12.1, -13.1, 14.1, 15.1);
            User mockPassenger = User.builder().isPassenger(false).build();
            Mockito.when(userService.getUser(Mockito.anyString())).thenReturn(mockPassenger);
            rideService.requestRide(mockRideDTO);
        });
        Assertions.assertEquals("O usuário requisitante não tem perfil de passageiro", userException.getMessage());
    }

    @Test
    public void test_request_ride_fail_pendding_ride() {
        RideException rideException = Assertions.assertThrows(RideException.class, () -> {
            RequestRideDTO mockRideDTO = new RequestRideDTO("123456", (float) 12.5, -12.1, -13.1, 14.1, 15.1);
            User mockPassenger = User.builder().isPassenger(true).build();
            Mockito.when(userService.getUser(Mockito.anyString())).thenReturn(mockPassenger);
            Mockito.when(rideRepository.countPassengerRidesWithStatusNotEquals(Mockito.anyString(), Mockito.anyString())).thenReturn(1);
            rideService.requestRide(mockRideDTO);
        });
        Assertions.assertEquals("O passageiro possui corridas pendentes ou em execução", rideException.getMessage());
    }

    @Test
    public void test_is_in_progress_ride_success() {
        RideException rideException = Assertions.assertThrows(RideException.class, () -> {
            Ride ride = Ride.builder().rideStatus("").build();
            rideService.isInProgressRide(ride);
        });
        Assertions.assertEquals("Não é possível começar uma corrida que não está aceita", rideException.getMessage());
    }

    @Test
    public void test_accept_ride_success() {
        AcceptRideDTO acceptRideDTO = new AcceptRideDTO("12345", "12345");
        Ride responseRide = Ride.builder().rideStatus(RideStatusEnum.STATUS_ACCEPTED.getRideStatus()).driverAccountId(acceptRideDTO.getAccountId()).build();
        Mockito.when(userService.getUser(Mockito.anyString())).thenReturn(User.builder().isPassenger(false).build());
        Mockito.when(rideRepository.countDriverPendingRide(Mockito.anyString())).thenReturn(0);
        Mockito.when(rideRepository.findByRideId(acceptRideDTO.getRideId())).thenReturn(
                Optional.of(Ride.builder().rideStatus(RideStatusEnum.STATUS_REQUESTED.getRideStatus()).build())
        );
        Mockito.when(rideRepository.save(Mockito.any(Ride.class))).thenReturn(responseRide);
        rideService.acceptRide(acceptRideDTO);
        Assertions.assertEquals(responseRide.getRideStatus(), RideStatusEnum.STATUS_ACCEPTED.getRideStatus());
        Assertions.assertEquals(responseRide.getDriverAccountId(), acceptRideDTO.getAccountId());
    }

    @Test
    public void test_start_ride_success() {
        String rideId = "12345";
        Ride mockedRide = Ride.builder().rideStatus(RideStatusEnum.STATUS_ACCEPTED.getRideStatus()).build();
        Ride savedRide = Ride.builder().rideStatus(RideStatusEnum.STATUS_IN_PROGRESS.getRideStatus()).build();
        Mockito.when(rideRepository.findByRideId(rideId)).thenReturn(Optional.of(mockedRide));
        Mockito.when(rideRepository.save(Mockito.any(Ride.class))).thenReturn(savedRide);
        rideService.startRide(rideId);
        Assertions.assertEquals(savedRide.getRideStatus(), RideStatusEnum.STATUS_IN_PROGRESS.getRideStatus());
    }

    @Test
    public void test_accept_ride_fail_ride_not_found() {
        RideNotFoundException exception = Assertions.assertThrows(RideNotFoundException.class, () -> {
            AcceptRideDTO acceptRideDTO = new AcceptRideDTO("12345", "12345");
            Mockito.when(userService.getUser(Mockito.anyString())).thenReturn(User.builder().isPassenger(true).build());
            Mockito.when(rideRepository.countDriverPendingRide(Mockito.anyString())).thenReturn(0);
            Mockito.when(rideService.getRide(acceptRideDTO.getRideId())).thenReturn(new Ride());
            rideService.acceptRide(acceptRideDTO);
        });
        Assertions.assertEquals("A corrida informada não foi localizada", exception.getMessage());
    }

    @Test
    public void test_accept_ride_fail_user_is_passenger() {
        UserException exception = Assertions.assertThrows(UserException.class, () -> {
            AcceptRideDTO acceptRideDTO = new AcceptRideDTO("12345", "12345");
            Mockito.when(userService.getUser(Mockito.anyString())).thenReturn(User.builder().isPassenger(true).build());
            rideService.acceptRide(acceptRideDTO);
        });
        Assertions.assertEquals("O usuário do tipo passageiro não pode aceitar corridas", exception.getMessage());
    }

    @Test
    public void test_accept_ride_fail_driver_is_busy() {
        RideException exception = Assertions.assertThrows(RideException.class, () -> {
            AcceptRideDTO acceptRideDTO = new AcceptRideDTO("12345", "12345");
            Mockito.when(userService.getUser(Mockito.anyString())).thenReturn(User.builder().isPassenger(false).build());
            Mockito.when(rideRepository.countDriverPendingRide(acceptRideDTO.getAccountId())).thenReturn(1);
            rideService.acceptRide(acceptRideDTO);
        });
        Assertions.assertEquals("Não é possível aceitar corridas simultâneas", exception.getMessage());
    }

    @Test
    public void test_accept_ride_fail_invalid_ride_status() {
        RideException exception = Assertions.assertThrows(RideException.class, () -> {
            AcceptRideDTO acceptRideDTO = new AcceptRideDTO("12345", "12345");
            Ride mockedRide = Ride.builder().rideStatus("xpto").build();
            Mockito.when(userService.getUser(Mockito.anyString())).thenReturn(User.builder().isPassenger(false).build());
            Mockito.when(rideRepository.countDriverPendingRide(acceptRideDTO.getAccountId())).thenReturn(0);
            Mockito.when(rideRepository.findByRideId(acceptRideDTO.getRideId())).thenReturn(Optional.of(mockedRide));
            rideService.acceptRide(acceptRideDTO);
        });
        Assertions.assertEquals("Não é possível aceitar uma corrida em progresso ou encerrada", exception.getMessage());
    }
}
