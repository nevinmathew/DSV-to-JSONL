package com.spring.crud;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Pattern;

import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.DateTimeFormatterBuilder;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class DsvToJsonlConvert {

	public static void convert(String[] paths) throws IOException {

		String inputPathArgs = paths[0];
		String outputPathArgs = paths[1];

		Path fileName = Path.of(inputPathArgs);
		String dsvTotalData = Files.readString(fileName);
		File file = new File(outputPathArgs);
		file.createNewFile();

		String[] eachDataSet = dsvTotalData.split("\\r?\\n");

		writeToFile(outputPathArgs, convertToJson(eachDataSet));
	}

	public static JSONObject convertToJson(String[] eachDataSet) {

		String[] eachValue = null;
		Map jsonSubObject = null;
		JSONArray jsonArray = new JSONArray();
		JSONObject jsonFinal = new JSONObject();

		String[] dataSet = Arrays.copyOfRange(eachDataSet, 1, eachDataSet.length);
		String[] parameterNames = eachDataSet[0].split(",|\\|");

		for (int i = 0; i < dataSet.length; i++) {
			if (i < dataSet.length && eachValue == null) {
				if (dataSet[i].contains(", "))
					dataSet[i] = dataSet[i].replace(", ", "!");
				if (dataSet[i].contains(" |"))
					dataSet[i] = dataSet[i].replace(" |", "@");
				eachValue = dataSet[0].contains(",") ? dataSet[i].split(",") : dataSet[i].split("|");
				System.out.println("Splitting "+eachValue);
			}
			jsonSubObject = new LinkedHashMap<String, String>();
			for (int j = 0; j < parameterNames.length; j++) {
				if (eachValue[j].contains("!"))
					eachValue[j] = eachValue[j].replace("!", ", ").replace("\"", "");
				if (eachValue[j].contains("@"))
					eachValue[j] = eachValue[j].replace("@", " |").replace("\"", "");
				System.out.println("replacing "+eachValue);

				LocalDate date = stringAsDate(eachValue[j]);
				if (date != null) 
					jsonSubObject.put(parameterNames[j], date.toString());
				else if (Pattern.matches("\\d", eachValue[j])) 
					jsonSubObject.put(parameterNames[j], Long.parseLong(eachValue[j]));
				else
					jsonSubObject.put(parameterNames[j], eachValue[j]);
				System.out.println("json object "+jsonSubObject);
			}
			jsonArray.add(jsonSubObject);
			System.out.println("json array"+jsonArray);
			eachValue = null;
		}
		jsonFinal.put("Users: ", jsonArray);
		System.out.println("final json "+jsonFinal);
		return jsonFinal;
	}

	public static void writeToFile(String outputPathArgs, JSONObject jsonFinal) {
		try (PrintWriter out = new PrintWriter(new FileWriter(outputPathArgs.toString()))) {
			out.write(jsonFinal.toString());
//            out.write(jsonFinal.toJSONString());
			System.out.println(jsonFinal.toJSONString());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static LocalDate stringAsDate(String strDate) {
		DateTimeFormatter formatter = null;
		if (strDate == null || strDate.isEmpty()) {
			return null;
		} else {
			try {
				if (formatter == null) {
					formatter = new DateTimeFormatterBuilder()
							.appendOptional(DateTimeFormat.forPattern("dd-MM-yyyy").getParser())
							.appendOptional(DateTimeFormat.forPattern("dd/MM/yyyy").getParser())
							.appendOptional(DateTimeFormat.forPattern("yyyy/MM/dd").getParser())
							.appendOptional(DateTimeFormat.forPattern("yyyy-MM-dd").getParser())
							.toFormatter();
				}
				LocalDateTime dateTime = LocalDateTime.parse(strDate, formatter);
				return dateTime.toLocalDate();
			} catch (Exception e) {
				return null;
			}
		}
	}
}