package controller.gamecontroller;

import model.chess.ChessPiece;
import model.gamemodel.Model;
import view.View;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Toolkit;
import javax.swing.*;

import controller.handlers.HighlightHandler;
import controller.handlers.MenuHandler;
import controller.handlers.MovementHandler;
import controller.handlers.SoundHandler;

// Singleton Design Pattern: Ensures that only one instance 
// of GameController exists throughout the application.

// Mediator Design Pattern: Centralises communication between 
// menu,movement and highlight handler in 
// GameController, reducing direct dependencies.

public class GameController implements ActionListener {

    private Model model;
    private View view;
    private ChessPiece selectedPiece;
    private MenuHandler menuHandler;
    private MovementHandler movementHandler;
    private HighlightHandler highlightHandler;
    private SoundHandler soundHandler;
    private static GameController controller; // - controller : GameController
                                                   

    // Mediator
    private GameController(Model model, View view) {

        this.model = model;
        this.view = view;
        menuHandler = new MenuHandler(this);
        movementHandler = new MovementHandler(this);
        highlightHandler = new HighlightHandler(this);
        soundHandler = new SoundHandler();

        attachListeners();
    }
    
    // Singleton design pattern
    public static GameController getController(Model model, View view) {

        if (controller == null)
            synchronized (GameController.class) {
                if (controller == null)
                    controller = new GameController(model, view);
            }

        return controller;
    }

    // Start game in main program
    public void startProgram() {
        menuHandler.handleStartGame();
    }

    public Model getModel() {
        return model;
    }

    public View getView() {
        return view;
    }


    public void playSound(String soundFilePath, boolean isCritical) {
        
        soundHandler.playSound(soundFilePath, isCritical);
    }

    private void attachListeners() {
        // Add listeners for menu items
        view.getMenuItems()[0].addActionListener(e -> menuHandler.handleResetGame());
        view.getMenuItems()[1].addActionListener(e -> menuHandler.handleSaveGame());
        view.getMenuItems()[2].addActionListener(e -> menuHandler.handleLoadGame());
        view.getMenuItems()[3].addActionListener(e -> menuHandler.toggleAutoFlip());
        view.getMenuItems()[4].addActionListener(e -> menuHandler.toggleMoveHint());

        for (int row = 0; row < view.getButtons().length; row++) {
            for (int col = 0; col < view.getButtons()[row].length; col++) {
                view.getButtons()[row][col].addActionListener(this);
            }
        }
    }

    private void initialiseGame() {
        view.initialiseBoard();
        model.initialiseBoard();
        model.setGameInitialised(true);
    }

    public void resetGame() {

        view.setFlip(false);
        model.resetGame();
        initialiseGame();
        resetSelection();
        refreshBoard();
    }

    public void startGame() {

        if (!model.isGameInitialised()) {
            initialiseGame();
        }
    }

    private void checkGameOver() {
        if (model.isGameOver()) {
            menuHandler.handleGameOver();
        }
    }

    private void updateBoardFlipState() {
        boolean flipped = (model.getMovementCounter() % 2 != 0);
        if (menuHandler.getAutoFlip()) {
            view.setFlip(flipped);
        }
        else{
            view.setFlip(false);
        }
        view.refreshBoardLayout();
    }

    public void updateGameState() {
        int movementCount = model.getMovementCounter();
        view.updateGameStateLabel(movementCount);
    }

    public void refreshBoard() {

        updateBoardFlipState();

        int maxRow = view.getButtons().length;
        int maxCol = view.getButtons()[view.getButtons().length-1].length;

        String[][] pieceIcon = new String[maxRow][maxCol];

        for (int row = 0; row < pieceIcon.length; row++) {
            for (int col = 0; col < pieceIcon[row].length; col++) {
                ChessPiece chess = model.getPieceAt(row, col);
                if (chess != null) {
                    pieceIcon[row][col] = movementHandler.getIconNameForPiece(chess);

                    // checked whether is a ram piece automatically and non Ram pieces always return false
                    boolean ramIsReversed = model.getPieceReversed(chess); 
                    view.setReverse(row, col, ramIsReversed);  

                } else
                    pieceIcon[row][col] = null;

            }
        }
        updateGameState();
        view.updatePieceIcon(pieceIcon);
    }

    // get the piece once select it
    private ChessPiece getSelectedPiece(int selectedRow, int selectedCol) {

        return model.getPieceAt(selectedRow, selectedCol);
    }

    public void resetSelection() {
        this.selectedPiece = null;
        highlightHandler.resetHighlights();
    }

    private void handlePieceSelection(int row, int col) {
        ChessPiece piece = getSelectedPiece(row, col);

        if (piece != null && model.isCorrectTurn(piece)) {

            highlightHandler.highlightSelectedPiece(row, col);
            selectedPiece = piece;

            if(menuHandler.getMoveHint())
            highlightHandler.highlightValidMoves(selectedPiece);

        } else {
            Toolkit.getDefaultToolkit().beep();
            resetSelection();
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JButton source = (JButton) e.getSource();

        // Determine the row and column of the clicked button (square)
        int[] position = view.getPieceIconPosition(source);
        int row = position[0];
        int col = position[1];

        if (selectedPiece == null) {

            handlePieceSelection(row, col);
        } else {

            movementHandler.handlePieceMovement(row, col, selectedPiece);
        }
        checkGameOver();
    }
}