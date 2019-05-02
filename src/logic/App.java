package logic;

import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextCharacter;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.gui2.*;
import com.googlecode.lanterna.gui2.Button;
import com.googlecode.lanterna.gui2.Component;
import com.googlecode.lanterna.gui2.Label;
import com.googlecode.lanterna.gui2.Panel;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.screen.TabBehaviour;
import com.googlecode.lanterna.screen.TerminalScreen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.swing.SwingTerminalFontConfiguration;
import com.googlecode.lanterna.terminal.swing.SwingTerminalFrame;
import com.googlecode.lanterna.gui2.GridLayout;
import gen.MalePlayerGenerator;
import gui.AdvanceNextRound;
import gui.Gui;
import gui.KeyStrokeListener;
import logic.ingame.Kill;
import logic.service.*;
import utils.Money;

import java.awt.*;
import java.io.IOException;
import java.lang.reflect.Array;
import java.math.BigInteger;
import java.text.DecimalFormat;
import java.util.*;
import java.util.function.Function;

public class App {

    private Gui _gui;
    private World _world;
    private Random _randomGenerator = new Random();

    private Manager _player;
    private Team _playerTeam;

    public App() {

        _world = new World();

        // Setup terminal and screen layers
        DefaultTerminalFactory terminalFactory = new DefaultTerminalFactory();

        SwingTerminalFontConfiguration plexMonoSemiBold = SwingTerminalFontConfiguration.newInstance(
                new Font("Source Code Pro", Font.PLAIN, 20));

        terminalFactory.setTerminalEmulatorFontConfiguration(plexMonoSemiBold);

        terminalFactory.setTerminalEmulatorTitle("CS:GO Manager");

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

                    TeamService.insertTeam(new Team(
                            playerTeamId,
                            teamName,
                            teamAcronym,
                            moneyOptions[_gui.getRadioBoxLists().get("new-game-starting-money-radio").getCheckedItemIndex()]));

                    _playerTeam = TeamService.getTeamById(playerTeamId);

                    String firstName = _gui.getTextBox("first-name-textbox").getText();
                    String lastName = _gui.getTextBox("last-name-textbox").getText();

                    _player = new Manager(
                            "player",
                            firstName,
                            lastName,
                            playerTeamId
                    );

                    ManagerService.insertManager(_player);

                    //spawn recruitment window
                    ((AbstractWindow)_gui.getWindow("create-first-team-window")).setTitle(teamName + " | Recruit Players");

                    drawView("createFirstTeam");
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

            case "createFirstTeam": {
                _gui.showWindow("create-first-team-window");
                _gui.getPanel("create-first-team-players-panel").removeAllComponents();

                //headers
                Label nameHeader = new Label("Name").setForegroundColor(TextColor.Factory.fromString("#646464"));
                nameHeader.setLayoutData(GridLayout.createLayoutData(
                        GridLayout.Alignment.CENTER,
                        GridLayout.Alignment.CENTER,
                        true,
                        true,
                        1,
                        1
                ));
                _gui.getPanel("create-first-team-players-panel").addComponent(nameHeader);
                _gui.getPanel("create-first-team-players-panel").addComponent(new Label("Salary").setForegroundColor(TextColor.Factory.fromString("#646464")));
                _gui.getPanel("create-first-team-players-panel").addComponent(new Label("Signing Fee").setForegroundColor(TextColor.Factory.fromString("#646464")));

                _gui.getPanel("create-first-team-players-panel").addComponent(new Label(""));
                _gui.getPanel("create-first-team-players-panel").addComponent(new Label(""));

                _gui.getLabel("create-first-team-top-money-label").setText(_playerTeam.getMoney().toString());

                ArrayList<String> freeAgents =  PlayerService.getFreeAgents();
                ArrayList<String> selectedPlayers = new ArrayList<>();

                freeAgents.forEach((id) -> {
                    Player player = PlayerService.getPlayerById(id);

                    _gui.getPanel("create-first-team-players-panel").addComponent(
                            new Label(player.getFirstName() + " " + "\"" + player.getNickName() + "\"" + " " + player.getLastName()));

                    _gui.getPanel("create-first-team-players-panel").addComponent(new Label(String.valueOf(player.getSalary().toString())).setForegroundColor(TextColor.Factory.fromString("#85BB65")));
                    _gui.getPanel("create-first-team-players-panel").addComponent(new Label(String.valueOf(player.getSigningBonus().toString())).setForegroundColor(TextColor.Factory.fromString("#85BB65")));

                    Button detailsButton = new Button(" Details ", () -> {

                        _gui.loadWindow("player-details-window");
                        ((AbstractWindow)_gui.getWindow("player-details-window")).setTitle(player.getFullName());
                        _gui.getLabel("player-details-firstName").setText(player.getFirstName());
                        _gui.getLabel("player-details-nickName").setText(player.getNickName());
                        _gui.getLabel("player-details-lastName").setText(player.getLastName());
                        _gui.getLabel("player-details-salary").setText(String.valueOf(player.getSalary().toString()));
                        _gui.getLabel("player-details-signingBonus").setText(String.valueOf(player.getSigningBonus().toString()));

                        _gui.getButton("player-details-close-window-button").addListener((button) -> {
                            _gui.unloadWindow("player-details-window");
                        });
                    });

                    _gui.getPanel("create-first-team-players-panel").addComponent(detailsButton);
                    _gui.getWindow("create-first-team-window").setFocusedInteractable(detailsButton);

                    Button recruitButton = new Button("  Recruit  ");

                    recruitButton.addListener((button) -> {
                        if(button.getLabel().equals("  Recruit  ")){

                            if(selectedPlayers.size() < 5){
                                button.setLabel(" Recruited ");
                                selectedPlayers.add(id);
                            }

                        } else {

                            button.setLabel("  Recruit  ");
                            selectedPlayers.remove(id);

                        }

                        Money owedMoney = new Money("0");
                        Money playerMoney = new Money(_playerTeam.getMoney());

                        selectedPlayers.forEach((selectedPlayerId) -> {
                            owedMoney.plus(PlayerService.getPlayerById(selectedPlayerId).getSigningBonus());
                        });

                        playerMoney.minus(owedMoney);

                        _gui.getLabel("create-first-team-top-money-label").setText(playerMoney.toString());

                    });

                    _gui.getPanel("create-first-team-players-panel").addComponent(recruitButton);
                });

                _gui.getButton("create-first-team-next-button").addListener((button) -> {

                    Money owedMoney = new Money("0");
                    Money playerMoney = new Money(_playerTeam.getMoney());

                    if(selectedPlayers.size() == 5 && owedMoney.lessThan(playerMoney)){

                        selectedPlayers.forEach((selectedPlayerId) -> {
                            _playerTeam.insertPlayer(selectedPlayerId);
                        });

                        _playerTeam.getMoney().minus(owedMoney);

                        LeagueService.getLeagueById("league2").removeTeam(LeagueService.getLeagueById("league2").getTeams().get(0));
                        LeagueService.getLeagueById("league2").insertTeam(_playerTeam);

                        LeagueService.getAll().forEach((id, league) -> league.createNewSeason());

                        drawView("main-window");
                    }
                });

            } break;

            case "main-window": {

                _gui.showWindow("main-window");

                ((AbstractWindow)_gui.getWindow("main-window")).setTitle(_playerTeam.getName());
                _gui.getLabel("main-window-money").setText(_playerTeam.getMoney().toString());

                Runnable refreshTime = () -> {
                    _gui.getLabel("main-window-date").setText(TimeService.getDate());
                    _gui.getLabel("main-window-time").setText(TimeService.getTime());
                };
                Runnable refreshLeagueMatches = () -> {
                    //refresh league matches panel
                    _gui.getPanel("main-window-league-matches-items-panel").removeAllComponents();
                    ArrayList<Schedule> leagueMatches = new ArrayList<>();

                    _playerTeam.getActiveLeagues().forEach(leagueId -> {
                        leagueMatches.addAll(LeagueService.getLeagueById(leagueId).getNextSchedules());
                    });

                    leagueMatches.sort(Comparator.naturalOrder());

                    leagueMatches.forEach(schedule -> {
                        Label label = new Label(
                                TeamService.getTeamById( schedule.getHomeTeam()).getAcronym() +
                                        " vs " +
                                        TeamService.getTeamById(schedule.getAwayTeam()).getAcronym() + " | " +
                                        schedule.getTime().getTime()
                        );

                        _gui.getPanel("main-window-league-matches-items-panel").addComponent(label);
                    });

                };
                Runnable refreshLatestResults = () -> {
                    _gui.getPanel("main-window-latest-results-items").removeAllComponents();
                    ArrayList<Match> latestResults = new ArrayList<>();

                    _playerTeam.getActiveLeagues().forEach(leagueId -> {
                        latestResults.addAll(LeagueService.getLeagueById(leagueId).getLatestPlayedMatches());
                    });

                    latestResults.sort(Comparator.comparing(Match::getTime).reversed());

                    latestResults.forEach(match -> {
                        Label label = new Label(
                                match.getHomeTeam().getAcronym() + " " +
                                    match.getHomeTeamScore()+ " : " +
                                    match.getAwayTeamScore() + " " +
                                    match.getAwayTeam().getAcronym()
                        );
                        _gui.getPanel("main-window-latest-results-items").addComponent(label);
                    });
                };

                refreshTime.run();
                refreshLeagueMatches.run();

                _gui.getButton("main-window-next-button").addListener(button -> {

                    TimeService.addHours(1);
                    refreshTime.run();

                    _playerTeam.getActiveLeagues().forEach((league) -> {

                        if(LeagueService.getLeagueById(league).getNextSchedule().getTime().compareTo(TimeService.now()) <= 0){

                            MatchReplay nextMatch = new MatchReplay(LeagueService.getLeagueById(league).simulateNextMatch());

                            System.out.println(nextMatch.getCurrentRound());

                            _gui.loadWindow("match-overview-window");

                            _gui.getLabel("match-overview-score-hometeam-name").setText(nextMatch.getHomeTeam().getName());
                            _gui.getLabel("match-overview-score-awayteam-name").setText(nextMatch.getAwayTeam().getName());
                            _gui.getLabel("match-overview-score-hometeam-score").setText(String.valueOf(nextMatch.getCurrentHomeTeamScore()));
                            _gui.getLabel("match-overview-score-awayteam-score").setText(String.valueOf(nextMatch.getCurrentAwayTeamScore()));

                            Button matchOverviewNextButton = new Button("Next");

                            matchOverviewNextButton.addListener(button1 -> {

                                if(nextMatch.isFinished()){

                                    _gui.unloadWindow("match-overview-window");
                                    _gui.getPanel("match-overview-control-panel").removeAllComponents();
                                    _gui.getPanel("match-overview-killfeed-panel").removeAllComponents();
                                    _gui.getPanel("match-overview-scoreboard-panel").removeAllComponents();

                                    refreshLeagueMatches.run();
                                    refreshLatestResults.run();

                                } else {

                                    _gui.getLabel("match-overview-score-hometeam-score").setText(String.valueOf(nextMatch.getCurrentHomeTeamScore()));
                                    _gui.getLabel("match-overview-score-awayteam-score").setText(String.valueOf(nextMatch.getCurrentAwayTeamScore()));

                                    _gui.getPanel("match-overview-killfeed-panel").removeAllComponents();

                                    nextMatch.getKills().forEach(kill -> {
                                        _gui.getPanel("match-overview-killfeed-panel").addComponent(
                                                new Label (PlayerService.getPlayerById(kill.getKillerId()).getNickName() +
                                                        " killed " +
                                                        PlayerService.getPlayerById(kill.getKilledId()).getNickName()
                                                )
                                        );
                                    });

                                    Panel scoreboardPanel = _gui.getPanel("match-overview-scoreboard-panel");
                                    scoreboardPanel.removeAllComponents();

                                    scoreboardPanel.addComponent(new Label("Name"));
                                    scoreboardPanel.addComponent(new Label("Kills"));
                                    scoreboardPanel.addComponent(new Label("Deaths"));

                                    nextMatch.getScoreboard().getEntries().forEach((entry) -> {
                                        scoreboardPanel.addComponent(new Label(PlayerService.getPlayerById(entry.getPlayerId()). getNickName()));
                                        scoreboardPanel.addComponent(new Label(String.valueOf(entry.getKills())));
                                        scoreboardPanel.addComponent(new Label(String.valueOf(entry.getDeaths())));
                                    });

                                    nextMatch.advanceToNextRound();

                                }

                            });

                            _gui.getPanel("match-overview-control-panel").addComponent(matchOverviewNextButton);
                            _gui.getWindow("match-overview-window").setFocusedInteractable(matchOverviewNextButton);

                        }
                    });

                });

            } break;


        }
    }
}

