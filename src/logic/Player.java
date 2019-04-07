package logic;

import utils.Money;

public class Player {

    private String _id;
    private String _firstName;
    private String _lastName;
    private String _nickName;
    private String _teamId;
    private Money _salary;
    private Money _signingBonus;
    private double _skillRating; //temporary; will be a Skills class with Accuracy, Movement etc

    public Player() {
    }

    public Player(String id, String firstName, String lastName, String nickName, String teamId, double skillRating, String salary, String signingBonus) {
        _id = id;
        _firstName = firstName;
        _lastName = lastName;
        _nickName = nickName;
        _teamId = teamId;
        _skillRating = skillRating;
        _salary = new Money(salary);
        _signingBonus = new Money(signingBonus);
    }

    public String getTeamId() {
        return _teamId;
    }

    public void setTeamId(String teamId) {
        _teamId = teamId;
    }

    @Override
    public String toString() {
        return "Player{" +
                "_id='" + _id + '\'' +
                ", _firstName='" + _firstName + '\'' +
                ", _lastName='" + _lastName + '\'' +
                ", _nickName='" + _nickName + '\'' +
                ", _teamId='" + _teamId + '\'' +
                ", _salary=" + _salary +
                ", _signingBonus=" + _signingBonus +
                ", _skillRating=" + _skillRating +
                "}\n";
    }

    public String getFullName() {
        return _firstName + " '" + _nickName + "' " + _lastName;
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

    public String getNickName() {
        return _nickName;
    }

    public void setNickName(String nickName) {
        _nickName = nickName;
    }

    public double getSkillRating() {
        return _skillRating;
    }

    public void setSkillRating(double skillRating) {
        _skillRating = skillRating;
    }

    public Money getSalary() {
        return _salary;
    }

    public Money getSigningBonus() {
        return _signingBonus;
    }
}
