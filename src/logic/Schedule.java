package logic;

public class Schedule {

    private String _id;
    private String _homeTeamId;
    private String _awayTeamId;

    public Schedule(String id, String homeTeamId, String awayTeamId) {
        _id = id;
        _homeTeamId = homeTeamId;
        _awayTeamId = awayTeamId;
    }

    @Override
    public String toString() {
        return "Schedule{" +
                "_id='" + _id + '\'' +
                ", _homeTeamId=" + _homeTeamId +
                ", _awayTeamId=" + _awayTeamId +
                "}\n";
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

}
