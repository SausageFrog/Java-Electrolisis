package com.myua.modeloelectrolisis;

import javafx.application.Application;
import java.util.ArrayList;

public class ModeloElectrolisis {
    public ResultadosSimulados simular(ParametrosSimulados parametros, double tiempoSegundos) {
        if (tiempoSegundos <= 0) {
            return new ResultadosSimulados(0.0, 0.0,25);
        }
        //variables obtenidas de parametro
        double volAplicado = parametros.getVoltaje();
        double intensidad = parametros.getCorriente();
        double VolMinimo = parametros.getVoltajeMinimo();
        double eficiencia = parametros.getEficienciaFaradica() / 100.0;

        // Si el voltaje ingresado es menor al mínimo necesario no se inicia el proceso de elctrolisis
        double corrienteEfectiva = intensidad;
        if (volAplicado < VolMinimo) {
            corrienteEfectiva = 0.0;
        }
        //tiempo maximo que puede operar el [rograma
        double tiempoEfectivo = tiempoSegundos;
        double tiempoMaximo = parametros.getTiempoMaximoSegundos();
        if (tiempoMaximo > 0 && tiempoSegundos > tiempoMaximo) {
            tiempoEfectivo = tiempoMaximo;
        }
        //molesObtenidos a traves de los parametros
        double molesObtenidos = (corrienteEfectiva * tiempoEfectivo) / (ResultadosSimulados.CONSTANTE_FARADAY * ResultadosSimulados.ELECTRONES_H2);
        double molesH2 = molesObtenidos * eficiencia;
        double volH2 = molesH2 * 22.4;
        //corriente
        double energiaTotal = volAplicado * corrienteEfectiva * tiempoEfectivo;

        //el calor se genera a traves del voltaje no utilizado o desperdiciado en el proceso
        double potenciaCalor = 0.0;
        if (volAplicado > VolMinimo) {
            potenciaCalor = (volAplicado - VolMinimo) * corrienteEfectiva;
        }
        double Joules = potenciaCalor * tiempoEfectivo; 
        double deltaT = Joules / (parametros.getMasaH2() * parametros.getCalorEspecifico()); //diferencia de temperatura
        double tempFinal = parametros.getTemperaturaInicial() + deltaT;

        return new ResultadosSimulados(volH2, energiaTotal, tempFinal);

    }
    //info para generar el grafico
    public static double tiempoEjeX = 10;
    public ArrayList<Double> SerieH2(ParametrosSimulados parametros, double tiempoTotal) {
        ArrayList<Double> serieH2 = new ArrayList<>();
        
        serieH2.add(0.0);

        double tiempoLimite = tiempoTotal;
        double tiempomaximo = parametros.getTiempoMaximoSegundos();
        if(tiempoLimite <= 180) tiempoEjeX = 30;

        if (tiempomaximo > 0 && tiempomaximo < tiempoTotal) {
            tiempoLimite = tiempomaximo;
        }

        for (double t = tiempoEjeX; t <= tiempoLimite; t += tiempoEjeX) {
            ResultadosSimulados res = simular(parametros, t);
            serieH2.add(res.getVolumenH2());
        }

        if (tiempoLimite % tiempoEjeX != 0) {
            ResultadosSimulados res = simular(parametros, tiempoLimite);
            serieH2.add(res.getVolumenH2());
        }

        return serieH2;
    }

    public static void main(String[] args) {
        Application.launch(SimulacionGUI.class, args);
    }
}