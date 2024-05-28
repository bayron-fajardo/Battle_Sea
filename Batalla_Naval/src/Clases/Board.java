package Clases;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashSet;
import java.util.Set;

public class Board {
    private final int SIZE = 10;
    private JButton[][] buttons;
    private Ship[][] ships;
    private JFrame frame;
    private int ships1x1Placed = 0;
    private int vertical2x2Placed = 0;
    private int vertical3x3Placed = 0;
    private int horizontal2x2Placed = 0;
    private int horizontal3x3Placed = 0;
    private JPanel boardPanel;
    private boolean isPlayerBoard;
    private ActionListener buttonActionListener;
    private Set<Point> hitShots;
    MachineShooter machineShooter;

    public Board(JFrame frame, boolean isPlayerBoard) {
        this.frame = frame;
        this.isPlayerBoard = isPlayerBoard;
        buttons = new JButton[SIZE][SIZE];
        ships = new Ship[SIZE][SIZE];
        hitShots = new HashSet<>();
        boardPanel = new JPanel();
        initializeBoard();
    }

    private void initializeBoard() {
        boardPanel = new JPanel(new GridLayout(SIZE + 1, SIZE + 1));
        ButtonListener listener = new ButtonListener(isPlayerBoard);

        boardPanel.add(new JLabel(""));
        for (int i = 1; i <= SIZE; i++) {
            JLabel label = new JLabel(String.valueOf(i), SwingConstants.CENTER);
            label.setPreferredSize(new Dimension(40, 40));
            boardPanel.add(label);
        }

        for (int i = 0; i < SIZE; i++) {
            JLabel label = new JLabel(String.valueOf(i + 1), SwingConstants.CENTER);
            label.setPreferredSize(new Dimension(40, 40));
            boardPanel.add(label);
            for (int j = 0; j < SIZE; j++) {
                buttons[i][j] = new JButton();
                buttons[i][j].setPreferredSize(new Dimension(40, 40));
                buttons[i][j].addActionListener(listener);
                boardPanel.add(buttons[i][j]);
            }
        }
        boardPanel.setBorder(BorderFactory.createLineBorder(Color.black));
    }

    

