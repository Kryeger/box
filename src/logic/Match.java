package logic;

import logic.ingame.IGTeam;
import logic.service.TeamService;
import org.apache.commons.collections4.map.LinkedMap;
import utils.Time;

import java.util.UUID;

public class Match {

    private String _id;
    private IGTeam _homeTeam;
    private IGTeam _awayTeam;
    private int _homeTeamScore = 0;
    private int _awayTeamScore = 0;
    private int _homeTeamMoney = 800;
    private int _awayTeamMoney = 800;
    private boolean _isFinished = false;
    private String _winner;
    private Map _map;
    private LinkedMap<String, Round> _rounds = new LinkedMap<>();
    private Time _time;

    public Match(String id, String homeTeamId, String awayTeamId, Time time) {
        _id = id;
        _homeTeam = new IGTeam(homeTeamId);
        _awayTeam = new IGTeam(awayTeamId);
        _time = time;
    }

    public Time getTime() {
        return _time;
    }

    public IGTeam getHomeTeam() {
        return _homeTeam;
    }

    public IGTeam getAwayTeam() {
        return _awayTeam;
    }

    public void simulateNextRound() {

//        System.out.println(_homeTeam.getAcronym() + " " + _homeTeamScore + " : " + _awayTeamScore + " " + _awayTeam.getAcronym());
//        System.out.println("Simulating round #" + getRoundNumber());

        Round newRound = new Round(
                UUID.randomUUID().toString(),
                _homeTeam,
                _awayTeam,
                _homeTeamMoney,
                _awayTeamMoney,
                _homeTeamScore,
                _awayTeamScore,
                getRoundNumber()
        );

        _rounds.put(newRound.getId(), newRound);

        switch (newRound.simulate()) {
            case "home":
                _homeTeamScore++;
                break;

            case "away":
                _awayTeamScore++;
                break;
        }

        _rounds.put(newRound.getId(), newRound);


        checkWin();
    }

    public int getRoundNumber() {
        return _homeTeamScore + _awayTeamScore + 1;
    }

    private void checkWin() {
        if (_homeTeamScore >= 16) {
            _winner = "home";
            _isFinished = true;
        }

        if (_awayTeamScore >= 16) {
            _winner = "away";
            _isFinished = true;
        }

        if ((_awayTeamScore == 15) && (_homeTeamScore == 15)) {
            _winner = "draw";
            _isFinished = true;
        }
    }

    public int getHomeTeamScore() {
        return _homeTeamScore;
    }

    public int getAwayTeamScore() {
        return _awayTeamScore;
    }

    public String getWinner() {
        return _winner;
    }

    public Map getMap() {
        return _map;
    }

    public LinkedMap<String, Round> getRounds() {
        return _rounds;
    }

    public boolean isFinished() {
        return _isFinished;
    }

    public String getId() {
        return _id;
    }
}
