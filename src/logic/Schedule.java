package logic;

import utils.Time;

import java.io.Serializable;

public class Schedule implements Comparable<Schedule>, Serializable {

    private String _id;
    private String _homeTeamId;
    private String _awayTeamId;
    private Time _time;

    public Schedule(String id, String homeTeamId, String awayTeamId, Time time) {
        _id = id;
        _homeTeamId = homeTeamId;
        _awayTeamId = awayTeamId;
        _time = time;
    }

    public Time getTime() {
        return _time;
    }

    public void setTime(Time time) {
        _time = time;
    }

    public String getId() {
        return _id;
    }

    public void setId(String id) {
        _id = id;
    }

    public String getHomeTeam() {
        return _homeTeamId;
    }

    public void setHomeTeam(String homeTeamId) {
        _homeTeamId = homeTeamId;
    }

    public String getAwayTeam() {
        return _awayTeamId;
    }

    public void setAwayTeam(String awayTeamId) {
        _awayTeamId = awayTeamId;
    }

    @Override
    public int compareTo(Schedule schedule) {
        return (_time.compareTo(schedule._time));
    }

}
