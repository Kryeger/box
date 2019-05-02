package logic;

import logic.service.MatchService;
import logic.service.TimeService;
import org.apache.commons.collections4.map.LinkedMap;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import utils.Time;

import java.util.*;

public class Season {

    private String _id;
    private String _name;
    private LinkedMap<String, Schedule> _schedules = new LinkedMap<>();
    private ArrayList<String> _teams = new ArrayList<>();
    private ArrayList<String> _matches = new ArrayList<>();
    private String _leagueId;
    private Time _startTime;

    public Season(String id, String name, String leagueId, Time startTime) {
        _id = id;
        _name = name;
        _leagueId = leagueId;
        _startTime = startTime;
    }

    public LinkedMap<String, Schedule> getSchedules() {
        return _schedules;
    }

    public String getLeagueId() {
        return _leagueId;
    }

    public void setLeagueId(String leagueId) {
        _leagueId = leagueId;
    }

    public String getId() {
        return _id;
    }

    public void setId(String id) {
        _id = id;
    }

    public String getName() {
        return _name;
    }

    public void setName(String name) {
        _name = name;
    }

    public void insertTeam(String teamId) {
        _teams.add(teamId);
    }

    public void generateSchedule() {
        ArrayList<Pair<Integer, Integer>> pairs = new ArrayList<>();

        for (int i = 0; i < _teams.size(); i++) {
            for (int j = 0; j < _teams.size(); j++) {
                if (i != j) {
                    pairs.add(new ImmutablePair<>(i, j));
                }
            }
        }

        Time scheduleTime = new Time(_startTime);

        Collections.shuffle(pairs);

        pairs.forEach((el) -> {
            insertSchedule(
                    new Schedule(
                            UUID.randomUUID().toString(),
                            _teams.get(el.getLeft()),
                            _teams.get(el.getRight()),
                            new Time(scheduleTime)
                    )
            );
            scheduleTime.addHours(1);
        });

    }

    public void insertSchedule(Schedule schedule) {
        _schedules.put(schedule.getId(), schedule);
    }

    public ArrayList<String> getMatches() {
        return _matches;
    }

    public Match simulateNextMatch() {
        Schedule nextSchedule = _schedules.get(_schedules.firstKey());
        _schedules.remove(_schedules.indexOf(_schedules.firstKey()));

        Match nextMatch = new Match(UUID.randomUUID().toString(), nextSchedule.getHomeTeam(), nextSchedule.getAwayTeam(), nextSchedule.getTime());

        MatchService.insertMatch(nextMatch);
        _matches.add(nextMatch.getId());

        while (!nextMatch.isFinished()) {
            nextMatch.simulateNextRound();
        }

        return nextMatch;
    }
}
