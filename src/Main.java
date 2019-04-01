import com.googlecode.lanterna.*;
import com.googlecode.lanterna.bundle.LanternaThemes;
import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.gui2.*;
import com.googlecode.lanterna.gui2.dialogs.MessageDialog;
import com.googlecode.lanterna.gui2.dialogs.MessageDialogButton;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.screen.TerminalScreen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.Terminal;
import com.googlecode.lanterna.terminal.TerminalResizeListener;

import com.opencsv.CSVReader;

import logic.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.TimeZone;

public class Main {

    private String _gameName = "CSGO Universe";

    public static void main(String[] args) {

        //RANDOM! not preexisting

        World world = new World();

        DefaultTerminalFactory terminalFactory = new DefaultTerminalFactory();
        Screen screen = null;

        try {

            screen = terminalFactory.createScreen();
            screen.startScreen();

            final WindowBasedTextGUI textGUI = new MultiWindowTextGUI(screen);

            final Window window = new BasicWindow("Welcome");
            System.out.println(textGUI.getScreen().getTerminalSize());

            //CONTENT PANEL
            Panel contentPanel = new Panel(new GridLayout(1));
            GridLayout contentPanelLayoutManager = (GridLayout)contentPanel.getLayoutManager();
            contentPanelLayoutManager.setVerticalSpacing(1);

            //NEW_GAME PANEL
            Panel welcomePanel = new Panel(new GridLayout(1));
            GridLayout newGamePanelLayoutManager = (GridLayout)welcomePanel.getLayoutManager();

            Button newGameButton = new Button("New Game", new Runnable() {
                @Override
                public void run() {
                    MessageDialog.showMessageDialog(textGUI, "New Game", "Starting a new game...", MessageDialogButton.OK);
                }
            });
            welcomePanel.addComponent(newGameButton);

            Button loadGameButton = new Button("Load Game", new Runnable() {
                @Override
                public void run() {
                    MessageDialog.showMessageDialog(textGUI, "New Game", "Starting a new game...", MessageDialogButton.OK);
                }
            });
            welcomePanel.addComponent(loadGameButton);

            contentPanel.addComponent(welcomePanel);

            window.setComponent(contentPanel);
            textGUI.addWindowAndWait(window);

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (screen != null) {
                try {
                    screen.stopScreen();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
