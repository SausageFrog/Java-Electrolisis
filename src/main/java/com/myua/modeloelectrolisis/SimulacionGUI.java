package com.myua.modeloelectrolisis;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import java.util.ArrayList;
import java.util.Locale;

public class SimulacionGUI extends Application {

    private Slider sldVoltaje;
    private Slider sldCorriente;
    private Slider sldEficiencia;
    private Slider sldTiempoMax;
    private Slider sldTempInicial;
    private Slider sldTiempoSim;
    
    private Label lblValVoltaje;
    private Label lblValCorriente;
    private Label lblValEficiencia;
    private Label lblValTiempoMax;
    private Label lblValTempInicial;
    private Label lblValTiempoSimulado;

    private Label lblVolumenH2;
    private Label lblEnergiaConsumida;
    private Label lblTempFinal;

    private Stage primaryStage;
    private Grafico graficoComponente;

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        primaryStage.setTitle("Simulador Electrólisis");
        
        VBox panelIzquierdo = new VBox(15); 
        panelIzquierdo.setPadding(new Insets(10));
        panelIzquierdo.setMinWidth(350);
        
        VBox panelPotencia = new VBox(10);
        panelPotencia.getStyleClass().add("tiled-panel");
        
        sldVoltaje = new Slider(0, 200, 20); lblValVoltaje = new Label("2.0 V");
        panelPotencia.getChildren().add(crearControlSlider("Voltaje Aplicado", sldVoltaje, lblValVoltaje, " V", 10.0));

        sldCorriente = new Slider(0, 100, 50); lblValCorriente = new Label("5.0 A");
        panelPotencia.getChildren().add(crearControlSlider("Corriente", sldCorriente, lblValCorriente, " A", 10.0));

        sldEficiencia = new Slider(0, 100, 85); lblValEficiencia = new Label("85 %");
        panelPotencia.getChildren().add(crearControlSlider("Eficiencia Farádica", sldEficiencia, lblValEficiencia, " %", 1.0));

        sldTiempoMax = new Slider(0, 3600, 1800); lblValTiempoMax = new Label("1800 s");
        panelPotencia.getChildren().add(crearControlSlider("Tiempo Máximo", sldTiempoMax, lblValTiempoMax, " s", 1.0));

        sldTempInicial = new Slider(0, 100, 25); lblValTempInicial = new Label("25 °C");
        panelPotencia.getChildren().add(crearControlSlider("Temperatura Inicial", sldTempInicial, lblValTempInicial, " °C", 1.0));

        sldTiempoSim = new Slider(0, 3600, 600); lblValTiempoSimulado = new Label("600 s");
        panelPotencia.getChildren().add(crearControlSlider("Tiempo Simulación", sldTiempoSim, lblValTiempoSimulado, " s", 1.0));
        
        TitledPane potenciaPane = new TitledPane("Fuentes de Poder y Tiempo", panelPotencia);
        potenciaPane.setCollapsible(false);
        panelIzquierdo.getChildren().add(potenciaPane);

        
        
        Button btnSimular = new Button("Iniciar Simulación");
        btnSimular.setMaxWidth(Double.MAX_VALUE);
        btnSimular.setOnAction(e -> realizarSimulacion());
        btnSimular.getStyleClass().add("boton-simular");
        panelIzquierdo.getChildren().add(btnSimular);
        
        GridPane panelResultados = new GridPane();
        panelResultados.setPadding(new Insets(10));
        panelResultados.setHgap(10);
        panelResultados.setVgap(10);
        panelResultados.getStyleClass().add("tiled-panel");
        
        lblVolumenH2        = new Label("---");
        lblEnergiaConsumida = new Label("---");
        lblTempFinal        = new Label("---");

        lblVolumenH2.getStyleClass().add("resultado-valor");
        lblEnergiaConsumida.getStyleClass().add("resultado-valor");
        lblTempFinal.getStyleClass().add("resultado-valor");

        panelResultados.add(new Label("Hidrógeno Obtenido (L):"), 0, 0); panelResultados.add(lblVolumenH2, 1, 0);
        panelResultados.add(new Label("Energía Consumida (J):"), 0, 1); panelResultados.add(lblEnergiaConsumida, 1, 1);
        panelResultados.add(new Label("Temperatura Final (°C):"), 0, 2); panelResultados.add(lblTempFinal, 1, 2);
        
        TitledPane resultadosPane = new TitledPane("Resultados Obtenidos:", panelResultados);
        resultadosPane.setCollapsible(false);
        panelIzquierdo.getChildren().add(resultadosPane);
        
