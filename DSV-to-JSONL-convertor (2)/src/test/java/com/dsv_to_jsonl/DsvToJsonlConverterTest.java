package com.dsv_to_jsonl;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.Test;

public class DsvToJsonlConverterTest {

    @Test
    public void testConversion() {
        List<String> inputLines = Arrays.asList(
                "firstName,middleName,lastName,gender,dateOfBirth,salary",
                "Wolfgang,Amadeus,Mozart,Male,1756-01-27,1000",
                "Albert,,Einstein,Male,1955/04/18,2000",
                "\"Marie, Salomea\",Skłodowska |,Curie,Female,04-07-1934,3000"
        );

        List<String> expectedOutput = Arrays.asList(
                "{\"firstName\": \"Wolfgang\",\"middleName\": \"Amadeus\",\"lastName\": \"Mozart\",\"gender\": \"Male\",\"dateOfBirth\": \"1756-01-27\",\"salary\": 1000}",
                "{\"firstName\": \"Albert\",\"lastName\": \"Einstein\",\"gender\": \"Male\",\"dateOfBirth\": \"1955-04-18\",\"salary\": 2000}",
                "{\"firstName\": \"Marie, Salomea\",\"middleName\": \"Skłodowska |\",\"lastName\": \"Curie\",\"gender\": \"Female\",\"dateOfBirth\": \"1934-07-04\",\"salary\": 3000}"
        );

        List<String> actualOutput = inputLines.stream()
                .skip(1)
                .map(DsvToJsonlConverter::convertToJson)
                .collect(Collectors.toList());
        System.out.println(expectedOutput);
        System.out.println(actualOutput);
        assertEquals(expectedOutput, actualOutput);

    }
}
