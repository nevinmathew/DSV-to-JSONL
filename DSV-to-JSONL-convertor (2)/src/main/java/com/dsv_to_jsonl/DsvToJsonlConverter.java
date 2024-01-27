package com.dsv_to_jsonl;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.joda.time.LocalDateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.DateTimeFormatterBuilder;

public class DsvToJsonlConverter {

    public static void main(String[] args) {
        if (args.length < 2) {
            System.exit(1);
        }

        String inputFile = args[0];
        List<String> lines = readLines(inputFile);

        List<String> jsonLines = lines.stream()
                .skip(1) 
                .map(DsvToJsonlConverter::convertToJson)
                .collect(Collectors.toList());

        writeLines(args[1], jsonLines);
    }

    private static List<String> readLines(String filePath) {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            return br.lines().collect(Collectors.toList());
        } catch (IOException e) {
            throw new RuntimeException("Error reading input file", e);
        }
    }

    private static void writeLines(String filePath, List<String> lines) {
        try (FileWriter writer = new FileWriter(filePath)) {
            for (String line : lines) {
                writer.write(line + "\n");
            }
        } catch (IOException e) {
            throw new RuntimeException("Error writing output file", e);
        }
    }

    static String convertToJson(String line) {
        try {
            CSVParser parser = CSVParser.parse(line, CSVFormat.DEFAULT);
            CSVRecord record = parser.iterator().next();

            String firstName = record.get(0).replaceAll("\"", "");
            String middleName = record.size() > 1 && !record.get(1).isBlank() ? record.get(1).replaceAll("\"", "") : null;
            String lastName = record.size() > 2 ? record.get(2).replaceAll("\"", "") : "";
            String gender = record.size() > 3 ? record.get(3) : "";
            String dateOfBirth = record.size() > 4 ? formatDate(record.get(4)) : "";
            String salary = record.size() > 5 ? record.get(5) : "";

            StringBuilder jsonBuilder = new StringBuilder();
            jsonBuilder.append("{\"firstName\": \"").append(firstName).append("\",");
            if (middleName != null) {
                jsonBuilder.append("\"middleName\": \"").append(middleName).append("\",");
            }
            jsonBuilder.append("\"lastName\": \"").append(lastName).append("\",")
                    .append("\"gender\": \"").append(gender).append("\",")
                    .append("\"dateOfBirth\": \"").append(dateOfBirth).append("\",")
                    .append("\"salary\": ").append(salary).append("}");

            return jsonBuilder.toString();
        } catch (IOException e) {
            throw new RuntimeException("Error parsing CSV record", e);
        }
    }

    private static String formatDate(String date) {
    	DateTimeFormatter formatter = null;
		if (date == null || date.isEmpty()) {
			return null;
		} else {
			try {
				if (formatter == null) {
					formatter = new DateTimeFormatterBuilder()
							.appendOptional(DateTimeFormat.forPattern("dd-MM-yyyy").getParser())
							.appendOptional(DateTimeFormat.forPattern("dd/MM/yyyy").getParser())
							.appendOptional(DateTimeFormat.forPattern("yyyy/MM/dd").getParser())
							.appendOptional(DateTimeFormat.forPattern("yyyy-MM-dd").getParser()).toFormatter();
				}
				LocalDateTime dateTime = LocalDateTime.parse(date, formatter);
				return dateTime.toLocalDate().toString();
			} catch (Exception e) {
				return null;
			}
		}
	}
}
