package com.curso.cleancode.branas;

import com.curso.cleancode.branas.dto.user.CreateUserDTO;
import com.curso.cleancode.branas.exceptions.UserException;
import com.curso.cleancode.branas.exceptions.UserNotFoundException;
import com.curso.cleancode.branas.model.User;
import com.curso.cleancode.branas.repository.UserRepository;
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
import java.util.UUID;

public class UserServiceTests extends MockitoExtension{

    @InjectMocks
    private UserService userService;
    @Mock
    private UserRepository mockUserRepository;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void test_create_passanger_success() {
        CreateUserDTO createUserDTO = new CreateUserDTO("John Doe", "john.doe@gmail.com", "539.265.190-96", true);
        User createdUser = User.builder()
            .accountId(UUID.randomUUID().toString())
            .nome(createUserDTO.getNome())
            .email(createUserDTO.getEmail())
            .cpf(createUserDTO.getCpf())
            .isPassenger(createUserDTO.getIsPassenger())
            .build();
        Mockito.when(mockUserRepository.findByEmail(createUserDTO.getEmail())).thenReturn(Optional.empty());
        userService.createPassenger(createUserDTO);
        Assertions.assertNotNull(createdUser);
    }

    @Test
    public void test_create_passanger_fail_exist_user() {
        UserException exception = Assertions.assertThrows(UserException.class, () -> {
            CreateUserDTO createUserDTO = new CreateUserDTO("John Doe", "john.doe@gmail.com", "539.265.190-96", true);
            Mockito.when(mockUserRepository.findByEmail(createUserDTO.getEmail())).thenReturn(Optional.of(new User()));
            userService.createPassenger(createUserDTO);
        });
        Assertions.assertEquals("Usuário já cadastrado com esse e-mail", exception.getMessage());
    }

    @Test
    public void test_create_passanger_fail_invalid_name() {
        UserException exception = Assertions.assertThrows(UserException.class, () -> {
            CreateUserDTO createUserDTO = new CreateUserDTO("John Doe123", null, null, null);
            userService.createPassenger(createUserDTO);
        });
        Assertions.assertEquals("O nome do usuário é inválido", exception.getMessage());
    }

    @Test
    public void test_create_passanger_fail_invalid_email() {
        UserException exception = Assertions.assertThrows(UserException.class, () -> {
            CreateUserDTO createUserDTO = new CreateUserDTO("John Doe", "123", null, null);
            userService.createPassenger(createUserDTO);
        });
        Assertions.assertEquals("O email do usuário é inválido", exception.getMessage());
    }

    @Test
    public void test_create_passanger_fail_invalid_cpf() {
        UserException exception = Assertions.assertThrows(UserException.class, () -> {
            CreateUserDTO createUserDTO = new CreateUserDTO("John Doe", "john.doe@gmail.com", "xpto123", null);
            userService.createPassenger(createUserDTO);
        });
        Assertions.assertEquals("O cpf do usuário é inválido", exception.getMessage());
    }

    @Test
    public void test_get_user() {
        User user = User.builder().accountId("12345").email("john.doe@gmail.com").nome("john doe").cpf("123456789").isPassenger(true).build();
        Mockito.when(this.mockUserRepository.findByAccountId(Mockito.anyString())).thenReturn(Optional.of(user));
        User response = this.userService.getUser("");
        Assertions.assertEquals(response.getAccountId(), user.getAccountId());
        Assertions.assertEquals(response.getEmail(), user.getEmail());
        Assertions.assertEquals(response.getNome(), user.getNome());
        Assertions.assertEquals(response.getCpf(), user.getCpf());
        Assertions.assertEquals(response.getIsPassenger(), user.getIsPassenger());
    }

    @Test
    public void test_get_invalid_user() {
        UserNotFoundException userNotFoundException = Assertions.assertThrows(UserNotFoundException.class, ()-> {
            Mockito.when(this.mockUserRepository.findByAccountId(Mockito.anyString())).thenReturn(Optional.empty());
            this.userService.getUser(Mockito.anyString());
        });
        Assertions.assertEquals("Usuário não encontrado", userNotFoundException.getMessage());
    }
}
