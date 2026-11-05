package com.angel.tresenraya;

import javafx.animation.PauseTransition;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * Ventana principal del juego hecha con JavaFX.
 */
public class App extends Application {

    private final Tablero tablero = new Tablero();
    private final JugadorMaquina maquina = new JugadorMaquina();
    private final Button[][] botones = new Button[3][3];

    private Label lblEstado;
    private Label lblMarcador;
    private RadioButton rbContraMaquina;

    private int victoriasX = 0;
    private int victoriasO = 0;
    private int empates = 0;
    private boolean esperandoMaquina = false;

    @Override
    public void start(Stage stage) {
        lblEstado = new Label();
        lblEstado.getStyleClass().add("estado");

        lblMarcador = new Label();
        lblMarcador.getStyleClass().add("marcador");

        // modo de juego
        ToggleGroup grupoModo = new ToggleGroup();
        RadioButton rbDosJugadores = new RadioButton("2 jugadores");
        rbContraMaquina = new RadioButton("Contra la maquina");
        rbDosJugadores.setToggleGroup(grupoModo);
        rbContraMaquina.setToggleGroup(grupoModo);
        rbContraMaquina.setSelected(true);
        grupoModo.selectedToggleProperty().addListener((obs, antes, ahora) -> nuevaPartida());
        HBox modos = new HBox(15, rbDosJugadores, rbContraMaquina);
        modos.setAlignment(Pos.CENTER);

        // tablero de botones
        GridPane grid = new GridPane();
        grid.setHgap(6);
        grid.setVgap(6);
        grid.setAlignment(Pos.CENTER);
        for (int f = 0; f < 3; f++) {
            for (int c = 0; c < 3; c++) {
                Button b = new Button();
                b.getStyleClass().add("casilla");
                final int fila = f;
                final int col = c;
                b.setOnAction(e -> clicCasilla(fila, col));
                botones[f][c] = b;
                grid.add(b, c, f);
            }
        }

        Button btnNueva = new Button("Nueva partida");
        btnNueva.setOnAction(e -> nuevaPartida());

        Button btnReset = new Button("Reiniciar marcador");
        btnReset.setOnAction(e -> {
            victoriasX = 0;
            victoriasO = 0;
            empates = 0;
            nuevaPartida();
        });

        HBox acciones = new HBox(10, btnNueva, btnReset);
        acciones.setAlignment(Pos.CENTER);

        VBox raiz = new VBox(15, lblEstado, modos, grid, lblMarcador, acciones);
        raiz.setAlignment(Pos.CENTER);
        raiz.setPadding(new Insets(20));

        Scene escena = new Scene(raiz, 380, 520);
        escena.getStylesheets().add(getClass().getResource("estilos.css").toExternalForm());

        stage.setTitle("Tres en raya");
        stage.setScene(escena);
        stage.setResizable(false);
        stage.show();

        nuevaPartida();
    }

    private void clicCasilla(int fila, int col) {
        if (esperandoMaquina || !tablero.jugar(fila, col)) {
            return;
        }
        actualizarVista();

        if (!tablero.haTerminado() && rbContraMaquina.isSelected()) {
            // pequena pausa para que no parezca que la maquina juega instantaneamente
            esperandoMaquina = true;
            PauseTransition pausa = new PauseTransition(Duration.millis(400));
            pausa.setOnFinished(e -> {
                int[] jugada = maquina.elegirJugada(tablero, tablero.getTurno());
                tablero.jugar(jugada[0], jugada[1]);
                esperandoMaquina = false;
                actualizarVista();
            });
            pausa.play();
        }
    }

    private void nuevaPartida() {
        tablero.reiniciar();
        esperandoMaquina = false;
        resultadoContado = false;
        actualizarVista();
    }

    private void actualizarVista() {
        for (int f = 0; f < 3; f++) {
            for (int c = 0; c < 3; c++) {
                char ficha = tablero.getCasilla(f, c);
                Button b = botones[f][c];
                b.setText(ficha == Tablero.VACIA ? "" : String.valueOf(ficha));
                b.getStyleClass().removeAll("ficha-x", "ficha-o");
                if (ficha == Tablero.X) {
                    b.getStyleClass().add("ficha-x");
                } else if (ficha == Tablero.O) {
                    b.getStyleClass().add("ficha-o");
                }
            }
        }

        char ganador = tablero.getGanador();
        if (ganador != Tablero.VACIA) {
            lblEstado.setText("Gana " + ganador + "!");
            sumarResultado(ganador);
        } else if (tablero.hayEmpate()) {
            lblEstado.setText("Empate");
            sumarResultado(Tablero.VACIA);
        } else {
            lblEstado.setText("Turno de " + tablero.getTurno());
        }
        lblMarcador.setText(String.format("X: %d   |   O: %d   |   Empates: %d", victoriasX, victoriasO, empates));
    }

    // para no sumar dos veces la misma partida
    private boolean resultadoContado = false;

    private void sumarResultado(char ganador) {
        if (resultadoContado) {
            return;
        }
        if (ganador == Tablero.X) {
            victoriasX++;
        } else if (ganador == Tablero.O) {
            victoriasO++;
        } else {
            empates++;
        }
        resultadoContado = true;
        lblMarcador.setText(String.format("X: %d   |   O: %d   |   Empates: %d", victoriasX, victoriasO, empates));
    }

    public static void main(String[] args) {
        launch(args);
    }
}
