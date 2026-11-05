package com.angel.tresenraya;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Jugador controlado por el ordenador. No es imbatible a proposito, sigue estas reglas:
 * 1. Si puede ganar en esta jugada, gana.
 * 2. Si el rival puede ganar en la siguiente, le bloquea.
 * 3. Si el centro esta libre, lo coge.
 * 4. Si no, una esquina libre al azar, y si no, cualquier casilla libre.
 */
public class JugadorMaquina {

    private final Random random = new Random();

    /** Devuelve {fila, columna} de la jugada elegida. */
    public int[] elegirJugada(Tablero tablero, char ficha) {
        char rival = (ficha == Tablero.X) ? Tablero.O : Tablero.X;

        int[] jugada = buscarJugadaGanadora(tablero, ficha);
        if (jugada != null) {
            return jugada;
        }
        jugada = buscarJugadaGanadora(tablero, rival);
        if (jugada != null) {
            return jugada; // bloquear al rival
        }
        if (tablero.estaLibre(1, 1)) {
            return new int[]{1, 1};
        }

        List<int[]> esquinas = new ArrayList<>();
        int[][] posiblesEsquinas = {{0, 0}, {0, 2}, {2, 0}, {2, 2}};
        for (int[] e : posiblesEsquinas) {
            if (tablero.estaLibre(e[0], e[1])) {
                esquinas.add(e);
            }
        }
        if (!esquinas.isEmpty()) {
            return esquinas.get(random.nextInt(esquinas.size()));
        }

        List<int[]> libres = new ArrayList<>();
        for (int f = 0; f < 3; f++) {
            for (int c = 0; c < 3; c++) {
                if (tablero.estaLibre(f, c)) {
                    libres.add(new int[]{f, c});
                }
            }
        }
        return libres.get(random.nextInt(libres.size()));
    }

    /** Prueba cada casilla libre y mira si con esa ficha se gana. */
    private int[] buscarJugadaGanadora(Tablero tablero, char ficha) {
        for (int f = 0; f < 3; f++) {
            for (int c = 0; c < 3; c++) {
                if (tablero.estaLibre(f, c)) {
                    tablero.ponerTemporal(f, c, ficha);
                    boolean gana = tablero.getGanador() == ficha;
                    tablero.quitar(f, c);
                    if (gana) {
                        return new int[]{f, c};
                    }
                }
            }
        }
        return null;
    }
}
