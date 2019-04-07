package logic;

import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextCharacter;
import com.googlecode.lanterna.gui2.*;
import com.googlecode.lanterna.gui2.Button;
import com.googlecode.lanterna.gui2.Component;
import com.googlecode.lanterna.gui2.Label;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.screen.TabBehaviour;
import com.googlecode.lanterna.screen.TerminalScreen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.swing.SwingTerminalFontConfiguration;
import com.googlecode.lanterna.terminal.swing.SwingTerminalFrame;
import com.googlecode.lanterna.gui2.GridLayout;
import gen.MalePlayerGenerator;
import gui.Gui;
import gui.KeyStrokeListener;

import java.awt.*;
import java.io.IOException;
import java.math.BigInteger;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.UUID;

public class App {

    private Gui _gui;
    private World _world;

    private Manager _player;
    private Team _playerTeam;

    public App() {

        _world = new World();

        // Setup terminal and screen layers
        DefaultTerminalFactory terminalFactory = new DefaultTerminalFactory();

        SwingTerminalFontConfiguration plexMonoSemiBold = SwingTerminalFontConfiguration.newInstance(
                new Font("Source Code Pro", Font.PLAIN, 20));

        terminalFactory.setTerminalEmulatorFontConfiguration(plexMonoSemiBold);

        terminalFactory.setTerminalEmulatorTitle("CS:GO Manager 0.0.1");

        terminalFactory.setInitialTerminalSize(new TerminalSize(130, 40));

        Screen screen = null;

        try {
            SwingTerminalFrame terminal = (SwingTerminalFrame)terminalFactory.createTerminal();

            screen = new TerminalScreen(terminal);
            screen.doResizeIfNecessary();
            screen.setTabBehaviour(TabBehaviour.CONVERT_TO_FOUR_SPACES);
            screen.startScreen();
        } catch (IOException e) {
            e.printStackTrace();
        }

        _gui = new Gui(screen, "./xml/gui.xml");

    }

