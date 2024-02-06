# DSV-to-JSONL

## Overview
A Maven project that converts DSV (Delimiter-separated values) files into JSONL (JSON line) format.

- Dates in JSONL output file is in YYYY-MM-dd format.
- The project can be built using Maven in a terminal, with "mvn package".    
- Any DSV file with any arbitrary data is convertible with this tool. This means that the real structure of the data at runtime is dynamic.
- An executable JAR is created to allow user to run the conversion in a terminal.
- The user can specify the input file and any additional parameters if necessary via command-line arguments.

# DSV-to-JSONL Converter (2)
A Maven project that converts DSV (Delimiter-separated values) files into JSONL (JSON line) format.

## Table of Contents
- [Introduction](#introduction)
- [Features](#features)
- [Build and Run](#build-and-run)
- [Usage](#usage)
- [Notes](#notes)

## Overview
This project is a Maven-based utility for converting DSV files into JSONL format. DSV files, containing delimiter-separated values, are commonly used to represent tabular data. The tool ensures dynamic conversion of arbitrary DSV files with varying structures at runtime.

## Features
- Dates in the JSONL output file are formatted in `YYYY-MM-dd`.
- The project can be built using Maven in a terminal, e.g., with `mvn package`.
- An executable JAR is created to allow users to run the conversion from the terminal.
- Users can specify the input file and any additional parameters via command-line arguments.
- Both reading input and writing output files are done in a streaming manner.
  - Does not store any data entries in memory at the same time (Expects millions of entries in a real use case).
  - Uses Java Stream API for stream processing.

## Build and Run
To build the project, use the following Maven command in the terminal:

```bash
mvn clean build
```

To run the converter, use the generated JAR file:

```bash
java -jar "path/to/DSV-to-JSONL-convertor (2)-0.0.1-SNAPSHOT.jar" "path/to/input_file.txt" "path/to/output_file.jsonl"
```

## Usage
- **path/to/DSV-to-JSONL-convertor (2)-0.0.1-SNAPSHOT.jar**: The path to the executable JAR file.
- **path/to/input_file.txt**: The path to the input DSV file.
- **path/to/output_file.jsonl**: The path to the output JSONL file.

## Notes
- Both reading input and writing output files are implemented in a streaming manner using the Java Stream API.
- Ensure the input DSV file adheres to the correct format.

```
Replace placeholders like 'path/to/DsvToJsonl-0.0.1-SNAPSHOT.jar', 'path/to/input_file', and 'path/to/output_file.jsonl'
```

### Libraries
- **Joda-Time**: A library for date and time manipulation in Java.
- **Commons CSV**: A library for working with CSV files in Java.
