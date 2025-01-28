package main;

import view.View;
import controller.gamecontroller.GameController;
import model.gamemodel.Model;

public class MainProgram {
    
    public static void main(String[] args) {
        Model model = new Model();
        View view = new View();
        GameController gameConsole = GameController.getController(model,view);
        gameConsole.startProgram();
    }
}
