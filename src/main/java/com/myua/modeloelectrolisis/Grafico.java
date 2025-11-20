package com.myua.modeloelectrolisis;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class Grafico extends JDialog {

    public Grafico(JFrame ventanaPrincipal, ArrayList<Double> datosObtenidos) {
        super(ventanaPrincipal, "Gráfico Hidrogeno Obtenido", true);

        add(new JPanel() {
            {
                setBackground(Color.BLACK);
            }
            @Override
            protected void paintComponent(Graphics g)  {  //grafico
                super.paintComponent(g);
                Graphics2D dibujador = (Graphics2D) g;

                int ancho = getWidth();
                int altura = getHeight();
                int margen = 30;

                // Cálculo de Máximo
                double max = 1.5;
                for (Double valor : datosObtenidos) {
                    if (valor > max) {
                        max = valor + 0.5;
                    }
                }
                dibujador.setStroke(new BasicStroke(2));
                dibujador.setColor(Color.WHITE);

                //grafico
                dibujador.drawLine(margen, margen, margen, altura - margen);
                dibujador.drawLine(margen, altura - margen, ancho - margen, altura - margen);
                dibujador.setColor(Color.WHITE);
                dibujador.setStroke(new BasicStroke(2));

                int valoresEjes = 5;

                for (int i = 0; i <= valoresEjes; i++) {
                    double volumenH2 = max * ((double) i / valoresEjes);
                    int ejeY = altura - margen - (int) ((volumenH2 / max) * (altura - 2 * margen));
                    dibujador.drawString(String.format("%.2f", volumenH2), 5, ejeY + 5);
                    dibujador.drawLine(margen - 5, ejeY, margen, ejeY);
                }

                dibujador.drawString("Volumen H₂ (L)", 5, margen - 10);
                dibujador.drawString("Tiempo (s)", ancho - margen - 60, altura - 5);

                //puntos en el grafico
                int inicioX = margen, inicioY = altura - margen;

                for (int i = 0; i < datosObtenidos.size(); i++) {
                    int x = margen + i * (ancho - 2 * margen) / (datosObtenidos.size() - 1);
                    int y = altura - margen - (int) ((datosObtenidos.get(i) / max) * (altura - 2 * margen));
                    dibujador.drawLine(inicioX, inicioY, x, y);

                    dibujador.setColor(Color.WHITE);
                    dibujador.drawString(String.valueOf(i * ModeloElectrolisis.tiempoEjeX) + "s", x - 10, altura - margen + 18);
                    dibujador.setColor(Color.GREEN);

                    inicioX = x; inicioY = y;
                }


            }
        });

        setSize(600, 400);
        setLocationRelativeTo(ventanaPrincipal); //para que se pueda iniciar en modeloelectrolisis
    }
}