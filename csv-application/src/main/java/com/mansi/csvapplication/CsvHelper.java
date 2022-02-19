package com.mansi.csvapplication;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.mansi.csvapplication.Model.CsvModel;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.csv.QuoteMode;
import org.springframework.web.multipart.MultipartFile;

public class CsvHelper {
    public static String TYPE = "text/csv";
    static String[] HEADERs = { "Id", "Name", "Email", "Phone Number" };

    public static boolean hasCSVFormat(MultipartFile file) {
        //validation of file format uploaded is done
        if (TYPE.equals(file.getContentType())
                || file.getContentType().equals("application/vnd.ms-excel")) {
            return true;
        }

        return false;
    }

    public static List<CsvModel> csvTodata(InputStream is) {
        //to store csv file data in database
        try (BufferedReader fileReader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
             CSVParser csvParser = new CSVParser(fileReader,            //Csv parser to read and write data
                     CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim());) {

            List<CsvModel> userDataList = new ArrayList<>();        //entity created will store data in array

            Iterable<CSVRecord> csvRecords = csvParser.getRecords();

            for (CSVRecord csvRecord : csvRecords) {            //returning data from file using for loop
                CsvModel csvModel = new CsvModel(
                        Long.parseLong(csvRecord.get("Id")),
                        csvRecord.get("Name"),
                        csvRecord.get("Email"),
                        Long.parseLong(csvRecord.get("Phone Number"))
                );
                userDataList.add(csvModel);
            }

            return userDataList;
        } catch (IOException e) {
            throw new RuntimeException("fail to parse CSV file: " + e.getMessage());
        }
    }

    public static ByteArrayInputStream dataTocsv(List<CsvModel> userDataList) {
        final CSVFormat format = CSVFormat.DEFAULT.withQuoteMode(QuoteMode.MINIMAL);

        try (ByteArrayOutputStream out = new ByteArrayOutputStream();
             CSVPrinter csvPrinter = new CSVPrinter(new PrintWriter(out), format);) {
            for (CsvModel csvModel : userDataList) {
                List<String> data = Arrays.asList(
                        String.valueOf(csvModel.getId()),
                        csvModel.getName(),
                        csvModel.getEmail(),
                        String.valueOf(csvModel.getPhonenumber())
                );

                csvPrinter.printRecord(data);
            }

            csvPrinter.flush();
            return new ByteArrayInputStream(out.toByteArray());
        } catch (IOException e) {
            throw new RuntimeException("fail to import data to CSV file: " + e.getMessage());
        }
    }
}
