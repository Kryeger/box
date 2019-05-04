package logic;

import logic.ingame.IGPlayer;
import logic.ingame.IGTeam;
import logic.ingame.Kill;
import org.apache.commons.math3.distribution.EnumeratedDistribution;
import org.apache.commons.math3.util.Pair;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Round implements Serializable {

    private String _id;
    private IGTeam _homeTeam;
    private IGTeam _awayTeam;
    private int _homeTeamMoney;
    private int _awayTeamMoney;
    private int _currentHomeTeamScore;
    private int _currentAwayTeamScore;
    private int _roundNumber;
    private ArrayList<Kill> _killfeed = new ArrayList<>();
    private String _winner;

    public Round(String id, IGTeam homeTeam, IGTeam awayTeam, int homeTeamMoney, int awayTeamMoney, int currentHomeTeamScore, int currentAwayTeamScore, int roundNumber) {
        _id = id;
        _homeTeam = homeTeam;
        _awayTeam = awayTeam;
        _homeTeamMoney = homeTeamMoney;
        _awayTeamMoney = awayTeamMoney;
        _currentHomeTeamScore = currentHomeTeamScore;
        _currentAwayTeamScore = currentAwayTeamScore;
        _roundNumber = roundNumber;
    }

    public int getCurrentHomeTeamScore() {
        return _currentHomeTeamScore;
    }

    public int getCurrentAwayTeamScore() {
        return _currentAwayTeamScore;
    }

    public ArrayList<Kill> getKillfeed() {
        return _killfeed;
    }

    public String simulate() {
        ArrayList<IGPlayer> homeTeamPlayers = new ArrayList<>();
        ArrayList<IGPlayer> awayTeamPlayers = new ArrayList<>();

        _homeTeam.getPlayers().forEach((id, player) -> {
            player.setHealth(100);
            homeTeamPlayers.add(player);
        });

        _awayTeam.getPlayers().forEach((id, player) -> {
            player.setHealth(100);
            awayTeamPlayers.add(player);
        });

        while (homeTeamPlayers.size() > 0 && awayTeamPlayers.size() > 0) {
            Random randomGenerator = new Random();

            int randomHomePlayer = randomGenerator.nextInt(homeTeamPlayers.size());
            int randomAwayPlayer = randomGenerator.nextInt(awayTeamPlayers.size());

            List<Pair<String, Double>> weightedList = new ArrayList<>();
            weightedList.add(new Pair<>(
                    "home",
                    homeTeamPlayers.get(randomHomePlayer).getSkillRating() * homeTeamPlayers.get(randomHomePlayer).getHealth()
            ));

            weightedList.add(new Pair<>(
                    "away",
                    awayTeamPlayers.get(randomAwayPlayer).getSkillRating() * awayTeamPlayers.get(randomAwayPlayer).getHealth()
            ));

            switch (new EnumeratedDistribution<>(weightedList).sample()) {
                case "home":
                    homeTeamPlayers.get(randomHomePlayer).addHealth(-60);
                    awayTeamPlayers.get(randomAwayPlayer).addHealth(-100);

                    _killfeed.add(new Kill(homeTeamPlayers.get(randomHomePlayer).getId(), awayTeamPlayers.get(randomAwayPlayer).getId()));

//                    System.out.println(
//                            homeTeamPlayers.get(randomHomePlayer).getNickName() + " killed " + awayTeamPlayers.get(randomAwayPlayer).getNickName()
//                    );

                    awayTeamPlayers.remove(randomAwayPlayer);
                    break;

                case "away":
                    awayTeamPlayers.get(randomAwayPlayer).addHealth(-60);
                    homeTeamPlayers.get(randomHomePlayer).addHealth(-100);

                    _killfeed.add(new Kill(awayTeamPlayers.get(randomAwayPlayer).getId(), homeTeamPlayers.get(randomHomePlayer).getId()));

//                    System.out.println(
//                            awayTeamPlayers.get(randomAwayPlayer).getNickName() + " killed " + homeTeamPlayers.get(randomHomePlayer).getNickName()
//                    );

                    homeTeamPlayers.remove(randomHomePlayer);
                    break;

            }

        }

        if (homeTeamPlayers.size() == 0) {

            _winner = "away";
            return _winner;

        } else {

            _winner = "home";
            return _winner;

        }

    }

    public String getWinner() {
        return _winner;
    }

    public String getId() {
        return _id;
    }

    public void setId(String id) {
        _id = id;
    }

    public int getHomeTeamMoney() {
        return _homeTeamMoney;
    }

    public void setHomeTeamMoney(int homeTeamMoney) {
        _homeTeamMoney = homeTeamMoney;
    }

    public int getAwayTeamMoney() {
        return _awayTeamMoney;
    }

    public void setAwayTeamMoney(int awayTeamMoney) {
        _awayTeamMoney = awayTeamMoney;
    }

    public int getRoundNumber() {
        return _roundNumber;
    }

    public void setRoundNumber(int roundNumber) {
        _roundNumber = roundNumber;
    }
}
