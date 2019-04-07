package logic;

import java.util.ArrayList;

public class League {

    String _name;
    ArrayList<Season> _seasons = new ArrayList<>();
    ArrayList<Team> _teams = new ArrayList<>();

    public League(String name) {
        _name = name;
    }

    public void insertTeam(Team team){
        _teams.add(team);
    }

    public void insertSeason(Season season){
        _seasons.add(season);
    }

    @Override
    public String toString() {
        return "League{" +
                "_name='" + _name + '\'' +
                ", _seasons=" + _seasons.toString() +
                ", _teams=" + _teams.toString() +
                "}\n";
    }
}
