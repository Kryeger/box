package logic;

public class Manager {

    private String _firstName;
    private String _lastName;
    private String _teamId;

    public Manager(String firstName, String lastName, String teamId) {
        _firstName = firstName;
        _lastName = lastName;
        _teamId = teamId;
    }

    public String getFirstName() {
        return _firstName;
    }

    public void setFirstName(String firstName) {
        _firstName = firstName;
    }

    public String getLastName() {
        return _lastName;
    }

    public void setLastName(String lastName) {
        _lastName = lastName;
    }

    public String getTeamId() {
        return _teamId;
    }

    public void setTeamId(String teamId) {
        _teamId = teamId;
    }
}
