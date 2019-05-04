package logic.ingame;

import logic.Player;

import java.io.Serializable;

public class IGPlayer implements Serializable {

    private int _health = 100;
    private int _money = 800;

    private String _id;

    private String _firstName;
    private String _nickName;
    private String _lastName;

    private double _skillRating;

    public IGPlayer(Player player){
        _firstName = player.getFirstName();
        _nickName = player.getNickName();
        _lastName = player.getLastName();

        _skillRating = player.getSkillRating();
        _id = player.getId();
    }

    public int getHealth() {
        return _health;
    }

    public void setHealth(int health) {
        _health = health;
    }

    public void addHealth(int health) {
        _health = Math.max(0, _health - health);
    }

    public int getMoney() {
        return _money;
    }

    public void setMoney(int money) {
        _money = money;
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

    public String getNickName() {
        return _nickName;
    }

    public void setNickName(String nickName) {
        _nickName = nickName;
    }

    public String getLastName() {
        return _lastName;
    }

    public void setLastName(String lastName) {
        _lastName = lastName;
    }

    public String getFullName() {
        return _firstName + " '" + _nickName + "' " + _lastName;
    }

    public double getSkillRating() {
        return _skillRating;
    }

    public void setSkillRating(double skillRating) {
        _skillRating = skillRating;
    }
}
