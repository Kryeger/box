package logic;

public class Manager {

    private String _name;
    private int _money;
    private String _teamId;

    public Manager(String name, int money, String teamId) {
        _name = name;
        _money = money;
        _teamId = teamId;
    }

    public String getName() {
        return _name;
    }

    public void setName(String name) {
        _name = name;
    }

    public int getMoney() {
        return _money;
    }

    public void setMoney(int money) {
        _money = money;
    }

    public String getTeamId() {
        return _teamId;
    }

    public void setTeamId(String teamId) {
        _teamId = teamId;
    }
}
