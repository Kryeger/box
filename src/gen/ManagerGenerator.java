package gen;

import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import logic.Manager;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Random;
import java.util.UUID;

public class ManagerGenerator {

    static String pathToMaleFirstNames = "./gen/names/male/male_first_names.csv";
    static String pathToLastNames = "./gen/names/last_names.csv";

    private static ArrayList<String> _maleFirstNames = new ArrayList<>();
    private static ArrayList<String> _lastNames = new ArrayList<>();

    private static Random randomGenerator = new Random();

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
    }

    public static Manager generate() {

        String firstName = _maleFirstNames.get(randomGenerator.nextInt(_maleFirstNames.size()));
        String lastName = _lastNames.get(randomGenerator.nextInt(_lastNames.size()));

        return new Manager(UUID.randomUUID().toString(), firstName, lastName, "");

    }

}
