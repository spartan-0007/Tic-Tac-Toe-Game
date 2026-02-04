import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class TicTacToe {

    int boardWidth = 600;
    int boardHeight = 650;

    JFrame frame = new JFrame("Tic-Tac-Toe");
    JLabel textLabel = new JLabel();
    JPanel textPanel = new JPanel();
    JPanel boardPanel = new JPanel();

    JButton[][] board = new JButton[3][3];

    String playerX = "X";
    String playerO = "O";
    String currentPlayer = playerX;

    String playerXName;
    String playerOName;

    int playerXScore = 0;
    int playerOScore = 0;

    boolean gameOver = false;
    int turns = 0;

    TicTacToe() {

        playerXName = JOptionPane.showInputDialog("Enter Player X Name:");
        playerOName = JOptionPane.showInputDialog("Enter Player O Name:");

        if (playerXName == null || playerXName.isEmpty()) playerXName = "Player X";
        if (playerOName == null || playerOName.isEmpty()) playerOName = "Player O";

        frame.setVisible(true);
        frame.setSize(boardWidth, boardHeight);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        textLabel.setBackground(Color.darkGray);
        textLabel.setForeground(Color.WHITE);
        textLabel.setFont(new Font("Arial", Font.BOLD, 22));
        textLabel.setHorizontalAlignment(JLabel.CENTER);
        textLabel.setOpaque(true);

        updateScoreboardText();

        textPanel.setLayout(new BorderLayout());
        textPanel.add(textLabel);
        frame.add(textPanel, BorderLayout.NORTH);

        boardPanel.setLayout(new GridLayout(3, 3));
        boardPanel.setBackground(Color.darkGray);
        frame.add(boardPanel);

        for (int r = 0; r < 3; r++) {
            for (int c = 0; c < 3; c++) {
                JButton tile = new JButton();
                board[r][c] = tile;
                boardPanel.add(tile);

                tile.setBackground(Color.darkGray);
                tile.setForeground(Color.white);
                tile.setFont(new Font("Arial", Font.BOLD, 120));
                tile.setFocusable(false);

                tile.addActionListener(e -> {
                    if (gameOver) return;

                    JButton clickedTile = (JButton) e.getSource();

                    if (clickedTile.getText().equals("")) {
                        clickedTile.setText(currentPlayer);
                        turns++;
                        checkWinner();

                        if (!gameOver) {
                            currentPlayer = currentPlayer.equals(playerX) ? playerO : playerX;
                            updateScoreboardText();
                        }
                    }
                });
            }
        }
    }

    void checkWinner() {

        for (int r = 0; r < 3; r++) {
            if (board[r][0].getText().equals("")) continue;
            if (board[r][0].getText().equals(board[r][1].getText()) &&
                board[r][1].getText().equals(board[r][2].getText())) {
                setWinner(board[r][0], board[r][1], board[r][2]);
                return;
            }
        }

        for (int c = 0; c < 3; c++) {
            if (board[0][c].getText().equals("")) continue;
            if (board[0][c].getText().equals(board[1][c].getText()) &&
                board[1][c].getText().equals(board[2][c].getText())) {
                setWinner(board[0][c], board[1][c], board[2][c]);
                return;
            }
        }

        if (!board[0][0].getText().equals("") &&
            board[0][0].getText().equals(board[1][1].getText()) &&
            board[1][1].getText().equals(board[2][2].getText())) {
            setWinner(board[0][0], board[1][1], board[2][2]);
            return;
        }

        if (!board[0][2].getText().equals("") &&
            board[0][2].getText().equals(board[1][1].getText()) &&
            board[1][1].getText().equals(board[2][0].getText())) {
            setWinner(board[0][2], board[1][1], board[2][0]);
            return;
        }

        if (turns == 9) {
            textLabel.setText("It's a Tie!");
            gameOver = true;
            resetBoardAfterDelay();
        }
    }

    void setWinner(JButton b1, JButton b2, JButton b3) {

        b1.setForeground(Color.green);
        b2.setForeground(Color.green);
        b3.setForeground(Color.green);

        gameOver = true;

        if (currentPlayer.equals(playerX)) {
            playerXScore++;
            textLabel.setText(playerXName + " Wins!");
            showStylishWinPopup(playerXName, "X");
        } else {
            playerOScore++;
            textLabel.setText(playerOName + " Wins!");
            showStylishWinPopup(playerOName, "O");
        }

        resetBoardAfterDelay();
    }

    // 🌟 STYLISH POPUP
    void showStylishWinPopup(String winnerName, String symbol) {

        Toolkit.getDefaultToolkit().beep(); // sound

        JDialog dialog = new JDialog(frame, "Winner!", true);
        dialog.setSize(350, 220);
        dialog.setLocationRelativeTo(frame);

        JPanel panel = new JPanel();
        panel.setBackground(new Color(30, 30, 30));
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JLabel title = new JLabel("🏆 VICTORY! 🏆");
        title.setFont(new Font("Arial", Font.BOLD, 26));
        title.setForeground(Color.YELLOW);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel winnerLabel = new JLabel(winnerName + " (" + symbol + ") Wins!");
        winnerLabel.setFont(new Font("Arial", Font.BOLD, 20));
        winnerLabel.setForeground(Color.GREEN);
        winnerLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton okBtn = new JButton("Continue");
        okBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        okBtn.setFont(new Font("Arial", Font.BOLD, 16));
        okBtn.addActionListener(e -> dialog.dispose());

        panel.add(Box.createVerticalStrut(15));
        panel.add(title);
        panel.add(Box.createVerticalStrut(15));
        panel.add(winnerLabel);
        panel.add(Box.createVerticalStrut(20));
        panel.add(okBtn);

        dialog.add(panel);
        dialog.setVisible(true);
    }

    void updateScoreboardText() {
        textLabel.setText(
            playerXName + " (X): " + playerXScore +
            "  |  " +
            playerOName + " (O): " + playerOScore +
            "  — Turn: " +
            (currentPlayer.equals(playerX) ? playerXName : playerOName)
        );
    }

    void resetBoardAfterDelay() {
        Timer timer = new Timer(1500, e -> resetBoard());
        timer.setRepeats(false);
        timer.start();
    }

    void resetBoard() {
        for (int r = 0; r < 3; r++) {
            for (int c = 0; c < 3; c++) {
                board[r][c].setText("");
                board[r][c].setBackground(Color.darkGray);
                board[r][c].setForeground(Color.white);
            }
        }
        turns = 0;
        gameOver = false;
        currentPlayer = playerX;
        updateScoreboardText();
    }

    public static void main(String[] args) {
        new TicTacToe();
    }
}
