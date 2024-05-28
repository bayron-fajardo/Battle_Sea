package GUI;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import Clases.Archivo;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class GameRecord extends JFrame {
    private JTable table;
    private JScrollPane scrollPane;

    public GameRecord() {
        setTitle("Game Records");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        DefaultTableModel model = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Hacer todas las celdas no editables
            }
        };

        table = new JTable(model);
        model.addColumn("Name");
        model.addColumn("Time");
        model.addColumn("Winner");
        model.addColumn("Date");

        loadData();

        scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        JButton clearButton = new JButton("Clear Data");
        clearButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int option = JOptionPane.showConfirmDialog(null, "Are you sure you want to clear all data?", "Confirm", JOptionPane.YES_NO_OPTION);
                if (option == JOptionPane.YES_OPTION) {
                    clearData();
                    ((DefaultTableModel) table.getModel()).setRowCount(0);
                }
            }
        });
        add(clearButton, BorderLayout.SOUTH);
    }

    private void loadData() {
        Archivo archivo = new Archivo("Leaderboard");
        try (BufferedReader reader = new BufferedReader(new FileReader(archivo.getFileName()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 4) {
                    DefaultTableModel model = (DefaultTableModel) table.getModel();
                    model.addRow(new Object[]{parts[0], parts[1], parts[2], parts[3]});
                }
            }
        } catch (IOException e) {
            showError("Error loading data from file: " + e.getMessage());
        }
    }

    private void clearData() {
        Archivo archivo = new Archivo("Leaderboard");
        try {
            Files.deleteIfExists(Paths.get(archivo.getFileName()));
        } catch (IOException e) {
            showError("Error clearing data: " + e.getMessage());
        }
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(null, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new GameRecord().setVisible(true);
            }
        });
    }
}
