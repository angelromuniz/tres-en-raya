package com.angel.tresenraya;

/**
 * Logica del tres en raya, separada de la interfaz.
 * El tablero es una matriz 3x3 donde ' ' es una casilla vacia.
 */
public class Tablero {

    public static final char VACIA = ' ';
    public static final char X = 'X';
    public static final char O = 'O';

    private final char[][] casillas = new char[3][3];
    private char turno;

    public Tablero() {
        reiniciar();
    }

    public void reiniciar() {
        for (int f = 0; f < 3; f++) {
            for (int c = 0; c < 3; c++) {
                casillas[f][c] = VACIA;
            }
        }
        turno = X; // siempre empieza la X
    }

    public char getTurno() {
        return turno;
    }

    public char getCasilla(int fila, int col) {
        return casillas[fila][col];
    }

    public boolean estaLibre(int fila, int col) {
        return casillas[fila][col] == VACIA;
    }

    /**
     * Pone la ficha del jugador actual y cambia el turno.
     * Devuelve false si la casilla esta ocupada o la partida ya ha terminado.
     */
    public boolean jugar(int fila, int col) {
        if (!estaLibre(fila, col) || haTerminado()) {
            return false;
        }
        casillas[fila][col] = turno;
        turno = (turno == X) ? O : X;
        return true;
    }

    /** Devuelve 'X' u 'O' si alguien ha hecho tres en raya, o VACIA si no. */
    public char getGanador() {
        // filas y columnas
        for (int i = 0; i < 3; i++) {
            if (casillas[i][0] != VACIA && casillas[i][0] == casillas[i][1] && casillas[i][1] == casillas[i][2]) {
                return casillas[i][0];
            }
            if (casillas[0][i] != VACIA && casillas[0][i] == casillas[1][i] && casillas[1][i] == casillas[2][i]) {
                return casillas[0][i];
            }
        }
        // diagonales
        if (casillas[1][1] != VACIA) {
            if (casillas[0][0] == casillas[1][1] && casillas[1][1] == casillas[2][2]) {
                return casillas[1][1];
            }
            if (casillas[0][2] == casillas[1][1] && casillas[1][1] == casillas[2][0]) {
                return casillas[1][1];
            }
        }
        return VACIA;
    }

    public boolean estaLleno() {
        for (char[] fila : casillas) {
            for (char c : fila) {
                if (c == VACIA) {
                    return false;
                }
            }
        }
        return true;
    }

    public boolean hayEmpate() {
        return getGanador() == VACIA && estaLleno();
    }

    public boolean haTerminado() {
        return getGanador() != VACIA || estaLleno();
    }

    /** Pone una ficha sin cambiar el turno. Lo usa la maquina para probar jugadas. */
    void ponerTemporal(int fila, int col, char ficha) {
        casillas[fila][col] = ficha;
    }

    void quitar(int fila, int col) {
        casillas[fila][col] = VACIA;
    }
}
