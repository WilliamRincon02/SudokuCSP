/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author William Rincon julio
 */

 import javax.swing.*;
import java.awt.*;
import java.util.Random;

public class SudokuSolverGUI extends JFrame {
    public static final int SIZE = 9;

    private final SudokuBoard board;

    private final JPanel sudokuPanel = new JPanel(new GridLayout(SIZE, SIZE));
    private final JTextField[][] sudokuFields = new JTextField[SIZE][SIZE];

    public SudokuSolverGUI(int[][] initialBoard) {
        setTitle("Sudoku Solver");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        board = new SudokuBoard(initialBoard);

        // Crear los campos de texto para el tablero
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                sudokuFields[i][j] = new JTextField(1);
                sudokuFields[i][j].setHorizontalAlignment(JTextField.CENTER);
                sudokuPanel.add(sudokuFields[i][j]);
            }
        }

        // Rellenar los campos con el tablero inicial
        updateSudokuBoard(initialBoard);

        // Botón para resolver el Sudoku
        JButton solveButton = new JButton("Resolver");
        solveButton.addActionListener(e -> solveSudoku());

        // Layout de la ventana
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(sudokuPanel, BorderLayout.CENTER);
        mainPanel.add(solveButton, BorderLayout.SOUTH);
        setContentPane(mainPanel);

        pack();
        setLocationRelativeTo(null); // Centrar la ventana
        setVisible(true);
    }

    private void solveSudoku() {
        int[][] sudokuValues = readSudokuBoard();
        SudokuSolver solver = new SudokuSolver(new SudokuBoard(sudokuValues));
        if (solver.solve()) {
            updateSudokuBoard(sudokuValues);
        } else {
            JOptionPane.showMessageDialog(this, "No se encontró ninguna solución para el Sudoku dado.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private int[][] readSudokuBoard() {
        int[][] sudokuValues = new int[SIZE][SIZE];
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                try {
                    sudokuValues[i][j] = Integer.parseInt(sudokuFields[i][j].getText());
                } catch (NumberFormatException e) {
                    sudokuValues[i][j] = 0;
                }
            }
        }
        return sudokuValues;
    }

    private void updateSudokuBoard(int[][] sudokuValues) {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                sudokuFields[i][j].setText(Integer.toString(sudokuValues[i][j]));
            }
        }
    }

    public static void main(String[] args) {
        int[][][] boards = {
            // Tablero 1
            {
                {5, 3, 0, 0, 7, 0, 0, 0, 0},
                {6, 0, 0, 1, 9, 5, 0, 0, 0},
                {0, 9, 8, 0, 0, 0, 0, 6, 0},
                {8, 0, 0, 0, 6, 0, 0, 0, 3},
                {4, 0, 0, 8, 0, 3, 0, 0, 1},
                {7, 0, 0, 0, 2, 0, 0, 0, 6},
                {0, 6, 0, 0, 0, 0, 2, 8, 0},
                {0, 0, 0, 4, 1, 9, 0, 0, 5},
                {0, 0, 0, 0, 8, 0, 0, 7, 9}
            },
            // Tablero 2
            {
                {0, 2, 0, 6, 0, 8, 0, 0, 0},
                {5, 8, 0, 0, 0, 9, 7, 0, 0},
                {0, 0, 0, 0, 4, 0, 0, 0, 0},
                {3, 7, 0, 0, 0, 0, 5, 0, 0},
                {6, 0, 0, 0, 0, 0, 0, 0, 4},
                {0, 0, 8, 0, 0, 0, 0, 1, 3},
                {0, 0, 0, 0, 2, 0, 0, 0, 0},
                {0, 0, 9, 8, 0, 0, 0, 3, 6},
                {0, 0, 0, 3, 0, 6, 0, 9, 0}
            }
        };        
        
        Random rand = new Random();
        int[][] selectedBoard = boards[rand.nextInt(boards.length)];

        SwingUtilities.invokeLater(() -> new SudokuSolverGUI(selectedBoard));
    }
}

class SudokuSolver {
    private final SudokuBoard board;

    public SudokuSolver(SudokuBoard board) {
        this.board = board;
    }

    public boolean solve() {
        return solve(0, 0);
    }

    private boolean solve(int row, int col) {
        if (row == SudokuSolverGUI.SIZE) {
            row = 0;
            if (++col == SudokuSolverGUI.SIZE) {
                return true;
            }
        }
        if (board.getCell(row, col) != 0) {
            return solve(row + 1, col);
        }
        for (int num = 1; num <= SudokuSolverGUI.SIZE; num++) {
            if (board.isValid(row, col, num)) {
                board.setCell(row, col, num);
                if (solve(row + 1, col)) {
                    return true;
                }
            }
        }
        board.setCell(row, col, 0);
        return false;
    }
}

class SudokuBoard {
    private final int[][] board;

    public SudokuBoard(int[][] board) {
        this.board = board;
    }

    public int getCell(int row, int col) {
        return board[row][col];
    }

    public void setCell(int row, int col, int value) {
        board[row][col] = value;
    }

    public boolean isValid(int row, int col, int num) {
        for (int x = 0; x < SudokuSolverGUI.SIZE; x++) {
            if (board[row][x] == num || board[x][col] == num || board[row - row % 3 + x / 3][col - col % 3 + x % 3] == num) {
                return false;
            }
        }
        return true;
    }
}
