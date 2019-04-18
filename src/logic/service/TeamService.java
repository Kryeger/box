package logic.service;

import logic.Team;

import java.util.ArrayList;
import java.util.HashMap;

public class TeamService {

    private static final TeamService _instance = new TeamService();

    private HashMap<String, Team> _teams = new HashMap<>();

    private TeamService() {}

    public static TeamService get_instance(){
        return _instance;
    }

    public static void createTeam(String id, String name, String acronym, String money){
        _instance._teams.put(id, new Team(id, name, acronym, money));
    }

    public static void insertTeam(Team team){
        _instance._teams.put(team.getId(), team);
    }

    public static Team getTeamById(String teamId){
        if(_instance._teams.containsKey(teamId)){
            return _instance._teams.get(teamId);
        }

        return null;
    }

    public static boolean checkExistence(String teamId){
        return _instance._teams.containsKey(teamId);
    }

    public static void insertPlayerInTeam(String playerId, String teamId){
        if(checkExistence(teamId)){
            TeamService.getTeamById(teamId).insertPlayer(playerId);
            PlayerService.getPlayerById(playerId).setTeamId(teamId);
        }
    }

    public static ArrayList<String> getTeamPlayersById(String teamId){
        return getTeamById(teamId).getPlayers();
    }

}
