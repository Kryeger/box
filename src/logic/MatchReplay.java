package logic;

import logic.ingame.Kill;
import logic.service.TeamService;
import org.apache.commons.collections4.map.LinkedMap;

import java.util.ArrayList;

public class MatchReplay {

    private LinkedMap<String, Round> _rounds = new LinkedMap<>();
    private Team _homeTeam;
    private Team _awayTeam;
    private int _currentHomeTeamScore = 0;
    private int _currentAwayTeamScore = 0;
    private int _currentRound = 0;
    private int _currentKill = 0;
    private Scoreboard _scoreboard;

    MatchReplay(Match match) {
        _rounds = match.getRounds();
        _homeTeam = TeamService.getTeamById(match.getHomeTeam().getId());
        _awayTeam = TeamService.getTeamById(match.getAwayTeam().getId());

        _scoreboard = new Scoreboard(_homeTeam, _awayTeam);
    }

    public boolean isFinished() {
        return _currentRound == _rounds.size() - 1;
    }

    public Team getHomeTeam() {
        return _homeTeam;
    }

    public Team getAwayTeam() {
        return _awayTeam;
    }

    public int getCurrentHomeTeamScore() {
        return _currentHomeTeamScore;
    }

    public int getCurrentAwayTeamScore() {
        return _currentAwayTeamScore;
    }

    public int getCurrentRound() {
        return _currentRound;
    }

    public Scoreboard getScoreboard() {
        return _scoreboard;
    }

    public void advanceToNextRound() {
        if(!isFinished()){

            if (_rounds.getValue(_currentRound).getWinner().equals("home")) {
                _currentHomeTeamScore++;
            } else {
                _currentAwayTeamScore++;
            }

            getKills().forEach(kill -> {
                _scoreboard.addKill(kill.getKillerId());
                _scoreboard.addDeath(kill.getKilledId());
            });

            _currentRound++;

        }
    }

    public ArrayList<Kill> getKills() {
        return _rounds.getValue(_currentRound).getKillfeed();
    }

}
