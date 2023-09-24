package com.curso.cleancode.branas.service;

import com.curso.cleancode.branas.constants.RegexEnum;
import com.curso.cleancode.branas.dto.user.CreateUserDTO;
import com.curso.cleancode.branas.exceptions.UserException;
import com.curso.cleancode.branas.exceptions.UserNotFoundException;
import com.curso.cleancode.branas.model.User;
import com.curso.cleancode.branas.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class UserService {

    private final UserRepository userRepository;

    UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void createPassenger(CreateUserDTO createUserDTO) {
        this.validateWithRegex(createUserDTO.getNome(), RegexEnum.VALIDATE_NAME_CARACTERS);
        this.validateWithRegex(createUserDTO.getEmail(), RegexEnum.VALIDATE_EMAIL_CARATERES);
        this.validateWithRegex(createUserDTO.getCpf(), RegexEnum.VALIDATE_CPF_CARACTERES);
        this.checkExistPassenger(createUserDTO.getEmail());
        User user = CreateUserDTO.parseToEntity(createUserDTO);
        userRepository.save(user);
    }

    public User getUser(String accountId) {
        Optional<User> user = userRepository.findByAccountId(accountId);
        if(user.isEmpty()) {
            throw new UserNotFoundException("Usuário não encontrado");
        }
        return user.get();
    }

    private void checkExistPassenger(String passengerEmail) {
        Optional<User> passenger = userRepository.findByEmail(passengerEmail);
        if(passenger.isPresent()) {
            throw new UserException("Usuário já cadastrado com esse e-mail");
        }
    }

    private void validateWithRegex(String stringToValidate, RegexEnum regexValidator) {
        Pattern pattern = Pattern.compile(regexValidator.getRegex());
        Matcher matcher = pattern.matcher(stringToValidate);
        if(!matcher.find()) {
            switch (regexValidator.name()) {
                case "VALIDATE_NAME_CARACTERS" -> throw new UserException("O nome do usuário é inválido");
                case "VALIDATE_EMAIL_CARATERES" -> throw new UserException("O email do usuário é inválido");
                case "VALIDATE_CPF_CARACTERES" -> throw new UserException("O cpf do usuário é inválido");
                default -> throw new UserException(String.format("O dado \"%s\" do usuário é inválido", stringToValidate));
            }
        }
    }
}
