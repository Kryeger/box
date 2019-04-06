import com.googlecode.lanterna.*;
import com.googlecode.lanterna.bundle.LanternaThemes;
import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.gui2.*;
import com.googlecode.lanterna.gui2.Button;
import com.googlecode.lanterna.gui2.Label;
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
import com.googlecode.lanterna.terminal.swing.SwingTerminalFontConfiguration;
import com.googlecode.lanterna.terminal.swing.SwingTerminalFrame;
import com.opencsv.CSVReader;

import com.sun.jdi.request.AccessWatchpointRequest;
import gui.Gui;
import gui.KeyStrokeListener;
import logic.*;
import gen.*;

import javax.swing.*;
import java.awt.*;
import java.awt.GridLayout;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class Main {

    public static void main(String[] args) throws IOException {

        World world = new World();

        DecimalFormat formatter = new DecimalFormat("#.00");

        // Setup terminal and screen layers
        DefaultTerminalFactory terminalFactory = new DefaultTerminalFactory();

        SwingTerminalFontConfiguration plexMonoSemiBold = SwingTerminalFontConfiguration.newInstance(
                new Font("Source Code Pro", Font.PLAIN, 20));

        terminalFactory.setTerminalEmulatorFontConfiguration(plexMonoSemiBold);

        terminalFactory.setTerminalEmulatorTitle("CS:GO Manager");

        SwingTerminalFrame terminal = (SwingTerminalFrame)terminalFactory.createTerminal();
        //terminal.setExtendedState(JFrame.MAXIMIZED_BOTH);
        //terminal.setSize(Toolkit.getDefaultToolkit().getScreenSize());

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
                gui.getPanels().get("player-firstName").addComponent(new Label(player.getFirstName()));
                gui.getPanels().get("player-nickName").addComponent(new Label(player.getNickName()));
                gui.getPanels().get("player-lastName").addComponent(new Label(player.getLastName()));
                gui.getPanels().get("player-skillRating").addComponent(new Label(String.valueOf(formatter.format(player.getSkillRating()))));
            });

            world.getTeams().forEach((id, team) -> {
                gui.getPanels().get("team-name").addComponent(new Label(team.getName()));
                gui.getPanels().get("team-acronym").addComponent(new Label(team.getAcronym()));
            });
        });

        try {

            gui.getThread().waitForStop();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
