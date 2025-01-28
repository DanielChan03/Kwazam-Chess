package model.gamepersistence;

import model.board.ChessBoard;
import model.gamemodel.Model;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class GameDataManager {

    private String saveDirectory;

    public GameDataManager() {
        this.saveDirectory = System.getProperty("user.dir") + File.separator + "savedfiles";
    }

    public void saveGame(String gameName, int movementCounter, ChessBoard board) {
        if (!gameName.endsWith(".txt")) {
            gameName += ".txt";
        }

        File saveDir = new File(saveDirectory);
        if (!saveDir.exists()) {
            saveDir.mkdirs();
        }

        File saveFile = new File(saveDir, gameName);
        SaveGame saveGame = new SaveGame(saveFile.getAbsolutePath());
        saveGame.saveGame(movementCounter, board);
    }

    public void loadGame(String gameName, Model model) {
        File saveFile = new File(saveDirectory, gameName);
        LoadGame loadGame = new LoadGame(saveFile.getAbsolutePath());
        loadGame.loadGame(model);
    }

    public List<String> listSavedGames() {
        File saveDir = new File(saveDirectory);
        if (!saveDir.exists() || saveDir.listFiles().length == 0) {
            return new ArrayList<>();
        }

        File[] saveFiles = saveDir.listFiles((dir, name) -> name.endsWith(".txt"));
        List<String> fileNames = new ArrayList<>();
        for (File file : saveFiles) {
            fileNames.add(file.getName());
        }
        return fileNames;
    }
}