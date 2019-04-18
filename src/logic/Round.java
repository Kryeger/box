package logic;

import logic.ingame.IGPlayer;
import logic.ingame.IGTeam;
import org.apache.commons.math3.distribution.EnumeratedDistribution;
import org.apache.commons.math3.util.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Round {

    private String _id;
    private IGTeam _homeTeam;
    private IGTeam _awayTeam;
    private int _homeTeamMoney;
    private int _awayTeamMoney;
    private int _roundNumber;

    public Round(String id, IGTeam homeTeam, IGTeam awayTeam, int homeTeamMoney, int awayTeamMoney, int roundNumber) {
        _id = id;
        _homeTeam = homeTeam;
        _awayTeam = awayTeam;
        _homeTeamMoney = homeTeamMoney;
        _awayTeamMoney = awayTeamMoney;
        _roundNumber = roundNumber;
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

        while(homeTeamPlayers.size() > 0 && awayTeamPlayers.size() > 0){
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

            switch(new EnumeratedDistribution<>(weightedList).sample()){
                case "home":
                    homeTeamPlayers.get(randomHomePlayer).addHealth(-60);
                    awayTeamPlayers.get(randomAwayPlayer).addHealth(-100);

                    System.out.println(
                            homeTeamPlayers.get(randomHomePlayer).getNickName() + " killed " +  awayTeamPlayers.get(randomAwayPlayer).getNickName()
                    );

                    awayTeamPlayers.remove(randomAwayPlayer);
                    break;

                case "away":
                    awayTeamPlayers.get(randomAwayPlayer).addHealth(-60);
                    homeTeamPlayers.get(randomHomePlayer).addHealth(-100);

                    System.out.println(
                            awayTeamPlayers.get(randomAwayPlayer).getNickName() + " killed " +  homeTeamPlayers.get(randomHomePlayer).getNickName()
                    );

                    homeTeamPlayers.remove(randomHomePlayer);
                    break;

            }

        }

        if(homeTeamPlayers.size() == 0){

            return "away";

        } else {

            return "home";

        }

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
