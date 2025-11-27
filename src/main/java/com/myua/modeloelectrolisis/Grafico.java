package com.myua.modeloelectrolisis;

import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.VBox;
import javafx.scene.layout.Priority;
import java.util.List;

public class Grafico extends VBox { 
    
    private LineChart<Number, Number> lineChart;

    public Grafico() {
        this.lineChart = crearGraficoVacio();
        VBox.setVgrow(this.lineChart, Priority.ALWAYS);
        this.getChildren().add(this.lineChart);
        this.getStyleClass().add("grafico-contenedor");
    }
    
    private LineChart<Number, Number> crearGraficoVacio() {
        final NumberAxis xAxis = new NumberAxis();
        final NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("Tiempo (s)");
        yAxis.setLabel("Volumen H₂ (Lt)");
        
        final LineChart<Number, Number> chart = new LineChart<>(xAxis, yAxis);
        chart.setTitle("Producción de Hidrógeno Acumulado");
        chart.setLegendVisible(false);
        chart.setCreateSymbols(true);
        chart.getStyleClass().add("chart-background"); 
        
        return chart;
    }

    public void actualizarDatos(List<Double> serieDeTiempo) {
        this.lineChart.getData().clear();
        
        XYChart.Series<Number, Number> series = new XYChart.Series<>();
        series.setName("Volumen de H2");
        
        double tiempoEjeX = ModeloElectrolisis.tiempoEjeX; 
        double tiempoActual = 0;
        
        for (double volumen : serieDeTiempo) {
            tiempoActual += tiempoEjeX;
            series.getData().add(new XYChart.Data<>(tiempoActual, volumen));
        }
        
        this.lineChart.getData().add(series);
    }
}
/*
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class Grafico extends JDialog {

    public Grafico(JFrame ventanaPrincipal, ArrayList<Double> datosObtenidos) {
        super(ventanaPrincipal, "Gráfico Hidrogeno Obtenido", true);

        add(new JPanel() {
            {
                setBackground(new Color(4, 2, 59));
            }
            @Override
            protected void paintComponent(Graphics g)  {  //grafico
                super.paintComponent(g);
                Graphics2D dibujador = (Graphics2D) g;

                int ancho = getWidth();
                int altura = getHeight();
                int margen = 40;
                //suavizado de lineas
                dibujador.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                dibujador.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
                
                dibujador.setFont(new Font("Arial", Font.BOLD, 14));
                // Cálculo de Máximo
                double maxY = 3;
                for (Double valor : datosObtenidos) {
                    if (valor > maxY) {
                        maxY = valor;
                    }
                }
                
                
                dibujador.setStroke(new BasicStroke(2));
                dibujador.setColor(Color.WHITE);

                //grafico
                dibujador.drawLine(margen, margen, margen, altura - margen);
                dibujador.drawLine(margen, altura - margen, ancho - margen, altura - margen);
                dibujador.setColor(Color.WHITE);
                dibujador.setStroke(new BasicStroke(3));

                //eje Y
                int valoresEjes = 5;
                for (int i = 0; i <= valoresEjes; i++) {
                    double volumenH2 = maxY * ((double) i / valoresEjes);
                    int ejeY = altura - margen - (int) ((volumenH2 / maxY) * (altura - 2 * margen));
                    dibujador.drawString(String.format("%.2f", volumenH2), 5, ejeY + 5);
                    dibujador.drawLine(margen - 5, ejeY, margen, ejeY);
                }

                dibujador.drawString("Volumen H₂ (Lt)", 5, margen - 10);
                dibujador.drawString("Tiempo (s)", ancho - margen - 60, altura - 5);

                int puntosEjeX = 8;
                int valoresEjeX = Math.max(1, datosObtenidos.size() / puntosEjeX);
                
                //puntos en el grafico
                int inicioX = margen;
                int inicioY = altura - margen;

                for (int i = 0; i < datosObtenidos.size(); i++) {
                    int x = margen + i * (ancho - 2 * margen) / (datosObtenidos.size() - 1);
                    int y = altura - margen - (int) ((datosObtenidos.get(i) / maxY) * (altura - 2 * margen));
                    dibujador.drawLine(inicioX, inicioY, x, y);
                    
                    //puntos en al grafica
                    
                    
                    if(i % valoresEjeX == 0 || i == datosObtenidos.size() - 1){
                        dibujador.setColor(Color.WHITE);
                        dibujador.drawString(String.valueOf((int)(i * ModeloElectrolisis.tiempoEjeX)) + "s", x - 10, altura - margen + 18);
                        dibujador.setColor(Color.WHITE);
                        int diametro = 10;
                        int radio = diametro / 2;
                        dibujador.fillOval(x - radio, y - radio, diametro, diametro);    
                    }
                    dibujador.setColor(Color.GREEN);
                   

                    inicioX = x; 
                    inicioY = y;
                }


            }
        });

        setSize(750, 650);
        setLocationRelativeTo(ventanaPrincipal); //para que se pueda iniciar en modeloelectrolisis
    }
}*/