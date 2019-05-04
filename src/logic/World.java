package logic;

import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import gen.MalePlayerGenerator;
import gen.ManagerGenerator;
import gen.TeamGenerator;
import logic.service.*;
import org.apache.commons.collections4.map.LinkedMap;
import utils.Time;

import java.io.IOException;
import java.io.Reader;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class World implements Serializable {

    String pathToLeagues;
    String pathToPlayers = "./data/players/players.csv";
    String pathToTeams = "./data/teams/teams.csv";
    String pathToManagers;

    String pathToMatches;

    public World() {
        
        MalePlayerGenerator malePlayerGenerator = new MalePlayerGenerator();
        TeamGenerator teamGenerator = new TeamGenerator();

        for (int i = 0; i < 3; i++) {

            League newLeague = new League("league" + i, "League" + (i + 1));

            for (int j = 0; j < 30; j++) {

                Team newTeam = teamGenerator.generate();
                TeamService.insertTeam(newTeam);

                Manager newManager = ManagerGenerator.generate();
                newManager.setTeamId(newTeam.getId());
                ManagerService.insertManager(newManager);

                for (int k = 0; k < 5; k++) {

                    Player newPlayer = malePlayerGenerator.generate((3 - i) * 25);
                    PlayerService.insertPlayer(newPlayer);
                    TeamService.insertPlayerInTeam(newPlayer.getId(), newTeam.getId());

                }

                newLeague.insertTeam(newTeam);
                newTeam.insertActiveLeague(newLeague.getId());
            }

            LeagueService.insertLeague(newLeague);

        }

        for (int i = 1; i < 5; i += 1) {
            for (int j = 0; j < 3; j++) {
                Player newPlayer = malePlayerGenerator.generate(i * 10);
                PlayerService.insertPlayer(newPlayer);
            }
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
                TeamService.insertTeam(newTeam);
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
                PlayerService.insertPlayer(newPlayer);

                TeamService.insertPlayerInTeam(newPlayer.getId(), newPlayer.getTeamId());
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

}
