package com.curso.cleancode.branas.service;

import com.curso.cleancode.branas.constants.RegexEnum;
import com.curso.cleancode.branas.dto.CreatePassengerDTO;
import com.curso.cleancode.branas.exceptions.PassengerException;
import com.curso.cleancode.branas.exceptions.PassengerNotFoundException;
import com.curso.cleancode.branas.model.Passenger;
import com.curso.cleancode.branas.repository.PassengerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class PassengerService {

    @Autowired
    PassengerRepository passengerRepository;

    public void createPassenger(CreatePassengerDTO createPassengerDTO) {
        this.validateWithRegex(createPassengerDTO.getNome(), RegexEnum.VALIDATE_NAME_CARACTERS);
        this.validateWithRegex(createPassengerDTO.getEmail(), RegexEnum.VALIDATE_EMAIL_CARATERES);
        this.validateWithRegex(createPassengerDTO.getCpf(), RegexEnum.VALIDATE_CPF_CARACTERES);
        this.checkExistPassenger(createPassengerDTO.getEmail());
        Passenger passenger = CreatePassengerDTO.parseToEntity(createPassengerDTO);
        passengerRepository.save(passenger);
    }

    public Passenger getPassenger(String accountId) {
        Optional<Passenger> passenger = passengerRepository.accountId(accountId);
        if(passenger.isEmpty()) {
            throw new PassengerNotFoundException("Passageiro não encontrado");
        }
        return passenger.get();
    }

    private void checkExistPassenger(String passengerEmail) {
        Optional<Passenger> passenger = passengerRepository.findByEmail(passengerEmail);
        if(passenger.isPresent()) {
            throw new PassengerException("Passageiro já cadastrado com esse e-mail");
        }
    }

    private void validateWithRegex(String stringToValidate, RegexEnum regexValidator) {
        Pattern pattern = Pattern.compile(regexValidator.getRegex());
        Matcher matcher = pattern.matcher(stringToValidate);
        if(!matcher.find()) {
            switch (regexValidator.name()) {
                case "VALIDATE_NAME_CARACTERS" -> throw new PassengerException("O nome do passageiro é inválido");
                case "VALIDATE_EMAIL_CARATERES" -> throw new PassengerException("O email do passageiro é inválido");
                case "VALIDATE_CPF_CARACTERES" -> throw new PassengerException("O cpf do passageiro é inválido");
                default -> throw new PassengerException(String.format("O dado \"%s\" é inválido", stringToValidate));
            }
        }
    }
}
