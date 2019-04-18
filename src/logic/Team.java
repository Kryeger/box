package logic;

import logic.service.PlayerService;
import utils.Money;

import java.util.ArrayList;
import java.util.HashMap;

public class Team {

    private String _id;
    private String _name;
    private String _acronym;
    private Manager _manager;
    private ArrayList<String> _players = new ArrayList<>();
    private Money _money;
    private ArrayList<String> _activeLeagues = new ArrayList<>();

    public Team() {
    }

    public Team(String id, String name, String acronym, String money) {
        _id = id;
        _name = name;
        _acronym = acronym;
        _money = new Money(money);
    }

    public void insertActiveLeague(String leagueId){
        _activeLeagues.add(leagueId);
    }

    public void removeActiveLeague(String leagueId){
        _activeLeagues.remove(leagueId);
    }

    public ArrayList<String> getPlayers() {
        return _players;
    }

    public Money getMoney() {
        return _money;
    }

    public void setMoney(Money money) {
        _money = money;
    }

    @Override
    public String toString() {
        return "Team{" +
                "_id='" + _id + '\'' +
                ", _name='" + _name + '\'' +
                ", _acronym='" + _acronym + '\'' +
                ", _manager=" + _manager +
                ", _players=" + _players +
                "}\n";
    }

    public String getName() {
        return _name;
    }

    public void setName(String name) {
        _name = name;
    }

    public Manager getManager() {
        return _manager;
    }

    public void setManager(Manager manager) {
        _manager = manager;
    }

    public void insertPlayer(String playerId) {
        if (_players.size() < 5) {
            _players.add(playerId);
            PlayerService.getPlayerById(playerId).setTeamId(getId());
        }
    }

    public String getId() {
        return _id;
    }

    public void setId(String id) {
        _id = id;
    }

    public String getAcronym() {
        return _acronym;
    }

    public void setAcronym(String acronym) {
        _acronym = acronym;
    }
}
