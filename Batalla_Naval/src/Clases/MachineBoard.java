package Clases;

import java.util.Random;

public class MachineBoard {
    private final int SIZE = 10;
    private Ship[][] ships;
    private boolean[][] hits;
    private Random random;
    

    public MachineBoard() {
        ships = new Ship[SIZE][SIZE];
        hits = new boolean[SIZE][SIZE];
        random = new Random();
        placeShips();
        printShipPositions(); // Llamar al método para imprimir las posiciones de los barcos
    }

    private void placeShips() {
        // Coloca 2 barcos 1x1
        placeRandomShips(2, 1, true);

        // Coloca 2 barcos 2x2 horizontales y 2 barcos 2x2 verticales
        placeRandomShips(2, 2, true);
        placeRandomShips(2, 2, false);

        // Coloca 2 barcos 3x3 horizontales y 2 barcos 3x3 verticales
        placeRandomShips(2, 3, true);
        placeRandomShips(2, 3, false);
    }

    private void placeRandomShips(int numShips, int shipSize, boolean horizontal) {
        for (int i = 0; i < numShips; i++) {
            boolean placed = false;
            while (!placed) {
                int x = random.nextInt(SIZE);
                int y = random.nextInt(SIZE);
                Ship ship = new Ship(shipSize, horizontal);
                placed = placeShip(x, y, ship);
            }
        }
    }

    private boolean placeShip(int x, int y, Ship ship) {
        if (ship.isVertical()) {
            if (y + ship.getSize() > SIZE) {
                return false;
            }
        } else {
            if (x + ship.getSize() > SIZE) {
                return false;
            }
        }

        for (int i = 0; i < ship.getSize(); i++) {
            if (ship.isVertical()) {
                if (ships[x][y + i] != null) {
                    return false;
                }
            } else {
                if (ships[x + i][y] != null) {
                    return false;
                }
            }
        }

        for (int i = 0; i < ship.getSize(); i++) {
            if (ship.isVertical()) {
                ships[x][y + i] = ship;
            } else {
                ships[x + i][y] = ship;
            }
        }

        return true;
    }

    private void printShipPositions() {
        System.out.println("Posiciones de los barcos enemigos:");
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (ships[i][j] != null) {
                    System.out.print("S "); // Imprime 'S' para una parte del barco
                } else {
                    System.out.print(". "); // Imprime '.' para una parte vacía
                }
            }
            System.out.println();
        }
    }

    // Método para realizar un disparo en las coordenadas especificadas
    public boolean shoot(int x, int y) {
        if (x < 0 || x >= SIZE || y < 0 || y >= SIZE) {
            return false; // Disparo fuera del tablero
        }

        if (hits[x][y]) {
            return false; // Ya se ha disparado en estas coordenadas
        }

        hits[x][y] = true; // Marca el disparo en el tablero

        if (ships[x][y] != null) {
            System.out.println("Impacto en (" + (x+1) + ", " + (y+1) + ")"); // Mensaje de depuración
            return true; // ¡Disparo acertado!
        } else {
            System.out.println("Fallido en (" + (x+1) + ", " + (y+1) + ")"); // Mensaje de depuración
            return false; // ¡Disparo fallido!
        }
    }

    // Método para comprobar si todos los barcos han sido hundidos
    public boolean allShipsSunk() {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (ships[i][j] != null && !hits[i][j]) {
                    return false; // Todavía hay barcos sin hundir
                }
            }
        }
        return true; // Todos los barcos han sido hundidos
    }
}
