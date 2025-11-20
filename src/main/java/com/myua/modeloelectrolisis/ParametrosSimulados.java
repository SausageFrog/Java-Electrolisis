package com.myua.modeloelectrolisis;

public class ParametrosSimulados {
    private double voltaje;
    private double corriente;
    private double eficienciaFaradica;
    private double tiempoMaximoSegundos;
    private double temperaturaInicial;

    // Constantes
    private final double voltajeMinimo = 1.5;
    private final double masaH2 = 1.0;
    private final double calorEspecifico = 4186.0; //J/kg C

    public ParametrosSimulados(double voltaje, double corriente, double eficienciaFaradica, double tiempoMaximoSegundos, double temperaturaInicial) {
        this.voltaje = voltaje;
        this.corriente = corriente;
        this.eficienciaFaradica = eficienciaFaradica;
        this.tiempoMaximoSegundos = tiempoMaximoSegundos;
        this.temperaturaInicial = temperaturaInicial;
    }

    // Getters
    public double getVoltaje() { return voltaje; }
    public double getCorriente() { return corriente; }
    public double getEficienciaFaradica() { return eficienciaFaradica; }
    public double getTiempoMaximoSegundos() { return tiempoMaximoSegundos; }
    public double getTemperaturaInicial() { return temperaturaInicial; }

    public double getVoltajeMinimo() { return voltajeMinimo; }
    public double getMasaH2() { return masaH2; }
    public double getCalorEspecifico() { return calorEspecifico; }
}