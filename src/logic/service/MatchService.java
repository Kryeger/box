package logic.service;

import logic.Match;
import utils.Time;

import java.util.HashMap;

public class MatchService {

    private static final MatchService _instance = new MatchService();

    private HashMap<String, Match> _matches = new HashMap<>();

    private MatchService() {}

    public static MatchService get_instance() {
        return _instance;
    }

    public void createMatch(String id, String homeTeamId, String awayTeamId, Time time) {
        _instance._matches.put(id, new Match(id, homeTeamId, awayTeamId, time));
    }

    public static void insertMatch(Match match){
        _instance._matches.put(match.getId(), match);
    }

    public static Match getMatchById(String id){
        if(_instance._matches.containsKey(id)){
            return _instance._matches.get(id);
        }
        return null;
    }

    public static boolean checkExistence(String matchId){
        return _instance._matches.containsKey(matchId);
    }
}
