package com.github.starwacki.global.model.grades;

import lombok.Data;
import lombok.Getter;

public enum Degree {
    ONE("1",1),
    ONE_PLUS("1+",1.5),
    TWO_MINUS("2-",1.75),
    TWO("2",2),
    TWO_PLUS("2+",2.5),
    THREE_MINUS("3-",2.75),
    THREE("3",3),
    THREE_PLUS("3+",3.5),
    FOUR_MINUS("4-",3.75),
    FOUR("4",4),
    FOUR_PLUS("4+",4.5),
    FIVE_MINUS("5-",4.75),
    FIVE("5",5),
    FIVE_PLUS("5+",5.5),
    SIX_MINUS("6-",5.75),
    SIX("6",6),
    MINUS("-",0),
    PLUS("+",0),
    NB("nb",0);

    private String symbol;
    private double value;

    Degree(String symbol, double value) {
        this.symbol =symbol;
        this.value = value;
    }

    public String getSymbol() {
        return symbol;
    }

    public double getValue() {
        return value;
    }
}
