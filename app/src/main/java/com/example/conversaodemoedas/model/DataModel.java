package com.example.conversaodemoedas.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DataModel {

    private Double valorReal;
    private Double valorDolar;

    public DataModel(Double valorReal, Double valorDolar) {
        this.valorReal = valorReal;
        this.valorDolar = valorDolar;
    }

    public String getValorRealFormatted() {
        return String.format("R$ %.2f", valorReal);
    }

    public String getValorDolarFormatted() {
        return String.format("$ %.2f", valorDolar);
    }
}