    public void run() {

        _gui.getThread().start();

        //gui control
        _gui.getThread().invokeLater(() -> {

            drawView("welcome");
            /*
            _world.getPlayers().forEach((id, player) -> {
                _gui.getPanel("player-firstName").addComponent(new com.googlecode.lanterna.gui2.Label(player.getFirstName()));
                _gui.getPanel("player-nickName").addComponent(new com.googlecode.lanterna.gui2.Label(player.getNickName()));
                _gui.getPanel("player-lastName").addComponent(new com.googlecode.lanterna.gui2.Label(player.getLastName()));
                _gui.getPanel("player-skillRating").addComponent(new com.googlecode.lanterna.gui2.Label(String.valueOf(formatter.format(player.getSkillRating()))));
            });

            _world.getTeams().forEach((id, team) -> {
                _gui.getPanel("team-name").addComponent(new com.googlecode.lanterna.gui2.Label(team.getName()));
                _gui.getPanel("team-acronym").addComponent(new Label(team.getAcronym()));
            });*/

        });

        try {

            _gui.getThread().waitForStop();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void drawView(String viewId){
        switch(viewId){
            case "welcome": {

                _gui.showWindow("welcome-window");

                _gui.getButton("new-game-button").addListener((button) -> {
                    drawView("newGame");
                });

            } break;

            case "newGame": {

                _gui.showWindow("new-game-window");

                _gui.getButton("start-game-button").addListener((button) -> {
                    //init world
                    String teamName = _gui.getTextBox("team-name-textbox").getText();
                    String teamAcronym = _gui.getTextBox("team-acronym-textbox").getText();

                    String[] moneyOptions = {"30000", "20000", "10000"};
                    String playerTeamId = UUID.randomUUID().toString();
                    _world.getTeams().put(playerTeamId, new Team(
                            playerTeamId,
                            teamName,
                            teamAcronym,
                            moneyOptions[_gui.getRadioBoxLists().get("new-game-starting-money-radio").getCheckedItemIndex()]));

                    _playerTeam = _world.getTeams().get(playerTeamId);

                    String firstName = _gui.getTextBox("first-name-textbox").getText();
                    String lastName = _gui.getTextBox("last-name-textbox").getText();

                    _player = new Manager(
                            firstName,
                            lastName,
                            playerTeamId
                    );

                    _world.getManagers().put("player", _player);

                    //spawn recruitment window
                    ((AbstractWindow)_gui.getWindow("create-first-team-window")).setTitle(teamName + " | Recruit Players");

                    drawView("createTeam");
                    //_gui.getLabels().get("create-first-team-top-money-label").setText(_player.getMoney());

                });

                _gui.getRadioBoxLists().get("new-game-starting-money-radio").addListener((current, last) -> {
                    switch(current){
                        case 0:
                            _gui.getLabel("starting-money-label").setText("$30,000.000");
                            break;
                        case 1:
                            _gui.getLabel("starting-money-label").setText("$20,000.000");
                            break;
                        case 2:
                            _gui.getLabel("starting-money-label").setText("$10,000.000");
                            break;
                    }
                });

            } break;

            case "createTeam": {
                _gui.showWindow("create-first-team-window");
                _gui.getPanel("create-first-team-players-panel").removeAllComponents();

                _gui.getLabel("create-first-team-top-money-label").setText(_playerTeam.getMoney().toString());

                ArrayList<Player> players = new ArrayList<>();
                MalePlayerGenerator mpg = new MalePlayerGenerator();

                for (int i = 0; i < 10; i++) {
                    players.add(mpg.generate(20));
                }

                players.forEach((player) -> {

                    _gui.getPanel("create-first-team-players-panel").addComponent(new Label(player.getFirstName()));
                    _gui.getPanel("create-first-team-players-panel").addComponent(new Label("\"" + player.getNickName() + "\""));
                    _gui.getPanel("create-first-team-players-panel").addComponent(new Label(player.getLastName()));
                    _gui.getPanel("create-first-team-players-panel").addComponent(new Label(player.getTeamId()));
                    _gui.getPanel("create-first-team-players-panel").addComponent(new Label(String.valueOf(player.getSkillRating())));
                    _gui.getPanel("create-first-team-players-panel").addComponent(new Label(String.valueOf(player.getSalary().toString())));
                    _gui.getPanel("create-first-team-players-panel").addComponent(new Label(String.valueOf(player.getSigningBonus().toString())));

                    Button detailsButton = new Button(" Details ", () -> {

                        _gui.loadWindow("player-details-window");
                        ((AbstractWindow)_gui.getWindow("player-details-window")).setTitle(player.getFullName());
                        _gui.getLabel("player-details-firstName").setText(player.getFirstName());
                        _gui.getLabel("player-details-nickName").setText(player.getNickName());
                        _gui.getLabel("player-details-lastName").setText(player.getLastName());
                        _gui.getLabel("player-details-skillRating").setText(String.valueOf(player.getSkillRating()));
                        _gui.getLabel("player-details-salary").setText(String.valueOf(player.getSalary().toString()));
                        _gui.getLabel("player-details-signingBonus").setText(String.valueOf(player.getSigningBonus().toString()));

                        _gui.getButton("player-details-close-window-button").addListener((button) -> {
                            _gui.unloadWindow("player-details-window");
                        });
                    });

                    _gui.getWindow("create-first-team-window").setFocusedInteractable(detailsButton);
                    _gui.getPanel("create-first-team-players-panel").addComponent(detailsButton);

                    Button recruitButton = new Button(" Recruit ");
                    recruitButton.addListener((button) -> {
                        if(button.getLabel().equals(" Recruit ")){
                            button.setLabel(" Recruited ");
                        } else {
                            button.setLabel(" Recruit ");
                        }
                    });

                    _gui.getPanel("create-first-team-players-panel").addComponent(recruitButton);
                });

            } break;
        }
    }
}
