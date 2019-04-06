package logic;

import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import gen.MalePlayerGenerator;
import gen.TeamGenerator;

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

    private HashMap<String, League> _leagues = new HashMap<>();
    private HashMap<String, Team> _teams = new HashMap<>();
    private HashMap<String, Player> _players = new HashMap<>();
    private HashMap<String, Manager> _managers = new HashMap<>();
    private HashMap<String, Match> _matches = new HashMap<>();

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

//        initLeagues(pathToLeagues);
//        initTeams(pathToTeams);
//        initPlayers(pathToPlayers);
//        initManagers(pathToManagers);
//        initMatches(pathToMatches);

        MalePlayerGenerator mpg = new MalePlayerGenerator();
        for(int i = 0; i < 20; i++){
            Player newPlayer = mpg.generate();
            _players.put(newPlayer.getId(), newPlayer);
        }

        TeamGenerator tg = new TeamGenerator();
        for(int i = 0; i < 20; i++){
            Team newTeam = tg.generate();
            _teams.put(newTeam.getId(), newTeam);
        }
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

    public HashMap<String, Player> getPlayers() {
        return _players;
    }

    public HashMap<String, Team> getTeams() {
        return _teams;
    }
}
