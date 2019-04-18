package logic.ingame;

import logic.Player;
import logic.Team;
import logic.service.PlayerService;
import logic.service.TeamService;

import java.util.HashMap;

public class IGTeam {

    private String _id;

    private HashMap<String, IGPlayer> _players = new HashMap<>();

    private String _name;
    private String _acronym;

    public IGTeam(String teamId){

        TeamService.getTeamPlayersById(teamId).forEach((id) -> {
            _players.put(id, new IGPlayer(PlayerService.getPlayerById(id)));
        });

        _name = TeamService.getTeamById(teamId).getName();
        _acronym = TeamService.getTeamById(teamId).getAcronym();
        _id = TeamService.getTeamById(teamId).getId();
    }

    public String getId() {
        return _id;
    }

    public HashMap<String, IGPlayer> getPlayers() {
        return _players;
    }

    public String getName() {
        return _name;
    }

    public String getAcronym() {
        return _acronym;
    }
}
