package gen;

import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

public class PlayerData {

    static String pathToFirstNames = "./gen/names/male/male_first_names.csv";

    static ArrayList<String> _firstNames = new ArrayList<>();
    static ArrayList<String> _lastNames = new ArrayList<>();
    static ArrayList<String> _nickNames = new ArrayList<>();

    static {
        try (
            Reader reader = Files.newBufferedReader(Paths.get(pathToFirstNames));
            CSVReader csvReader = new CSVReaderBuilder(reader)
                    .withSkipLines(1)
                    .build();
        ) {
            // Reading Records One by One in a String array
            String[] nextRecord;
            while ((nextRecord = csvReader.readNext()) != null) {
                System.out.println("Name : " + nextRecord[0]);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private PlayerData() {}

}
