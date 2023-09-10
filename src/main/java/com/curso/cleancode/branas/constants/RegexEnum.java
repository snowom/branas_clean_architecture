package com.curso.cleancode.branas.constants;

import lombok.Getter;

public enum RegexEnum {
    VALIDATE_NAME_CARACTERS("^[A-Za-záàâãéèêíïóôõöúçñÁÀÂÃÉÈÍÏÓÔÕÖÚÇÑ ]+$"),
    VALIDATE_EMAIL_CARATERES("^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?(?:\\.[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?)*$"),
    VALIDATE_CPF_CARACTERES("[0-9]{3}\\.?[0-9]{3}\\.?[0-9]{3}\\-?[0-9]{2}");

    @Getter
    private String regex;

    RegexEnum(String regex) {
        this.regex = regex;
    }
}
