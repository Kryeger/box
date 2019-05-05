package logic.service;

import com.opencsv.CSVWriter;

import java.io.File;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Paths;

public class AuditService {

    private static final AuditService _instance = new AuditService();

    private CSVWriter _csvWriter;
    private boolean _active = true;
    private Writer _writer;

    private AuditService() {

        try {

            _writer = Files.newBufferedWriter(Paths.get("./audit/audit.csv"));

            _csvWriter = new CSVWriter(_writer,
                    CSVWriter.DEFAULT_SEPARATOR,
                    CSVWriter.NO_QUOTE_CHARACTER,
                    CSVWriter.DEFAULT_ESCAPE_CHARACTER,
                    CSVWriter.DEFAULT_LINE_END);

            String[] headerRecord = {"action", "timestamp"};

            _csvWriter.writeNext(headerRecord);

        } catch (IOException e){
            e.printStackTrace();
        }

    }

    public static void log(String message){

        if(!_instance._active) return;

        System.out.println(message);

        _instance._csvWriter.writeNext(new String[]{message, String.valueOf(System.currentTimeMillis())});

    }

}
