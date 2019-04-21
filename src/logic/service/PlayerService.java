package logic.service;

import logic.Player;

import java.util.ArrayList;
import java.util.HashMap;

public class PlayerService {

    private static final PlayerService _instance = new PlayerService();

    private HashMap<String, Player> _players = new HashMap<>();

    private PlayerService() {}

    public static PlayerService get_instance() {
        return _instance;
    }

    public void createPlayer(
            String id, String firstName, String lastName,
            String nickName, String teamId, double skillRating,
            String salary, String signingBonus) {

        _instance._players.put(id, new Player(id, firstName, lastName, nickName, teamId, skillRating, salary, signingBonus));
    }

    public static void insertPlayer(Player player){
        _instance._players.put(player.getId(), player);
    }

    public static Player getPlayerById(String id){
        if(_instance._players.containsKey(id)){
            return _instance._players.get(id);
        }
        return null;
    }

    public static boolean checkExistence(String playerId){
        return _instance._players.containsKey(playerId);
    }

    public static ArrayList<String> getFreeAgents() {
        ArrayList<String> freeAgents = new ArrayList<>();

        _instance._players.forEach((id, player) -> {
            if(player.getTeamId().equals("")){
                freeAgents.add(player.getId());
            }
        });

        return freeAgents;
    }

}
