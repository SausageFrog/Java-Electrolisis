package com.myua.modeloelectrolisis;


public class ResultadosSimulados {
    private double volumenH2;
    private double consumoEnergia;
    private double temperaturaFinal;

    public static final double CONSTANTE_FARADAY = 96485.3321;
    public static final int ELECTRONES_H2 = 2;

    public ResultadosSimulados(double volumenH2, double consumoEnergia, double temperaturaFinal) {
        this.volumenH2 = volumenH2;
        this.consumoEnergia = consumoEnergia;
        this.temperaturaFinal = temperaturaFinal;
    }

    public double getVolumenH2() {
        return volumenH2;
    }

    public double getConsumoEnergia() {
        return consumoEnergia;
    }

    public double getTemperaturaFinal() {
        return temperaturaFinal;
    }
}