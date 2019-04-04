import com.googlecode.lanterna.*;
import com.googlecode.lanterna.bundle.LanternaThemes;
import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.gui2.*;
import com.googlecode.lanterna.gui2.Button;
import com.googlecode.lanterna.gui2.Window;
import com.googlecode.lanterna.gui2.dialogs.MessageDialog;
import com.googlecode.lanterna.gui2.dialogs.MessageDialogButton;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.screen.TabBehaviour;
import com.googlecode.lanterna.screen.TerminalScreen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.Terminal;
import com.googlecode.lanterna.terminal.TerminalResizeListener;

import com.googlecode.lanterna.terminal.swing.AWTTerminalFontConfiguration;
import com.opencsv.CSVReader;

import gui.Gui;
import gui.KeyStrokeListener;
import logic.*;
import gen.*;

import java.awt.*;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class Main {

    public static void main(String[] args) throws IOException {

        World world = new World();

        // Setup terminal and screen layers
        DefaultTerminalFactory terminalFactory = new DefaultTerminalFactory();

        AWTTerminalFontConfiguration plexMonoSemiBold = AWTTerminalFontConfiguration.newInstance(
                new Font("Source Code Pro", Font.ITALIC, 30));

        terminalFactory.setTerminalEmulatorFontConfiguration(plexMonoSemiBold);

        terminalFactory.setTerminalEmulatorTitle("CS:GO Manager");

        terminalFactory.setInitialTerminalSize(new TerminalSize(200, 60));

        Terminal terminal = terminalFactory.createTerminal();

        Screen screen = new TerminalScreen(terminal);
        screen.doResizeIfNecessary();
        screen.setTabBehaviour(TabBehaviour.CONVERT_TO_FOUR_SPACES);
        screen.startScreen();

        Gui gui = new Gui(screen, "./xml/gui.xml");

        Window mainWindow = gui.getWindows().get("main");

        mainWindow.addWindowListener(new KeyStrokeListener());

        gui.getThread().start();

        gui.getThread().invokeLater(() -> {
            world.getPlayers().forEach((id, player) -> {
                gui.getPanels().get("player-firstName").addComponent(new Button(player.getFirstName(), () -> {
                    System.out.println(player.toString());
                }));
                gui.getPanels().get("player-nickName").addComponent(new Button(player.getNickName()));
                gui.getPanels().get("player-lastName").addComponent(new Button(player.getLastName()));

                System.out.println(player.toString());
            });

        });

        try {

            gui.getThread().waitForStop();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