        //GRÁFICO
        graficoComponente = new Grafico();
        
        //MENU
        HBox root = new HBox(10);
        root.setPadding(new Insets(10));
        
        HBox.setHgrow(graficoComponente, Priority.ALWAYS);
        
        root.getChildren().addAll(panelIzquierdo, graficoComponente);
        
        Scene scene = new Scene(root, 1100, 650); // Ventana
        scene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());
        
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private HBox crearControlSlider(String titulo, Slider slider, Label labelValor, String simbolo, double divisor) {
        HBox panel = new HBox(10);
        panel.setAlignment(Pos.CENTER_LEFT);
        
        Label lblTitulo = new Label(titulo);
        lblTitulo.setMinWidth(200); 
        
        slider.setShowTickMarks(true);
        slider.setMajorTickUnit(slider.getMax() / 5);
        slider.setPrefWidth(200);
        HBox.setHgrow(slider, Priority.ALWAYS); 
        
        slider.valueProperty().addListener((obs, oldVal, newVal) -> {
            double valorReal = newVal.doubleValue() / divisor;
            String formato = (divisor == 1.0) ? "%.0f" : "%.1f";
            
            labelValor.setText(String.format(Locale.US, formato, valorReal) + simbolo);
        });
        
        panel.getChildren().addAll(lblTitulo, slider, labelValor);
        return panel;
    }

    private void realizarSimulacion() {
        double voltaje = sldVoltaje.getValue() / 10.0;
        double corriente = sldCorriente.getValue() / 10.0;
        double eficiencia = sldEficiencia.getValue();
        double tiempoMax = sldTiempoMax.getValue();
        double tempInicial = sldTempInicial.getValue();
        double tiempoSimulado = sldTiempoSim.getValue();
        
        ParametrosSimulados parametros = new ParametrosSimulados(
            voltaje, corriente, eficiencia, tiempoMax, tempInicial
        );
        
        ModeloElectrolisis modelo = new ModeloElectrolisis();
        ResultadosSimulados resultados = modelo.simular(parametros, tiempoSimulado);

        lblVolumenH2.setText(String.format(Locale.US, "%.4f Lt", resultados.getVolumenH2()));
        lblEnergiaConsumida.setText(String.format(Locale.US, "%.2f J", resultados.getConsumoEnergia()));
        lblTempFinal.setText(String.format(Locale.US, "%.2f °C", resultados.getTemperaturaFinal()));

        ArrayList<Double> serieDeTiempo = modelo.SerieH2(parametros, tiempoSimulado);
        
        if (!serieDeTiempo.isEmpty()) { 
            graficoComponente.actualizarDatos(serieDeTiempo);
        } else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.initOwner(primaryStage);
            alert.setTitle("Advertencia");
            alert.setHeaderText(null);
            alert.setContentText("No se puede generar un gráfico con esa cantidad de tiempo o con los parámetros actuales.");
            alert.showAndWait();
        }
    }
}

