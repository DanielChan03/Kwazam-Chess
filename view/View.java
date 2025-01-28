package view;

import javax.swing.*;

import java.awt.*;

public class View {

    private JFrame frame;
    private JLabel gameStateLabel;
    private BoardGUI boardInterface;
    private GameMenu gameMenu;
    private JLabel bottomLabel; // to balance the position of board on top and bottom

    public View() {

        // Allows Mac and Win to show the same GUI
        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        boardInterface = new BoardGUI();

        frame = new JFrame("Kwazam Chess Game");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel chessPanel = new JPanel(new GridBagLayout());
        // chessPanel.setBackground(new Color(245, 245, 220));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1; // Allow horizontal expansion
        gbc.weighty = 1; // Allow vertical expansion
        gbc.fill = GridBagConstraints.BOTH; // Fill the entire space
        chessPanel.add(boardInterface, gbc);

        frame.getContentPane().add(chessPanel);
        // Set minimum size for the frame
        int minWidth = 360; // Example minimum width
        int minHeight = 580; // Example minimum height
        frame.setMinimumSize(new Dimension(minWidth, minHeight));
        
        gameStateLabel = new JLabel("Turn Count: 0, Player 1's Turn");
        gameStateLabel.setHorizontalAlignment(SwingConstants.CENTER);
        gameStateLabel.setBackground(new Color(245, 245, 220));
        gameStateLabel.setOpaque(true);
        gameStateLabel.setFont(new Font("Arial", Font.BOLD, 16));
        
        bottomLabel = new JLabel(" ");
        bottomLabel.setBackground(new Color(245, 245, 220));
        bottomLabel.setOpaque(true);

        frame.add(gameStateLabel, BorderLayout.NORTH);
        frame.add(bottomLabel,BorderLayout.SOUTH);
        // Initialise GameMenu
        gameMenu = new GameMenu(frame);
        
        frame.setResizable(true);
        frame.pack();
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);
    }

    public void initialiseBoard() {
        boardInterface.initialiseBoard();
    }

    public JMenuItem[] getMenuItems() {

        return gameMenu.getMenuItems();
    }

    public JButton[][] getButtons() {
        return boardInterface.getButtons();
    }

    public void highlightSelectedPiece(int row, int col) {

        JButton button = boardInterface.getButtons()[row][col];
        if (button instanceof HighlightableButton) {

            HighlightableButton highlightButton = (HighlightableButton) button;
            Color selectedHighlight = highlightButton.getSelectedHighlight();
            ((HighlightableButton) button).setHighlight(true, selectedHighlight); // Highlight selected button
        }
    }

    public void highlightPossibleMove(int row, int col, String colour, boolean isAttack) {
        JButton button = boardInterface.getButtons()[row][col];
        if (button instanceof HighlightableButton){

            HighlightableButton highlightButton = (HighlightableButton) button;
            Color highlightBlue = highlightButton.getBlueHighlight();
            Color highlightRed = highlightButton.getRedHighlight();

            // Determine color based on move or attack
            Color highlightColor = (colour.equals("Blue"))
                    ? (isAttack ? highlightRed : highlightBlue)
                    : (isAttack ? highlightBlue : highlightRed);

            highlightButton.setHighlight(true, highlightColor);
        }
    }

    public void resetHighlights() {
        JButton[][] buttons = boardInterface.getButtons();

        for (int row = 0; row < buttons.length; row++) {
            for (int col = 0; col < buttons[row].length; col++) {
                if (buttons[row][col] instanceof HighlightableButton) {
                    ((HighlightableButton) buttons[row][col]).resetHighlight();
                }
            }
        }
    }

    public void startGameMessage() {
        JOptionPane.showMessageDialog(frame, "Click to Start!", "KWAZAN CHESS", JOptionPane.INFORMATION_MESSAGE);
    }

    public void resetGameMessage() {
        JOptionPane.showMessageDialog(frame, "Game is Reset!", "Game Message", JOptionPane.INFORMATION_MESSAGE);
    }

    public void gameOverMessage(String winner) {
        JOptionPane.showMessageDialog(frame, winner + " wins the game!");
    }

    public void saveGameMessage() {
        JOptionPane.showMessageDialog(frame, "Game saved!", "Game Saved", JOptionPane.INFORMATION_MESSAGE);
    }

    public void saveGameErrorMessage() {
        JOptionPane.showMessageDialog(frame,  "Save game name cannot be empty.", "Error", JOptionPane.ERROR_MESSAGE);
    }

    public String saveGameNameMessage() {
        return JOptionPane.showInputDialog(frame, "Enter a name for the saved game:", "Save Game", JOptionPane.PLAIN_MESSAGE);
    }

    public void loadGameMessage() {
        JOptionPane.showMessageDialog(frame, "Game loaded!", "Game Loaded", JOptionPane.INFORMATION_MESSAGE);
    }

    public void noSavedGamesMessage() {
        JOptionPane.showMessageDialog(frame, "No saved games found.", "Load Game", JOptionPane.INFORMATION_MESSAGE);
    }

    public String selectGameToLoadMessage(String[] fileNames) {
        return (String) JOptionPane.showInputDialog(frame, "Select a game to load:", "Load Game",
                JOptionPane.PLAIN_MESSAGE, null, fileNames, fileNames[0]);
    }

    public void updateGameStateLabel(int movementCount) {
        String currentPlayer = (movementCount % 2 == 0)? "Player 1" : "Player 2";
        int turnCount = movementCount / 2;  // 2 movements = 1 turn

        gameStateLabel.setText("Turn Count: " + turnCount + ", " + currentPlayer + "'s Turn");
    }

    public void updateGameStateEndLabel(){
        gameStateLabel.setText("Game Over!");
    }

    public void setFlip(boolean flip) {
        boardInterface.setFlip(flip);
    }

    public void setReverse(int row, int col, boolean reversed) {
        boardInterface.setReverse(row, col, reversed);
    }
    
    public boolean getIsFlipped() {
        return boardInterface.getIsFlipped();
    }
    
    public boolean getIsReversed(int row, int col) {
        return boardInterface.getIsReversed(row, col);
    }

    public void refreshGUI() {
        boardInterface.revalidate();
        boardInterface.repaint();
    }

    public void setPieceImage(int row, int col, String pieceName) {
        boardInterface.setPieceImage(row, col, pieceName);
    }

    public void updatePieceImagePosition(String iconName, int oldRow, int oldCol, int newRow, int newCol) {
        // Clear the old position
        boardInterface.resetPieceImage(oldRow, oldCol);

        // Set the new position
        setPieceImage(newRow, newCol, iconName);
    }

    public void refreshBoardLayout() {

        boardInterface.removeAll();

        if (getIsFlipped()) {
            // Add buttons in flipped view
            for (int row = boardInterface.getButtons().length - 1; row >= 0; row--) {
                for (int col = boardInterface.getButtons()[row].length - 1; col >= 0; col--) {
                    boardInterface.add(boardInterface.getButtons()[row][col]);
                    setPieceImage(row, col, boardInterface.getPieceName(row, col));
                }
            }
        } else {
            // Add buttons in normal order
            for (int row = 0; row < boardInterface.getButtons().length; row++) {
                for (int col = 0; col < boardInterface.getButtons()[row].length; col++) {
                    boardInterface.add(boardInterface.getButtons()[row][col]);
                    setPieceImage(row, col, boardInterface.getPieceName(row, col));
                }
            }
        }

        // Refresh the panel
        refreshGUI();
    }

    public int[] getPieceIconPosition(JButton translateButton) {

        int[] position = new int[2];

        JButton[][] buttons = boardInterface.getButtons();

        for (int row = 0; row < boardInterface.getButtons().length; row++) {
            for (int col = 0; col < boardInterface.getButtons()[row].length; col++) {
                if (buttons[row][col] == translateButton) {
                    position[0] = row;
                    position[1] = col;

                    return position;
                }
            }
        }
        return position;
    }

    // Update
    public void updatePieceIcon(String[][] pieceIcon) {

        JButton[][] buttons = boardInterface.getButtons();

        for (int row = 0; row < buttons.length; row++) {
            for (int col = 0; col < buttons[row].length; col++) {
                String piece = pieceIcon[row][col];

                if (piece != null) {
                    setPieceImage(row, col, piece);
                } else {
                    // Reset the piece icon if no piece is present
                    setPieceImage(row, col, null);

                }
            }
        }
    }
}