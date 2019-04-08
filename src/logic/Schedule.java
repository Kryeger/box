package logic;

public class Schedule {

    private String _id;
    private Team _homeTeam;
    private Team _awayTeam;

    public Schedule(String id, Team homeTeam, Team awayTeam) {
        _id = id;
        _homeTeam = homeTeam;
        _awayTeam = awayTeam;
    }

    @Override
    public String toString() {
        return "Schedule{" +
                "_id='" + _id + '\'' +
                ", _homeTeam=" + _homeTeam +
                ", _awayTeam=" + _awayTeam +
                "}\n";
    }

    public String getId() {
        return _id;
    }

    public void setId(String id) {
        _id = id;
    }

    public Team getHomeTeam() {
        return _homeTeam;
    }

    public void setHomeTeam(Team homeTeam) {
        _homeTeam = homeTeam;
    }

    public Team getAwayTeam() {
        return _awayTeam;
    }

    public void setAwayTeam(Team awayTeam) {
        _awayTeam = awayTeam;
    }

}
