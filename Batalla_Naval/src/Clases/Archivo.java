package Clases;

import java.util.List;
import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;

public class Archivo {
    private String fileName;

    public Archivo(String name) {
        this.fileName = "src/Docs/" + name + ".txt";
        File archivo = new File(fileName);

        try {
            if (!archivo.exists()) {
                archivo.createNewFile();
                System.out.println("Archivo creado: " + archivo.getAbsolutePath());
            }
        } catch (IOException e) {
            System.out.println("Error al crear el archivo " + e.getMessage());
        }
    }

    public void saveDataLibros(String name, String time, LocalDate fecha, String winner) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("src/Docs/Leaderboard.txt", true))) {
            writer.write(  name + ", " + time + ", " + winner + ", " + fecha + "\n");
        } catch (IOException e) {
            System.out.println("Error al guardar los datos de la partida: " + e.getMessage());
        }
    }

    public void clearData(String fileName) {
        try (BufferedWriter write = new BufferedWriter(new FileWriter(fileName))) {
            // Clear the file content
        } catch (IOException e) {
            System.out.println("Error al borrar los datos del archivo: " + e.getMessage());
        }
    }

    public List<String> readLeaderboardData() {
        List<String> data = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader("src/Docs/Leaderboard.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                data.add(line);
            }
        } catch (IOException e) {
            System.out.println("Error al leer los datos del archivo Leaderboard: " + e.getMessage());
        }
        return data;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
