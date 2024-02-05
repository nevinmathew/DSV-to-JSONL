package com.dsv_to_jsonl;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
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
		String outputFile = args[1];

		readLines(inputFile, outputFile);
	}

	private static void readLines(String inputFile, String outputFile) {
		try (BufferedReader reader = new BufferedReader(new FileReader(inputFile));
				BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile))) {

			List<String> jsonLinesArray = Collections.synchronizedList(new ArrayList<>(1000));

			reader.lines()
				.parallel()
				.skip(1)
				.forEach(lines -> {
					writeLines(writer, jsonLinesArray);
					jsonLinesArray.clear();
					jsonLinesArray.add(convertToJson(lines));
				});
			
			writeLines(writer, jsonLinesArray);
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException("Error processing input file", e);
		}
	}

	private static void writeLines(BufferedWriter writer, List<String> lines) {
		try {
			String nextLine = lines.parallelStream().collect(Collectors.joining("\n"));
			writer.write(nextLine);
			writer.flush();
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException("Error writing output file", e);
		}
	}

	static String convertToJson(String line) {
		try {
			CSVParser parser = CSVParser.parse(line, CSVFormat.DEFAULT);
			CSVRecord record = parser.iterator().next();

			String firstName = record.get(0).replaceAll("\"", "");
			String middleName = record.size() > 1 && !record.get(1).isBlank() ? record.get(1).replaceAll("\"", "")
					: null;
			String lastName = record.size() > 2 ? record.get(2).replaceAll("\"", "") : "";
			String gender = record.size() > 3 ? record.get(3) : "";
			String dateOfBirth = record.size() > 4 ? formatDate(record.get(4)) : "";
			String salary = record.size() > 5 ? record.get(5) : "";

			StringBuilder jsonBuilder = new StringBuilder();
			jsonBuilder.append("{\"firstName\": \"").append(firstName).append("\",");
			if (middleName != null) {
				jsonBuilder.append("\"middleName\": \"").append(middleName).append("\",");
			}
			jsonBuilder.append("\"lastName\": \"").append(lastName).append("\",").append("\"gender\": \"")
					.append(gender).append("\",").append("\"dateOfBirth\": \"").append(dateOfBirth).append("\",")
					.append("\"salary\": ").append(salary).append("}");

			return jsonBuilder.toString();
		} catch (IOException e) {
			e.printStackTrace();
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
				e.printStackTrace();
				throw new RuntimeException("Error formating the date", e);
			}
		}
	}
}
