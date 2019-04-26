package logic.service;

import logic.Season;
import utils.Time;

import java.util.HashMap;

public class SeasonService {
    
    private static final SeasonService _instance = new SeasonService();

    private HashMap<String, Season> _seasons = new HashMap<>();

    private SeasonService() {}

    public static SeasonService get_instance() {
        return _instance;
    }

    public void createSeason(String id, String name, String leagueId, Time startTime) {
        _instance._seasons.put(id, new Season(id, name, leagueId, startTime));
    }

    public static void insertSeason(Season season){
        _instance._seasons.put(season.getId(), season);
    }

    public static Season getSeasonById(String id){
        if(_instance._seasons.containsKey(id)){
            return _instance._seasons.get(id);
        }
        return null;
    }

    public static boolean checkExistence(String seasonId){
        return _instance._seasons.containsKey(seasonId);
    }
    
}
