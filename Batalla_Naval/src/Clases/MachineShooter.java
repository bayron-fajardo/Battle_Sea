package Clases;

import java.util.Random;

public class MachineShooter {
    private Random random;
    private int size;
    private boolean[][] shots;
    private int[] lastHit; // Último disparo exitoso

    public int[] getLastHit() {
        return lastHit;
    }

    public void setLastHit(int[] lastHit) {
        this.lastHit = lastHit;
    }

    public MachineShooter(int size) {
        this.size = size;
        this.random = new Random();
        this.shots = new boolean[size][size];
        this.lastHit = new int[]{-1, -1};
    }

    public int[] shoot() {
        int x, y;

        // Si hay algún disparo exitoso, buscar alrededor de esa posición
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (shots[i][j]) {
                    // Comprobar si se puede disparar arriba
                    if (i > 0 && !shots[i - 1][j]) {
                        shots[i - 1][j] = true;
                        return new int[]{i - 1, j};
                    }
                    // Comprobar si se puede disparar abajo
                    if (i < size - 1 && !shots[i + 1][j]) {
                        shots[i + 1][j] = true;
                        return new int[]{i + 1, j};
                    }
                    // Comprobar si se puede disparar a la izquierda
                    if (j > 0 && !shots[i][j - 1]) {
                        shots[i][j - 1] = true;
                        return new int[]{i, j - 1};
                    }
                    // Comprobar si se puede disparar a la derecha
                    if (j < size - 1 && !shots[i][j + 1]) {
                        shots[i][j + 1] = true;
                        return new int[]{i, j + 1};
                    }
                }
            }
        }

        // Si no se encontraron disparos exitosos o no se pudo encontrar ningún barco adyacente, disparar aleatoriamente
        do {
            x = random.nextInt(size);
            y = random.nextInt(size);
        } while (shots[x][y]); // Asegurarse de no disparar en la misma posición

        shots[x][y] = true;
        return new int[]{x, y};
    }
    public void updateLastHit(int[] coordinates) {
        this.lastHit = coordinates;
    }
}
