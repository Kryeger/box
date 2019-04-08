package logic;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import java.text.SimpleDateFormat;
import java.util.*;

public class League {

    private String _id;
    private String _name;
    private HashMap<String, Season> _seasons = new HashMap<>();
    private HashMap<String, Team> _teams = new HashMap<>();
    private HashMap<String, Schedule> _schedules = new HashMap<>();

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

    public void insertTeam(Team team) {
        _teams.put(team.getId(), team);
    }

    public void insertSeason(Season season) {
        _seasons.put(season.getId(), season);
    }

    public void insertSchedule(Schedule schedule) {
        _schedules.put(schedule.getId(), schedule);
    }

    public void generateSchedule() {

        ArrayList<String> teams = new ArrayList<>(_teams.keySet());
        ArrayList<Pair<Integer, Integer>> pairs = new ArrayList<>();

        for(int i = 0; i < teams.size(); i++){
            for(int j = 0; j < teams.size(); j++){
                if(i != j){
                    pairs.add(new ImmutablePair<>(i, j));
                }
            }
        }

        Collections.shuffle(pairs);

        pairs.forEach((el) -> {
            insertSchedule(
                    new Schedule(
                            UUID.randomUUID().toString(),

                            _teams.get(
                                    teams.get(
                                            el.getLeft())),

                            _teams.get(
                                    teams.get(
                                            el.getRight()))
                    )
            );
        });

    }

}
