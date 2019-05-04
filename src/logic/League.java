package logic;

import logic.service.MatchService;
import logic.service.SeasonService;
import logic.service.TeamService;
import logic.service.TimeService;
import org.apache.commons.collections4.map.LinkedMap;

import java.io.Serializable;
import java.util.*;

public class League implements Serializable {

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

    public Match simulateNextMatch() {
        return SeasonService.getSeasonById(_seasons.get(_seasons.size() - 1)).simulateNextMatch();
    }

    public Schedule getNextSchedule() {
        return (SeasonService.getSeasonById(_seasons.get(_seasons.size() - 1)).getSchedules().getValue(0));
    }

    public ArrayList<Schedule> getNextSchedules() {
        ArrayList<Schedule> nextSchedules = new ArrayList<>();

        LinkedMap<String, Schedule> schedules = SeasonService.getSeasonById(_seasons.get(_seasons.size() - 1)).getSchedules();

        int nextSchedulesCount = Math.min(3, schedules.size());

        for (int i = 0; i < nextSchedulesCount; i++) {
            nextSchedules.add(schedules.getValue(i));
        }

        return nextSchedules;
    }

    public ArrayList<Match> getLatestPlayedMatches() {
        ArrayList<Match> latestPlayedMatches = new ArrayList<>();

        ArrayList<String> seasonMatches = SeasonService.getSeasonById(_seasons.get(_seasons.size() - 1)).getMatches();

        int latestPlayedMatchesCount = Math.min(3, seasonMatches.size());

        for (int i = 0; i < latestPlayedMatchesCount; i++) {
            latestPlayedMatches.add(MatchService.getMatchById(seasonMatches.get(seasonMatches.size() - i - 1)));
        }

        return latestPlayedMatches;
    }

}
