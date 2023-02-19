package com.github.starwacki.account.exceptions;

public class WrongFileException extends RuntimeException{

    public WrongFileException() {
        super();
    }

    public WrongFileException(String message) {
        super(message);
    }

}
