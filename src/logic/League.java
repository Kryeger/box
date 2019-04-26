package logic;

import logic.service.SeasonService;
import logic.service.TeamService;
import logic.service.TimeService;
import org.apache.commons.collections4.map.LinkedMap;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import java.util.*;

public class League {

    private String _id;
    private String _name;
    private ArrayList<String> _seasons = new ArrayList<>();
    private ArrayList<String> _teams = new ArrayList<>();

    public League(String id, String name) {
        _id = id;
        _name = name;
    }

    public String getId() {
        return _id;
    }

    public void setId(String id) {
        _id = id;
    }

    @Override
    public String toString() {
        return "League{" +
                "_name='" + _name + '\'' +
                ", _seasons=" + _seasons.toString() +
                ", _teams=" + _teams.toString() +
                "}\n";
    }

    public ArrayList<String> getTeams() {
        return _teams;
    }

    public void insertTeam(Team team) {
        _teams.add(team.getId());
        team.insertActiveLeague(getId());
    }

    public void removeTeam(String teamId){
        _teams.remove(teamId);
        TeamService.getTeamById(teamId).removeActiveLeague(getId());
    }

    public void insertSeason(String seasonId) {
        _seasons.add(seasonId);
    }

    public void createNewSeason() {

        Season season = new Season(UUID.randomUUID().toString(), "Season " + (_seasons.size() + 1), _id, TimeService.now());

        SeasonService.insertSeason(season);

        _teams.forEach(season::insertTeam);

        season.generateSchedule();

        insertSeason(season.getId());

    }

    public void simulateNextMatch() {
        SeasonService.getSeasonById(_seasons.get(_seasons.size() - 1)).simulateNextMatch();
    }

}
