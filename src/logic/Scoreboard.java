package logic;

import logic.service.PlayerService;
import org.apache.commons.collections4.map.LinkedMap;

import java.util.ArrayList;
import java.util.Comparator;

public class Scoreboard {

    private LinkedMap<String, ScoreboardEntry> _entries = new LinkedMap<>();
    private Team _homeTeam;
    private Team _awayTeam;

    Scoreboard(Team home, Team away) {

        _homeTeam = home;
        _awayTeam = away;

        _homeTeam.getPlayers().forEach(playerId -> {
            _entries.put(playerId, new ScoreboardEntry(playerId));
        });

        _awayTeam.getPlayers().forEach(playerId -> {
            _entries.put(playerId, new ScoreboardEntry(playerId));
        });

    }

    public void addKill(String playerId) {
        _entries.get(playerId).addKill();
    }

    public void addDeath(String playerId) {
        _entries.get(playerId).addDeath();
    }

    public ArrayList<ScoreboardEntry> getEntries() {
        ArrayList<ScoreboardEntry> entries = new ArrayList<>();

        _entries.forEach((id, entry) -> {
            entries.add(entry);
        });

        entries.sort(Comparator.reverseOrder());

        return entries;
    }

    class ScoreboardEntry implements Comparable<ScoreboardEntry> {

        private int _kills = 0;
        private int _deaths = 0;
        private String _playerId;

        ScoreboardEntry(String playerId) {
            _playerId = playerId;
        }

        public String getPlayerId() {
            return _playerId;
        }

        public void addKill() {
            _kills++;
        }

        public void addDeath() {
            _deaths++;
        }

        public int getKills() {
            return _kills;
        }

        public int getDeaths() {
            return _deaths;
        }

        @Override
        public int compareTo(ScoreboardEntry scoreboardEntry) {
            return Integer.compare(_kills, scoreboardEntry._kills);
        }

    }

}
