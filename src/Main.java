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

import gui.GuiMaker;
import logic.*;
import gen.*;

import java.io.IOException;
import java.util.*;

public class Main {

    private String _gameName = "CSim";

    public static void main(String[] args) {

        //RANDOM! not preexisting

        World world = new World();
        GuiMaker guiMaker;

        DefaultTerminalFactory terminalFactory = new DefaultTerminalFactory();
        Screen screen = null;

        try {

            screen = terminalFactory.createScreen();
            screen.startScreen();

            guiMaker = new GuiMaker(screen);

            WindowBasedTextGUI textGUI = guiMaker.generateGui("./xml/gui.xml");

            world.getPlayers().forEach((id, player) -> {
                guiMaker.getPanels().get("player-firstName").addComponent(new Label(player.getFirstName()));
                guiMaker.getPanels().get("player-nickName").addComponent(new Label(player.getNickName()));
                guiMaker.getPanels().get("player-lastName").addComponent(new Label(player.getLastName()));

                System.out.println(player.toString());
            });

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
