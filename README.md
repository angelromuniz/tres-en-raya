# Tres en raya

El clásico tres en raya con interfaz gráfica hecha en JavaFX. Proyecto del módulo de Programación de 1.º de DAM.

## Funcionalidades

- Modo **2 jugadores** en el mismo ordenador y modo **contra la máquina**.
- Detección de victoria (filas, columnas y diagonales) y de empate.
- Marcador de partidas ganadas por X, por O y empates.
- Botones para empezar otra partida y para reiniciar el marcador.

## Cómo juega la máquina

No es imbatible a propósito. Sigue estas reglas en orden:

1. Si puede ganar en esa jugada, gana.
2. Si el rival va a ganar en la siguiente, le bloquea.
3. Si el centro está libre, lo coge.
4. Si no, coge una esquina libre al azar y, si no quedan, cualquier casilla.

## Estructura

```
src/main/java/com/angel/tresenraya/
├── App.java              -> ventana JavaFX y eventos de los botones
├── Tablero.java          -> lógica del juego (matriz 3x3, turnos, ganador)
└── JugadorMaquina.java   -> jugadas del ordenador
src/main/resources/.../estilos.css  -> estilos de la ventana
```

La lógica (`Tablero`) está separada de la interfaz (`App`), así que se podría reutilizar para una versión de consola o para Android.

## Cómo ejecutarlo

Necesita Java 17 o superior y Maven. Desde IntelliJ IDEA basta con abrir el proyecto (detecta el `pom.xml`) y ejecutar `App`, o desde terminal:

```bash
mvn javafx:run
```
