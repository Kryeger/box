package logic.service;

import logic.League;

import java.util.HashMap;

public class LeagueService {

    private static final LeagueService _instance = new LeagueService();

    private HashMap<String, League> _leagues = new HashMap<>();

    private LeagueService() {}

    public static LeagueService get_instance() {
        return _instance;
    }

    public void createLeague(String id, String name) {
        _instance._leagues.put(id, new League(id, name));
    }

    public static void insertLeague(League league){
        _instance._leagues.put(league.getId(), league);
    }

    public static League getLeagueById(String id){
        if(_instance._leagues.containsKey(id)){
            return _instance._leagues.get(id);
        }
        return null;
    }

    public static boolean checkExistence(String leagueId){
        return _instance._leagues.containsKey(leagueId);
    }

    public static HashMap<String, League> getAll(){
        return _instance._leagues;
    }

}
