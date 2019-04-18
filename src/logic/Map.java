package logic;

import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import org.jgrapht.graph.DirectedWeightedPseudograph;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Map {

    String _id;
    String _name;
    DirectedWeightedPseudograph<MapNode, MapEdge> _mapNetwork;

    public Map(String id, String name, String pathToMap){

        //Nodes
        try (
            Reader reader = Files.newBufferedReader(Paths.get(pathToMap));
            CSVReader csvReader = new CSVReaderBuilder(reader).withSkipLines(1).build();
        ) {
            String[] nextRecord;
            while ((nextRecord = csvReader.readNext()) != null) {

            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        try (
            Reader reader = Files.newBufferedReader(Paths.get(pathToMap));
            CSVReader csvReader = new CSVReaderBuilder(reader).withSkipLines(1).build();
        ) {
            String[] nextRecord;
            while ((nextRecord = csvReader.readNext()) != null) {

            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
