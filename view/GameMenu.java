package view;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

public class GameMenu {
    private JMenuItem[] menuItems;
    private JCheckBoxMenuItem autoFlipCheckBox;
    private JCheckBoxMenuItem moveHintCheckBox;

    GameMenu(JFrame frame){
        createMenuBar(frame);
    }
    
    public JMenuItem[] getMenuItems(){
        return menuItems;
    }

    private void createMenuBar(JFrame frame) {
        // Create a menu bar
        JMenuBar menuBar = new JMenuBar();

        // Create a "View" menu
        JMenu viewMenu = new JMenu("Menu");
        menuItems = new JMenuItem[5];

        this.menuItems[0] = new JMenuItem("Reset Game");
        this.menuItems[1] = new JMenuItem("Save");
        this.menuItems[2] = new JMenuItem("Load");
        
        this.autoFlipCheckBox = new JCheckBoxMenuItem("Auto-Flip");
        this.moveHintCheckBox = new JCheckBoxMenuItem("Move Hint");
        autoFlipCheckBox.setSelected(true);
        moveHintCheckBox.setSelected(true);
        this.menuItems[3] = autoFlipCheckBox;
        this.menuItems[4] = moveHintCheckBox;

        // Add the menu item to the "View" menu
        for(int i =0; i < menuItems.length;i++)
        viewMenu.add(menuItems[i]);

        // Add the "View" menu to the menu bar
        menuBar.add(viewMenu);

        // Add the menu bar to the frame
        frame.setJMenuBar(menuBar);
    }
}
