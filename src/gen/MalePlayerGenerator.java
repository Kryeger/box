package gen;

import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import logic.Player;
import org.apache.commons.math3.distribution.*;
import org.apache.commons.math3.util.Pair;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class MalePlayerGenerator implements Generator<Player>{

    static String pathToMaleFirstNames = "./gen/names/male/male_first_names.csv";
    static String pathToLastNames = "./gen/names/last_names.csv";
    static String pathToAdjAll = "./gen/nicknames/adj_all.csv";
    static String pathToAdjShort = "./gen/nicknames/adj_short.csv";
    static String pathToAdvAll = "./gen/nicknames/adv_all.csv";
    static String pathToNounAll = "./gen/nicknames/noun_all.csv";
    static String pathToNounShort = "./gen/nicknames/noun_short.csv";
    static String pathToVerbAll = "./gen/nicknames/verb_all.csv";


    static Random randomGenerator = new Random();


    private static ArrayList<String> _maleFirstNames = new ArrayList<>();
    private static ArrayList<String> _lastNames = new ArrayList<>();

    private static ArrayList<String> _adjAll = new ArrayList<>();
    private static ArrayList<String> _adjShort = new ArrayList<>();
    private static ArrayList<String> _advAll = new ArrayList<>();
    private static ArrayList<String> _nounAll = new ArrayList<>();
    private static ArrayList<String> _nounShort = new ArrayList<>();
    private static ArrayList<String> _verbAll = new ArrayList<>();


    private static double nounShortChance = 0.9;
    private static double nounAllChance = 0.1;

    private static double adjShortChance = 0.9;
    private static double adjAllChance = 0.1;

    private static double adjNounChance = 0.26;
    private static double advNounChance = 0.26;
    private static double verbNounChance = 0.26;
    private static double onlyNounChance = 0.2;

    static {
        //male first names
        try (
            Reader reader = Files.newBufferedReader(Paths.get(pathToMaleFirstNames));
            CSVReader csvReader = new CSVReaderBuilder(reader).build();
        ) {
            String[] nextRecord;
            while ((nextRecord = csvReader.readNext()) != null) {
                _maleFirstNames.add(nextRecord[0]);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        //last names (these are uppercase, need to be transformed)
        try (
            Reader reader = Files.newBufferedReader(Paths.get(pathToLastNames));
            CSVReader csvReader = new CSVReaderBuilder(reader).build();
        ) {
            String[] nextRecord;
            while ((nextRecord = csvReader.readNext()) != null) {
                _lastNames.add(nextRecord[0].substring(0, 1) + nextRecord[0].substring(1).toLowerCase());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        //adj all
        try (
            Reader reader = Files.newBufferedReader(Paths.get(pathToAdjAll));
            CSVReader csvReader = new CSVReaderBuilder(reader).build();
        ) {
            String[] nextRecord;
            while ((nextRecord = csvReader.readNext()) != null) {
                _adjAll.add(nextRecord[0]);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        //adj short
        try (
            Reader reader = Files.newBufferedReader(Paths.get(pathToAdjShort));
            CSVReader csvReader = new CSVReaderBuilder(reader).build();
        ) {
            String[] nextRecord;
            while ((nextRecord = csvReader.readNext()) != null) {
                _adjShort.add(nextRecord[0]);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        //adv all
        try (
                Reader reader = Files.newBufferedReader(Paths.get(pathToAdvAll));
                CSVReader csvReader = new CSVReaderBuilder(reader).build();
        ) {
            String[] nextRecord;
            while ((nextRecord = csvReader.readNext()) != null) {
                _advAll.add(nextRecord[0]);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        //noun all
        try (
            Reader reader = Files.newBufferedReader(Paths.get(pathToNounAll));
            CSVReader csvReader = new CSVReaderBuilder(reader).build();
        ) {
            String[] nextRecord;
            while ((nextRecord = csvReader.readNext()) != null) {
                _nounAll.add(nextRecord[0].substring(0, 1).toUpperCase() + nextRecord[0].substring(1));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        //noun short
        try (
            Reader reader = Files.newBufferedReader(Paths.get(pathToNounShort));
            CSVReader csvReader = new CSVReaderBuilder(reader).build();
        ) {
            String[] nextRecord;
            while ((nextRecord = csvReader.readNext()) != null) {
                _nounShort.add(nextRecord[0].substring(0, 1).toUpperCase() + nextRecord[0].substring(1));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        //verb all
        try (
            Reader reader = Files.newBufferedReader(Paths.get(pathToVerbAll));
            CSVReader csvReader = new CSVReaderBuilder(reader).build();
        ) {
            String[] nextRecord;
            while ((nextRecord = csvReader.readNext()) != null) {
                _verbAll.add(nextRecord[0]);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public MalePlayerGenerator(){}

    public Player generate(double median) {

        String firstName = generateMaleFirstName();
        String lastName = generateLastName();
        String nickName = generateNickName();

        while(nickName.length() > 12){
            nickName = generateNickName();
        }

        double skillRating = generateSkillRating(median);

        while(skillRating < 1 || skillRating > 100){
            skillRating = generateSkillRating(median);
        }

        double salary = generateSalary(skillRating);

        double signingBonus = generateSigningBonus(skillRating);

        return new Player(UUID.randomUUID().toString(), firstName, lastName, nickName, "", skillRating, String.valueOf((int)salary), String.valueOf((int)signingBonus));
    }

    private String generateMaleFirstName() {
        return _maleFirstNames.get(randomGenerator.nextInt(_maleFirstNames.size()));
    }

    private String generateLastName() {
        return _lastNames.get(randomGenerator.nextInt(_lastNames.size()));
    }

    private String generateNickName() {
        List<Pair<String, Double>> presetWeights = new ArrayList<>();
        presetWeights.add(new Pair<>("adjNoun", adjNounChance));
        presetWeights.add(new Pair<>("advNoun", advNounChance));
        presetWeights.add(new Pair<>("verbNoun", verbNounChance));
        presetWeights.add(new Pair<>("onlyNoun", onlyNounChance));

        List<Pair<String, Double>> adjLengthWeights = new ArrayList<>();
        adjLengthWeights.add(new Pair<>("adjShort", adjShortChance));
        adjLengthWeights.add(new Pair<>("adjAll", adjAllChance));

        List<Pair<String, Double>> nounLengthWeights = new ArrayList<>();
        nounLengthWeights.add(new Pair<>("nounShort", nounShortChance));
        nounLengthWeights.add(new Pair<>("nounAll", nounAllChance));

        String selectedNickNamePreset = new EnumeratedDistribution<>(presetWeights).sample();
        String selectedAdjPreset = new EnumeratedDistribution<>(adjLengthWeights).sample();
        String selectedNounPreset = new EnumeratedDistribution<>(nounLengthWeights).sample();

        StringBuilder nickName = new StringBuilder();

        if(selectedNickNamePreset.equals("adjNoun")){
            switch(selectedAdjPreset){
                case "adjShort":
                    nickName.append(_adjShort.get(randomGenerator.nextInt(_adjShort.size())));
                    break;
                case "adjAll":
                    nickName.append(_adjAll.get(randomGenerator.nextInt(_adjAll.size())));
                    break;
            }
        }

        if(selectedNickNamePreset.equals("advNoun")){
            nickName.append(_advAll.get(randomGenerator.nextInt(_advAll.size())));
        }

        if(selectedNickNamePreset.equals("verbNoun")){
            nickName.append(_verbAll.get(randomGenerator.nextInt(_verbAll.size())));
        }

        switch(selectedNounPreset){

            case "nounShort":
                nickName.append(_nounShort.get(randomGenerator.nextInt(_nounShort.size())));
                break;

            case "nounAll":
                nickName.append(_nounAll.get(randomGenerator.nextInt(_nounAll.size())));
                break;

        }

        //spice

        StringBuilder spicedNickName = new StringBuilder();

        for (int i = 0; i < nickName.length(); i++) {
            switch (nickName.charAt(i)){
                case 'A':
                case 'a':
                    if(randomGenerator.nextInt(10) > 7){
                        if(randomGenerator.nextBoolean()){
                            spicedNickName.append('4');
                        } else {
                            spicedNickName.append('@');
                        }
                    } else {
                        spicedNickName.append(nickName.charAt(i));
                    }
                break;

                case 'E':
                case 'e':
                    if(randomGenerator.nextInt(10) > 7){
                        spicedNickName.append('3');
                    } else {
                        spicedNickName.append(nickName.charAt(i));
                    }
                break;

                case 'O':
                case 'o':
                    if(randomGenerator.nextInt(10) > 7){
                        spicedNickName.append('0');
                    } else {
                        spicedNickName.append(nickName.charAt(i));
                    }
                    break;

                case 'S':
                case 's':
                    if(randomGenerator.nextInt(10) > 7){
                        if(randomGenerator.nextBoolean()){
                            spicedNickName.append('z');
                        } else {
                            spicedNickName.append('$');
                        }
                    } else {
                        spicedNickName.append(nickName.charAt(i));
                    }
                    break;

                case 'H':
                    if(randomGenerator.nextInt(10) > 7){
                        spicedNickName.append('#');
                    } else {
                        spicedNickName.append(nickName.charAt(i));
                    }
                    break;

                case 'I':
                    if(randomGenerator.nextInt(10) > 7){
                        if(randomGenerator.nextBoolean()){
                            spicedNickName.append('1');
                        } else {
                            spicedNickName.append('Y');
                        }
                    } else {
                        spicedNickName.append(nickName.charAt(i));
                    }
                    break;

                case 'i':
                    if(randomGenerator.nextInt(10) > 7){
                        if(randomGenerator.nextBoolean()){
                            spicedNickName.append('1');
                        } else {
                            spicedNickName.append('y');
                        }
                    } else {
                        spicedNickName.append(nickName.charAt(i));
                    }
                    break;

                case 't':
                case 'T':
                    if(randomGenerator.nextInt(10) > 7){
                        spicedNickName.append('7');
                    } else {
                        spicedNickName.append(nickName.charAt(i));
                    }
                    break;

                default:
                    spicedNickName.append(nickName.charAt(i));

            }
        }

        return spicedNickName.toString();
    }

    private double generateSkillRating(double median) {
        CauchyDistribution cd = new CauchyDistribution(median, 7);
        double sample = cd.sample();

        while(sample < median - 10 || sample > median + 25){
            sample = cd.sample();
        }

        return sample;
    }

    private double generateSalary(double skillRating){
        CauchyDistribution cd = new CauchyDistribution(200, 60);
        double base = (skillRating * skillRating) * (0.52631577947);
        double mod = cd.sample();

        while(base + mod <= 0){
            mod = cd.sample();
        }

        return base + mod;
    }

    private double generateSigningBonus(double skillRating){
        CauchyDistribution cd = new CauchyDistribution(skillRating * 15, 75);
        double base = (skillRating * skillRating) * (0.52631577947);
        double mod = cd.sample();

        while(base + mod <= 0){
            mod = cd.sample();
        }

        return base + mod;
    }

}