    public JPanel getPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        //panel.add(initializeHeader(), BorderLayout.NORTH);
        panel.add(boardPanel, BorderLayout.CENTER);
        return panel;
    }
    public JButton getButton(int x, int y) {
        return buttons[x][y];
    }
    public boolean shoot(int x, int y, boolean isPlayerShot) {
        if (ships[x][y] != null) {
            hitShots.add(new Point(x, y));
            buttons[x][y].setBackground(Color.RED); // Cambiar color del botón si es un impacto
            if (!isPlayerShot) {
                // Si el disparo proviene de la máquina, se marca automáticamente en el tablero de disparos
                machineShooter.updateLastHit(new int[]{x, y});
            }
            return true;
        } else {
            if (!isPlayerShot) {
                // Si el disparo proviene de la máquina, se marca automáticamente en el tablero de disparos
                machineShooter.updateLastHit(new int[]{x, y});
            }
            buttons[x][y].setBackground(Color.BLUE); // Cambiar color del botón si es un disparo fallido
        }
        return false;
    }
    public boolean allShipsSunk() {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (ships[i][j] != null && !hitShots.contains(new Point(i, j))) {
                    return false; // Si hay algún barco no golpeado, retornar false
                }
            }
        }
        return true; // Todos los barcos han sido golpeados
    }
    

    private class ButtonListener implements ActionListener {
        private boolean isPlayerBoard;

        public ButtonListener(boolean isPlayerBoard) {
            this.isPlayerBoard = isPlayerBoard;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            JButton button = (JButton) e.getSource();
            int x = -1, y = -1;
        
            for (int i = 0; i < SIZE; i++) {
                for (int j = 0; j < SIZE; j++) {
                    if (buttons[i][j] == button) {
                        x = i;
                        y = j;
                        break;
                    }
                }
            }
        
            if (isPlayerBoard) {
                if (ships[x][y] != null) {
                    JOptionPane.showMessageDialog(frame, "Position already occupied by another ship.");
                    return;
                }
        
                if (ships1x1Placed < 2) {
                    if (placeShip(x, y, new Ship(1, true)) == 0) {
                        ships1x1Placed++;
                    }
                } else if (vertical2x2Placed < 2) {
                    if (placeShip(x, y, new Ship(2, true)) == 0) {
                        vertical2x2Placed++;
                    }
                } else if (vertical3x3Placed < 2) {
                    if (placeShip(x, y, new Ship(3, true)) == 0) {
                        vertical3x3Placed++;
                    }
                } else if (horizontal2x2Placed < 2) {
                    if (placeShip(x, y, new Ship(2, false)) == 0) {
                        horizontal2x2Placed++;
                    }
                } else if (horizontal3x3Placed < 2) {
                    if (placeShip(x, y, new Ship(3, false)) == 0) {
                        horizontal3x3Placed++;
                    }
                } else {
                    JOptionPane.showMessageDialog(frame, "All ships placed.");
                    return;
                }
        
                // Verificar si la posición en ships está inicializada antes de acceder a getSize()
                if (ships[x][y] != null) {
                    button.setBackground(Color.GREEN);
                    for (int i = 1; i < ships[x][y].getSize(); i++) {
                        if (ships[x][y].isVertical()) {
                            if (y + i < SIZE) {
                                buttons[x][y + i].setBackground(Color.GREEN);
                            } else {
                                buttons[x][y - i].setBackground(Color.GREEN);
                            }
                        } else {
                            if (x + i < SIZE) {
                                buttons[x + i][y].setBackground(Color.GREEN);
                            } else {
                                buttons[x - i][y].setBackground(Color.GREEN);
                            }
                        }
                    }
                }
            } else {
        
            }
        }
        
    }
    public void setButtonActionListener(ActionListener listener) {
        this.buttonActionListener = listener;
        // Asegurar que el listener no sea null antes de intentar agregarlo a los botones
        if (buttonActionListener != null) {
            for (int i = 0; i < SIZE; i++) {
                for (int j = 0; j < SIZE; j++) {
                    buttons[i][j].addActionListener(buttonActionListener);
                }
            }
        }
    }
    public JButton[][] getButtons() {
        return buttons;
    }

    public int getSIZE() {
        return SIZE;
    }

    

    

    

   
    

    

   
    

    public int placeShip(int x, int y, Ship ship) {
        if (x < 0 || y < 0 || x >= SIZE || y >= SIZE) {
            JOptionPane.showMessageDialog(frame, "Error: Ship cannot be placed outside the board.");
            return -1;
        }

        if (ship.isVertical()) {
            if (y + ship.getSize() > SIZE) {
                JOptionPane.showMessageDialog(frame, "Error: Ship cannot be placed in this position.");
                return -2;
            }
        } else {
            if (x + ship.getSize() > SIZE) {
                JOptionPane.showMessageDialog(frame, "Error: Ship cannot be placed in this position.");
                return -2;
            }
        }

        for (int i = 0; i < ship.getSize(); i++) {
            if (ship.isVertical()) {
                if (y + i < SIZE && ships[x][y + i] != null) {
                    JOptionPane.showMessageDialog(frame, "Error: Position already occupied by another ship.");
                    return -3;
                }
            } else {
                if (x + i < SIZE && ships[x + i][y] != null) {
                    JOptionPane.showMessageDialog(frame, "Error: Position already occupied by another ship.");
                    return -3;
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

        return 0;
        
    }
    public boolean isHit(int x, int y) {
    for (Point hitShot : hitShots) {
        if (hitShot.getX() == x && hitShot.getY() == y) {
            return true;
        }
    }
    return false;
}
    


    public void setButtons(JButton[][] buttons) {
        this.buttons = buttons;
    }

    public Ship[][] getShips() {
        return ships;
    }

    public void setShips(Ship[][] ships) {
        this.ships = ships;
    }

    public JFrame getFrame() {
        return frame;
    }

    public void setFrame(JFrame frame) {
        this.frame = frame;
    }

    public int getShips1x1Placed() {
        return ships1x1Placed;
    }

    public void setShips1x1Placed(int ships1x1Placed) {
        this.ships1x1Placed = ships1x1Placed;
    }

    public int getVertical2x2Placed() {
        return vertical2x2Placed;
    }

    public void setVertical2x2Placed(int vertical2x2Placed) {
        this.vertical2x2Placed = vertical2x2Placed;
    }

    public int getVertical3x3Placed() {
        return vertical3x3Placed;
    }

    public void setVertical3x3Placed(int vertical3x3Placed) {
        this.vertical3x3Placed = vertical3x3Placed;
    }

    public int getHorizontal2x2Placed() {
        return horizontal2x2Placed;
    }

    public void setHorizontal2x2Placed(int horizontal2x2Placed) {
        this.horizontal2x2Placed = horizontal2x2Placed;
    }

    public int getHorizontal3x3Placed() {
        return horizontal3x3Placed;
    }

    public void setHorizontal3x3Placed(int horizontal3x3Placed) {
        this.horizontal3x3Placed = horizontal3x3Placed;
    }

    public JPanel getBoardPanel() {
        return boardPanel;
    }

    public void setBoardPanel(JPanel boardPanel) {
        this.boardPanel = boardPanel;
    }

    public boolean isPlayerBoard() {
        return isPlayerBoard;
    }

    public void setPlayerBoard(boolean isPlayerBoard) {
        this.isPlayerBoard = isPlayerBoard;
    }
}
