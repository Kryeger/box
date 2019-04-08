package logic;

public class Manager {

    private String _id;
    private String _firstName;
    private String _lastName;
    private String _teamId;

    public Manager(String id, String firstName, String lastName, String teamId) {
        _id = id;
        _firstName = firstName;
        _lastName = lastName;
        _teamId = teamId;
    }

    public String getId() {
        return _id;
    }

    public void setId(String id) {
        _id = id;
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