/*
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;

public class SimulacionGUI extends JFrame {

    // Sliders
    private JSlider sldVoltaje;
    private JSlider sldCorriente;
    private JSlider sldEficiencia;
    private JSlider sldTiempoMax;
    private JSlider sldTempInicial;
    private JSlider sldTiempoSim;

    //valores en etiqueta
    private JLabel lblValVoltaje;
    private JLabel lblValCorriente;
    private JLabel lblValEficiencia;
    private JLabel lblValTiempoMax;
    private JLabel lblValTempInicial;
    private JLabel lblValTiempoSimulado;

    //resultados obtenidos h2 enregia consumida y temperatura final
    private JLabel lblVolumenH2;
    private JLabel lblEnergiaConsumida;
    private JLabel lblTempFinal;

    public SimulacionGUI() {
        super("Simulador Electrólisis");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));
        setSize(500, 700);

        //PANEL DATOS DE ETRADA EN SLIDERS
        JPanel panelSliders = new JPanel();
        panelSliders.setLayout(new GridLayout(6, 2, 10, 10));
        panelSliders.setBorder(BorderFactory.createTitledBorder("Fuentes de poder"));

        sldVoltaje = new JSlider(0, 200, 20);
        lblValVoltaje = new JLabel("2.0 V");
        panelSliders.add(PanelControl("Voltaje", sldVoltaje, lblValVoltaje, " V", 10.0));

        sldCorriente = new JSlider(0, 100, 50);
        lblValCorriente = new JLabel("5.0 A");
        panelSliders.add(PanelControl("Corriente", sldCorriente, lblValCorriente, " A", 10.0));

        sldEficiencia = new JSlider(0, 100, 85);
        lblValEficiencia = new JLabel("85 %");
        panelSliders.add(PanelControl("Eficiencia Farádica", sldEficiencia, lblValEficiencia, " %", 1.0));

        sldTiempoMax = new JSlider(0, 3600, 1800);
        lblValTiempoMax = new JLabel("1800 s");
        panelSliders.add(PanelControl("Tiempo maximo", sldTiempoMax, lblValTiempoMax, " s", 1.0));

        sldTempInicial = new JSlider(0, 100, 25);
        lblValTempInicial = new JLabel("25 °C");
        panelSliders.add(PanelControl("Temperatura Inicial", sldTempInicial, lblValTempInicial, " °C", 1.0));

        sldTiempoSim = new JSlider(0, 3600, 600);
        lblValTiempoSimulado = new JLabel("600 s");
        panelSliders.add(PanelControl("Tiempo Simulación", sldTiempoSim, lblValTiempoSimulado, " s", 1.0));

        JButton btnSimular = new JButton("Iniciar Simulación");
        btnSimular.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnSimular.addActionListener(e -> realizarSimulacion());

        JPanel panelBoton = new JPanel();
        panelBoton.add(btnSimular);

        //RESULATDOS OBTENIDOS
        JPanel panelResultados = new JPanel(new GridLayout(3, 2, 10, 10));
        panelResultados.setBorder(BorderFactory.createTitledBorder("Resultados obtenidos: "));

        lblVolumenH2        = new JLabel("---");
        lblEnergiaConsumida = new JLabel("---");
        lblTempFinal        = new JLabel("---");

        panelResultados.add(new JLabel("Hidrógeno Obtenido (L):")); panelResultados.add(lblVolumenH2);
        panelResultados.add(new JLabel("Energía Generada (J):")); panelResultados.add(lblEnergiaConsumida);
        panelResultados.add(new JLabel("Temperatura Final (°C):")); panelResultados.add(lblTempFinal);

        //posicion de los cuadros
        add(panelSliders, BorderLayout.NORTH);
        add(panelResultados, BorderLayout.CENTER);
        add(panelBoton, BorderLayout.SOUTH);
        setLocationRelativeTo(null);
        
    }

    private JPanel PanelControl(String titulo, JSlider slider, JLabel labelValor, String simbolo, double divisor) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        JLabel lblTitulo = new JLabel(titulo);
        lblTitulo.setPreferredSize(new Dimension(130, 20));

        slider.setPaintTicks(true);
        slider.setMajorTickSpacing(slider.getMaximum()/5);

        //cambiar el valor qeu esta al lado de los sliders
        slider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                double valorReal = slider.getValue() / divisor;
                if(divisor == 1.0) labelValor.setText((int)valorReal + simbolo);
                else labelValor.setText(String.format("%.1f", valorReal) + simbolo);
            }
        });

        panel.add(lblTitulo, BorderLayout.WEST);
        panel.add(slider, BorderLayout.CENTER);
        panel.add(labelValor, BorderLayout.EAST);
        return panel;
    }

    private void realizarSimulacion() {
        double voltaje = sldVoltaje.getValue() / 10.0;
        double corriente = sldCorriente.getValue() / 10.0;
        double eficiencia = sldEficiencia.getValue();
        double tiempoMax = sldTiempoMax.getValue();
        double tempInicial = sldTempInicial.getValue();
        double tiempoSimulado = sldTiempoSim.getValue();

        ParametrosSimulados parametros = new ParametrosSimulados(voltaje, corriente, eficiencia, tiempoMax, tempInicial);
        ModeloElectrolisis modelo = new ModeloElectrolisis();
        ResultadosSimulados resultados = modelo.simular(parametros, tiempoSimulado);

        lblVolumenH2.setText(String.format("%.4f Lt", resultados.getVolumenH2()));
        lblEnergiaConsumida.setText(String.format("%.2f J", resultados.getConsumoEnergia()));
        lblTempFinal.setText(String.format("%.2f °C", resultados.getTemperaturaFinal()));

        //info para el grafico
        ArrayList<Double> serieDeTiempo = modelo.SerieH2(parametros, tiempoSimulado);
        if (serieDeTiempo.size() >= 2) {
            Grafico grafico = new Grafico(this, serieDeTiempo);
            grafico.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(this,
                    "No se puede generar un grafico con esa cantidad de tiempo",
                    "Advertencia", JOptionPane.INFORMATION_MESSAGE);
        }
    }
}
*/