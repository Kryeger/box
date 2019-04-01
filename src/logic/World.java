package logic;

import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class World {

    String pathToLeagues;
    String pathToPlayers = "./data/players/players.csv";
    String pathToTeams = "./data/teams/teams.csv";
    String pathToManagers;
    String pathToMatches;

    private HashMap<String, League> _leagues;
    private HashMap<String, Team> _teams;
    private HashMap<String, Player> _players;
    private HashMap<String, Manager> _managers;
    private HashMap<String, Match> _matches;

    @Override
    public String toString() {
        return "World{" +
                "pathToLeagues='" + pathToLeagues + '\'' +
                ", pathToPlayers='" + pathToPlayers + '\'' +
                ", pathToTeams='" + pathToTeams + '\'' +
                ", pathToManagers='" + pathToManagers + '\'' +
                ", pathToMatches='" + pathToMatches + '\'' +
                ", _leagues=" + _leagues +
                ", _teams=" + _teams +
                ", _players=" + _players +
                ", _managers=" + _managers +
                ", _matches=" + _matches +
                "}\n";
    }

    public World() {

        _leagues = new HashMap<>();
        _players = new HashMap<>();
        _teams = new HashMap<>();
        _managers = new HashMap<>();
        _matches = new HashMap<>();

//        initLeagues(pathToLeagues);
        initTeams(pathToTeams);
        initPlayers(pathToPlayers);
//        initManagers(pathToManagers);
//        initMatches(pathToMatches);

        System.out.println(_teams.toString());
    }

    private void initTeams(String pathToTeams) {
        try (
                Reader reader = Files.newBufferedReader(Paths.get(pathToTeams));
        ) {
            ColumnPositionMappingStrategy strategy = new ColumnPositionMappingStrategy();

            strategy.setType(Team.class);
            String[] memberFieldsToBindTo = {"id", "name"};
            strategy.setColumnMapping(memberFieldsToBindTo);

            @SuppressWarnings("unchecked") //ffs
                    CsvToBean<Team> csvToBean = new CsvToBeanBuilder(reader)
                    .withMappingStrategy(strategy)
                    .withSkipLines(1)
                    .withIgnoreLeadingWhiteSpace(true)
                    .withStrictQuotes(true)
                    .withQuoteChar('"')
                    .build();

            Iterator<Team> teamIterator = csvToBean.iterator();

            while (teamIterator.hasNext()) {
                Team newTeam = teamIterator.next();
                _teams.put(newTeam.getId(), newTeam);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initPlayers(String pathToPlayers) {
        //TODO: ask about try(){}catch{}
        try (
                Reader reader = Files.newBufferedReader(Paths.get(pathToPlayers));
        ) {
            ColumnPositionMappingStrategy strategy = new ColumnPositionMappingStrategy();

            strategy.setType(Player.class);
            String[] memberFieldsToBindTo = {"id", "firstName", "lastName", "nickName", "skillRating", "teamId"};
            strategy.setColumnMapping(memberFieldsToBindTo);

            @SuppressWarnings("unchecked") //ffs
                    CsvToBean<Player> csvToBean = new CsvToBeanBuilder(reader)
                    .withMappingStrategy(strategy)
                    .withSkipLines(1)
                    .withIgnoreLeadingWhiteSpace(true)
                    .withStrictQuotes(true)
                    .withQuoteChar('"')
                    .build();

            Iterator<Player> playerIterator = csvToBean.iterator();

            while (playerIterator.hasNext()) {
                Player newPlayer = playerIterator.next();
                _players.put(newPlayer.getId(), newPlayer);

                System.out.println(newPlayer.toString());
                if(_teams.containsKey(newPlayer.getTeamId())){
                    _teams.get(newPlayer.getTeamId()).insertPlayer(newPlayer.getId());
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initLeagues(String pathToLeagues) {

    }

    private void initManagers(String pathToManagers) {
    }

    private void initMatches(String pathToMatches) {
    }

    public String getPlayersAllDetails() {
        StringBuilder ret = new StringBuilder();

        for(Map.Entry<String, Player> entry: _players.entrySet()){
            ret.append(entry.getValue().getAllDetails());
            ret.append("\n");
        }

        return ret.toString();
    }

}
