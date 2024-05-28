package GUI;

import javax.swing.*;

import Clases.Archivo;
import Clases.Board;
import Clases.MachineBoard;
import Clases.MachineShooter;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;

public class BattleshipGame {

    private JFrame frame;
    private Board playerBoard;
    private Board shootingBoard;
    private MachineBoard machineBoard; // Tablero de la máquina
    private JButton startButton;
    private int turno;
    public String playerName;
    public LocalDate startDate;
    public LocalDate endDate;
    public LocalTime startTime;
    public LocalTime endTime;
    private String winner;
    private Archivo archivo;

    public BattleshipGame() {
        turno = 1;
        frame = new JFrame("Battleship");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        frame.setResizable(false);

        JLabel titleLabel = new JLabel("Battleship", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Serif", Font.BOLD, 24));
        frame.add(titleLabel, BorderLayout.NORTH);

        JPanel boardsPanel = new JPanel(new GridLayout(1, 2, 10, 10));
        playerBoard = new Board(frame, true); // Tablero de jugador
        shootingBoard = new Board(frame, false); // Tablero de disparos
        machineBoard = new MachineBoard(); // Tablero de la máquina

        boardsPanel.add(createBoardPanel("Player's Board", playerBoard));
        boardsPanel.add(createBoardPanel("Shooting Board", shootingBoard));
        frame.add(boardsPanel, BorderLayout.CENTER);

        JPanel controlPanel = new JPanel();
        startButton = new JButton("Start");
        JButton rankingButton = new JButton("Ranking");
        JButton exitButton = new JButton("Exit");

        controlPanel.add(startButton);
        controlPanel.add(rankingButton);
        controlPanel.add(exitButton);

        frame.add(controlPanel, BorderLayout.SOUTH);

        archivo = new Archivo("Leaderboard");

        // ActionListener para el botón Start
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Habilitar la interacción con los tableros y los botones de disparo al presionar Start
                playerName = JOptionPane.showInputDialog(frame, "Enter your name:", "Player Name", JOptionPane.PLAIN_MESSAGE);
                if (playerName != null && !playerName.trim().isEmpty()) {
                    enableBoard(playerBoard, true);
                    enableBoard(shootingBoard, true);
                    startButton.setEnabled(false);
                    exitButton.setEnabled(false); // Deshabilitar el botón de salida mientras se juega
                    startDate = LocalDate.now();
                    startTime = LocalTime.now();
                } else {
                    JOptionPane.showMessageDialog(frame, "Please enter a valid name to start the game.", "Invalid Name", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // ActionListener para el botón Ranking
        rankingButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                GameRecord gameRecord = new GameRecord();
                gameRecord.setVisible(true);
            }
        });

        // ActionListener para el botón Exit
        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
            }
        });

        // ActionListener para los botones del tablero de disparos
        ActionListener shootingActionListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Obtener coordenadas del botón que disparó el evento
                JButton button = (JButton) e.getSource();
                int x = -1, y = -1;
                for (int i = 0; i < shootingBoard.getSIZE(); i++) {
                    for (int j = 0; j < shootingBoard.getSIZE(); j++) {
                        if (shootingBoard.getButtons()[i][j] == button) {
                            x = i;
                            y = j;
                            break;
                        }
                    }
                }

                // Si es el turno del jugador (turno = 1), permitir disparo y cambiar turno a la máquina
                if (turno == 1) {
                    boolean hit = machineBoard.shoot(x, y);
                    if (hit) {
                        button.setBackground(Color.RED); // Cambiar color del botón si es un impacto
                    } else {
                        button.setBackground(Color.BLUE); // Cambiar color del botón si es un disparo fallido
                    }
                    button.setEnabled(false); // Deshabilitar el botón después de disparar
                    turno = 2; // Cambiar turno a la máquina
                    machineTurn(); // Invocar el turno de la máquina
                } else {
                    // Si no es el turno del jugador, no permitir disparo (puede mostrar un mensaje al usuario)
                    JOptionPane.showMessageDialog(frame, "Espera tu turno.");
                }
            }
        };

        // Asignar el ActionListener a todos los botones del tablero de disparos
        shootingBoard.setButtonActionListener(shootingActionListener);
        disableGameControls();
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private JPanel createBoardPanel(String title, Board board) {
        JPanel panel = new JPanel(new BorderLayout());
        JLabel label = new JLabel(title, SwingConstants.CENTER);
        label.setFont(new Font("Serif", Font.BOLD, 18));
        panel.add(label, BorderLayout.NORTH);
        panel.add(board.getPanel(), BorderLayout.CENTER);
        return panel;
    }

    private void enableBoard(Board board, boolean enabled) {
        JButton[][] buttons = board.getButtons();
        for (int i = 0; i < buttons.length; i++) {
            for (int j = 0; j < buttons[i].length; j++) {
                buttons[i][j].setEnabled(enabled);
            }
        }
    }

    private void enableBoardButtons(Board board) {
        JButton[][] buttons = board.getButtons();
        for (int i = 0; i < buttons.length; i++) {
            for (int j = 0; j < buttons[i].length; j++) {
                buttons[i][j].setEnabled(true);
            }
        }
    }

    private void machineTurn() {
        MachineShooter machineShooter = new MachineShooter(playerBoard.getSIZE());
        int[] shot;

        // Intentar disparar usando el algoritmo de búsqueda inteligente
        shot = machineShooter.shoot();
        boolean hit = playerBoard.shoot(shot[0], shot[1], true);

        // Obtener los botones del tablero de disparos
        JButton[][] buttons = playerBoard.getButtons();

        if (hit) {
            buttons[shot[0]][shot[1]].setBackground(Color.RED); // Cambiar color del botón si es un impacto
        } else {
            buttons[shot[0]][shot[1]].setBackground(Color.BLUE); // Cambiar color del botón si es un disparo fallido
        }

        buttons[shot[0]][shot[1]].setEnabled(false); // Deshabilitar el botón después de disparar

        if (playerBoard.allShipsSunk()) { // Usar el método allShipsSunk de playerBoard
            winner = "Machine";
            endGame();
        } else if (machineBoard.allShipsSunk()) {
            winner = "Player";
            endGame();
        } else {
            turno = 1;
            enableBoardButtons(playerBoard);
        }
    }

    private void endGame() {
        endDate = LocalDate.now();
        endTime = LocalTime.now();
        saveGameData();
        showEndGameMessage();
        frame.dispose();
    }

    private void saveGameData() {
        Duration duration = Duration.between(startTime, endTime);
        long seconds = duration.getSeconds();
        long hours = seconds / 3600;
        seconds %= 3600;
        long minutes = seconds / 60;
        seconds %= 60;

        String timeTaken = String.format("%02d:%02d:%02d", hours, minutes, seconds);
        archivo.saveDataLibros(playerName, timeTaken, endDate, winner);
    }

    private void showEndGameMessage() {
        Duration duration = Duration.between(startTime, endTime);
        long seconds = duration.getSeconds();
        long hours = seconds / 3600;
        seconds %= 3600;
        long minutes = seconds / 60;
        seconds %= 60;

        String timeTaken = String.format("%02d:%02d:%02d", hours, minutes, seconds);
        JOptionPane.showMessageDialog(frame, String.format("¡El juego ha terminado!\nGanador: %s\nFecha: %s\nTiempo de juego: %s", winner, endDate, timeTaken));
    }

    private void disableGameControls() {
        enableBoard(playerBoard, false);
        enableBoard(shootingBoard, false);
        startButton.setEnabled(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new BattleshipGame();
            }
        });
    }
}
