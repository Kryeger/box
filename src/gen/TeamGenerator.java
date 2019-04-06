package gen;

import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import logic.Team;
import org.apache.commons.math3.distribution.EnumeratedDistribution;
import org.apache.commons.math3.util.Pair;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

public class TeamGenerator implements Generator<Team>{

    private static String pathToAdj = "./gen/teams/adj.csv";
    private static String pathToLocations = "./gen/teams/locations.csv";
    private static String pathToTheLocations = "./gen/teams/theLocations.csv";
    private static String pathToNouns = "./gen/teams/nouns.csv";

    private static Random randomGenerator = new Random();

    private static ArrayList<String> _adj= new ArrayList<>();
    private static ArrayList<String> _locations= new ArrayList<>();
    private static ArrayList<String> _theLocations= new ArrayList<>();
    private static ArrayList<String> _nouns= new ArrayList<>();

    private static char[] _acronymSymbols = {'_', '^', '-', '|', '/', '\\'};

    private static double adjNounChance = 0.3;
    private static double nounTheLocationChance = 0.3;
    private static double theNounFromLocationChance = 0.4;

    static {

        //adj
        try (
                Reader reader = Files.newBufferedReader(Paths.get(pathToAdj));
                CSVReader csvReader = new CSVReaderBuilder(reader).build();
        ) {
            String[] nextRecord;
            while ((nextRecord = csvReader.readNext()) != null) {
                _adj.add(nextRecord[0].substring(0, 1).toUpperCase() + nextRecord[0].substring(1));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        //locations
        try (
                Reader reader = Files.newBufferedReader(Paths.get(pathToLocations));
                CSVReader csvReader = new CSVReaderBuilder(reader).build();
        ) {
            String[] nextRecord;
            while ((nextRecord = csvReader.readNext()) != null) {
                _locations.add(nextRecord[0].substring(0, 1).toUpperCase() + nextRecord[0].substring(1));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        //"the" locations
        try (
                Reader reader = Files.newBufferedReader(Paths.get(pathToTheLocations));
                CSVReader csvReader = new CSVReaderBuilder(reader).build();
        ) {
            String[] nextRecord;
            while ((nextRecord = csvReader.readNext()) != null) {
                _theLocations.add(nextRecord[0].substring(0, 1).toUpperCase() + nextRecord[0].substring(1));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        //nouns
        try (
                Reader reader = Files.newBufferedReader(Paths.get(pathToNouns));
                CSVReader csvReader = new CSVReaderBuilder(reader).build();
        ) {
            String[] nextRecord;
            while ((nextRecord = csvReader.readNext()) != null) {
                _nouns.add(nextRecord[0].substring(0, 1).toUpperCase() + nextRecord[0].substring(1));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public TeamGenerator() {}

    public Team generate() {
        List<Pair<String, Double>> presetWeights= new ArrayList<>();
        presetWeights.add(new Pair<>("adjNoun", adjNounChance));
        presetWeights.add(new Pair<>("nounTheLocation", nounTheLocationChance));
        presetWeights.add(new Pair<>("theNounFromLocation", theNounFromLocationChance));

        StringBuilder newTeamName = new StringBuilder();
        StringBuilder newTeamAcronym = new StringBuilder();

        int nextAdj, nextNoun, nextTheLocation, nextLocation;

        switch(new EnumeratedDistribution<>(presetWeights).sample()){
            case "adjNoun":
                nextAdj = randomGenerator.nextInt(_adj.size());
                newTeamName.append(_adj.get(nextAdj));

                newTeamAcronym.append(_adj.get(nextAdj).substring(0, 1).toUpperCase());

                if(randomGenerator.nextBoolean()){
                    newTeamAcronym.append(_adj.get(nextAdj).substring(1, 2));
                }

                newTeamName.append(" ");

                if(randomGenerator.nextBoolean()){
                    newTeamAcronym.append(_acronymSymbols[randomGenerator.nextInt(_acronymSymbols.length)]);
                }

                nextNoun = randomGenerator.nextInt(_nouns.size());
                newTeamName.append(_nouns.get(nextNoun));

                newTeamAcronym.append(_nouns.get(nextNoun).substring(0, 1).toUpperCase());

                if(randomGenerator.nextBoolean()){
                    newTeamAcronym.append(_nouns.get(nextNoun).substring(1, 2));
                }

                break;

            case "nounTheLocation":
                nextNoun = randomGenerator.nextInt(_nouns.size());
                newTeamName.append(_nouns.get(nextNoun));

                newTeamAcronym.append(_nouns.get(nextNoun).substring(0, 1).toUpperCase());

                if(randomGenerator.nextBoolean()){
                    newTeamAcronym.append(_nouns.get(nextNoun).substring(1, 2));
                }

                if(randomGenerator.nextBoolean()){
                    newTeamAcronym.append(_acronymSymbols[randomGenerator.nextInt(_acronymSymbols.length)]);
                }

                if(randomGenerator.nextBoolean()){
                     newTeamName.append(" from the ");
                } else {
                    newTeamName.append(" of the ");
                }

                nextTheLocation = randomGenerator.nextInt(_theLocations.size());
                newTeamName.append(_theLocations.get(nextTheLocation));
                newTeamAcronym.append(_theLocations.get(nextTheLocation).substring(0, 1).toUpperCase());


                if(randomGenerator.nextBoolean()){
                    newTeamAcronym.append(_theLocations.get(nextTheLocation).substring(1, 2));
                }

                break;

            case "theNounFromLocation":
                newTeamName.append("The ");

                nextNoun = randomGenerator.nextInt(_nouns.size());
                newTeamName.append(_nouns.get(nextNoun));
                newTeamAcronym.append(_nouns.get(nextNoun).substring(0, 1).toUpperCase());


                if(randomGenerator.nextBoolean()){
                    newTeamAcronym.append(_nouns.get(nextNoun).substring(1, 2));
                }

                if(randomGenerator.nextBoolean()){
                    newTeamAcronym.append(_acronymSymbols[randomGenerator.nextInt(_acronymSymbols.length)]);
                }
                newTeamName.append(" from ");

                nextLocation = randomGenerator.nextInt(_locations.size());
                newTeamName.append(_locations.get(nextLocation));
                newTeamAcronym.append(_locations.get(nextLocation).substring(0, 1).toUpperCase());


                if(randomGenerator.nextBoolean()){
                    newTeamAcronym.append(_locations.get(nextLocation).substring(1, 2));
                }

                break;
        }

        return new Team(UUID.randomUUID().toString(), newTeamName.toString(), newTeamAcronym.toString());

    }
}
