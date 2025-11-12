# Most Active Cookie CLI

A lightweight Java 22 command-line tool that reads a cookie log (CSV) and prints the most active cookie(s) for a specified UTC date.  
If multiple cookies tie for highest frequency, all are printed in lexicographical order.

---

## How to Run

### Prerequisites
- **Java 22** (or newer) installed and added to your system PATH  
- **Maven 3.8+** installed (for building from source)

### Build the project
From the project root:
```bash
mvn clean install
```

This compiles the source code, runs all tests, and generates an executable JAR file at:
```target/most-active-cookie-1.0.0.jar```

Run the program

Use the command below to run the CLI:

```bash 
java -jar target/most-active-cookie-1.0.0.jar -f <path_to_csv> -d <YYYY-MM-DD>
```

Example
```bash
java -jar target/most-active-cookie-1.0.0.jar -f src/test/resources/sample_with_header.csv -d 2018-12-09
```

Output:
```
AtY0laUfhglK3lC7
```

Command-line Flags
| Flag         | Description                              |
| ------------ | ---------------------------------------- |
| `-f, --file` | Input CSV file path (use `-` for STDIN)  |
| `-d, --date` | Target date in UTC (format `YYYY-MM-DD`) |
| `--verbose`  | Show diagnostic information              |
| `--quiet`    | Suppress non-error output (default)      |
| `--help`     | Display usage instructions               |
| `--version`  | Print version and exit                   |


Exit Codes
| Code | Meaning           |
| ---- | ----------------- |
| `0`  | Success           |
| `2`  | Invalid arguments |
| `3`  | File I/O error    |
| `4`  | Parse error       |
| `5`  | Internal error    |


Run all tests with:
```bash
mvn test
```


All tests should pass before submission.