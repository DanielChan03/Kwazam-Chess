package controller.handlers;

import java.util.List;

import controller.gamecontroller.GameController;

public class MenuHandler {
    private GameController controller;
    private boolean autoFlip = true;
    private boolean moveHint = true;

    public MenuHandler(GameController controller) {
        this.controller = controller;
    }

    public void handleResetGame() {

        int movementCount = controller.getModel().getMovementCounter();
        boolean gameOver = controller.getModel().isGameOver();

        // Game will not show resetMessage when the chess movementCount is 0 and game is over.
        if (movementCount == 0 || gameOver) {
            controller.resetGame();
            return;
        }

        controller.resetGame();
        controller.getView().resetGameMessage();
    }

    public void handleStartGame() {

    controller.getView().startGameMessage();
    controller.startGame();
       
    }

    public void handleGameOver() {
        String winner = (controller.getModel().getMovementCounter() % 2 == 0) ? "Player 2" : "Player 1";
        String soundName = "game_over";
        controller.playSound(soundName, true);
        controller.getView().updateGameStateEndLabel();
        controller.getView().gameOverMessage(winner);
        controller.getModel().setGameInitialised(false);
        handleResetGame();
    }

    public void handleSaveGame() {
        String gameName = controller.getView().saveGameNameMessage();
        if (gameName == null) {
            return;
        }

        if (!gameName.trim().isEmpty()) {
            controller.getModel().saveGame(gameName);
            controller.getView().saveGameMessage();
        } else {
            controller.getView().saveGameErrorMessage();
        }
    }

    public void handleLoadGame() {

        List<String> savedGames = controller.getModel().listSavedGames();
        if (savedGames.isEmpty()) {
            controller.getView().noSavedGamesMessage();
            return;
        }

        String selectedFile = controller.getView().selectGameToLoadMessage(savedGames.toArray(new String[0]));
        if (selectedFile != null) {
            controller.getModel().loadGame(selectedFile);
            controller.refreshBoard();
            controller.getView().loadGameMessage();
        }
    }

    public boolean getAutoFlip() {
        return autoFlip;
    }

    public boolean getMoveHint() {
        return moveHint;
    }

    public void toggleAutoFlip() {
        this.autoFlip = !autoFlip;
        controller.refreshBoard();
    }

    public void toggleMoveHint() {
        this.moveHint = !moveHint;
        controller.resetSelection();
    }
}
