# DSV-to-JSONL
A Maven project that converts DSV (Delimiter-separated values) files into JSONL (JSON line) format.

- Dates in JSONL output file is in YYYY-MM-dd format.
- The project can be built using Maven in a terminal, with "mvn package".    
- Any DSV file with any arbitrary data is convertible with this tool. This means that the real structure of the data at runtime is dynamic.
- An executable JAR is created to allow user to run the conversion in a terminal.
- The user can specify the input file and any additional parameters if necessary via command-line arguments.

To be included later:
- Both reading input and writing output files to be done in a streaming manner:
  ~ Must not store all data entries in memory at the same time (Expect millions of entries in a real use case).
  ~ Should use Java Stream API for stream processing.
