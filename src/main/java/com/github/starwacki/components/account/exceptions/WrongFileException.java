package com.github.starwacki.components.account.exceptions;

public class WrongFileException extends RuntimeException{

    public WrongFileException(String message) {
        super(message);
    }

    public WrongFileException(Code code) {
        super(code.getMessage());
    }
    public WrongFileException(Code code, int line) {
        super(code.getMessage() + line);
    }

    public enum Code {
        LINE("Line is empty or not have call, line number: "),
        YEAR_FORMAT("Wrong year format, line number: "),
        VALIDATION("Incorrect data format, line number: "),
        FILE("File is empty or file can't be read");
        private final String message;
        Code(String message) {
            this.message = message;
        }


        public String getMessage() {
            return message;
        }

    }

}
