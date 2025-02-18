package com.example.conversaodemoedas.entity;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "cotacoes")
public class CotacoesTO {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private Double valorReal;
    private Double valorDolar;

    public CotacoesTO() {
    }

    @Ignore
    public CotacoesTO(int id) {
        this.id = id;
    }

    @Ignore
    public CotacoesTO(Double valorReal, Double valorDolar) {
        this.valorReal = valorReal;
        this.valorDolar = valorDolar;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Double getValorReal() {
        return valorReal;
    }

    public void setValorReal(Double valorReal) {
        this.valorReal = valorReal;
    }

    public Double getValorDolar() {
        return valorDolar;
    }

    public void setValorDolar(Double valorDolar) {
        this.valorDolar = valorDolar;
    }
}
